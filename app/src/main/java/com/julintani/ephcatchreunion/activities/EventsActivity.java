package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.adapter.EventsAdapter;
import com.julintani.ephcatchreunion.interfaces.OnEventClickedListener;
import com.julintani.ephcatchreunion.models.Event;
import com.julintani.ephcatchreunion.models.EventHolder;
import com.julintani.ephcatchreunion.models.ServerInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ell on 6/7/16.
 */
public class EventsActivity extends AppCompatActivity implements OnEventClickedListener {
    @Bind(R.id.rv_events)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.sr_events)
    protected SwipeRefreshLayout mSwipeRefresh;

    protected EventsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Events");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new EventsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refresh();
    }

    protected void refresh(){
        mSwipeRefresh.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(ServerInfo.BASE_URL + "events?page-size=75", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray data = response.getJSONArray("data");
                    List<EventHolder> holders = new Gson().fromJson(data.toString(), new TypeToken<ArrayList<EventHolder>>(){}.getType());
                    List<Event> events = new ArrayList<>(holders.size());
                    for (EventHolder holder : holders){
                        events.add(holder.getAttributes());
                    }
                    mAdapter.setEvents(events);
                }catch (Exception e){
                    e.printStackTrace();
                }

                mSwipeRefresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mSwipeRefresh.setRefreshing(false);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(EventsActivity.this));
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(this, ViewEventActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
}
