package com.julintani.ephcatchreunion.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.models.Message;
import com.julintani.ephcatchreunion.models.User;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by ell on 12/13/15.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
    private User mUser;
    private SimpleDraweeView mUserImageView;
    private TextView mMessageTextView;
    private TextView mTimeAgoTextView;


    public MessageViewHolder(Context context, View itemView, User otherUser) {
        super(itemView);
        mUser = otherUser;
        mUserImageView = (SimpleDraweeView) itemView.findViewById(R.id.iv_user);
        mMessageTextView = (TextView) itemView.findViewById(R.id.tv_message);

//        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/MuseoSans_100.otf");
//        mMessageTextView.setTypeface(font);
    }

    public void display(Message message){
        if (message.getSender().getUserId().equals(mUser.getObjectId())){
            WeedCastRowViewHolder.displayUserThumbnailImage(mUser.getProfileImageThumbnail(), mUserImageView);
        }else {
            WeedCastRowViewHolder.displayUserThumbnailImage(User.getCurrentUser().getProfileImageThumbnail(), mUserImageView);
        }

        List<MessagePart> messageParts = message.getMessageParts();
        for (MessagePart messagePart : messageParts){
            if (messagePart.getMimeType().equals("text/plain")){
                mMessageTextView.setText(new String(messagePart.getData(), Charset.defaultCharset()));
            }
        }
    }
}
