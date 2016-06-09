package com.julintani.ephcatchreunion.models;

import android.content.Context;

/**
 * Created by ell on 6/3/16.
 */
public class OnboardingManager {

    public static boolean isUserOnboarded(Context context){
        return context.getSharedPreferences("onboarding", Context.MODE_PRIVATE).getBoolean("isUserOnboarded", false);
    }

    public static void setUserOnboarded(Context context){
        context.getSharedPreferences("onboarding", Context.MODE_PRIVATE).edit().putBoolean("isUserOnboarded", true).apply();
    }
}
