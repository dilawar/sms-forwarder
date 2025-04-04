package com.dilawar.capplugins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
// import android.widget.Toast;

import com.dilawar.Message;

import java.util.Objects;

/**
 * Listen to incoming SMSs.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "broadcast_sms_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if(pdus == null) {
                    return;
                }
                if (pdus.length == 0) {
                    return;
                }

                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], "3gpp");
                    sb.append(messages[i].getMessageBody());
                }

                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
                Log.d(TAG, message + " from " + sender);

                // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                // Broadcast this message so that UI activity can listen to it.
                LiveSmsManager.getLiveSms().sendNotification(new Message(sender, message));
            }
        }
    }
}