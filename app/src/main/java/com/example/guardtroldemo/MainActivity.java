package com.example.guardtroldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText userName;
    EditText pointName;
    TextView location;
    Button save;
    Button getLocation;
    ListView savedLocation;
    double lon;
    double lat;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.name);
        pointName = findViewById(R.id.location_name);
        save = findViewById(R.id.save);
        getLocation = findViewById(R.id.get_location);
        savedLocation = findViewById(R.id.list_view);
        location = findViewById(R.id.location);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database  db = new database(MainActivity.this);

                ArrayList<Location> allL = new ArrayList<>();


                if (userName.getText().toString() !="" && pointName.getText().toString() != ""
                        && getLocation.getText().toString().length()>5){

                    db.insertRoute(userName.getText().toString(),lon,lat,address);


                 Cursor res =   db.getAllroutes();

                 while (res.moveToNext()){

                     allL.add(new Location(res.getDouble(2),res.getDouble(3),res.getString(4),res.getString(1)));


                 }

                }
                list_adapter_location myadapter = new list_adapter_location(MainActivity.this,allL);
                savedLocation.setAdapter(myadapter);



            }
        });




        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindLocation mylocal = new FindLocation(MainActivity.this);

                loadingLocationDialog dialog = new loadingLocationDialog(MainActivity.this);
                dialog.startLoading();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lon = mylocal.getLon();
                        lat = mylocal.getLat();

                        if (mylocal.getCurrent_adress() != null) {

                            address = mylocal.getCurrent_adress().get(0).getAddressLine(0);

                        }

                        String local = address + "\nLon:"+lon+ "\nLat:"+lat;
                        location.setText(address);

                    }
                },1000);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Location> allL = new ArrayList<>();
        database db = new database(MainActivity.this);
        Cursor res =   db.getAllroutes();

        while (res.moveToNext()){

            allL.add(new Location(res.getDouble(2),res.getDouble(3),res.getString(4),res.getString(1)));


        }


    list_adapter_location myadapter = new list_adapter_location(MainActivity.this,allL);

                savedLocation.setAdapter(myadapter);

}



}