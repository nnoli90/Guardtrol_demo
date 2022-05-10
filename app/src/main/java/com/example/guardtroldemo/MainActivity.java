package com.example.guardtroldemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText userName;
    EditText pointName;
    EditText image;
    TextView location;
    Button save;
    Button getLocation;
    ListView savedLocation;
    double lon;
    double lat;
    String address;
    ArrayList<Location> allL;
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
        image = findViewById(R.id.image_url);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database  db = new database(MainActivity.this);

                allL = new ArrayList<>();

               String img = image.getText().toString();

                if (userName.getText().toString() !="" && pointName.getText().toString() != ""
                        && getLocation.getText().toString().length()>5){

                     try {
                        db.insertRoute(userName.getText().toString(),lon,lat,address,img);
                    }catch (Exception e){

                        Toast.makeText(MainActivity.this, "Point code already Exist", Toast.LENGTH_SHORT).show();
                    }




                 Cursor res =   db.getAllroutes();

                 while (res.moveToNext()){

                     allL.add(new Location(res.getDouble(2),res.getDouble(3),res.getString(4),
                             res.getString(1),res.getString(5)));


                 }

                }
                list_adapter_location myadapter = new list_adapter_location(MainActivity.this,allL);
                savedLocation.setAdapter(myadapter);



            }
        });




        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    FindLocation mylocal = new FindLocation(MainActivity.this);

                    loadingLocationDialog dialog = new loadingLocationDialog(MainActivity.this);
                    dialog.startLoading();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismissDialog();
                            lon = mylocal.getLon();
                            lat = mylocal.getLat();
                            Toast.makeText(MainActivity.this, ""+lon+lat, Toast.LENGTH_SHORT).show();
                            if (mylocal.getCurrent_adress() != null) {

                                address = mylocal.getCurrent_adress().get(0).getAddressLine(0);

                            }

                            String local = address + "\nLon:" + lon + "\nLat:" + lat;
                            location.setText(local);

                        }
                    }, 10000);


                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 200);

                }

            }

        });

        savedLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              String  name = allL.get(i).name;
              String  image = allL.get(i).imageUrl;
                Intent intent = new Intent(MainActivity.this,Clock_in.class);
                 intent.putExtra("name",name);
                 intent.putExtra("image",image);
                 startActivity(intent);


            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();

        allL = new ArrayList<>();
        database db = new database(MainActivity.this);
        Cursor res =   db.getAllroutes();

        while (res.moveToNext()){

            allL.add(new Location(res.getDouble(2),res.getDouble(3),
                    res.getString(4),res.getString(1),res.getString(5)));


        }


    list_adapter_location myadapter = new list_adapter_location(MainActivity.this,allL);

                savedLocation.setAdapter(myadapter);

}





}