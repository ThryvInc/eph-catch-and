package com.julintani.ephcatchreunion.services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by ell on 6/8/16.
 */
public class ReunionIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, PushRegistrationIntentService.class);
        startService(intent);
    }
}
