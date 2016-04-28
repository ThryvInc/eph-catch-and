package com.julintani.ephcatchreunion.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ell on 4/24/16.
 */
public class Event implements Serializable {
    private Date startAt;
    private Date endAt;
    private String title;
    private String description;
    private String location;

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
