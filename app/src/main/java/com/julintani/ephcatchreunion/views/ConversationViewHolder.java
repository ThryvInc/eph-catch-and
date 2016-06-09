package com.julintani.ephcatchreunion.views;

import android.net.Uri;
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

    public ConversationViewHolder(View itemView, OnConversationClickedListener listener) {
        super(itemView);

        mUserImageView = (SimpleDraweeView)itemView.findViewById(R.id.iv_user_image);
        mUsernameTextView = (TextView)itemView.findViewById(R.id.tv_username);
        mMessageTextView = (TextView)itemView.findViewById(R.id.tv_notification);

        mListener = listener;
    }

    public void display(final Conversation conversation){
        mUserImageView.setImageURI(Uri.parse(conversation.getUser().getImageUrl()));
        mUsernameTextView.setText(conversation.getUser().getName());
        mMessageTextView.setText(conversation.getLastMessageText());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConversationClicked(conversation);
            }
        });
    }
}
