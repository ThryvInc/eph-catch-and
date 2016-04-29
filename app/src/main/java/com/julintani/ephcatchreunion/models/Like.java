package com.julintani.ephcatchreunion.models;

/**
 * Created by ell on 4/28/16.
 */
public class Like {
    private User otherUser;
    private boolean isMutual;
    private boolean isSecret;
    private boolean isPlatonic;

    public boolean isMutual() {
        return isMutual;
    }

    public void setMutual(boolean mutual) {
        isMutual = mutual;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    public boolean isPlatonic() {
        return isPlatonic;
    }

    public void setPlatonic(boolean platonic) {
        isPlatonic = platonic;
    }

    public User getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }
}
