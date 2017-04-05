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
import com.example.iapp.models.User;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 04/04/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> userlist;
    private OnItemClick onItemClick;

    public FriendsAdapter(Context context, ArrayList<User> userlist, OnItemClick onItemClick) {
        this.context = context;
        this.userlist = userlist;
        this.onItemClick=onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        User user = userlist.get(position);
        Glide.with(context).load(user.photoUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.photo) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.photo.setImageDrawable(circularBitmapDrawable);
            }
        });

        holder.friendName.setText(user.displayName);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.photo)
        CircularImageView photo;
        @Bind(R.id.friendName)
        TextView friendName;
        @Bind(R.id.cardContainer)
        CardView cardContainer;
        private Context context;


        @Bind(R.id.eventButton)
        Button eventButton;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            eventButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position=getAdapterPosition();

            if(position!=RecyclerView.NO_POSITION) {
                switch (view.getId()) {
                    case R.id.eventButton:
                        onItemClick.onFriendClick(position);
                        break;
                }
            }

        }
    }

}

