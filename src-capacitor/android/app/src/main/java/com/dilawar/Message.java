package com.dilawar;

import java.util.Optional;

public class Message {
    public String sender;
    public String body;
    public Optional<String> subject = null;
    public long timestamp;

    public Message(String address, String body, long timestamp) {
        this.sender = address;
        this.body = body;
        this.timestamp = timestamp;
    }
}

