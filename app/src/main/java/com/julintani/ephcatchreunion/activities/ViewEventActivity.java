package com.julintani.ephcatchreunion.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.models.Event;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ell on 6/7/16.
 */
public class ViewEventActivity extends AppCompatActivity {
    protected Event mEvent;

    @Bind(R.id.tv_title)
    protected TextView mTitleTextView;
    @Bind(R.id.tv_place)
    protected TextView mPlaceTextView;
    @Bind(R.id.tv_day)
    protected TextView mDayTextView;
    @Bind(R.id.tv_time)
    protected TextView mTimeTextView;
    @Bind(R.id.tv_description)
    protected TextView mDescriptionTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        mEvent = (Event) getIntent().getSerializableExtra("event");

        mTitleTextView.setText(mEvent.getName());
        mPlaceTextView.setText(mEvent.getLocation());
        mDayTextView.setText(mEvent.getDay());
        mTimeTextView.setText(mEvent.getTime());
        mDescriptionTextView.setText(mEvent.getDescription());
    }
}
