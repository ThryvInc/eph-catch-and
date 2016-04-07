package com.julintani.ephcatchreunion.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.helpers.ItemLongPressHelper;
import com.julintani.ephcatchreunion.interfaces.OnProfileCardClickListener;
import com.julintani.ephcatchreunion.interfaces.OnProfileCardLongClickListener;
import com.julintani.ephcatchreunion.models.User;
import com.julintani.ephcatchreunion.views.InfoViewHolder;
import com.julintani.ephcatchreunion.views.ProfileViewHolder;

import java.util.List;

/**
 * Created by ell on 3/26/16.
 */
public class EphAdapter extends RecyclerView.Adapter {
    protected List<User> mUsers;
    private OnProfileCardClickListener mClickListener;
    private ItemLongPressHelper.Callback mCallback;

    public EphAdapter(List<User> users, OnProfileCardClickListener clickListener,
                      ItemLongPressHelper.Callback callback){
        mUsers = users;
        mClickListener = clickListener;
        mCallback = callback;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.card_eph_info, parent, false);
            return new InfoViewHolder(view);
        }
        View view = inflater.inflate(R.layout.card_user, parent, false);
        return new ProfileViewHolder(view, ProfileViewHolder.ProfileType.MINIFIED, mCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getClass() == ProfileViewHolder.class){
            final ProfileViewHolder holder = (ProfileViewHolder) viewHolder;
            holder.displayUser(mUsers.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) mClickListener.onProfileCardClicked(holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) return mUsers.size();
        return 0;
    }
}
