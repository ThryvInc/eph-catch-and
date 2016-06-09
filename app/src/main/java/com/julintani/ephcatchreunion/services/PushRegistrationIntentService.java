package com.julintani.ephcatchreunion.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by ell on 6/8/16.
 */
public class PushRegistrationIntentService extends IntentService {

    public PushRegistrationIntentService() {
        super("regIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken("851561399658",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            SharedPreferences preferences = getSharedPreferences("currentUser", MODE_PRIVATE);
            preferences.edit().putString("pushToken", token).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
