package com.julintani.ephcatchreunion.models;

import org.json.JSONObject;

/**
 * Created by ell on 6/7/16.
 */
public class ConvoHolder {
    protected Conversation attributes;
    protected RelationshipsHolder relationships;

    public Conversation getAttributes() {
        return attributes;
    }

    public RelationshipsHolder getRelationships() {
        return relationships;
    }
}
