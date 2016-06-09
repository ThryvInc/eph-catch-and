package com.julintani.ephcatchreunion.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.adapter.ConversationAdapter;
import com.julintani.ephcatchreunion.models.Conversation;
import com.julintani.ephcatchreunion.models.ConversationParser;
import com.julintani.ephcatchreunion.models.Event;
import com.julintani.ephcatchreunion.models.EventHolder;
import com.julintani.ephcatchreunion.models.Message;
import com.julintani.ephcatchreunion.models.MessageHolder;
import com.julintani.ephcatchreunion.models.MessagingManager;
import com.julintani.ephcatchreunion.models.NewMessageHolder;
import com.julintani.ephcatchreunion.models.ServerInfo;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.services.ReunionGcmListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ell on 12/13/15.
 */
public class ConversationActivity extends AppCompatActivity {
    public static final String CONVERSATION_KEY = "conversation";
    private RecyclerView mRecyclerView;
    private EditText mMessageEditText;

    private Conversation mConversation;
    private List<Message> mMessages = new ArrayList<>();
    private ConversationAdapter mConversationAdapter;
    private User otherUser;
    private BroadcastReceiver mMsgReceivedBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_messages);
        mMessageEditText = (EditText) findViewById(R.id.et_message);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mConversation = (Conversation) extras.getSerializable(CONVERSATION_KEY);
            if (mConversation != null) {
                setUser(mConversation.getUser());
            } else {
                otherUser = (User) extras.getSerializable("otherUser");
                setUser(otherUser);
            }
        }

        View sendButton = findViewById(R.id.btn_send);
        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String body = mMessageEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(body)) {
                        if (body.length() < 100) {
                            if (mConversation != null) {
                                sendMessage(body);
                            }else {
                                sendNewMessage(body);
                            }
                        }else {
                            Toast.makeText(ConversationActivity.this, "Messages can only be 100 characters long, but yours is " + body.length(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

        mMsgReceivedBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateMessages();
            }
        };
        registerReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMsgReceivedBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    protected void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mMsgReceivedBroadcastReceiver,
                    new IntentFilter(ReunionGcmListenerService.MESSAGE_RECEIVED));
            isReceiverRegistered = true;
        }
    }

    private void sendNewMessage(String body){
        Map<String, Integer> userMap = new HashMap<>();
        userMap.put("user_id", otherUser.getId());

        List<Map> participantsList = new ArrayList<>(1);
        participantsList.add(userMap);

        Map<String, String> message = new HashMap<>();
        message.put("body", body);

        List<Map> messageList = new ArrayList<>(1);
        messageList.add(message);

        Map<String, List> messagesMap = new HashMap<>();
        messagesMap.put("messages", messageList);
        messagesMap.put("participants", participantsList);

        NewMessageHolder holder = new NewMessageHolder();
        holder.setConversation(messagesMap);

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ServerInfo.BASE_URL + "conversations", new JSONObject(new Gson().toJson(holder)), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    List<Conversation> conversations = ConversationParser.parse(ConversationActivity.this, response);
                    if (conversations.size() != 0){
                        mConversation = conversations.get(0);
                        updateMessages();
                        mMessageEditText.setText("");
                        MessagingManager.getInstance().refreshConversations(ConversationActivity.this);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                    headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(ConversationActivity.this));
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String body){
        Map<String, String> message = new HashMap<>();
        message.put("body", body);

        Map<String, Map> messageMap = new HashMap<>();
        messageMap.put("message", message);

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    ServerInfo.BASE_URL + "conversations/" + mConversation.getId() + "/messages",
                    new JSONObject(new Gson().toJson(messageMap)),
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    updateMessages();
                    mMessageEditText.setText("");
                    MessagingManager.getInstance().refreshConversations(ConversationActivity.this);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                    headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(ConversationActivity.this));
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUser(User user){
        otherUser = user;
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(otherUser.getName());
        if (getActionBar() != null) getActionBar().setTitle(otherUser.getName());

        mConversationAdapter = new ConversationAdapter(mMessages, otherUser);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mConversationAdapter);

        updateMessages();
    }

    private void updateMessages(){
        Conversation conversation = getConversation();
        if (conversation != null){
            JsonObjectRequest request = new JsonObjectRequest(ServerInfo.BASE_URL + "conversations/" + String.valueOf(conversation.getId()) + "/messages?page-size=70", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONArray data = response.getJSONArray("data");
                        List<MessageHolder> holders = new Gson().fromJson(data.toString(), new TypeToken<ArrayList<MessageHolder>>(){}.getType());
                        List<Message> messages = new ArrayList<>(holders.size());
                        for (MessageHolder holder : holders){
                            Message message = holder.getAttributes();
                            message.setUserId(Integer.valueOf((String)holder.getRelationship().get("user").get("data").get("id")));
                            messages.add(message);
                        }
                        mMessages.removeAll(mMessages);
                        mMessages.addAll(messages);
                        Collections.reverse(mMessages);
                        mConversationAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mMessages.size() - 1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                    headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(ConversationActivity.this));
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }
    }

    private Conversation getConversation(){
        if (mConversation == null && otherUser != null){
            for (Conversation conversation : MessagingManager.getInstance().getConversations()){
                if (conversation.getUser().getId() == otherUser.getId()){
                    mConversation = conversation;
                }
            }
        }
        return mConversation;
    }
}
