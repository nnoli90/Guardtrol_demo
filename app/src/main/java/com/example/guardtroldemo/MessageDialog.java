package com.example.guardtroldemo;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MessageDialog {

    public void showDialog(Activity activity, String message, String title){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);
        //set the message
        TextView mText = (TextView) dialog.findViewById(R.id.message);
        mText.setText(message);
//        sets the title of the message
        TextView tText = (TextView) dialog.findViewById(R.id.title);
        tText.setText(title);

        //set the okat button
        Button okay = (Button) dialog.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }


}
