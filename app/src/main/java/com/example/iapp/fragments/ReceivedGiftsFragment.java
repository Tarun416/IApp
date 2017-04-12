package com.example.iapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iapp.R;
import com.example.iapp.adapter.ReceiveGiftAdapter;
import com.example.iapp.adapter.SentGiftsAdapter;
import com.example.iapp.models.ReceivedGift;
import com.example.iapp.models.SendGift;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 02/04/17.
 */

public class ReceivedGiftsFragment extends Fragment {


    @Bind(R.id.receiveRecyclerView)
    RecyclerView receiveRecyclerView;
    @Bind(R.id.emptyLayout)
    TextView emptyLayout;

    private ReceiveGiftAdapter mAapter;
    private DatabaseReference mDatabaseReference;
    private SharedPreferences preferences;

    private ArrayList<ReceivedGift> receivedGifts;

    private ReceivedGift rmodel;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_received_gifts, container, false);
        ButterKnife.bind(this, view);
        receivedGifts = new ArrayList<>();
        rmodel = new ReceivedGift();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        receiveRecyclerView.setLayoutManager(linearLayoutManager);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getSentGiftsFromFirebase();
        return view;
    }

    private void getSentGiftsFromFirebase() {
        mDatabaseReference.child("users").child(preferences.getString("accountId", "")).child("receivedGifts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0)
                {
                    if(receiveRecyclerView!=null)
                    receiveRecyclerView.setVisibility(View.VISIBLE);
                    if(emptyLayout!=null)
                    emptyLayout.setVisibility(View.GONE);

                    for (DataSnapshot sentGiftDatasnapshot : dataSnapshot.getChildren()) {
                        rmodel = sentGiftDatasnapshot.getValue(ReceivedGift.class);
                        receivedGifts.add(rmodel);
                        Log.d("oname", rmodel.occassionName + " "+rmodel.occassionDate+" "+rmodel.senderName+" "+rmodel.receiveMoney);

                    }

                    mAapter = new ReceiveGiftAdapter(getActivity(), receivedGifts);
                    receiveRecyclerView.setAdapter(mAapter);
                } else {
                    if(receiveRecyclerView!=null)
                    receiveRecyclerView.setVisibility(View.GONE);

                    if(emptyLayout!=null)
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error", databaseError.getMessage());

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
