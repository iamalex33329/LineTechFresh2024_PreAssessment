package com.iamalex33329.linetechfresh2024_preassessment.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Message {

    private boolean sentByCurrentUser;
    private String content;
    private Date timestamp;

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

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message { sentByCurrentUser=" + sentByCurrentUser + ", content='" + content + '\'' + ", timestamp=" + timestamp + " }";
    }
}
