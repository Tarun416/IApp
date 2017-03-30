package com.example.iapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by rahul on 30/03/17.
 */

public class CommonUtils {

    private static ProgressDialog mProgressDialog;

    public static void displayProgressDialog(Context context, String message) {
        dismissProgressDialog();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            //((TextView)mProgressDialog.findViewById(R.id.loadingText)).setText(dialogueText);
        }

        //textView.setText(message);
    }


    public static void dismissProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}

