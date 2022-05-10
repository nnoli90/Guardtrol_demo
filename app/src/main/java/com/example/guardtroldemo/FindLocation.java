package com.example.guardtroldemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FindLocation {

    LocationManager locationManager;
    Context mContext;
     private double lat;
     private double lon;
     private List<Address> current_adress;
    Geocoder geocoder;
    boolean locationSClock_in = false;
    boolean locationSRoute = false;
    String distance;
    boolean location_enabled;


    @SuppressLint("MissingPermission")
        public FindLocation(Context c){

            mContext=c;
            locationManager=(LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000,
                    0, locationListenerGPS);
            isLocationEnabled();


        }


    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setCurrent_adress(List<Address> current_adress) {
        this.current_adress = current_adress;
    }

    public boolean isLocationSClock_in() {
        return locationSClock_in;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getDistance() {
        return distance;
    }

    public List<Address> getCurrent_adress() {
        return current_adress;
    }

    public boolean getLocationSRoute() {
        return  locationSRoute;
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
           setLat(location.getLatitude());
           setLon(location.getLongitude());

            if(getLon()+getLat() > 0) {

                geocoder = new Geocoder(mContext, Locale.getDefault());

                AsyncGetAddress address = new AsyncGetAddress();
                address.execute();

            }
      }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    protected void onResume(){

        isLocationEnabled();
    }

    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                    location_enabled = false;
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{
            location_enabled = true;

        }
    }


    void verify( double latpnt1, double lonpoint1, double latpoint2,double lonpoint2 ){

      double d =  calculateDistance(latpnt1, lonpoint1, latpoint2, lonpoint2);

      if (d <= 50){

//          Toast.makeText(mContext, "Login in progress", Toast.LENGTH_LONG).show();
          locationSClock_in = true;
          distance = String.format(Locale.US," %2f meters", d);

          if(d <= 18){

              locationSRoute = true;
              distance = String.format(Locale.US," %2f meters", d);

          }

      }else{

//          Toast.makeText(mContext, "you are " + String.format(Locale.US," %2f meters", d) + " from Your Expected Location. \n
//          Please Come closer"
//                  ,Toast.LENGTH_LONG).show();
            distance = String.format(Locale.US," %2f meters", d);
          locationSRoute = false;

      }

    }



  public  double calculateDistance(double latPoint1, double lngPoint1,
                             double latPoint2, double lngPoint2) {
        if(latPoint1 == latPoint2 && lngPoint1 == lngPoint2) {
            return 0;
        }

        final double EARTH_RADIUS = 6371.0; //km value;

        //converting to radians
        latPoint1 = Math.toRadians(latPoint1);
        lngPoint1 = Math.toRadians(lngPoint1);
        latPoint2 = Math.toRadians(latPoint2);
        lngPoint2 = Math.toRadians(lngPoint2);

        double distance = Math.pow(Math.sin((latPoint2 - latPoint1) / 2.0), 2)
                + Math.cos(latPoint1) * Math.cos(latPoint2)
                * Math.pow(Math.sin((lngPoint2 - lngPoint1) / 2.0), 2);

        distance = 2.0 * EARTH_RADIUS * Math.asin(Math.sqrt(distance));

  Toast.makeText(mContext, String.format(Locale.US," %2f meter", distance*1000), Toast.LENGTH_LONG).show();


        //meter value
        return distance * 1000;

    }

//
//    public boolean verifyLocation( double lat1, double lon1, double lat2, double lon2){
//       boolean status = false;
//
//       //Calculate longitude and latitude difference between
//
//      double  lonD = lon1-lon2;
//
//      double d = Math.sin(deg2rad(lat1))
//              * Math.sin(deg2rad(lat2))
//              + Math.cos(deg2rad(lat1))
//              * Math.cos(deg2rad(lat2))
//              * Math.cos(deg2rad(lonD));
//        d = Math.acos(d);
//        //convert distance radian to degree
//        d = rad2deg(d);
//        //distance in miles
//        d = d * 60* 1.1515;
//        // distance in kilomters
//        d = d * 1.609344;
//
//        d = d*1000;
//
//        Toast.makeText(mContext, String.format(Locale.US," %2f meters", d), Toast.LENGTH_LONG).show();
//
//
//      return status;
//    }

    //convert to radians
    private double rad2deg(double distance){

        return (distance * 180.0 /Math.PI);

    }

    public double deg2rad(double la1){

        return (la1*Math.PI/180.0);

    }

    private class AsyncGetAddress extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
                setCurrent_adress(addressList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }




    }




}


