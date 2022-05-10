package com.example.guardtroldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class database extends SQLiteOpenHelper {

    public static String index = "";
    public static String randomD = "";
    public static String array = "";
    public static int guard_id = 0;
    public static final String DATABASE_NAME = "Guardtrol_db";
    public static final String location = "Guard_db";
    public static final String routeTable = "Route_db";



    public Context ct;

    public database(Context c) {

        super(c, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("CREATE TABLE IF NOT EXISTS " + location + "(guard_id INTEGER PRIMARY KEY NOT NULL, " +
//                "name TEXT,image TEXT,Tour_no INTEGER,tour_time TEXT," +
//                "completed_tour INTEGER,message TEXT," +
//                "present boolean )");


        db.execSQL("CREATE TABLE IF NOT EXISTS " + location + "( id INTEGER PRIMARY KEY NOT NULL,location_name TEXT Unique," +
                "longitude DOUBLE, latitude DOUBLE,address TEXT,image TEXT,username TEXT  )");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + location);
        onCreate(db);
    }


    public boolean insertRoute(String location_name, double longitude, double latitude,
                               String address, String image, String userName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("location_name", location_name);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        contentValues.put("address", address);
        contentValues.put("image", image);
        contentValues.put("username", userName);


        long result = db.insert(location, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }


    }



    public Cursor getAllroutes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + location, null);


        return res;
    }


    public void deleteRouteDbData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + routeTable);

    }

}