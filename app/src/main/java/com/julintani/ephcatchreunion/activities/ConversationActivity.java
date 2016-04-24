package com.julintani.ephcatchreunion.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.julintani.ephcatchreunion.adapter.ConversationAdapter;
import com.julintani.ephcatchreunion.models.Conversation;
import com.julintani.ephcatchreunion.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by ell on 12/13/15.
 */
public class ConversationActivity extends AppCompatActivity {
    public static final String CONVERSATION_ID_KEY = "layer-conversation-id";
    private AutoCompleteTextView mToTextField;
    private RecyclerView mRecyclerView;
    private EditText mMessageEditText;

    private Conversation mConversation;
    private List<Message> mMessages = new ArrayList<>();
    private List<User> mUsers = new ArrayList<>();
    private ConversationAdapter mConversationAdapter;
    private User otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Bundle extras = getIntent().getExtras();
//        if (extras != null){
//            Uri convoId = extras.getParcelable(CONVERSATION_ID_KEY);
//            Conversation conversation = LayerManager.getStaticLayerClient().getConversation(convoId);
//            if (conversation != null && conversation.getParticipants() != null){
//                for (String objectId : conversation.getParticipants()){
//                    if (objectId != ParseUser.getCurrentUser().getObjectId()){
//                        User.userForId(objectId, new User.UserLoadedFromIdCallback() {
//                            @Override
//                            public void onUserLoadedFromId(User user) {
//                                setUser(user);
//                            }
//                        });
//                    }
//                }
//            }
//            mConversation = conversation;
//        }

        mToTextField = (AutoCompleteTextView) findViewById(R.id.atv_message_to);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_messages);
        mMessageEditText = (EditText) findViewById(R.id.et_message);

//            ParseQuery<ParseUser> query = ParseUser.getQuery();
//            query.whereContainedIn("objectId", User.getCurrentUser().getConnectsObjectIds());
//            query.setLimit(1500);
//            query.findInBackground(new FindCallback<ParseUser>() {
//                @Override
//                public void done(List<ParseUser> users, ParseException e) {
//                    mUsers.addAll(users);
//                }
//            });
//            mUserListAdapter = new UserListAdapter(this, mUsers);
//            mUserListAdapter.setShouldAllowEditing(false);
//            mToTextField.setVisibility(View.VISIBLE);
//            mToTextField.setAdapter(mUserListAdapter);
//            mToTextField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    setUser((User) mUserListAdapter.getItem(position));
//                    mMessageEditText.requestFocus();
//                }
//            });

        findViewById(R.id.photoImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MessagePart messagePart = LayerManager.getStaticLayerClient()
//                        .newMessagePart(mMessageEditText.getText().toString());
//
//                MessageOptions options = new MessageOptions();
//                options.pushNotificationMessage(mMessageEditText.getText().toString());
//
//                final Message message = LayerManager.getStaticLayerClient()
//                        .newMessage(options, Arrays.asList(messagePart));
//
//                if (otherUser != null){
//                    getConversation().send(message);
//                    mMessageEditText.setText("");
//                }else {
//                    ParseQuery<ParseUser> query = ParseUser.getQuery();
//                    query.setLimit(1);
//                    query.whereEqualTo("username", mToTextField.getText().toString());
//                    query.findInBackground(new FindCallback<ParseUser>() {
//                        @Override
//                        public void done(List<ParseUser> objects, ParseException e) {
//                            if (objects.size() > 0){
//                                setUser((User) objects.get(0));
//                                getConversation().send(message);
//                                mMessageEditText.setText("");
//                            }else {
//                                ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
//                                userParseQuery.whereMatches("username", mToTextField.getText().toString(), "i");
//                                userParseQuery.findInBackground(new FindCallback<ParseUser>() {
//                                    @Override
//                                    public void done(List<ParseUser> users, ParseException e) {
//                                        boolean isUserFound = false;
//                                        ParseUser user = null;
//                                        for (ParseUser parseUser : users) {
//                                            boolean isSameUserName = parseUser.getUsername().toLowerCase().equals(mToTextField.getText().toString().toLowerCase());
//                                            if (isSameUserName) {
//                                                isUserFound = true;
//                                                user = parseUser;
//                                                break;
//                                            }
//                                        }
//                                        if (isUserFound){
//                                            setUser((User) user);
//                                            getConversation().send(message);
//                                            mMessageEditText.setText("");
//                                        }else {
//                                            Toast.makeText(ConversationActivity.this, "No user found with that name...", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setUser(User user){
        otherUser = user;
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(otherUser.getUsername());
        if (getActionBar() != null) getActionBar().setTitle(otherUser.getUsername());
        mToTextField.setVisibility(View.GONE);

        mConversationAdapter = new ConversationAdapter(mMessages, otherUser);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mConversationAdapter);

        updateMessages();
    }

    private void updateMessages(){
        Conversation conversation = getConversation();
//        Query query = Query.builder(Message.class)
//                .predicate(new Predicate(Message.Property.CONVERSATION, Predicate.Operator.EQUAL_TO, conversation))
//                .sortDescriptor(new SortDescriptor(Message.Property.SENT_AT, SortDescriptor.Order.DESCENDING))
//                .limit(100)
//                .build();
        mMessages.removeAll(mMessages);
//        mMessages.addAll(LayerManager.getStaticLayerClient().executeQuery(query, Query.ResultType.OBJECTS));
        Collections.reverse(mMessages);
        mConversationAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mMessages.size() - 1);

//        ParseQuery<ParseMessage> messageQuery = new ParseQuery<>(ParseMessage.class);
//        messageQuery.orderByDescending("createdAt");
//        messageQuery.whereEqualTo(ParseMessage.CONVERSATION_IDENTIFIER, conversation.getId().toString());
//        messageQuery.findInBackground(new FindCallback<ParseMessage>() {
//            @Override
//            public void done(List<ParseMessage> parseMessages, ParseException e) {
//                List<ParseMessage> parseDeletableMessages = new ArrayList<>();
//                List<Message> layerDeletableMessages = new ArrayList<>();
//                for (Message message : mMessages) {
//                    if (!message.getSender().getUserId().equals(ParseUser.getCurrentUser().getObjectId())) {
//                        message.markAsRead();
//                        boolean isInParse = false;
//                        for (ParseMessage parseMessage : parseMessages) {
//                            if (parseMessage.getString("identifier").equals(message.getId().toString())) {
//                                isInParse = true;
//                                break;
//                            }
//                        }
//                        if (!isInParse) {
//                            ParseMessage parseMessage = new ParseMessage();
//                            parseMessage.put(ParseMessage.IDENTIFIER, message.getId().toString());
//                            parseMessage.put(ParseMessage.CONVERSATION_IDENTIFIER, mConversation.getId().toString());
//                            parseMessage.saveInBackground();
//                        }
//                    }
//                    for (ParseMessage parseMessage : parseMessages) {
//                        if (parseMessage.getString(ParseMessage.IDENTIFIER).equals(message.getId().toString())) {
//                            if (parseMessage.getCreatedAt().before(new Date(new Date().getTime() - 24 * 3600 * 1000))) {
//                                parseDeletableMessages.add(parseMessage);
//                                message.delete(LayerClient.DeletionMode.ALL_PARTICIPANTS);
//                            }
//                        }
//                    }
//                }
//                ParseMessage.deleteAllInBackground(parseDeletableMessages);
//                mMessages.removeAll(layerDeletableMessages);
//                mConversationAdapter.notifyDataSetChanged();
//            }
//        });
    }

    private Conversation getConversation(){
        if (mConversation == null && otherUser != null){
//            try {
//                Conversation conversation = LayerManager.getStaticLayerClient()
//                        .newConversation(Arrays.asList(otherUser.getObjectId()));
//                mConversation = conversation;
//            }catch (LayerConversationException e){
//                Query query = Query.builder(Conversation.class)
//                        .predicate(new Predicate(Conversation.Property.PARTICIPANTS, Predicate.Operator.IN,
//                                Arrays.asList(otherUser.getObjectId(), ParseUser.getCurrentUser().getObjectId())))
//                        .build();
//                List<Conversation> results = LayerManager.getStaticLayerClient().executeQuery(query, Query.ResultType.OBJECTS);
//                for (Conversation conversation : results){
//                    List<String> participants = conversation.getParticipants();
//                    if (participants.contains(otherUser.getObjectId()) && participants.contains(ParseUser.getCurrentUser().getObjectId())){
//                        mConversation = conversation;
//                    }
//                }
//            }
        }
        return mConversation;
    }
}
