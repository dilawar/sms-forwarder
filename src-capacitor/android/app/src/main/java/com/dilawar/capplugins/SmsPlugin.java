package com.dilawar.capplugins;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;

import com.dilawar.Matcher;
import com.dilawar.Message;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

@CapacitorPlugin(
        name = "SmsPlugin",
        permissions = {
                @Permission(strings = {Manifest.permission.RECEIVE_SMS}, alias = SmsPlugin.RECEIVE_SMS),
                @Permission(strings = {Manifest.permission.READ_SMS}, alias = SmsPlugin.READ_SMS),
        }
)
public class SmsPlugin extends Plugin {
    // Permission alias constants.
    static final String RECEIVE_SMS = "RECEIVE_SMS";
    static final String READ_SMS = "READ_SMS";
    private final String TAG = "sms_plugin";

    private final List<Message> listOfMessages = new Vector<>();

    // Observes SMS sent over LiveData.
    private final Observer<Message> liveSmsObserver = m -> {
        Log.i(TAG, "Got new sms: " + m);
        listOfMessages.add(m);
    };

    // When plugin is loaded.
    @Override
    public void load() {
        Log.i(TAG, "Calling plugin load...");
        // Where do I remove this listener?
        LiveSmsManager.getLiveSms().observeForever(liveSmsObserver);
    }


    @PluginMethod()
    public void echo(PluginCall call) {
        Log.d(TAG, "echo was called.");
        String value = call.getString("value");
        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PluginMethod()
    public void getLiveSms(PluginCall call) {
        JSONObject ret = new JSONObject();

        Message m;
        while (!listOfMessages.isEmpty()) {
            m = listOfMessages.remove(0);
            JSONObject sms = new JSONObject();
            try {
                sms.put("sender", m.sender);
                sms.put("body", m.body);
                sms.put("timestamp", m.timestamp);

                ret.append("result", sms);
            } catch (JSONException e) {
                Log.e(TAG, "Could not convert Message to JSON");
            }
        }

        try {
            call.resolve(JSObject.fromJSONObject(ret));
        } catch (Exception e) {
            Log.w(TAG, "Failed to convert JSONObject to JSObject.");
        }
    }

    @PluginMethod()
    public void sendMessage(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("result", false);

        String phoneNumber = call.getString("forward");
        String messageText = call.getString("body");

        // We assume that app has been granted required permissions.
        try {
            SmsManager smsManager = SmsManager.getDefault();
            Log.i(TAG, "Sending message to " + phoneNumber + " with text '" + messageText + "'");
            smsManager.sendTextMessage(phoneNumber, null, messageText, null, null);
            ret.put("result", true);
        } catch (Exception e) {
            // Handle any exceptions
            Log.e(TAG, "Failed to send message " + e.toString());
        }
        call.resolve(ret);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PluginMethod()
    public void querySms(PluginCall call) {

        // checks that we are read sms permission.
        requestPermissions(call);

        JSONObject listOfSms = new JSONObject();
        String query = call.getString("query");
        if (query == null) {
            Log.w(TAG, "Null query. We'll no sms.");
            call.resolve(null);
            return;
        }
        query = query.trim();
        if (query.isEmpty()) {
            Log.w(TAG, "Empty query. We'll no sms.");
            call.resolve(null);
            return;
        }

        Log.i(TAG, "Searching message with query '" + query + "'.");
        ContentResolver resolver = getContext().getContentResolver();

        // Read SMS. This table has following columns
        //
        // _id: <int>, thread_id, address, person:null,
        // date:1743390339994, date_sent:1743390340000,
        // protocol:0, read:1, status:-1,
        // type:1, reply_path_present:0, subject:null, body:<string>,
        // service_center:null locked:0 sub_id:1 error_code:0
        // creator:com.google.android.apps.messaging
        // seen:1
        Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"),
                new String[]{"_id", "address", "person", "date", "date_sent", "subject", "body"},
                null, null,
                null, null);

        if (!Objects.requireNonNull(cursor).moveToFirst()) {
            // Empty box, no SMS
            Log.i(TAG, "Empty INBOX.");
            cursor.close();
            return;
        }

        do {
            JSONObject data = new JSONObject();

            // check if any field matches the query
            boolean match = false;
            for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                String value = cursor.getString(idx);
                if (value != null && Matcher.matches(query, value)) {
                    match = true;
                }

                try {
                    data.put(cursor.getColumnName(idx), value);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to convert to JSON");
                }
            }

            Log.d(TAG, ">> Got msg" + data);
            if (!match) {
                continue;
            }

            // check if SMS matches the query of not.
            try {
                listOfSms.append("result", data);
            } catch (JSONException e) {
                Log.e(TAG, "Failed to convert msgData to JSON");
            }
        } while (cursor.moveToNext());
        cursor.close();

        try {
            call.resolve(JSObject.fromJSONObject(listOfSms));
        } catch (Exception e) {
            Log.e(TAG, "Failed to convert JSONObject to JSObject.");
        }
    }

    @PluginMethod()
    public void requestPermissions(PluginCall call) {
        Log.d(TAG, "User requested SMS related permissions.");
        String[] aliases = {RECEIVE_SMS};
        super.requestPermissionForAliases(aliases, call, "smsPermissionCallback");

        String[] readSms = {READ_SMS};
        super.requestPermissionForAliases(readSms, call, "smsPermissionCallback");
    }

    @PermissionCallback
    private void smsPermissionCallback(PluginCall call) {
        Log.d(TAG, "callback after asking user permission.");

        // check the READ_SMS permission has been granted.
        String perm = Manifest.permission.READ_SMS;
        int res = getContext().checkCallingOrSelfPermission(perm);
        if(res != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "We dont have READ_SMS permission yet.");
        }
    }
}