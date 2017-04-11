package com.example.iapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iapp.R;
import com.example.iapp.models.SendGift;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 11/04/17.
 */


public class SentGiftsAdapter extends RecyclerView.Adapter<SentGiftsAdapter.ViewHolder> {


    private Context context;
    private ArrayList<SendGift> sendGifts;

    public SentGiftsAdapter(Context context, ArrayList<SendGift> sendGifts) {
        this.context = context;
        this.sendGifts=sendGifts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sent_gifts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
          SendGift sendGift=sendGifts.get(position);
          holder.receiverName.setText(sendGift.receiverName);
          holder.occassionName.setText(sendGift.occassionName);
          holder.occassionDate.setText(sendGift.occassionDate);
          holder.sentAmount.setText(context.getString(R.string.rupee_sym)+" "+sendGift.sentMoney);
    }

    @Override
    public int getItemCount() {
        return sendGifts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.toText)
        TextView toText;
        @Bind(R.id.receiverName)
        TextView receiverName;
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
            ButterKnife.bind(this,itemView);
        }
    }
}
