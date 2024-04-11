package com.iamalex33329.linetechfresh2024_preassessment.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {

    private boolean sentByCurrentUser;
    private String content;
    private Date timestamp;

    public Message() {}

    public Message(boolean sentByCurrentUser, String content, Date timestamp) {
        this.sentByCurrentUser = sentByCurrentUser;
        this.content = content;
        this.timestamp = timestamp;
    }

    public boolean isSentByCurrentUser() {
        return sentByCurrentUser;
    }

    public void setSentByCurrentUser(boolean sentByCurrentUser) {
        this.sentByCurrentUser = sentByCurrentUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        if (timestamp instanceof Long) this.timestamp = new Date((Long) timestamp);
        else this.timestamp = (Date) timestamp;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sentByCurrentUser", sentByCurrentUser);
        result.put("content", content);
        result.put("timestamp", timestamp.getTime());
        return result;
    }
}
