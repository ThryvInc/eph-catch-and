package com.julintani.ephcatchreunion.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

/**
 * Created by ell on 3/22/16.
 */
public class ProfileActivity extends AppCompatActivity {
    public static final String USER_KEY = "user";
    protected User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mUser = (User) extras.getSerializable(USER_KEY);
        }

        ProfileViewHolder profileViewHolder = new ProfileViewHolder(findViewById(R.id.profile), ProfileViewHolder.ProfileType.LARGE);
        profileViewHolder.setTransitions();
        profileViewHolder.displayUser(mUser);
    }
}
