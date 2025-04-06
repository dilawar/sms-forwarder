package com.dilawar.capplugins;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;

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
import java.util.Vector;

@CapacitorPlugin(
        name = "SmsPlugin",
        permissions = {
                @Permission(strings = {Manifest.permission.RECEIVE_SMS}, alias = SmsPlugin.RECEIVE_SMS),
        }
)
public class SmsPlugin extends Plugin {
    // Permission alias constants.
    static final String RECEIVE_SMS = "RECEIVE_SMS";
    private final String TAG = "sms_plugin";

    private final List<Message> listOfMessages = new Vector();

    // Observes SMS sent over LiveData.
    private final Observer<Message> liveSmsObserver = new Observer<Message>() {
        @Override
        public void onChanged(Message m) {
            Log.i(TAG, "Got new sms: " + m);
            listOfMessages.add(m);
        }
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

    @PluginMethod()
    public void getLiveSms(PluginCall call) {
        JSONObject ret = new JSONObject();
        while (!listOfMessages.isEmpty()) {
            Message m = listOfMessages.remove(0);
            JSONObject sms = new JSONObject();
            try {
                sms.put("sender", m.sender);
                sms.put("message", m.message);
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PluginMethod()
    public void querySms(PluginCall call) {
        JSONObject listOfSms = new JSONObject();

        String query = call.getString("query").trim();
        if (query.isEmpty()) {
            Log.w(TAG, "Empty query. We'll no sms.");
            call.resolve(null);
        }

        Log.i(TAG, "Searching message with query '" + query + "'.");
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"),
                null, null,
                null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }
                Log.d(TAG, ">> Got msg" + msgData);
                // use msgData
                if (msgData.isEmpty()) {
                    continue;
                }
                try {
                    listOfSms.append("result", msgData);
                } catch (JSONException e) {
                    Log.e(TAG, "failed to convert msgData to JSON");
                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
            Log.w(TAG, "Empty INBOX.");
        }

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
    }

    @PermissionCallback
    private void smsPermissionCallback(PluginCall call) {
        Log.d(TAG, "smsPermissionCallback");
    }


}