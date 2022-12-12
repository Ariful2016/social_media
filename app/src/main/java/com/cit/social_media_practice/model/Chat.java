package com.cit.social_media_practice.model;

public class Chat {

    String chat_id,message,receiver,sender;

    public Chat() {
    }

    public Chat(String chat_id, String message, String receiver, String sender) {
        this.chat_id = chat_id;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
