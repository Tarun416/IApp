package com.example.iapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iapp.apiInterface.Authorization;
import com.example.iapp.generator.ApiGenerator;
import com.example.iapp.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

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
    private String token;
    private SharedPreferences preferences;
    private String friendAccountId;


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

        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        friendAccountId=bundle.getString("friendAccountId");
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
        preferences.edit().putString("token",null).apply();
        finish();}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMoney:
                if(preferences.getString("token","")==null || preferences.getString("token","")=="")
                invokeDialog();
                else
                 invokeSelectAccount();
                break;

            case R.id.loginButton:
                CommonUtils.displayProgressDialog(WishingActivity.this, "Authorizing");
                authorizationApi = ApiGenerator.createServiceAuthorization(Authorization.class);
                authorizationApi.authorizeClient(clientId.getText().toString().trim(), accessCode.getText().toString(), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        CommonUtils.dismissProgressDialog();
                        String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());
                        token = bodyString.split(":")[1].substring(1, bodyString.split(":")[1].length() - 3).trim();
                        Log.d("success", token);
                        preferences.edit().putString("token",token).apply();
                        preferences.edit().putString("clientId",clientId.getText().toString()).apply();
                        dialog.dismiss();
                        invokeSelectAccount();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("failure", error.getMessage());
                        CommonUtils.dismissProgressDialog();
                        dialog.dismiss();

                    }
                });
                break;


            case R.id.cancelButton:
                dialog.dismiss();
                break;

            case R.id.saveButton:
                if((firstRadioButton.isChecked()|| secondRadioButton.isChecked()))
                {
                    Intent i=new Intent(WishingActivity.this,PaymentActivity.class);
                    i.putExtra("accountNo",firstRadioButton.isChecked()?firstRadioButton.getText().toString():secondRadioButton.getText().toString());
                    i.putExtra("receiverName",name);
                    i.putExtra("occassionName",occassionName1);
                    i.putExtra("occassionDate",occassionDate1);
                    i.putExtra("friendAccountId",friendAccountId);
                    startActivity(i);
                   // finish();
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(this,"Please select one account",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private Button cancelButton;
    private Button saveButton;
    private RadioButton firstRadioButton;
    private RadioButton secondRadioButton;


    private void invokeSelectAccount() {
        alertLayout=getLayoutInflater().inflate(R.layout.layout_select_account,null);
        cancelButton= (Button) alertLayout.findViewById(R.id.cancelButton);
        saveButton=(Button)alertLayout.findViewById(R.id.saveButton);
        firstRadioButton=(RadioButton) alertLayout.findViewById(R.id.firstRadioButton);
        secondRadioButton=(RadioButton)alertLayout.findViewById(R.id.secondRadioButton);
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        firstRadioButton.setOnClickListener(this);
        secondRadioButton.setOnClickListener(this);
        AlertDialog.Builder alert=new AlertDialog.Builder(WishingActivity.this);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        dialog=alert.create();
        dialog.show();
    }

    private Button loginButton;
    private EditText accessCode;
    private EditText clientId;


    private void invokeDialog() {
        alertLayout=getLayoutInflater().inflate(R.layout.dialog_banklogin,null);
        loginButton=(Button)alertLayout.findViewById(R.id.loginButton);
        accessCode=(EditText)alertLayout.findViewById(R.id.accessCode);
        clientId=(EditText)alertLayout.findViewById(R.id.clientId);
        clientId.setText(BuildConfig.CLIENT_ID);
        accessCode.setText(BuildConfig.ACCESS_CODE);
        loginButton.setOnClickListener(this);
        AlertDialog.Builder alert=new AlertDialog.Builder(alertLayout.getContext());
        alert.setView(alertLayout);
        alert.setCancelable(true);
        dialog=alert.create();
        dialog.show();
    }
}
