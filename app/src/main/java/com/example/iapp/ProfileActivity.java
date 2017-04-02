package com.example.iapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.iapp.models.User;
import com.example.iapp.utils.CommonUtils;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 31/03/17.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.photo)
    CircularImageView photo;

    private static String TAG = ProfileActivity.class.getName();
    @Bind(R.id.username)
    EditText username1;
    @Bind(R.id.email)
    EditText email1;
    @Bind(R.id.saveTick)
    ImageView saveTick;
    private SharedPreferences preferences;
    private DatabaseReference mDatabase;
    private String username;
    private String email;
    private String photoUrl;
    private Bundle bundle;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        bundle=getIntent().getExtras();
        saveTick.setOnClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        CommonUtils.displayProgressDialog(this,"fetching details");
        mDatabase.child("users").child(preferences.getString("accountId", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommonUtils.dismissProgressDialog();
                User user = dataSnapshot.getValue(User.class);
                username = user.displayName;
                email = user.email;
                photoUrl = user.photoUrl;
                setUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CommonUtils.dismissProgressDialog();
                Log.d(TAG, databaseError.getMessage() + " " + databaseError.getDetails());

            }
        });
    }

    private void setUI() {
        Glide.with(this).load(photoUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(photo) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                photo.setImageDrawable(circularBitmapDrawable);
            }
        });

        username1.setText(username);
        email1.setText(email);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.saveTick:
                if(bundle!=null && bundle.getBoolean("fromSignIn"))
                {
                    Intent i=new Intent(this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    finish();
                }
                break;
        }

    }
}
