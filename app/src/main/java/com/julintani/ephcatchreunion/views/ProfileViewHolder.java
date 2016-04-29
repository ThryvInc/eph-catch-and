package com.julintani.ephcatchreunion.views;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
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

    protected View mLikeView;
    protected View mSuperLikeView;
    protected View mCatchUpView;

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
        }else {
            mLikeView = view.findViewById(R.id.ll_like);
            mSuperLikeView = view.findViewById(R.id.ll_super_like);
            mCatchUpView = view.findViewById(R.id.ll_catch_up);

            mLikeView.setVisibility(View.GONE);
            mSuperLikeView.setVisibility(View.GONE);
            mCatchUpView.setVisibility(View.GONE);

            setDrawableForActionView(mLikeView, R.drawable.heart_icon_white);
            setDrawableForActionView(mSuperLikeView, R.drawable.star);
            setDrawableForActionView(mCatchUpView, R.drawable.cat);
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
                setLikes(user);
            }

            new ItemLongPressHelper(mCallback).attachToViewHolder(this);
        }
    }

    public void setLikes(User user){
        if (user.hasMatchedWith(User.getCurrentUser())){
            mLikeView.setVisibility(View.VISIBLE);
            selectActionView(mLikeView);
        }else if (User.getCurrentUser().doesLike(user)){
            mLikeView.setVisibility(View.VISIBLE);
        }
        if (user.hasSuperLiked(User.getCurrentUser())){
            mSuperLikeView.setVisibility(View.VISIBLE);
            selectActionView(mSuperLikeView);
        }else if (User.getCurrentUser().hasSuperLiked(user)){
            mSuperLikeView.setVisibility(View.VISIBLE);
        }
        if (user.doesWantToCatchUpWith(User.getCurrentUser())){
            mCatchUpView.setVisibility(View.VISIBLE);
            selectActionView(mCatchUpView);
        }else if (User.getCurrentUser().doesWantToCatchUpWith(user)){
            mCatchUpView.setVisibility(View.VISIBLE);
        }
    }

    private void setDrawableForActionView(View actionView, int resId){
        ImageButton actionButton = (ImageButton)actionView.findViewById(R.id.btn_action);
        actionButton.setImageDrawable(itemView.getResources().getDrawable(resId));
    }

    private void selectActionView(View actionView){
        ImageButton actionButton = (ImageButton)actionView.findViewById(R.id.btn_action);
        actionButton.setSelected(true);
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
