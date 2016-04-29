package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.adapter.EphAdapter;
import com.julintani.ephcatchreunion.fragments.MessagesFragment;
import com.julintani.ephcatchreunion.fragments.UserCollectionFragment;
import com.julintani.ephcatchreunion.helpers.ItemLongPressHelper;
import com.julintani.ephcatchreunion.interfaces.OnProfileCardClickListener;
import com.julintani.ephcatchreunion.listeners.CatchUpOnClickListener;
import com.julintani.ephcatchreunion.listeners.LikeOnClickListener;
import com.julintani.ephcatchreunion.listeners.MessageOnClickListener;
import com.julintani.ephcatchreunion.listeners.SuperLikeOnClickListener;
import com.julintani.ephcatchreunion.models.ActionAnimationHelper;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements UserCollectionFragment.UserSource {
    private UserCollectionFragment mUsersFragment;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragment.setUsers(dummyUsersPage());
            }
        }, 1000);
    }

    @Override
    public void pageUsers(final UserCollectionFragment fragment){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragment.addUsers(dummyUsersPage());
            }
        }, 1000);
    }

    public List<User> dummyUsersPage(){
        ArrayList<User> users = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            users.add(User.generateDummyUser());
        }
        return users;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_messages) {
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
