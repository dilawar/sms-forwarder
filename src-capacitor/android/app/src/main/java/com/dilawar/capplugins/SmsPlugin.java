package com.dilawar.capplugins;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONObject;

@CapacitorPlugin(name = "SmsPlugin")
public class SmsPlugin extends Plugin {

    @Override
    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSONObject ret = new JSONObject();
        ret.put("value", value);
        call.resolve(ret);
    }

}