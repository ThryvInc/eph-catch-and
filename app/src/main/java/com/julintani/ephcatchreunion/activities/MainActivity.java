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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.adapter.EphAdapter;
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

public class MainActivity extends AppCompatActivity {
    protected ArrayList<User> mUsers = new ArrayList<>();
    private EphAdapter mAdapter;
    private int mPage;
    private float mXCoordinate;
    private float mYCoordinate;

    @Bind(R.id.rv_ephs)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.sr_ephs)
    protected SwipeRefreshLayout mSwipeRefresh;

    @Bind(R.id.ll_like)
    protected View mLikeView;
    @Bind(R.id.ll_super_like)
    protected View mSuperLikeView;
    @Bind(R.id.ll_messages)
    protected View mMessageView;
    @Bind(R.id.ll_catch_up)
    protected View mCatchUpView;
    @Bind(R.id.ll_exit)
    protected View mExitView;

    protected View[] mButtons;

    protected ImageButton mLikeButton;
    protected ImageButton mSuperLikeButton;
    protected ImageButton mMessageButton;
    protected ImageButton mCatchUpButton;
    protected ImageButton mExitButton;

    private User mChosenUser;

    protected ItemLongPressHelper.Callback mCallback = new ItemLongPressHelper.Callback() {
        @Override
        public void onLongClick(RecyclerView.ViewHolder viewHolder, float xCoordinate, float yCoordinate) {
            mChosenUser = ((ProfileViewHolder) viewHolder).getUser();
            float pixelsPerDip = getResources().getDisplayMetrics().density;
            ActionAnimationHelper.animateButtons(true, xCoordinate, yCoordinate, pixelsPerDip, mButtons, mExitView);

            mXCoordinate = xCoordinate;
            mYCoordinate = yCoordinate;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadViews();

        setupRecycler();
        setupSwipeRefresh();

        mSwipeRefresh.setRefreshing(true);

        refreshUsers();
    }

    protected void loadViews(){
        mLikeButton = (ImageButton)mLikeView.findViewById(R.id.btn_action);
        mSuperLikeButton = (ImageButton)mSuperLikeView.findViewById(R.id.btn_action);
        mCatchUpButton = (ImageButton)mCatchUpView.findViewById(R.id.btn_action);
        mMessageButton = (ImageButton)mMessageView.findViewById(R.id.btn_action);
        mExitButton = (ImageButton)mExitView.findViewById(R.id.btn_action);

        View[] buttonsArray = { mLikeView, mSuperLikeView, mMessageView, mCatchUpView };
        mButtons = buttonsArray;

        mLikeView.setVisibility(View.GONE);
        mSuperLikeView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        mCatchUpView.setVisibility(View.GONE);
        mExitView.setVisibility(View.GONE);

        mLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_icon_white));
        mSuperLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.star));
        mMessageButton.setImageDrawable(getResources().getDrawable(R.drawable.messages));
        mCatchUpButton.setImageDrawable(getResources().getDrawable(R.drawable.cat));
        mExitButton.setImageDrawable(getResources().getDrawable(R.drawable.x_mark));

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float pixelsPerDip = getResources().getDisplayMetrics().density;
                ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, mButtons, mExitView);
                mChosenUser = null;
            }
        });

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LikeOnClickListener().onClickUserAction(mLikeView, mChosenUser);
                float pixelsPerDip = getResources().getDisplayMetrics().density;
                ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, mButtons, mExitView);
                mChosenUser = null;
            }
        });
        mSuperLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SuperLikeOnClickListener().onClickUserAction(mLikeView, mChosenUser);
                float pixelsPerDip = getResources().getDisplayMetrics().density;
                ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, mButtons, mExitView);
                mChosenUser = null;
            }
        });
        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MessageOnClickListener().onClickUserAction(mLikeView, mChosenUser);
                float pixelsPerDip = getResources().getDisplayMetrics().density;
                ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, mButtons, mExitView);
                mChosenUser = null;
            }
        });
        mCatchUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CatchUpOnClickListener().onClickUserAction(mLikeView, mChosenUser);
                float pixelsPerDip = getResources().getDisplayMetrics().density;
                ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, mButtons, mExitView);
                mChosenUser = null;
            }
        });
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
        }, mCallback);
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

                if (mChosenUser != null) {
                    float pixelsPerDip = getResources().getDisplayMetrics().density;
                    ActionAnimationHelper.animateButtons(false, mXCoordinate, mYCoordinate, pixelsPerDip, mButtons, mExitView);
                    mChosenUser = null;
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
