package com.example.iapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.iapp.apiInterface.Authorization;
import com.example.iapp.generator.ApiGenerator;
import com.example.iapp.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahul on 05/04/17.
 */

public class WishingActivity extends AppCompatActivity implements View.OnClickListener {

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

    private View alertLayout;
    private AlertDialog dialog;
    private Authorization authorizationApi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wishing);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Wish your friend");
        sendMoney.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMoney:
                invokeDialog();
                break;

            case R.id.loginButton:
                CommonUtils.displayProgressDialog(WishingActivity.this,"Authorizing");
                authorizationApi=ApiGenerator.createServiceAuthorization(Authorization.class);
                authorizationApi.authorizeClient(clientId.getText().toString().trim(),accessCode.getText().toString(),new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        CommonUtils.dismissProgressDialog();
                        Log.d("success",response.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("failure",error.getMessage());
                        CommonUtils.dismissProgressDialog();
                        dialog.dismiss();

                    }
                });
                break;

        }
    }

   private Button loginButton;
    private EditText accessCode;
    private EditText clientId;


    private void invokeDialog() {
        alertLayout=getLayoutInflater().inflate(R.layout.dialog_banklogin,null);
        loginButton=(Button)alertLayout.findViewById(R.id.loginButton);
        accessCode=(EditText)alertLayout.findViewById(R.id.accessCode);
        clientId=(EditText)alertLayout.findViewById(R.id.clientId);
        loginButton.setOnClickListener(this);
        AlertDialog.Builder alert=new AlertDialog.Builder(alertLayout.getContext());
        alert.setView(alertLayout);
        alert.setCancelable(true);
        dialog=alert.create();
        dialog.show();
    }
}
