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
    public static final String guardTable = "Guard_db";
    public static final String routeTable = "Route_db";
    public static final String routeStatus = "Route_status";
    public static final String routeHistory = "Route_history";
    public static final String data = "Data";
    public static final String allBeat = "All_beat";
    public static final String firstQueue = "first_queue";
    public static final String secondQueue = "second_queue";


    public Context ct;

    public database(Context c) {

        super(c, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + guardTable + "(guard_id INTEGER PRIMARY KEY NOT NULL, " +
                "name TEXT,image TEXT,Tour_no INTEGER,tour_time TEXT," +
                "completed_tour INTEGER,message TEXT," +
                "present boolean )");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + routeStatus + "(guard_id INTEGER PRIMARY KEY NOT NULL, " +
                "route_code TEXT)");


//        db.execSQL("CREATE TABLE IF NOT EXISTS " + routeTable + "( route_id INTEGER PRIMARY KEY NOT NULL,route_code TEXT," +
//                "route_name TEXT,beat_id TEXT,longitude DOUBLE, latitude DOUBLE,address TEXT )");


        db.execSQL("CREATE TABLE IF NOT EXISTS " + routeHistory + "(guard_id INTEGER PRIMARY KEY NOT NULL, " +
                "routeCode TEXT,tour_time TEXT," +
                "time_completed DATETIME DEFAULT CURRENT_TIMESTAMP,no_of_point INTEGER," +
                "Completed_point INTEGER )");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + data + "(id INTEGER PRIMARY KEY NOT NULL, " +
                "beat_id INTEGER UNIQUE,timer_start boolean," +
                "countdown_start boolean, tour_time TEXT,longitude DOUBLE, latitude DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + allBeat + "(beat_id TEXT PRIMARY KEY NOT NULL, " +
                "beat_name TEXT,beat_address Text, beat_status TEXT)");



        db.execSQL("CREATE TABLE IF NOT EXISTS " + firstQueue + "(id INTEGER PRIMARY KEY NOT NULL,guard_id INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + secondQueue + "(id INTEGER PRIMARY KEY NOT NULL,guard_id INTEGER NOT NULL)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + guardTable);
        onCreate(db);
    }

    public boolean insertGuardsDetails(int guardId, String guardName,
                                       String guardImage, int tourNo, String tourTime,
                                       int completedTour, String message, boolean present) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("guard_id", guardId);
        contentValues.put("name", guardName);
        contentValues.put("image", guardImage);
        contentValues.put("Tour_no", tourNo);
        contentValues.put("tour_time", tourTime);
        contentValues.put("completed_tour", completedTour);
        contentValues.put("message", message);
        contentValues.put("present", present);

        long result = db.insert(guardTable, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }


    }


    public boolean allBeat(String beat_id,
                                       String beat_name, String beat_address , String beat_status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("beat_id", beat_id);
        contentValues.put("beat_name", beat_name);
        contentValues.put("beat_address", beat_address);
        contentValues.put("beat_status", beat_status);

        long result = db.insert(allBeat, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }


    }


    public boolean insertRouteHistory(int guardId, String routeCode,
                                      String tourtime, int no_of_point,
                                      int completedPoint) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("guard_id", guardId);
        contentValues.put("name", routeCode);
        contentValues.put("tour_time", tourtime);
        contentValues.put("no_of_point", no_of_point);
        contentValues.put("Completed_point", completedPoint);

        long result = db.insert(routeHistory, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }


    }


    public boolean insertToFirstQueue(int guard_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("guard_id", guard_id);

        long result = db.insert(firstQueue, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }

    }

    public boolean insertToSecondQueue(int guard_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("guard_id", guard_id);

        long result = db.insert(secondQueue, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }

    }

    public Cursor getFirstQueue() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + firstQueue, null);


        return res;

    }


    public Cursor getSecondQueue() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + secondQueue, null);


        return res;

    }

    public Cursor getAllBeat() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + allBeat, null);


        return res;

    }

    public boolean insertRoute(String route_code, double longitude, double latitude, String address) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("route_code", route_code);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        contentValues.put("address", address);


        long result = db.insert(routeTable, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }


    }

    //insert completed route into route status

    public boolean insertRouteStatus(int guard_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("guard_id", guard_id);
        contentValues.put("route_code", "  ");

        long result = db.insert(routeStatus, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }


    }

    public boolean insertBeat(int beat) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("beat_id", beat);

        long result = db.insert(data, null, contentValues);

        if (result == -1) {
            return false;

        } else {
            return true;
        }
    }

    public boolean updateRouteStatus(int guard_id, String route_code) {
        boolean status;
        String rcode = "";

        if (!(route_code.trim() == "")) {
            rcode = "," + route_code;
        }
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("UPDATE " + routeStatus + " SET route_code = route_code || '" + rcode + "' WHERE guard_id =" + guard_id);
            status = true;
        }


        return status;
    }


    //****** Retrieve method starts here

    public Cursor getAllguards() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + guardTable, null);


        return res;
    }

    public Cursor getAllRouteHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + routeHistory, null);


        return res;
    }

    public Cursor getAllroutes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + routeTable, null);


        return res;
    }


    public Cursor getAllSavedBeats() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + data, null);


        return res;
    }

//    public int getTimerStart(int beat_id) {
//        int timer = -1;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT timer_start FROM " + data + " WHERE beat_id = " + beat_id + "", null);
//
//        while (res.moveToNext()) {
//
//            timer = res.getInt(0);
//
//        }
//
//        return timer;
//    }


//    public int getCountDownTimer(int beat_id) {
//
//        int timer = -1;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT countdown_start FROM " + data + " WHERE beat_id = " + beat_id + "", null);
//
//        while (res.moveToNext()) {
//
//            timer = res.getInt(0);
//
//        }
//
//        return timer;
//    }

    public int getroutesCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + routeTable, null);


        return res.getCount();
    }

    //checks guard already completed route
    public boolean getGuardRouteStatus(int id) {
        boolean routeCode = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + routeStatus + " WHERE guard_id = " + id + "", null);


        while (res.moveToNext()) {

            routeCode = true;

        }

        return routeCode;
    }

    //returns guard id from the firstQueue table
    public int getNextGuardId(int id) {
        int guard_id = -2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT guard_id FROM " + firstQueue + " WHERE id = " + id + "", null);


        while (res.moveToNext()) {

            guard_id = res.getInt(0);

        }

        return guard_id;
    }

    public String getGuardImage(int id) {
        String img = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT image FROM " + guardTable + " WHERE guard_id = " + id + "", null);


        while (res.moveToNext()) {

            img = res.getString(0);

        }

        return img;
    }

    //checks guard already completed route
    public Cursor getGuardDetails(int id) {


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + guardTable + " WHERE guard_id = " + id + "", null);


        return res;
    }

    //checks if the route code has already been used by the guard

    public boolean usedRoutecode(int id, String rCode) {
        boolean used = false;
        String routeCode = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + routeStatus + " WHERE guard_id = " + id + "", null);


        if (res.getCount() > 0) {

            while (res.moveToNext()) {
                routeCode = res.getString(1);
            }
            String[] routelist = routeCode.trim().split(",");


            for (int i = 0; i < routelist.length; i++) {

                if (routelist[i].equals(rCode)) {

                    used = true;
                }

            }

        }

        return used;
    }


    public int completedRoutecodecount(int id) {
        int count = 0;
        String routeCode = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + routeStatus + " WHERE guard_id = " + id + "", null);


        if (res.getCount() > -1) {


            while (res.moveToNext()) {
                routeCode = res.getString(1);
            }

            if (routeCode.trim().equals("")) {

                count = 0;
                return count;
            } else {

                String[] routelist = routeCode.trim().split(",");
                if (routelist.length > 0) {
                    int valid = 0;
                    for (int i = 0; i < routelist.length; i++) {

                        if (!routelist[i].trim().equals("")) {

                            valid++;
                        }
                    }
                    count = valid;
                }


            }
        }


        return count;
    }


    public Cursor getAllrouteStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + routeStatus, null);
        return res;
    }


    public double[] getLonLat(String routecode) {
        double[] lonlat = new double[2];
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Longitude,Latitude FROM " + routeTable + " WHERE route_code = '" + routecode + "' ", null);

        while (res.moveToNext()) {

            lonlat[0] = res.getDouble(0);
            lonlat[1] = res.getDouble(1);

        }


        return lonlat;
    }


    //checks if the route code already exists
    public boolean duplicateRouteCode(String code) {
        boolean status = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + routeTable + " WHERE route_code ='" + code + "' ", null);

        if (rs.getCount() > 0) {

            status = true;
        }

        return status;

    }


    public boolean incrementCompletedTour(int id) {
        boolean status = false;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("UPDATE " + guardTable + " SET completed_tour=completed_tour +1 WHERE guard_id =" + id);
            status = true;
        }
        return status;

    }


    public boolean guardSignIn(int id) {
        boolean status = false;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("UPDATE " + guardTable + " SET present=1 WHERE guard_id = " + id);
            status = true;
        }
        return status;

    }

    public boolean setTimer(int value, int beat_id) {
        boolean status = false;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("UPDATE " + data + " SET timer_start=" + value + " WHERE beat_id = " + beat_id);
            status = true;
        }
        return status;
    }


    public boolean setCountdown(int value, int beat_id) {
        boolean status = false;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("UPDATE " + data + " SET countdown_start=" + value + " WHERE beat_id = " + beat_id);
            status = true;
        } catch (Exception e) {
        }
        return status;

    }

    public boolean setTime(String value, int beat_id) {
        boolean status = false;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("UPDATE " + data + " SET tour_time='" + value + "' WHERE beat_id = " + beat_id);
            status = true;
        } catch (Exception e) {
        }
        return status;

    }

    // delete methods starts from here
    public boolean deleteRouteCode(String code) {
        boolean status = true;

        SQLiteDatabase db = this.getWritableDatabase();
        int dele = db.delete(routeTable, "route_code ='" + code + "' ", null);

        if (dele == -1) {
            status = false;
        }

        return status;

    }  // delete methods starts from here

    public boolean deleteFirstQueue(int id) {
        boolean status = true;

        SQLiteDatabase db = this.getWritableDatabase();
        int dele = db.delete(firstQueue, "guard_id =" + id + " ", null);

        if (dele == -1) {
            status = false;
        }

        return status;

    }


    public boolean deleteSecondQueue(int id) {
        boolean status = true;

        SQLiteDatabase db = this.getWritableDatabase();
        int dele = db.delete(secondQueue, "guard_id =" + id + " ", null);

        if (dele == -1) {
            status = false;
        }

        return status;

    }


    public void deleteRoutStatusDbData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + routeStatus);

    }


    public void deleteGuardDbData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + guardTable);

    }


    public void deleteRouteDbData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + routeTable);

    }
    public void deleteAllBeat() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + allBeat);

    }

    public String getName(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT name FROM " + guardTable + " WHERE guard_id =" + id, null);
        String name = "";

        while (res.moveToNext()) {
            name = res.getString(0);


        }

        return name;
    }

    public String getTime(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT tour_time FROM " + data + " WHERE beat_id =" + id, null);
        String name = "";

        while (res.moveToNext()) {
            name = res.getString(0);


        }

        return name;
    }


    //auto generate guard for tour

    public int getNextGuard() {
        int gId = -1;

        Cursor fQ = getFirstQueue();
        Log.i("i ran = ", "first");
        if (fQ.getCount() >= 1) {
            randomD = "first queue = " + fQ.getCount();

            ArrayList<Integer> fArray = new ArrayList<Integer>();
            Log.i("i ran = ", "" + fQ.getCount());
            while (fQ.moveToNext()) {
                fArray.add(fQ.getInt(0));
                fArray.add(fQ.getInt(1));

            }
            array = fArray.toString();
            Log.i("array list size = ", "" + fArray.size());
            //gets random number
            int random = new Random().nextInt(fArray.size());

            if (random == 0) {
                random = 1;
            }
            index = String.valueOf(random);
            int guard_id = getNextGuardId(random);
            Log.i("primary key id =", "" + random);
            Log.i("guard_id", "" + guard_id);

            if (guard_id > -1) {
                deleteFirstQueue(guard_id);
                insertToSecondQueue(guard_id);
            }


            gId = guard_id;

        } else {
            Log.i("reoccur from fQ", "reoccur from fQ");

            Cursor sQ = getSecondQueue();
            randomD = "second queue = " + sQ.getCount();
            while (sQ.moveToNext()) {

                int guard_id = sQ.getInt(1);
                insertToFirstQueue(guard_id);
                deleteSecondQueue(guard_id);


            }
            getNextGuard();

        }


        return gId;
    }


    public int getNextGuardNew() {
        int gId = -1;

        do {
            Cursor fQ = getFirstQueue();
            Log.i("i ran = ", "first");
            if (fQ.getCount() >= 1) {
                randomD = "first queue = " + fQ.getCount();

                ArrayList<Integer> fArray = new ArrayList<Integer>();
                Log.i(randomD+" i ran = ", "" + fQ.getCount());
                while (fQ.moveToNext()) {
                    fArray.add(fQ.getInt(1));

                }
                array = fArray.toString();
                Log.i("array list size = ", "" + fArray.size());
                //gets random number
                int random = new Random().nextInt(fArray.size());


//            if(fArray.size() ==1 && random ==1){
//
//                random = 0;
//            }

                index = String.valueOf(random);
                guard_id = fArray.get(random);
                Log.i("guard_id", "" + guard_id);

                if (guard_id > 0) {
                    deleteFirstQueue(guard_id);
                    insertToSecondQueue(guard_id);
                }


                gId = guard_id;

            } else {
                Log.i("reoccur from fQ", "reoccur from fQ");

                Cursor sQ = getSecondQueue();
                randomD = "second queue = " + sQ.getCount();
                while (sQ.moveToNext()) {

                    int guard_id = sQ.getInt(1);
                    insertToFirstQueue(guard_id);
                    deleteSecondQueue(guard_id);


                }

            }
            Log.i("final", "i return = " + gId);


        }while (guard_id < 1);

        gId =guard_id;
        Log.i("break", "i return = " + gId);


        return gId;


    }
}