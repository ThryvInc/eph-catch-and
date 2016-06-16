package com.julintani.ephcatchreunion.views;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.activities.ProfileActivity;
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

    public void display(Message message, final User user){
        if (user != null){
            mUserImageView.setImageURI(Uri.parse(user.getImageUrl()));
            mUserImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_KEY, user);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
        mMessageTextView.setText(message.getBody());
    }
}
