package com.dilawar.capplugins;

public class LiveSmsManager {
    private static final LiveSms notifyLiveSms = new LiveSms();

    public static void sendLiveSms(String sms) {
        notifyLiveSms.sendNotification(sms);
    }

    public static LiveSms getLiveSms() {
        return notifyLiveSms;
    }
}
