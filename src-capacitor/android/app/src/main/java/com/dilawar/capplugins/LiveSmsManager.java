package com.dilawar.capplugins;

import com.dilawar.Message;

public class LiveSmsManager {
    private static final LiveSms notifyLiveSms = new LiveSms();

    public static void sendLiveSms(Message sms) {
        notifyLiveSms.sendNotification(sms);
    }

    public static LiveSms getLiveSms() {
        return notifyLiveSms;
    }
}
