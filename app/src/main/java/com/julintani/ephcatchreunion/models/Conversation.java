package com.julintani.ephcatchreunion.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ell on 4/22/16.
 */
public class Conversation implements Serializable{
    private User user;
    private Date lastMessageTimestamp;
    private String lastMessageText;
    private List<Message> messages;

    public static List<Conversation> generateConversations(){
        ArrayList<Conversation> conversations = new ArrayList<>();
        Conversation conversation = new Conversation();
        conversation.setUser(User.generateDummyUser());
        conversation.setLastMessageText("Sup bae");
        conversation.setLastMessageTimestamp(new Date(new Date().getTime() - 10 * 1000));
        conversations.add(conversation);

        ArrayList<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setSender(User.generateDummyUser());
        message.setText("Sup bae");
        message.setCreatedAt(new Date(new Date().getTime() - 10 * 1000));
        messages.add(message);

        return conversations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Date lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
