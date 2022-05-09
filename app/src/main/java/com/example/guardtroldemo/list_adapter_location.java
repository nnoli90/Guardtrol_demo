package com.example.guardtroldemo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class list_adapter_location extends ArrayAdapter<Location> {


    private Context context;
    private  ArrayList<Location> data = new ArrayList<>();

    public list_adapter_location(Context m, ArrayList<Location> beats ){

        super(m,R.layout.all_location, beats);
        this.context =m;
        data = beats;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Location beats = getItem(position);

        if (convertView == null){


            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_location, parent
                    , false);

        }



        TextView locationname = convertView.findViewById(R.id.location_name);

        TextView address = convertView.findViewById(R.id.adress);
        TextView lonLat = convertView.findViewById(R.id.lon_lat);



        // set the values of each view
//
        locationname.setText(beats.name);
        address.setText(beats.address);
        String lonL = "Lon: "+beats.lon+ "\nLat: "+ beats.lat;
        lonLat.setText(lonL);


        return convertView;
    }


}
