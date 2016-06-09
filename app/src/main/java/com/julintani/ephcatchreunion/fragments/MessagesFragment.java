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

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.activities.ConversationActivity;
import com.julintani.ephcatchreunion.adapter.MessagesAdapter;
import com.julintani.ephcatchreunion.listeners.OnConversationClickedListener;
import com.julintani.ephcatchreunion.models.Conversation;
import com.julintani.ephcatchreunion.models.ConversationParser;
import com.julintani.ephcatchreunion.models.ConvoHolder;
import com.julintani.ephcatchreunion.models.Event;
import com.julintani.ephcatchreunion.models.EventHolder;
import com.julintani.ephcatchreunion.models.ServerInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void onResume() {
        super.onResume();
        updateMessages();
    }

    private void updateMessages(){
        showProgress(true);
        JsonObjectRequest request = new JsonObjectRequest(ServerInfo.BASE_URL + "conversations?page-size=200", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Conversation> conversations = ConversationParser.parse(getActivity(), response);
                mConversations.removeAll(mConversations);
                mConversations.addAll(conversations);
                mAdapter.notifyDataSetChanged();
                showProgress(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showProgress(false);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(getActivity()));
                return headers;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
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
