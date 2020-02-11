package com.battmobile.battmobiletechnician.chat.model;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Map;

public class ChatMessageModel implements Serializable {


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }






    public String getChatUID() {
        return chatUID;
    }

    public void setChatUID(String chatUID) {
        this.chatUID = chatUID;
    }

    public String getToDisplayName() {
        return toDisplayName;
    }

    public void setToDisplayName(String toDisplayName) {
        this.toDisplayName = toDisplayName;
    }

    public String getFromDisplayName() {
        return fromDisplayName;
    }

    public void setFromDisplayName(String fromDisplayName) {
        this.fromDisplayName = fromDisplayName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    String toDisplayName;
    String fromDisplayName;
    String image;
    String lastMessage;
    Map<String, String> timestamp;
    String to;
    String from;
    String chatUID;

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }






    ChatMessageModel(){

    }

    public ChatMessageModel(String message, String to, String from, Map<String, String> timestamp,String toDisplayName,String fromDisplayName,String image,String chatUID){
        this.lastMessage=message;
        this.to=to;
        this.from=from;
        this.timestamp= timestamp;
        this.chatUID=chatUID;
        this.fromDisplayName=fromDisplayName;
        this.toDisplayName=toDisplayName;
        this.image=image;

    }
}
