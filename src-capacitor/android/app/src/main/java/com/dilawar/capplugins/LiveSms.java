package com.dilawar.capplugins;

import androidx.lifecycle.LiveData;

import com.dilawar.Message;

public class LiveSms extends LiveData<Message> {
    public void sendNotification(Message message) {
        postValue(message);
    }
}
