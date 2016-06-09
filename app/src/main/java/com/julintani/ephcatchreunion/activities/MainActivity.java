package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.fragments.UserCollectionFragment;
import com.julintani.ephcatchreunion.models.ServerInfo;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.models.UserHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements UserCollectionFragment.UserSource {
    private UserCollectionFragment mUsersFragment;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsersFragment = UserCollectionFragment.newInstance(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_fragment, mUsersFragment)
                .commitAllowingStateLoss();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void refreshUsers(final UserCollectionFragment fragment){
        mPage = 1;
        String endpoint = "users?page-size=20&page=" + String.valueOf(mPage);
        fetchUsers(endpoint, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                fragment.setUsers(parseUsers(response));
            }
        });
    }

    @Override
    public void pageUsers(final UserCollectionFragment fragment){
        mPage++;
        String endpoint = "users?page-size=20&page=" + String.valueOf(mPage);
        fetchUsers(endpoint, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                fragment.addUsers(parseUsers(response));
            }
        });
    }

    protected List<User> parseUsers(JSONObject response){
        try {
            JSONArray data = response.getJSONArray("data");
            List<UserHolder> holders = new Gson().fromJson(data.toString(), new TypeToken<ArrayList<UserHolder>>(){}.getType());
            List<User> users = new ArrayList<>(holders.size());
            for (UserHolder holder : holders){
                users.add(holder.getAttributes());
            }
            return users;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void fetchUsers(String endpoint, Response.Listener<JSONObject> listener){
        JsonObjectRequest request = new JsonObjectRequest(ServerInfo.BASE_URL + endpoint, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                headers.put(ServerInfo.API_KEY_HEADER, ServerInfo.getApiKey(MainActivity.this));
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_messages:
                Intent intent1 = new Intent(this, MessagesActivity.class);
                startActivity(intent1);
                break;
            case R.id.action_events:
                Intent intent2 = new Intent(this, EventsActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_profile:
                Intent intent3 = new Intent(this, EditProfileActivity.class);
                startActivity(intent3);
                break;
        }

        return true;
    }
}
