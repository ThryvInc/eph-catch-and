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

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.adapter.EphAdapter;
import com.julintani.ephcatchreunion.helpers.ItemLongPressHelper;
import com.julintani.ephcatchreunion.interfaces.OnProfileCardClickListener;
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

    protected Button mLikeButton;
    protected Button mSuperLikeButton;
    protected Button mMessageButton;
    protected Button mCatchUpButton;
    protected Button mExitButton;

    private User mChosenUser;

    protected ItemLongPressHelper.Callback mCallback = new ItemLongPressHelper.Callback() {
        @Override
        public void onLongClick(RecyclerView.ViewHolder viewHolder, float xCoordinate, float yCoordinate) {
            mChosenUser = ((ProfileViewHolder) viewHolder).getUser();
            animateButtons(true, xCoordinate, yCoordinate);

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
        mLikeButton = (Button) mLikeView.findViewById(R.id.btn_action);
        mSuperLikeButton = (Button) mSuperLikeView.findViewById(R.id.btn_action);
        mMessageButton = (Button) mMessageView.findViewById(R.id.btn_action);
        mCatchUpButton = (Button) mCatchUpView.findViewById(R.id.btn_action);
        mExitButton = (Button) mExitView.findViewById(R.id.btn_action);

        View[] buttonsArray = { mLikeView, mSuperLikeView, mMessageView, mCatchUpView };
        mButtons = buttonsArray;

        mLikeView.setVisibility(View.GONE);
        mSuperLikeView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        mCatchUpView.setVisibility(View.GONE);
        mExitView.setVisibility(View.GONE);

        mExitButton.setText("X");
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtons(false, mXCoordinate, mYCoordinate);
            }
        });

        mLikeButton.setText("L");
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtons(false, mXCoordinate, mYCoordinate);
            }
        });
        mSuperLikeButton.setText("S");
        mSuperLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtons(false, mXCoordinate, mYCoordinate);
            }
        });
        mMessageButton.setText("M");
        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtons(false, mXCoordinate, mYCoordinate);
            }
        });
        mCatchUpButton.setText("C");
        mCatchUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtons(false, mXCoordinate, mYCoordinate);
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
                    animateButtons(false, mXCoordinate, mYCoordinate);
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

    public void animateButtons(boolean isForward, float fromX, float fromY){
        float pixelsPerDip = getResources().getDisplayMetrics().density;
        int radius = (int) (20 * pixelsPerDip);
        int margin = (int) (4 * pixelsPerDip);
        fromX = fromX - radius;
        fromY = fromY - radius;

        float toX = radius * 4;
        float toY = 0;

        int duration = 50;

        if (isForward){
            for (int i = 0; i < mButtons.length; i++){
                mButtons[i].setVisibility(View.VISIBLE);
                animateButtonBy(mButtons[i], fromX, -toX, fromY, -toY, duration * i);

                toX = nextXGivenPreviousPoint(toX, toY, radius, margin);
                toY = toYGivenToX(toX, 4 * radius);
            }

            mExitView.setVisibility(View.VISIBLE);
            mExitView.setX(fromX);
            mExitView.setY(fromY);
            mExitView.setAlpha(1f);
            mExitView.animate()
                    .rotationBy(180)
                    .setDuration(duration * mButtons.length)
                    .start();
        }else {
            for (int i = 0; i < mButtons.length; i++){
                mButtons[i].setVisibility(View.VISIBLE);
                animateButtonBy(mButtons[i], fromX - toX, toX, fromY - toY, toY, duration * i);

                toX = nextXGivenPreviousPoint(toX, toY, radius, margin);
                toY = toYGivenToX(toX, 4 * radius);
            }

            mExitView.setVisibility(View.VISIBLE);
            mExitView.setAlpha(1f);
            mExitView.animate()
                    .rotationBy(180)
                    .setDuration(duration * mButtons.length)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mExitView.animate()
                                    .alpha(0f)
                                    .setDuration(100)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            mExitView.setVisibility(View.GONE);
                                        }
                                    }).start();
                            for (View button : mButtons){
                                button.setVisibility(View.GONE);
                            }
                        }
                    }).start();
        }
    }

    protected float toYGivenToX(float toX, float radius){
        return (float)Math.sqrt(Math.pow(radius, 2) - Math.pow(toX, 2));
    }

    //quadratic formula used here
    protected float nextXGivenPreviousPoint(float oldX, float oldY, int radius, int margin){
        double constant = 32 * Math.pow(radius, 2) - Math.pow(2 * radius + margin, 2);
        double b = - 4 * oldX * constant;
        double a = 4 * Math.pow(4 * radius, 2);
        double c = Math.pow(constant, 2) - Math.pow(8 * oldY * radius, 2);
        double radicand = Math.pow(b, 2) - 4 * a * c; // b^2 - 4ac
        double numerator = -b - Math.sqrt(radicand); // -b - root(b^2 - 4ac)
        double denominator = 2 * a;
        return (float) (numerator / denominator);
    }

    protected void animateButtonBy(View button, float fromX, float toX, float fromY, float toY, int offset){
        int duration = 100;
        button.setX(fromX);
        button.setY(fromY);
        button.animate()
                .setDuration(duration)
                .translationXBy(toX)
                .translationYBy(toY)
                .setStartDelay(offset)
                .start();
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
