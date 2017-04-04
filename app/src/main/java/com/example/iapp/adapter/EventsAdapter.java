package com.example.iapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iapp.R;
import com.example.iapp.models.EventsName;
import com.example.iapp.models.Occassion;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 04/04/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Occassion> occassions;

    public EventsAdapter(Context context, ArrayList<Occassion> occassions) {
        this.context = context;
        this.occassions=occassions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Occassion occassion = occassions.get(position);
         holder.occassionName.setText(occassion.occassionName);
         holder.date.setText(occassion.date);
        if(occassion.isFriendInvited)
            holder.invitationTime.setText("Your friends/relatives are invited at"+" "+occassion.time);
        else
            holder.invitationTime.setText("No friends/relatives are invited");
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
