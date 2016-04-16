package com.julintani.ephcatchreunion.views;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.helpers.ItemLongPressHelper;
import com.julintani.ephcatchreunion.models.User;

/**
 * Created by ell on 3/24/16.
 */
public class ProfileViewHolder extends RecyclerView.ViewHolder {
    public static final String PROFILE_IMAGE_TRANSITION = "PROFILE_IMAGE_TRANSITION";
//    public static final String LIKE_BUTTON_TRANSITION = "LIKE_BUTTON_TRANSITION";
//    public static final String SUPER_LIKE_BUTTON_TRANSITION = "SUPER_LIKE_BUTTON_TRANSITION";
//    public static final String MESSAGE_BUTTON_TRANSITION = "MESSAGE_BUTTON_TRANSITION";
//    public static final String CATCH_UP_BUTTON_TRANSITION = "CATCH_UP_BUTTON_TRANSITION";
//    public static final String NAME_TRANSITION = "NAME_TRANSITION";
//    public static final String JOB_TRANSITION = "JOB_TRANSITION";

    public enum ProfileType {
        MINIFIED, LARGE
    }

    protected ImageView mUserImageView;

    protected TextView mNameTextView;
    protected TextView mMajorTextView;
    protected TextView mExtraCurricularsTextView;
    protected TextView mJobTextView;

    private User mUser;
    private ProfileType mType;
    private ItemLongPressHelper.Callback mCallback;

    public ProfileViewHolder(View itemView, ProfileType type, ItemLongPressHelper.Callback callback) {
        super(itemView);
        mType = type;
        mCallback = callback;
        loadViews(itemView);
    }

    protected void loadViews(View view){
        mUserImageView = (ImageView) view.findViewById(R.id.iv_user);
        mNameTextView = (TextView) view.findViewById(R.id.tv_name);
        mJobTextView = (TextView) view.findViewById(R.id.tv_job);

        if (mType == ProfileType.LARGE){
            mMajorTextView = (TextView) view.findViewById(R.id.tv_major);
            mExtraCurricularsTextView = (TextView) view.findViewById(R.id.tv_extra_curriculars);
        }
    }

    public void setTransitions(){
        ViewCompat.setTransitionName(mUserImageView, PROFILE_IMAGE_TRANSITION);

//        ViewCompat.setTransitionName(mLikeButton, LIKE_BUTTON_TRANSITION);
//        ViewCompat.setTransitionName(mSuperLikeButton, SUPER_LIKE_BUTTON_TRANSITION);
//        ViewCompat.setTransitionName(mMessageButton, MESSAGE_BUTTON_TRANSITION);
//        ViewCompat.setTransitionName(mCatchUpButton, CATCH_UP_BUTTON_TRANSITION);
    }

    public void displayUser(User user){
        if (user != null){
            mUser = user;
            mNameTextView.setText(user.getName());
            mJobTextView.setText(user.getJob());
            Glide.with(this.itemView.getContext())
                    .load(user.getImageUrl())
                    .centerCrop()
                    .into(mUserImageView);

            if (mType == ProfileType.LARGE){
                mMajorTextView.setText(user.getMajor());
                mExtraCurricularsTextView.setText(user.getExtraCurriculars());
            }

            new ItemLongPressHelper(mCallback).attachToViewHolder(this);
        }
    }

    public User getUser(){
        return mUser;
    }

    public ImageView getUserImageView() {
        return mUserImageView;
    }

    public TextView getNameTextView() {
        return mNameTextView;
    }

    public TextView getJobTextView() {
        return mJobTextView;
    }
}
