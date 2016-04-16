package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.helpers.ItemLongPressHelper;
import com.julintani.ephcatchreunion.models.ActionAnimationHelper;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

import butterknife.ButterKnife;

/**
 * Created by ell on 3/26/16.
 */
public class OnboardingActivity extends AppCompatActivity {
    private float mXCoordinate;
    private float mYCoordinate;

    protected View mSmallCard;
    protected View mLargeCard;

    protected TextView mOnboardingTopTextView;
    protected TextView mOnboardingBottomTextView;

    protected View mLikeView;
    protected View mSuperLikeView;
    protected View mMessageView;
    protected View mCatchUpView;
    protected View mExitView;
    protected View mTopExampleView;
    protected View mBottomExampleView;

    protected ImageButton mLikeButton;
    protected ImageButton mSuperLikeButton;
    protected ImageButton mMessageButton;
    protected ImageButton mCatchUpButton;

    protected ImageButton mExitButton;
    protected ImageButton mTopExampleButton;
    protected ImageButton mBottomExampleButton;

    protected ProfileViewHolder mSmallCardProfileHolder;

    protected ItemLongPressHelper.Callback mCallback = new ItemLongPressHelper.Callback() {
        @Override
        public void onLongClick(RecyclerView.ViewHolder viewHolder, float xCoordinate, float yCoordinate) {
            float pixelsPerDip = getResources().getDisplayMetrics().density;
            View[] buttonsArray = { mLikeView, mSuperLikeView, mMessageView, mCatchUpView };
            ActionAnimationHelper.animateButtons(true, xCoordinate, yCoordinate, pixelsPerDip, buttonsArray, mExitView);

            mXCoordinate = xCoordinate;
            mYCoordinate = yCoordinate;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        View card = findViewById(R.id.card_profile);
        final ProfileViewHolder profileViewHolder = new ProfileViewHolder(card, ProfileViewHolder.ProfileType.LARGE, null);
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

        loadViews();
        mSmallCard.setAlpha(0f);
        likeStep();
    }

    protected void loadViews(){
        mSmallCard = findViewById(R.id.card_profile_small);
        mLargeCard = findViewById(R.id.card_profile);

        mLikeView = findViewById(R.id.ll_like);
        mSuperLikeView = findViewById(R.id.ll_super_like);
        mCatchUpView = findViewById(R.id.ll_catch_up);
        mMessageView = findViewById(R.id.ll_messages);
        mExitView = findViewById(R.id.ll_exit);
        mTopExampleView = findViewById(R.id.ll_top_example);
        mBottomExampleView = findViewById(R.id.ll_bottom_example);

        mLikeView.setVisibility(View.GONE);
        mSuperLikeView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        mCatchUpView.setVisibility(View.GONE);
        mExitView.setVisibility(View.GONE);

        mLikeView = mLargeCard.findViewById(R.id.ll_like);
        mSuperLikeView = mLargeCard.findViewById(R.id.ll_super_like);
        mCatchUpView = mLargeCard.findViewById(R.id.ll_catch_up);
        mMessageView = mLargeCard.findViewById(R.id.ll_messages);

        mLikeButton = (ImageButton)mLikeView.findViewById(R.id.btn_action);
        mSuperLikeButton = (ImageButton)mSuperLikeView.findViewById(R.id.btn_action);
        mCatchUpButton = (ImageButton)mCatchUpView.findViewById(R.id.btn_action);
        mMessageButton = (ImageButton)mMessageView.findViewById(R.id.btn_action);

        mTopExampleButton = (ImageButton) mTopExampleView.findViewById(R.id.btn_action);
        mBottomExampleButton = (ImageButton) mBottomExampleView.findViewById(R.id.btn_action);

        mLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_icon_white));
        mSuperLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.star));
        mMessageButton.setImageDrawable(getResources().getDrawable(R.drawable.messages));
        mCatchUpButton.setImageDrawable(getResources().getDrawable(R.drawable.cat));

        mOnboardingTopTextView = (TextView)findViewById(R.id.tv_onboarding1);
        mOnboardingBottomTextView = (TextView)findViewById(R.id.tv_onboarding2);
    }

    protected void likeStep(){
        mTopExampleButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_icon_white));
        nextAnimation("Like a user by pressing",
                "We won't notify them unless you both like each other.",
                getResources().getDrawable(R.drawable.heart_icon_white), null);

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                superLikeStep();
            }
        });
    }

    protected void superLikeStep(){
        nextAnimation("Super like a user by pressing",
                "We'll let them know you like them regardless.",
                getResources().getDrawable(R.drawable.star), null);

        clearClickListeners();
        mSuperLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catchUpStep();
            }
        });
    }

    protected void catchUpStep(){
        nextAnimation("If you just want to catch up, press", "",
                getResources().getDrawable(R.drawable.cat), null);

        clearClickListeners();
        mCatchUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longPressStep();
            }
        });
    }

    protected void longPressStep(){
        nextAnimation("On the main screen, long press a user to bring up options",
                "To continue, long press on the card and click the message button",
                null, getResources().getDrawable(R.drawable.messages));

        mLargeCard.animate().alpha(0f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                mSmallCard.animate().alpha(1f).setDuration(100).start();
            }
        }).start();

        mLikeView = findViewById(R.id.ll_like);
        mSuperLikeView = findViewById(R.id.ll_super_like);
        mCatchUpView = findViewById(R.id.ll_catch_up);
        mMessageView = findViewById(R.id.ll_messages);
        mExitView = findViewById(R.id.ll_exit);

        mLikeButton = (ImageButton)mLikeView.findViewById(R.id.btn_action);
        mSuperLikeButton = (ImageButton)mSuperLikeView.findViewById(R.id.btn_action);
        mCatchUpButton = (ImageButton)mCatchUpView.findViewById(R.id.btn_action);
        mMessageButton = (ImageButton)mMessageView.findViewById(R.id.btn_action);
        mExitButton = (ImageButton)mExitView.findViewById(R.id.btn_action);

        mLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_icon_white));
        mSuperLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.star));
        mMessageButton.setImageDrawable(getResources().getDrawable(R.drawable.messages));
        mCatchUpButton.setImageDrawable(getResources().getDrawable(R.drawable.cat));
        mExitButton.setImageDrawable(getResources().getDrawable(R.drawable.x_mark));

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float pixelsPerDip = getResources().getDisplayMetrics().density;
                View[] buttonsArray = { mLikeView, mSuperLikeView, mMessageView, mCatchUpView };
                ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, buttonsArray, mExitView);
            }
        });

        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float pixelsPerDip = getResources().getDisplayMetrics().density;
                View[] buttonsArray = { mLikeView, mSuperLikeView, mMessageView, mCatchUpView };
                ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, buttonsArray, mExitView);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        mSmallCardProfileHolder = new ProfileViewHolder(mSmallCard, ProfileViewHolder.ProfileType.MINIFIED, mCallback);
        mSmallCardProfileHolder.displayUser(User.generateDummyUser());
    }

    protected void nextAnimation(final String topString, final String bottomString,
                                 final Drawable topDrawable, final Drawable bottomDrawable){
        switchViewAnimation(mOnboardingTopTextView, 1, new Runnable() {
            @Override
            public void run() {
                mOnboardingTopTextView.setText(topString);
            }
        });
        switchViewAnimation(mTopExampleView, 1, new Runnable() {
            @Override
            public void run() {
                if (topDrawable != null){
                    mTopExampleButton.setVisibility(View.VISIBLE);
                    mTopExampleButton.setImageDrawable(topDrawable);
                }else {
                    mTopExampleButton.setVisibility(View.GONE);
                }
            }
        });
        switchViewAnimation(mOnboardingBottomTextView, -1, new Runnable() {
            @Override
            public void run() {
                mOnboardingBottomTextView.setText(bottomString);
            }
        });
        switchViewAnimation(mBottomExampleView, 1, new Runnable() {
            @Override
            public void run() {
                if (bottomDrawable != null){
                    mBottomExampleButton.setVisibility(View.VISIBLE);
                    mBottomExampleButton.setImageDrawable(bottomDrawable);
                }else {
                    mBottomExampleButton.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void switchViewAnimation(final View view, float direction, final Runnable changeViewRunnable){
        final int duration = 200;
        final float distance = 100 * direction;

        view.animate()
                .alpha(0f)
                .setDuration(duration)
                .translationYBy(distance)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        changeViewRunnable.run();
                        view.setY(view.getY() - 2 * distance);
                        view.animate()
                                .alpha(1f)
                                .setDuration(duration)
                                .translationYBy(distance)
                                .start();
                    }
                })
                .start();
    }

    protected void clearClickListeners(){
        mLikeButton.setOnClickListener(null);
        mSuperLikeButton.setOnClickListener(null);
        mMessageButton.setOnClickListener(null);
        mCatchUpButton.setOnClickListener(null);
    }
}
