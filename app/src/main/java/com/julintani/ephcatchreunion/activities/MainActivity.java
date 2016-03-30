package com.julintani.ephcatchreunion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.adapter.EphAdapter;
import com.julintani.ephcatchreunion.interfaces.OnProfileCardClickListener;
import com.julintani.ephcatchreunion.interfaces.OnProfileCardLongClickListener;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected ArrayList<User> mUsers = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EphAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadViews();
        setupRecycler();
        setupSwipeRefresh();
        mSwipeRefresh.setRefreshing(true);
        refreshUsers();
    }

    protected void loadViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ephs);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.sr_ephs);
    }

    protected void setupRecycler(){
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new EphAdapter(mUsers, new OnProfileCardClickListener() {
            @Override
            public void onProfileCardClicked(ProfileViewHolder holder) {
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                                Pair.create((View)holder.getUserImageView(), ProfileViewHolder.PROFILE_IMAGE_TRANSITION)
//                                Pair.create((View)holder.getLikeButton(), ProfileViewHolder.LIKE_BUTTON_TRANSITION),
//                                Pair.create((View)holder.getSuperLikeButton(), ProfileViewHolder.SUPER_LIKE_BUTTON_TRANSITION),
//                                Pair.create((View)holder.getMessageButton(), ProfileViewHolder.MESSAGE_BUTTON_TRANSITION),
//                                Pair.create((View)holder.getCatchUpButton(), ProfileViewHolder.CATCH_UP_BUTTON_TRANSITION)
                        );
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_KEY, holder.getUser());
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            }
        }, new OnProfileCardLongClickListener() {
            @Override
            public void onProfileCardLongClicked(ProfileViewHolder holder) {
                //TODO
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                int[] into = new int[2];
                layoutManager.findLastVisibleItemPositions(into);
                if (mUsers.size() - 10 == into[0] && !mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(true);
                    pageUsers();
                }
            }
        });
    }

    protected void setupSwipeRefresh(){
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                refreshUsers();
            }
        });
    }

    public void refreshUsers(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUsers.removeAll(mUsers);
                List<User> newUsers = dummyUsersPage();
                mUsers.addAll(newUsers);
                mAdapter.notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
            }
        }, 1000);
    }

    public void pageUsers(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mUsers.add(User.generateDummyUser());
                }
                mAdapter.notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
