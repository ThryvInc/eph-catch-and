package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.listeners.CatchUpOnClickListener;
import com.julintani.ephcatchreunion.listeners.LikeOnClickListener;
import com.julintani.ephcatchreunion.listeners.MessageOnClickListener;
import com.julintani.ephcatchreunion.listeners.SuperLikeOnClickListener;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mUser = (User) extras.getSerializable(USER_KEY);
        }

        ProfileViewHolder profileViewHolder = new ProfileViewHolder(findViewById(R.id.profile), ProfileViewHolder.ProfileType.LARGE);
        profileViewHolder.setTransitions();
        profileViewHolder.displayUser(mUser);

        findViewById(R.id.fab_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ConversationActivity.class);
                intent.putExtra("otherUser", mUser);
                startActivity(intent);
            }
        });
    }
}
