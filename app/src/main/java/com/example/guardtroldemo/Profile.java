package com.example.guardtroldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Profile extends AppCompatActivity {

    TextView guardName;
    de.hdodenhof.circleimageview.CircleImageView guardImage;

    String gN;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
            guardImage = findViewById(R.id.guard_image);
            guardName = findViewById(R.id.name);


        Bundle bundle = getIntent().getExtras();
        gN = bundle.getString("name");
        img = bundle.getString("image");
        guardName.setText(gN);
        //set image on image view
        Glide.with(this)
                .load(img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(guardImage);


    }




}