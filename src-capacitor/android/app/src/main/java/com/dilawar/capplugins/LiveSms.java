package com.dilawar.capplugins;

import androidx.lifecycle.LiveData;

public class LiveSms extends LiveData<String> {
    public void sendNotification(String message) {
        postValue(message);
    }
}
