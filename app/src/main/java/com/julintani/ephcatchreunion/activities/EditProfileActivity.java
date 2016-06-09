package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.fragments.EditProfileFragment;
import com.julintani.ephcatchreunion.services.PushRegistrationIntentService;

/**
 * Created by ell on 6/3/16.
 */
public class EditProfileActivity extends AppCompatActivity {
    public static final String IS_DURING_ONBOARDING_KEY = "IS_DURING_ONBOARDING_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setTitle("Edit Profile");

        startService(new Intent(this, PushRegistrationIntentService.class));

        EditProfileFragment fragment = EditProfileFragment.newInstance();
        if (getIntent() != null && getIntent().getBooleanExtra(IS_DURING_ONBOARDING_KEY, false)){
            fragment.isDuringOnboarding = true;
        }
        getFragmentManager().beginTransaction()
                .add(R.id.fl_fragment, fragment)
                .commitAllowingStateLoss();
    }
}
