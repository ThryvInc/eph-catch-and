package com.julintani.ephcatchreunion.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.listeners.OnConversationClickedListener;
import com.julintani.ephcatchreunion.models.Conversation;
import com.julintani.ephcatchreunion.views.ConversationViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 12/13/15.
 */
public class MessagesAdapter extends RecyclerView.Adapter<ConversationViewHolder> {
    private List<Conversation> mConversations = new ArrayList<>();
    private OnConversationClickedListener mListener;

    public MessagesAdapter(List<Conversation> conversations, OnConversationClickedListener listener){
        mConversations = conversations;
        mListener = listener;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ConversationViewHolder(inflater.inflate(R.layout.item_conversation, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        holder.display(mConversations.get(position));
    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }
}
