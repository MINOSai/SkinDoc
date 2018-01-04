package com.minosai.skindoc.chat.data;

/**
 * Created by minos.ai on 03/01/18.
 */

public class Message {

    private String message;
    private String sender;
    private String id;

    public Message() {
    }

    public Message(String message, String sender, String id) {
        this.message = message;
        this.sender = sender;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
