package com.example.iapp.fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.iapp.FriendEventsActivity;
import com.example.iapp.R;
import com.example.iapp.adapter.FriendsAdapter;
import com.example.iapp.interfaces.OnItemClick;
import com.example.iapp.models.User;
import com.example.iapp.utils.CommonUtils;
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

public class FriendsFragment extends Fragment {

    @Bind(R.id.noOrdersPhoto)
    ImageView noOrdersPhoto;
    @Bind(R.id.noOrdersTextView)
    TextView noOrdersTextView;
    @Bind(R.id.inviteButton)
    Button inviteButton;
    @Bind(R.id.emptyLayout)
    RelativeLayout emptyLayout;
    @Bind(R.id.friendsRecyclerView)
    RecyclerView friendsRecyclerView;

    private FriendsAdapter mAdapter;
    private DatabaseReference mDatabase;
    private SharedPreferences preferences;

    public FriendsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getFirendsFromDataBase();
        return view;
    }

    private int i;

    private void getFirendsFromDataBase() {
        CommonUtils.displayProgressDialog(getActivity(), "Fetching friends");
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        friendsRecyclerView.setLayoutManager(llm);

        mDatabase.child("users").child(preferences.getString("accountId", "")).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommonUtils.dismissProgressDialog();

                final ArrayList<String> friends = new ArrayList<String>();

                for (DataSnapshot friendDataSnapshot : dataSnapshot.getChildren()) {
                    friends.add(friendDataSnapshot.getValue(String.class));
                }


                if(friends.size()==0)
                {
                    emptyLayout.setVisibility(View.VISIBLE);
                    friendsRecyclerView.setVisibility(View.GONE);
                }



                final ArrayList<User> userlist = new ArrayList<User>();

                for (  i = 0; i < friends.size(); i++) {
                    mDatabase.child("users").child(friends.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User user = dataSnapshot.getValue(User.class);

                            userlist.add(user);
                         //   user=null;
                            if(i==friends.size())
                            {
                                if(userlist.size()>0) {
                                    friendsRecyclerView.setVisibility(View.VISIBLE);
                                    emptyLayout.setVisibility(View.GONE);
                                    mAdapter = new FriendsAdapter(getActivity(), userlist, new OnItemClick() {
                                        @Override
                                        public void onFriendClick(int position) {
                                            Intent i=new Intent(getActivity(), FriendEventsActivity.class);
                                            i.putExtra("accountId",userlist.get(position).accountId);
                                            i.putExtra("name",userlist.get(position).displayName);
                                            startActivity(i);
                                        }
                                    });
                                    friendsRecyclerView.setAdapter(mAdapter);
                                }
                                else
                                {
                                    friendsRecyclerView.setVisibility(View.GONE);
                                    emptyLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("error",databaseError.getMessage());

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CommonUtils.dismissProgressDialog();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
