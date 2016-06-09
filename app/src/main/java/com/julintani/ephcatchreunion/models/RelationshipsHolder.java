package com.julintani.ephcatchreunion.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by ell on 6/7/16.
 */
public class RelationshipsHolder {
    private Map<String, List<User>> users;
    @SerializedName("last-message")
    private Map<String, List<Message>> lastMessage;

    public Map<String, List<User>> getUsers() {
        return users;
    }

    public Map<String, List<Message>> getLastMessage() {
        return lastMessage;
    }
}
