package com.example.iapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iapp.apiInterface.Retail;
import com.example.iapp.generator.ApiGenerator;
import com.example.iapp.models.FundTransfer;
import com.example.iapp.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by rahul on 07/04/17.
 */

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.backArrowButton)
    ImageView backArrowButton;
    @Bind(R.id.sendMoneyText)
    TextView sendMoneyText;
    @Bind(R.id.sendMoneyContainer)
    LinearLayout sendMoneyContainer;
    @Bind(R.id.toText)
    TextView toText;
    @Bind(R.id.receiverAccountNo)
    TextView receiverAccountNo;
    @Bind(R.id.receiverName)
    TextView receiverName;
    @Bind(R.id.fromText)
    TextView fromText;
    @Bind(R.id.senderAccountNo)
    TextView senderAccountNo;
    @Bind(R.id.senderName)
    TextView senderName;
    @Bind(R.id.available_balance)
    TextView availableBalance;
    @Bind(R.id.amttext)
    TextView amttext;
    @Bind(R.id.rupeesymbol)
    TextView rupeesymbol;
    @Bind(R.id.rupee)
    EditText rupee;
    @Bind(R.id.sendMoneyButton)
    Button sendMoneyButton;
    private View alertLayout;
    private AlertDialog dialog;
    private Bundle bundle;

    private Retail retail;
    private String client_id;
    private String token;
    private SharedPreferences preferences;
    private DecimalFormat df;

    @Override
    public void onBackPressed() {
        finish();
        //dialog.dismiss();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment);
        ButterKnife.bind(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
        client_id = preferences.getString("clientId", "");
        token = preferences.getString("token", "");
        backArrowButton.setOnClickListener(this);
        sendMoneyButton.setOnClickListener(this);
        bundle = getIntent().getExtras();
        senderAccountNo.setText(bundle.getString("accountNo"));
        retail = ApiGenerator.createServiceRetailBanking(Retail.class);

        getBalance();

    }

    private void getBalance() {
        CommonUtils.displayProgressDialog(PaymentActivity.this, "Fetching balance");
        retail.balanceEnquiry(client_id, token, senderAccountNo.getText().toString(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                CommonUtils.dismissProgressDialog();
                String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());
                try {
                    JSONArray jsonArray = new JSONArray(bodyString);
                    availableBalance.setText(getString(R.string.available_balace) + " " + jsonArray.getJSONObject(1).getString("balance"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                CommonUtils.dismissProgressDialog();
                Log.d("error", error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backArrowButton:
                onBackPressed();
                break;


            case R.id.sendMoneyButton:
                if (rupee != null && Integer.parseInt(rupee.getText().toString()) > 1) {
                    CommonUtils.displayProgressDialog(PaymentActivity.this, "Sending money");

                    retail.fundTransfer(client_id, token, senderAccountNo.getText().toString(),receiverAccountNo.getText().toString(),  rupee.getText().toString(), "NA", "1", "Direct-To-Home payments", new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            CommonUtils.dismissProgressDialog();

                            Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_LONG).show();

                            String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());

                            try {
                                JSONArray jsonArray = new JSONArray(bodyString);

                                Log.d("fundTransfer", jsonArray.getJSONObject(1).getString("referance_no"));

                                Log.d("fundTransfer", jsonArray.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            getBalance();

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            CommonUtils.dismissProgressDialog();
                            Log.d("fundTransfer", error.getMessage());

                        }
                    });

                } else {
                    Toast.makeText(this, "Amount value should be greater than Rs 1.00", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
