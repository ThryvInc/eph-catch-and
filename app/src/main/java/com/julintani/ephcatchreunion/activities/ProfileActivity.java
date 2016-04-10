package com.julintani.ephcatchreunion.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

    @Bind(R.id.ll_like)
    protected View mLikeView;
    @Bind(R.id.ll_super_like)
    protected View mSuperLikeView;
    @Bind(R.id.ll_messages)
    protected View mMessageView;
    @Bind(R.id.ll_catch_up)
    protected View mCatchUpView;

    protected Button mLikeButton;
    protected Button mSuperLikeButton;
    protected Button mMessageButton;
    protected Button mCatchUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mUser = (User) extras.getSerializable(USER_KEY);
        }

        ProfileViewHolder profileViewHolder = new ProfileViewHolder(findViewById(R.id.profile), ProfileViewHolder.ProfileType.LARGE, null);
        profileViewHolder.setTransitions();
        profileViewHolder.displayUser(mUser);

        loadViews();
    }

    protected void loadViews(){
        mLikeButton = (Button) mLikeView.findViewById(R.id.btn_action);
        mSuperLikeButton = (Button) mSuperLikeView.findViewById(R.id.btn_action);
        mMessageButton = (Button) mMessageView.findViewById(R.id.btn_action);
        mCatchUpButton = (Button) mCatchUpView.findViewById(R.id.btn_action);

        mLikeButton.setText("L");
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LikeOnClickListener().onClickUserAction(mLikeView, mUser);
            }
        });
        mSuperLikeButton.setText("S");
        mSuperLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SuperLikeOnClickListener().onClickUserAction(mLikeView, mUser);
            }
        });
        mMessageButton.setText("M");
        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MessageOnClickListener().onClickUserAction(mLikeView, mUser);
            }
        });
        mCatchUpButton.setText("C");
        mCatchUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CatchUpOnClickListener().onClickUserAction(mLikeView, mUser);
            }
        });
    }
}
