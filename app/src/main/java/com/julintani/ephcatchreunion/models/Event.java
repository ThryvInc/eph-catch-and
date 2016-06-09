package com.julintani.ephcatchreunion.models;

import java.io.Serializable;

/**
 * Created by ell on 4/24/16.
 */
public class Event implements Serializable {
    private String name;
    private String description;
    private String location;
    private String time;
    private String day;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
