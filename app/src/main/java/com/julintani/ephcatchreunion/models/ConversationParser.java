package com.julintani.ephcatchreunion.models;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 6/7/16.
 */
public class ConversationParser {

    public static List<Conversation> parse(Context context, JSONObject response){
        Log.d("convos", response.toString());
        List<Conversation> conversations = new ArrayList<>();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        try{
            dataArray = response.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
            try {
                dataObject = response.getJSONObject("data");
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
        List<ConvoHolder> holders = null;
        if (dataArray != null){
            holders = new Gson().fromJson(dataArray.toString(), new TypeToken<ArrayList<ConvoHolder>>(){}.getType());
            List<Conversation> convos = new ArrayList<>(holders.size());
            for (ConvoHolder holder : holders){
                convos.add(holder.getAttributes());
            }
        }else if (dataObject != null){
            ConvoHolder convoHolder = new Gson().fromJson(dataObject.toString(), ConvoHolder.class);
            holders = new ArrayList<>(1);
            holders.add(convoHolder);
        }


        if (holders != null){
            conversations = new ArrayList<>(holders.size());
            for (ConvoHolder holder : holders){
                conversations.add(holder.getAttributes());
            }
        }
        try {
            JSONArray includedArray = response.getJSONArray("included");
            List<User> users = new ArrayList<>();
            List<Message> messages = new ArrayList<>();
            for (int i = 0; i<includedArray.length(); i++){
                JSONObject jsonObject = includedArray.getJSONObject(i);
                String type = jsonObject.getString("type");
                if (type.equals("user")){
                    users.add(new Gson().fromJson(jsonObject.getJSONObject("attributes").toString(), User.class));
                }else if(type.equals("message")){
                    messages.add(new Gson().fromJson(jsonObject.getJSONObject("attributes").toString(), Message.class));
                }
            }
            if (holders != null && holders.size() > 0){
                for (int i = 0; i<holders.size(); i++){
                    ConvoHolder convoHolder = holders.get(i);
                    Conversation conversation = conversations.get(i);

                    RelationshipsHolder relationships = convoHolder.getRelationships();
                    List<User> convoUsers = relationships.getUsers().get("data");
                    for (int j = 0; j<convoUsers.size(); j++){
                        User convoUser = convoUsers.get(j);
                        int userId = convoUser.getId();
                        if (userId != User.getCurrentUser(context).getId()){
                            for (User user : users){
                                if (userId == user.getId()){
                                    conversation.setUser(user);
                                }
                            }
                        }
                    }

                    List<Message> convoMessages = relationships.getLastMessage().get("data");
                    int msgId = convoMessages.get(0).getId();
                    for (Message message : messages){
                        if (msgId == message.getId()){
                            conversation.setLastMessageText(message.getBody());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return conversations;
    }
}
