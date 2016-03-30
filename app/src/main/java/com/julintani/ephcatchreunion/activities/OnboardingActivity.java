package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

/**
 * Created by ell on 3/26/16.
 */
public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View card = findViewById(R.id.card_profile);
        final ProfileViewHolder profileViewHolder = new ProfileViewHolder(card, ProfileViewHolder.ProfileType.LARGE);
        profileViewHolder.displayUser(User.generateDummyUser());
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(OnboardingActivity.this, profileViewHolder.getUserImageView(), ProfileViewHolder.PROFILE_IMAGE_TRANSITION);
                Intent intent = new Intent(OnboardingActivity.this, ProfileActivity.class);
                ActivityCompat.startActivity(OnboardingActivity.this, intent, options.toBundle());
            }
        });
    }
}
