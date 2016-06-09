package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.julintani.ephcatchreunion.models.OnboardingManager;
import com.julintani.ephcatchreunion.models.ServerInfo;
import com.julintani.ephcatchreunion.models.Session;
import com.julintani.ephcatchreunion.models.SessionHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 4/11/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (OnboardingManager.isUserOnboarded(this)){
            //start app
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else if (ServerInfo.getApiKey(this) != null){
            //edit user
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra(EditProfileActivity.IS_DURING_ONBOARDING_KEY, true);
            startActivity(intent);
            finish();
        }else {
            //choose user
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ServerInfo.BASE_URL + "sessions", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        ArrayList<SessionHolder> sessionHolders = new Gson().fromJson(data.toString(), new TypeToken<ArrayList<SessionHolder>>(){}.getType());

                        ArrayList<Session> sessions = new ArrayList<>();
                        for (SessionHolder sessionHolder : sessionHolders){
                            sessions.add(sessionHolder.getAttributes());
                        }

                        Intent intent = new Intent(SplashActivity.this, ChooseNameActivity.class);
                        intent.putExtra("sessions", sessions);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        }
    }
}
