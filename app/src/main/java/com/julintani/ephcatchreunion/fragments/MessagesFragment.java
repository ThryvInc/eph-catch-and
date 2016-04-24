package com.julintani.ephcatchreunion.fragments;

import android.app.Activity;
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
import com.julintani.ephcatchreunion.adapter.MessagesAdapter;
import com.julintani.ephcatchreunion.models.Conversation;
import com.julintani.ephcatchreunion.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 12/12/15.
 */
public class MessagesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mNewConvoButton;
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
                ((SocialHighActivity) getActivity()).showProgress(true, "Puff, puff, load...");
                String otherUserId = "";
                for (String objectId : conversation.getParticipants()) {
                    if (!objectId.equals(ParseUser.getCurrentUser().getObjectId()))
                        otherUserId = objectId;
                }
                LayerManager.storedConversation = conversation;
                User.userForId(otherUserId, new User.UserLoadedFromIdCallback() {
                    @Override
                    public void onUserLoadedFromId(User user) {
                        if (getActivity() != null) {
                            ((SocialHighActivity) getActivity()).showProgress(false, null);
                        }
                        User.storedUser = user;
                        startActivity(new Intent(getActivity(), ConversationActivity.class));
                    }
                });
            }

            @Override
            public void onConversationDeleteClicked(Conversation conversation) {
                int position = mConversations.indexOf(conversation);
                mConversations.remove(position);
                conversation.delete(LayerClient.DeletionMode.ALL_PARTICIPANTS);
                mAdapter.notifyItemRemoved(position);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mNewConvoButton = (FloatingActionButton) view.findViewById(R.id.btn_message);
        mNewConvoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ConversationActivity.class));
            }
        });

        updateMessages();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Localytics.tagScreen("MessagesFragment");
        }
    }

    private void updateMessages(){
        String userId = getContext().getSharedPreferences("user", Activity.MODE_PRIVATE).getString("objectId", "");
        if (userId != null){
            ArrayList userIds = new ArrayList();
            userIds.add(ParseUser.getCurrentUser().getObjectId());

            Query query = Query.builder(Conversation.class)
                    .predicate(new Predicate(Conversation.Property.PARTICIPANTS, Predicate.Operator.IN, userIds))
                    .sortDescriptor(new SortDescriptor(Conversation.Property.LAST_MESSAGE_RECEIVED_AT, SortDescriptor.Order.DESCENDING))
                    .limit(20)
                    .build();

            mConversations.removeAll(mConversations);
            mConversations.addAll(LayerManager.getStaticLayerClient().executeQuery(query, Query.ResultType.OBJECTS));
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void showProgress(boolean shouldShow){
        if (shouldShow){
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mNewConvoButton.setVisibility(View.GONE);
        }else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mNewConvoButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onChangeEvent(LayerChangeEvent layerChangeEvent) {
        updateMessages();
    }
}
