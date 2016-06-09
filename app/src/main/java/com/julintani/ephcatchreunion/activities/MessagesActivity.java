package com.julintani.ephcatchreunion.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.fragments.MessagesFragment;
import com.julintani.ephcatchreunion.models.User;

/**
 * Created by ell on 4/24/16.
 */
public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        getSupportActionBar().setTitle("Conversations");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_fragment, MessagesFragment.newInstance())
                .commitAllowingStateLoss();
    }
}
