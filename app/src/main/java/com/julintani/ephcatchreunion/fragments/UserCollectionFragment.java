package com.julintani.ephcatchreunion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.activities.ProfileActivity;
import com.julintani.ephcatchreunion.adapter.EphAdapter;
import com.julintani.ephcatchreunion.interfaces.OnProfileCardClickListener;
import com.julintani.ephcatchreunion.models.ServerInfo;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.models.UserHolder;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ell on 4/27/16.
 */
public class UserCollectionFragment extends Fragment {
    protected ArrayList<User> mUsers = new ArrayList<>();
    private EphAdapter mAdapter;
    private int mPage;

    @Bind(R.id.rv_ephs)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.sr_ephs)
    protected SwipeRefreshLayout mSwipeRefresh;

    private User mChosenUser;

    protected UserSource mUserSource;

    public static UserCollectionFragment newInstance(UserSource userSource){
        UserCollectionFragment fragment = new UserCollectionFragment();
        fragment.setUserSource(userSource);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_collection, container, false);
        ButterKnife.bind(this, view);

        setupRecycler();
        setupSwipeRefresh();

        mSwipeRefresh.setRefreshing(true);

        if (mUserSource != null){
            mUserSource.refreshUsers(this);
        }

        return view;
    }

    protected void setupRecycler(){
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new EphAdapter(mUsers, new OnProfileCardClickListener() {
            @Override
            public void onProfileCardClicked(ProfileViewHolder holder) {
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                Pair.create((View)holder.getUserImageView(), ProfileViewHolder.PROFILE_IMAGE_TRANSITION)
                        );
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_KEY, holder.getUser());
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
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
                    mUserSource.pageUsers(UserCollectionFragment.this);
                }

                mChosenUser = null;
            }
        });
    }

    protected void setupSwipeRefresh(){
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                mUserSource.refreshUsers(UserCollectionFragment.this);
            }
        });
    }

    protected void setUserSource(UserSource userSource){
        mUserSource = userSource;
    }

    public void setUsers(List<User> users){
        mUsers.removeAll(mUsers);
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
        mSwipeRefresh.setRefreshing(false);
    }

    public void addUsers(List<User> users){
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
        mSwipeRefresh.setRefreshing(false);
    }

    public interface UserSource {
        void pageUsers(UserCollectionFragment fragment);
        void refreshUsers(UserCollectionFragment fragment);
    }
}
