package com.julintani.ephcatchreunion.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 3/22/16.
 */
public class User implements Serializable{
    private static User currentUser = User.generateDummyUser();

    private int id;
    private String name;
    private String major;
    private String extraCurriculars;
    private String job;
    private String imageUrl;
    private List<Like> relevantLikes = new ArrayList<>();

    public static User generateDummyUser(){
        User user = new User();
        user.name = "Ephraim Williams";
        user.major = "Defeating-the-French major";
        user.extraCurriculars = "Being a Colonel, Establishing schools";
        user.job = "Namesake of the best college evar";
        user.imageUrl = "http://ephsports.williams.edu/images/Reading_Purple_Cow_at_380_x_260.jpg";
        return user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public boolean doesLike(User otherUser){
        for (Like like : this.relevantLikes){
            if (like.getOtherUser().equals(otherUser) && like.isSecret()){
                return true;
            }
        }
        return false;
    }

    public boolean hasMatchedWith(User otherUser){
        for (Like like : this.relevantLikes){
            if (like.getOtherUser().equals(otherUser) && like.isMutual()){
                return true;
            }
        }
        return false;
    }

    public boolean hasSuperLiked(User otherUser){
        for (Like like : this.relevantLikes){
            if (like.getOtherUser().equals(otherUser) && !like.isSecret()){
                return true;
            }
        }
        return false;
    }

    public boolean doesWantToCatchUpWith(User otherUser){
        for (Like like : this.relevantLikes){
            if (like.getOtherUser().equals(otherUser) && like.isPlatonic()){
                return true;
            }
        }
        return false;
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

    public String getExtraCurriculars() {
        return extraCurriculars;
    }

    public void setExtraCurriculars(String extraCurriculars) {
        this.extraCurriculars = extraCurriculars;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(this.getClass())) return false;

        User otherUser = (User)o;
        return this.id == otherUser.id;
    }
}
