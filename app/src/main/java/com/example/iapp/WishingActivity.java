package com.example.iapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 05/04/17.
 */

public class WishingActivity extends AppCompatActivity {


    @Bind(R.id.friendName)
    TextView friendName;
    @Bind(R.id.occassionText)
    TextView occassionText;
    @Bind(R.id.invitationDate)
    TextView invitationDate;
    @Bind(R.id.invitationTime)
    TextView invitationTime;
    @Bind(R.id.sendMoney)
    Button sendMoney;


    @Bind(R.id.occassionName)
    TextView oname;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String name;
    private String occassionName1;
    private String occassionDate1;
    private String occassionTime1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wishing);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Wish your friend");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        occassionName1 = bundle.getString("occassionname");
        occassionDate1 = bundle.getString("occassiondate");
        if (bundle.getBoolean("isFriendInvited")) {
            occassionTime1 = bundle.getString("occassiontime");
            time.setText(occassionTime1);
        } else {
            invitationTime.setVisibility(View.GONE);
            time.setVisibility(View.GONE);

        }

        friendName.setText(name);
        oname.setText(occassionName1);
        date.setText(occassionDate1);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
