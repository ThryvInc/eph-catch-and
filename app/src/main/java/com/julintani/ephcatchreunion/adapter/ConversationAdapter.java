package com.julintani.ephcatchreunion.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.julintani.ephcatchreunion.models.Message;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.MessageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 12/13/15.
 */
public class ConversationAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private User mUser;
    private List<Message> mMessages = new ArrayList<>();

    public ConversationAdapter(List<Message> messages, User otherUser){
        mMessages = messages;
        mUser = otherUser;
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getSender().getUserId().equals(User.getCurrentUser().getObjectId()) ? 0 : 1;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MessageViewHolder(parent.getContext(),
                inflater.inflate(viewType == 0 ? R.layout.item_message_from_self : R.layout.item_message_from_other,
                        parent, false),
                mUser);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.display(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
