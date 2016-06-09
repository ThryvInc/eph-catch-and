package com.julintani.ephcatchreunion.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.interfaces.OnEventClickedListener;
import com.julintani.ephcatchreunion.models.Event;
import com.julintani.ephcatchreunion.views.EventViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 6/7/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {
    protected List<Event> mEvents = new ArrayList<>();
    protected OnEventClickedListener mListener;

    public EventsAdapter(OnEventClickedListener listener) {
        mListener = listener;
    }

    public void setEvents(List<Event> events){
        mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_event, parent, false);
        return new EventViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.display(mEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
