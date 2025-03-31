package com.dilawar.smsfwd;

import android.os.Bundle;
import com.dilawar.capplugins.SmsPlugin;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // register our plugin.
        registerPlugin(SmsPlugin.class);
        super.onCreate(savedInstanceState);
    }

}