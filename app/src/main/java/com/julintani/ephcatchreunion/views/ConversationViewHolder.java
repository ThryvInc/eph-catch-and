package com.julintani.ephcatchreunion.views;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.listeners.OnConversationClickedListener;
import com.julintani.ephcatchreunion.models.Conversation;
import com.julintani.ephcatchreunion.models.User;

import java.nio.charset.Charset;

/**
 * Created by ell on 12/13/15.
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder {
    private OnConversationClickedListener mListener;
    private SimpleDraweeView mUserImageView;
    private TextView mUsernameTextView;
    private TextView mMessageTextView;
    private TextView mTimeAgoTextView;
    private ImageView mDeleteImageView;

    public ConversationViewHolder(View itemView, OnConversationClickedListener listener) {
        super(itemView);

        mUserImageView = (SimpleDraweeView)itemView.findViewById(R.id.iv_user_image);
        mUsernameTextView = (TextView)itemView.findViewById(R.id.tv_username);
        mMessageTextView = (TextView)itemView.findViewById(R.id.tv_notification);
        mTimeAgoTextView = (TextView)itemView.findViewById(R.id.tv_time_ago);
        mDeleteImageView = (ImageView) itemView.findViewById(R.id.iv_delete);

        mListener = listener;

//        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/MuseoSans_100.otf");
//        mUsernameTextView.setTypeface(font);
//        mMessageTextView.setTypeface(font);
//        mTimeAgoTextView.setTypeface(font);
    }

    public void display(final Conversation conversation){
        String otherUserId = "";
        for (String objectId : conversation.getParticipants()){
            if (!objectId.equals(ParseUser.getCurrentUser().getObjectId())) otherUserId = objectId;
        }
        User.userForId(otherUserId, new User.UserLoadedFromIdCallback() {
            @Override
            public void onUserLoadedFromId(User user) {
                setUser(user);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConversationClicked(conversation);
            }
        });

        if (conversation.getLastMessage() != null
                && conversation.getLastMessage().getMessageParts().get(0).getMimeType().startsWith("text")) {
            mMessageTextView.setText(new String(conversation.getLastMessage().getMessageParts().get(0).getData(), Charset.defaultCharset()));
            mTimeAgoTextView.setText(DateUtils.getRelativeTimeSpanString(conversation.getLastMessage().getSentAt().getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }else {
            mMessageTextView.setText("");
            mTimeAgoTextView.setText("");
        }

        mDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConversationDeleteClicked(conversation);
            }
        });

        mDeleteImageView.bringToFront();
    }

    private void setUser(User user){
        WeedCastRowViewHolder.displayUserThumbnailImage(user.getProfileImageThumbnail(), mUserImageView);
        mUsernameTextView.setText(user.getUsername());
    }
}
