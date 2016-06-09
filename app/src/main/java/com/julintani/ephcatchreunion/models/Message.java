package com.julintani.ephcatchreunion.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ell on 4/22/16.
 */
public class Message implements Serializable{
    private int id;
    private int userId;
    private String body;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
