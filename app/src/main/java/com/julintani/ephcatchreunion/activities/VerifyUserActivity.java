package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.models.ServerInfo;
import com.julintani.ephcatchreunion.models.Session;
import com.julintani.ephcatchreunion.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ell on 6/3/16.
 */
public class VerifyUserActivity extends AppCompatActivity implements View.OnClickListener {
    protected Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        session = (Session) getIntent().getExtras().getSerializable("session");

        View buttonView = findViewById(R.id.btn_verify);
        if (buttonView != null){
            buttonView.setOnClickListener(this);
        }

        View troubleView = findViewById(R.id.tv_trouble);
        if (troubleView != null){
            troubleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    String url = "mailto:ephcatchreunion@gmail.com" + "?subject=" + Uri.encode("Reunion Email Issue");
                    intent.setData(Uri.parse(url));
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });
        }

        TextView textView = (TextView) findViewById(R.id.tv_verify);
        if (session != null && textView != null){
            textView.setText("You should soon receive an email with your pin to:\n\n" + session.getEmail());
        }
    }

    @Override
    public void onClick(View v) {
        EditText pinField = (EditText) findViewById(R.id.et_pin);
        String pin = pinField.getText().toString();
        Session newSession = new Session();
        newSession.setEmail(session.getEmail());
        newSession.setPassword(pin);

        try {
            JSONObject sessionJson = new JSONObject(new Gson().toJson(newSession));
            JSONObject json = new JSONObject();
            json.put("session", sessionJson);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ServerInfo.BASE_URL + "sessions", json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String apiKey = response.getJSONObject("data").getJSONObject("attributes").getString("api-key");
                        ServerInfo.setApiKey(VerifyUserActivity.this, apiKey);

                        User user = new User();
                        user.setId(session.getId());
                        user.setName(session.getName());
                        User.setCurrentUser(VerifyUserActivity.this, user);

                        Intent intent = new Intent(VerifyUserActivity.this, EditProfileActivity.class);
                        intent.putExtra(EditProfileActivity.IS_DURING_ONBOARDING_KEY, true);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put(ServerInfo.CONTENT_TYPE, ServerInfo.CONTENT_TYPE_HEADER);
                    return headers;
                }
            };
            Volley.newRequestQueue(VerifyUserActivity.this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
