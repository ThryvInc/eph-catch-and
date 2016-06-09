package com.julintani.ephcatchreunion.models;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ell on 6/7/16.
 */
public class MessagingManager {
    private static final MessagingManager instance = new MessagingManager();
    private List<Conversation> conversations;

    public List<Conversation> getConversations() {
        return conversations;
    }

    public static MessagingManager getInstance() {
        return instance;
    }

    public void refreshConversations(final Context context){
        JsonObjectRequest request = new JsonObjectRequest(ServerInfo.BASE_URL + "conversations?page-size=75", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                conversations = ConversationParser.parse(context, response);
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
                headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(context));
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
