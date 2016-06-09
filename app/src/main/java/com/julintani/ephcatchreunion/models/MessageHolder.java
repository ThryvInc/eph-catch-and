package com.julintani.ephcatchreunion.models;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ell on 6/7/16.
 */
public class MessageHolder {
    private Message attributes;
    private Map<String, Map<String, Map>> relationships;

    public Message getAttributes() {
        return attributes;
    }

    public Map<String, Map<String, Map>> getRelationship() {
        return relationships;
    }
}
