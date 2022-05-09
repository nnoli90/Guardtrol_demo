package com.example.guardtroldemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loadingLocationDialog {
    private   Activity activity;
   private AlertDialog dialog;


    loadingLocationDialog(Activity myactivity) {
        activity = myactivity;

    }

    void startLoading() {
        AlertDialog.Builder buider = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        buider.setView(inflater.inflate(R.layout.custom_dialog, null));
        buider.setCancelable(false);
        dialog = buider.create();
        dialog.show();


    }

    void dismissDialog(){

        dialog.dismiss();


    }
}