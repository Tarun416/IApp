package com.example.iapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by rahul on 07/04/17.
 */

public class PaymentActivity extends AppCompatActivity {

    private View alertLayout;
    private AlertDialog dialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dialog.dismiss();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment);
        invokeDialog();
    }

    private void invokeDialog() {
        alertLayout=getLayoutInflater().inflate(R.layout.layout_select_account,null);
        AlertDialog.Builder alert=new AlertDialog.Builder(PaymentActivity.this);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        dialog=alert.create();
        dialog.show();
    }


}
