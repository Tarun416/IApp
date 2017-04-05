package com.example.iapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.iapp.R;
import com.example.iapp.interfaces.OnItemClick;
import com.example.iapp.models.Occassion;
import com.example.iapp.models.User;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 05/04/17.
 */

public class FriendsEventAdapter extends RecyclerView.Adapter<FriendsEventAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Occassion> occassions;

    public FriendsEventAdapter(Context context, ArrayList<Occassion> occassions) {
        this.context = context;
        this.occassions=occassions;
    }

    @Override
    public FriendsEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_events, parent, false);
        return new FriendsEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsEventAdapter.ViewHolder holder, int position) {
        Occassion occassion = occassions.get(position);
        holder.occassionName.setText(occassion.occassionName);
        holder.date.setText(occassion.date);
        if(occassion.isFriendInvited) {
            holder.invitationTime.setVisibility(View.VISIBLE);
            holder.invitationTime.setText("You are invited at" + " " + occassion.time);
        }
        else
            holder.invitationTime.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return occassions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.occassionName)
        TextView occassionName;
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.invitationTime)
        TextView invitationTime;
        @Bind(R.id.cardContainer)
        CardView cardContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
