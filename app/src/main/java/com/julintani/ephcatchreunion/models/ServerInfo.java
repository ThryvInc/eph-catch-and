package com.julintani.ephcatchreunion.models;

import android.content.Context;

/**
 * Created by ell on 6/3/16.
 */
public class ServerInfo {
    public static final String BASE_URL = "https://glacial-brushlands-22982.herokuapp.com/api/";
    public static final String CONTENT_TYPE_HEADER = "application/vnd.api+json";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String API_KEY_HEADER = "X-EphcatchReunion-Api-Key";

    public static String getApiKey(Context context){
        return context.getSharedPreferences(API_KEY_HEADER, Context.MODE_PRIVATE).getString(API_KEY_HEADER, null);
    }

    public static void setApiKey(Context context, String apiKey){
        context.getSharedPreferences(API_KEY_HEADER, Context.MODE_PRIVATE).edit().putString(API_KEY_HEADER, apiKey).apply();
    }
}
