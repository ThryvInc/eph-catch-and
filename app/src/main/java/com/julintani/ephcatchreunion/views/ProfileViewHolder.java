package com.julintani.ephcatchreunion.views;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.julintani.ephcatchreunion.R;
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

    protected View mLikeView;
    protected View mSuperLikeView;
    protected View mMessageView;
    protected View mCatchUpView;

    protected Button mLikeButton;
    protected Button mSuperLikeButton;
    protected Button mMessageButton;
    protected Button mCatchUpButton;

    protected TextView mNameTextView;
    protected TextView mMajorTextView;
    protected TextView mExtraCurricularsTextView;
    protected TextView mJobTextView;

    private User mUser;
    private ProfileType mType;

    public ProfileViewHolder(View itemView, ProfileType type) {
        super(itemView);
        mType = type;
        loadViews(itemView);
    }

    protected void loadViews(View view){
        mUserImageView = (ImageView) view.findViewById(R.id.iv_user);
        mNameTextView = (TextView) view.findViewById(R.id.tv_name);
        mJobTextView = (TextView) view.findViewById(R.id.tv_job);

        mLikeView = view.findViewById(R.id.ll_like);
        mSuperLikeView = view.findViewById(R.id.ll_super_like);
        mMessageView = view.findViewById(R.id.ll_messages);
        mCatchUpView = view.findViewById(R.id.ll_catch_up);

        mLikeButton = (Button) mLikeView.findViewById(R.id.btn_action);
        mSuperLikeButton = (Button) mSuperLikeView.findViewById(R.id.btn_action);
        mMessageButton = (Button) mMessageView.findViewById(R.id.btn_action);
        mCatchUpButton = (Button) mCatchUpView.findViewById(R.id.btn_action);

        if (mType == ProfileType.LARGE){
            mMajorTextView = (TextView) view.findViewById(R.id.tv_major);
            mExtraCurricularsTextView = (TextView) view.findViewById(R.id.tv_extra_curriculars);
        }else {
            mLikeView.setVisibility(View.GONE);
            mSuperLikeView.setVisibility(View.GONE);
            mMessageView.setVisibility(View.GONE);
            mCatchUpView.setVisibility(View.GONE);
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
            }else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
    }

    public User getUser(){
        return mUser;
    }

    public ImageView getUserImageView() {
        return mUserImageView;
    }

    public Button getLikeButton() {
        return mLikeButton;
    }

    public Button getSuperLikeButton() {
        return mSuperLikeButton;
    }

    public Button getMessageButton() {
        return mMessageButton;
    }

    public Button getCatchUpButton() {
        return mCatchUpButton;
    }

    public TextView getNameTextView() {
        return mNameTextView;
    }

    public TextView getJobTextView() {
        return mJobTextView;
    }
}
