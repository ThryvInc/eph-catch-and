package com.julintani.ephcatchreunion.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.interfaces.OnEventClickedListener;
import com.julintani.ephcatchreunion.models.Event;

/**
 * Created by ell on 6/7/16.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {
    protected OnEventClickedListener mListener;
    protected Event mEvent;

    protected TextView mTitleTextView;
    protected TextView mPlaceTextView;
    protected TextView mDayTextView;
    protected TextView mTimeTextView;

    public EventViewHolder(View itemView, OnEventClickedListener listener) {
        super(itemView);
        mListener = listener;

        mTitleTextView = (TextView) itemView.findViewById(R.id.tv_title);
        mPlaceTextView = (TextView) itemView.findViewById(R.id.tv_place);
        mDayTextView = (TextView) itemView.findViewById(R.id.tv_day);
        mTimeTextView = (TextView) itemView.findViewById(R.id.tv_time);
    }

    public void display(Event event){
        mEvent = event;

        mTitleTextView.setText(mEvent.getName());
        mPlaceTextView.setText(mEvent.getLocation());
        mDayTextView.setText(mEvent.getDay());
        mTimeTextView.setText(mEvent.getTime());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEventClicked(mEvent);
            }
        });
    }
}
