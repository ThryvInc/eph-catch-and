package com.julintani.ephcatchreunion.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.fragments.UserCollectionFragment;
import com.julintani.ephcatchreunion.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 4/27/16.
 */
public class MatchesActivity extends AppCompatActivity{
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabs = (TabLayout) findViewById(R.id.tabs);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.getTabAt(0).setIcon(R.drawable.cat);
        mTabs.getTabAt(1).setIcon(R.drawable.heart_icon_white);
        mTabs.getTabAt(2).setIcon(R.drawable.star);
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return UserCollectionFragment.newInstance(new UserCollectionFragment.UserSource() {
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
            });
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
