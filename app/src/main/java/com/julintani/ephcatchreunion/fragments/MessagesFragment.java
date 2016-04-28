package com.julintani.ephcatchreunion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.activities.ConversationActivity;
import com.julintani.ephcatchreunion.adapter.MessagesAdapter;
import com.julintani.ephcatchreunion.listeners.OnConversationClickedListener;
import com.julintani.ephcatchreunion.models.Conversation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 12/12/15.
 */
public class MessagesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MessagesAdapter mAdapter;
    private List<Conversation> mConversations = new ArrayList<>();

    public static MessagesFragment newInstance(){
        return new MessagesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_messages);

        mAdapter = new MessagesAdapter(mConversations, new OnConversationClickedListener() {
            @Override
            public void onConversationClicked(Conversation conversation) {
                Intent intent = new Intent(getContext(), ConversationActivity.class);
                intent.putExtra(ConversationActivity.CONVERSATION_KEY, conversation);
                startActivity(intent);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        updateMessages();

        return view;
    }

    private void updateMessages(){
        mConversations.addAll(Conversation.generateConversations());
        mAdapter.notifyDataSetChanged();
    }

    protected void showProgress(boolean shouldShow){
        if (shouldShow){
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
