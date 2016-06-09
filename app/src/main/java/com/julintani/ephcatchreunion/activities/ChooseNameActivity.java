package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.models.ServerInfo;
import com.julintani.ephcatchreunion.models.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ell on 6/3/16.
 */
public class ChooseNameActivity extends AppCompatActivity {
    protected ArrayList<Session> sessions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        getSupportActionBar().setTitle("Who are you?");

        sessions = (ArrayList<Session>)getIntent().getSerializableExtra("sessions");
        Collections.sort(sessions, new Comparator<Session>() {
            @Override
            public int compare(Session lhs, Session rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        ArrayList<String> names = new ArrayList<>();
        for (Session session : sessions){
            names.add(session.getName());
        }

        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        ListView listView = (ListView) findViewById(R.id.lv_sessions);
        listView.setAdapter(sessionAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Session session = sessions.get(position);
                StringRequest request = new StringRequest(ServerInfo.BASE_URL + "sessions/" + String.valueOf(session.getId()), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(ChooseNameActivity.this, VerifyUserActivity.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                Volley.newRequestQueue(ChooseNameActivity.this).add(request);
            }
        });
    }
}
