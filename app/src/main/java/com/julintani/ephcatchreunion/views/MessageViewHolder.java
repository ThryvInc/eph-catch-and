package com.julintani.ephcatchreunion.views;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.models.Message;
import com.julintani.ephcatchreunion.models.User;

/**
 * Created by ell on 12/13/15.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
    private SimpleDraweeView mUserImageView;
    private TextView mMessageTextView;


    public MessageViewHolder(View itemView, User otherUser) {
        super(itemView);
        mUserImageView = (SimpleDraweeView) itemView.findViewById(R.id.iv_user);
        mMessageTextView = (TextView) itemView.findViewById(R.id.tv_message);
    }

    public void display(Message message, User user){
        if (user != null){
            mUserImageView.setImageURI(Uri.parse(user.getImageUrl()));
        }
        mMessageTextView.setText(message.getBody());
    }
}
