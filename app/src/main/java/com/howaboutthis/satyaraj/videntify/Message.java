package com.howaboutthis.satyaraj.videntify;

import java.io.Serializable;

public class Message implements Serializable {
    private String id;
    private String message;

    Message() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}