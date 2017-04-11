package com.example.iapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iapp.R;
import com.example.iapp.models.ReceivedGift;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 11/04/17.
 */

public class ReceiveGiftAdapter extends RecyclerView.Adapter<ReceiveGiftAdapter.ViewHolder> {



    private Context context;
    private ArrayList<ReceivedGift> receivedGifts;

    public ReceiveGiftAdapter(Context context, ArrayList<ReceivedGift> receivedGifts) {
        this.context = context;
        this.receivedGifts = receivedGifts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.received_gifts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReceivedGift receivedGift = receivedGifts.get(position);
        holder.occassionDate.setText(receivedGift.occassionDate);
        holder.occassionName.setText(receivedGift.occassionName);
        holder.senderName.setText(receivedGift.senderName);
        holder.sentAmount.setText(context.getString(R.string.rupee_sym)+" "+receivedGift.receiveMoney);

    }

    @Override
    public int getItemCount() {
        return receivedGifts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.fromText)
        TextView fromText;
        @Bind(R.id.senderName)
        TextView senderName;
        @Bind(R.id.occassionText)
        TextView occassionText;
        @Bind(R.id.occassionName)
        TextView occassionName;
        @Bind(R.id.occassionDateText)
        TextView occassionDateText;
        @Bind(R.id.occassionDate)
        TextView occassionDate;
        @Bind(R.id.sentAmountText)
        TextView sentAmountText;
        @Bind(R.id.sentAmount)
        TextView sentAmount;
        @Bind(R.id.cardContainer)
        CardView cardContainer;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

