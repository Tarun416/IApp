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
import com.example.iapp.adapter.SentGiftsAdapter;
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

public class SentGiftsFragment extends Fragment {

    @Bind(R.id.sentRecyclerView)
    RecyclerView sentRecyclerView;
    @Bind(R.id.emptyTextView)
    TextView emptyTextView;

    private SentGiftsAdapter mAapter;
    private DatabaseReference mDatabaseReference;
    private SharedPreferences preferences;

    private ArrayList<SendGift> sendGifts;

    private SendGift smodel;
    private LinearLayoutManager linearLayoutManager;

    public SentGiftsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sent_gifts, container, false);
        ButterKnife.bind(this, view);
        sendGifts = new ArrayList<>();
        smodel = new SendGift();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        sentRecyclerView.setLayoutManager(linearLayoutManager);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getSentGiftsFromFirebase();
        return view;
    }

    private void getSentGiftsFromFirebase() {
        mDatabaseReference.child("users").child(preferences.getString("accountId", "")).child("sentGifts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getChildrenCount() > 0)

                {

                    sentRecyclerView.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);

                    for (DataSnapshot sentGiftDatasnapshot : dataSnapshot.getChildren()) {
                        smodel = sentGiftDatasnapshot.getValue(SendGift.class);
                        sendGifts.add(smodel);
                        Log.d("oname", smodel.occassionName + " "+smodel.occassionDate+" "+smodel.receiverName+" "+smodel.sentMoney);

                    }

                    mAapter = new SentGiftsAdapter(getActivity(), sendGifts);
                    sentRecyclerView.setAdapter(mAapter);
                } else {
                    sentRecyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
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
