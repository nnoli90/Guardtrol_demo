package com.example.guardtroldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText pointName;
    Button save;
    Button getLocation;
    ListView savedLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.name);
        pointName = findViewById(R.id.location_name);
        save = findViewById(R.id.save);
        getLocation = findViewById(R.id.get_location);
        savedLocation = findViewById(R.id.list_view);


    }
}