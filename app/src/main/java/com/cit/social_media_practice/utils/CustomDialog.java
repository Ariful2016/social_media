package com.cit.social_media_practice.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.cit.social_media_practice.activities.RegisterActivity;

public  class CustomDialog {
    public static void ValidateInputField(Context context,String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    public static void ShowProgress(Context context,String text){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(text);
        progressDialog.show();
    }
}
