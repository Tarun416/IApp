package com.example.iapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.iapp.adapter.EventsAdapter;
import com.example.iapp.adapter.FriendsEventAdapter;
import com.example.iapp.interfaces.OnItemClick;
import com.example.iapp.models.Occassion;
import com.example.iapp.utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.iapp.R.id.imagePlaceholder;

/**
 * Created by rahul on 05/04/17.
 */
public class FriendEventsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.friendsName)
    TextView friendsName;
    @Bind(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;
    @Bind(R.id.noocemptylayout)
    TextView noocemptylayout;

    private String name;
    private String accountId;
    private DatabaseReference mDatabase;
    private ArrayList<Occassion> occassion;
    private FriendsEventAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_event);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Friends's Events");
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        accountId = bundle.getString("accountId");
        friendsName.setText(name);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getOccassionFromFirebase();
    }

    private void getOccassionFromFirebase() {
        CommonUtils.displayProgressDialog(this, "Fetching events");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        eventsRecyclerView.setLayoutManager(linearLayoutManager);

        mDatabase.child("users").child(accountId).child("occassions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommonUtils.dismissProgressDialog();

                if (dataSnapshot.getChildrenCount() > 0) {
                    noocemptylayout.setVisibility(View.GONE);
                    eventsRecyclerView.setVisibility(View.VISIBLE);

                    Log.d("count", dataSnapshot.getChildrenCount() + "");

                    Occassion eventName = new Occassion();

                    occassion = new ArrayList<Occassion>();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        eventName = d.getValue(Occassion.class);
                        eventName.occassionName = d.getKey();
                        occassion.add(eventName);
                    }
                    mAdapter = new FriendsEventAdapter(FriendEventsActivity.this, occassion, new OnItemClick() {
                        @Override
                        public void onFriendClick(int position) {

                        }

                        @Override
                        public void onFriendEventClick(int position) {
                             Intent i=new Intent(FriendEventsActivity.this,WishingActivity.class);
                             i.putExtra("name",name);
                             i.putExtra("occassionname",occassion.get(position).occassionName);
                             i.putExtra("occassiondate",occassion.get(position).date);
                             i.putExtra("occassiontime",occassion.get(position).time);
                            i.putExtra("isFriendInvited",occassion.get(position).isFriendInvited);
                             startActivity(i);
                        }
                    });
                    eventsRecyclerView.setAdapter(mAdapter);
                } else {
                    noocemptylayout.setVisibility(View.VISIBLE);
                    eventsRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CommonUtils.dismissProgressDialog();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
