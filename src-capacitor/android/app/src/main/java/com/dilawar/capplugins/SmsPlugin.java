package com.dilawar.capplugins;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
        name = "SmsPlugin",
        permissions = {
        @Permission(strings = { Manifest.permission.RECEIVE_SMS }, alias=SmsPlugin.RECEIVE_SMS),
}
)
public class SmsPlugin extends Plugin {

    // Permission alias constants.
    static final String RECEIVE_SMS = "RECEIVE_SMS";
    private final String TAG = "sms_plugin";
    private BroadcastReceiver receiver;

    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dilawar.cppplugins.ACTION_RECV_SMS");
        Log.i(TAG, "Calling plugin onCreate...");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "received ACTION_RECV_SMS" + intent);
                Log.i(TAG, "received message " + context);
            }
        };
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void sendReceivedSMS(PluginCall call) {

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
    public void requestPermissions(PluginCall call) {
        Log.d(TAG, "User requested SMS related permissions.");
        String[] aliases = { RECEIVE_SMS };
        super.requestPermissionForAliases(aliases, call, "smsPermissionCallback");
    }

    @PermissionCallback
    private void smsPermissionCallback(PluginCall call) {
        Log.d(TAG, "smsPermissionCallback");
    }

}