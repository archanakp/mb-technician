package com.battmobile.battmobiletechnician.chat.model;

public class LoadMessageModel {
    String from;
    String to;

    LoadMessageModel(){

    }

    LoadMessageModel(String from,String to,String message,Long timestamp){

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    String message;
    Long timestamp;
}
