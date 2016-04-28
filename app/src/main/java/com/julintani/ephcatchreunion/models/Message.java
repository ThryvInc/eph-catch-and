package com.julintani.ephcatchreunion.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ell on 4/22/16.
 */
public class Message implements Serializable{
    private Date createdAt;
    private User sender;
    private String text;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
