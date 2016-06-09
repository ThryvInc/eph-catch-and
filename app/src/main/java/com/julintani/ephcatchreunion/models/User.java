package com.julintani.ephcatchreunion.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 3/22/16.
 */
public class User implements Serializable{
    private static User currentUser;

    private int id;
    private String name;
    private String major;
    private String extracurriculars;
    @SerializedName("current-activity")
    private String currentActivity;
    @SerializedName("image-url")
    private String imageUrl;
    @SerializedName("device-type")
    private String deviceType = "android";
    @SerializedName("push-token")
    private String pushToken;


    public static User getCurrentUser(Context context) {
        if (currentUser != null){
            return currentUser;
        }

        User user = new User();
        SharedPreferences preferences = context.getSharedPreferences("currentUser", Context.MODE_PRIVATE);
        user.setId(preferences.getInt("id", 0));
        user.setName(preferences.getString("name", ""));
        user.setMajor(preferences.getString("major", ""));
        user.setExtracurriculars(preferences.getString("extracurriculars", ""));
        user.setCurrentActivity(preferences.getString("currentActivity", ""));
        user.setImageUrl(preferences.getString("imageUrl", ""));
        user.setPushToken(preferences.getString("pushToken", ""));

        currentUser = user;
        return currentUser;
    }

    public static void setCurrentUser(Context context, User currentUser) {
        User.currentUser = currentUser;
        SharedPreferences.Editor editor = context.getSharedPreferences("currentUser", Context.MODE_PRIVATE).edit();
        editor.putInt("id", currentUser.getId());
        editor.putString("name", currentUser.getName());
        editor.putString("major", currentUser.getMajor());
        editor.putString("extracurriculars", currentUser.getExtracurriculars());
        editor.putString("currentActivity", currentUser.getCurrentActivity());
        editor.putString("imageUrl", currentUser.getImageUrl());
        editor.apply();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getExtracurriculars() {
        return extracurriculars;
    }

    public void setExtracurriculars(String extracurriculars) {
        this.extracurriculars = extracurriculars;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(this.getClass())) return false;

        User otherUser = (User)o;
        return this.id == otherUser.id;
    }
}
