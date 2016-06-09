package com.julintani.ephcatchreunion.views;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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

    public ProfileViewHolder(View itemView, ProfileType type) {
        super(itemView);
        mType = type;
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
    }

    public void displayUser(User user){
        if (user != null){
            mUser = user;
            mNameTextView.setText(user.getName());
            mJobTextView.setEllipsize(TextUtils.TruncateAt.END);
            mJobTextView.setText(user.getCurrentActivity());
            Glide.with(this.itemView.getContext())
                    .load(user.getImageUrl())
                    .centerCrop()
                    .into(mUserImageView);

            if (mType == ProfileType.LARGE){
                mMajorTextView.setText(user.getMajor());
                mExtraCurricularsTextView.setText(user.getExtracurriculars());
            }
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
