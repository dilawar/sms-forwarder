package com.dilawar.capplugins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "SmsPlugin")
public class SmsPlugin extends Plugin {

    private final String TAG = "sms_plugin";
    private BroadcastReceiver receiver;

    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dilawar.cppplugins.ACTION_RECV_SMS");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "received ACTION_RECV_SMS"+intent);
            }
        };
    }

    @PluginMethod()
    public void echo(PluginCall call) {
        Log.d(TAG, "echo was called.");
        String value = call.getString("value");
        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);
    }

}