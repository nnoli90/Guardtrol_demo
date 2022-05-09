package com.example.guardtroldemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;


public class Clock_in extends AppCompatActivity {


    // global variables for views
    Button verify;
    Button submit_tour;
    ImageButton openC;
    TextView longitude;
    TextView noOfRoute;
    TextView completedRoute;
    TextView adress;
    TextView guard_name;
    TextView route_code_text;
    EditText routeCode;
    EditText id;
    EditText guard_message;
    ImageView user_image;
    ImageView guard_pic;
    com.airbnb.lottie.LottieAnimationView loading;
    SeekBar progress_bar;

    //for route
    int tRoute;
    int cRoute;
    Boolean performRoute;
    int user_id;
    String rCode;
    String tTime;
    String ImgHttp;
    String guardName;


    //for images
    File photoFile;
    String imagename = "picture";
    String imagepath;
    String edit;

    boolean camera_permission;

    ActivityResultLauncher<Intent> someActivityResultLauncher;
    Bitmap imgVerification;
    Bitmap correct_orientation;


    double lat;
    double lon;
    user_picture mypic;
    StringBuffer image1;
    StringBuffer image2;

    //for location class
    FindLocation mylocation;


    //for database
    database db;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);

//        cancels all notification
        NotificationManagerCompat.from(this).cancelAll();


//        initialize view variables
        openC = findViewById(R.id.opencamera);
        verify = findViewById(R.id.verify);

        user_image = findViewById(R.id.img);
        guard_pic = findViewById(R.id.guard_pic);
        longitude = findViewById(R.id.longitude);
        adress = findViewById(R.id.adress);
        loading = findViewById(R.id.loading);

        guard_name = findViewById(R.id.guard_name);
        routeCode = findViewById(R.id.route_code);
        route_code_text = findViewById(R.id.route_text);



//      initialize the picture class
        mypic = new user_picture();

        Bundle bundle = getIntent().getExtras();
        String   name = bundle.getString("name");
         ImgHttp = bundle.getString("image");

        guard_name.setText(name);

        //set image on image view
        Glide.with(this)
                .load(ImgHttp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(guard_pic);

      progress_bar.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {
              return true;
          }
      });
        //initialize database
        db  = new database(this);








        DownloadImage d = new DownloadImage();
        d.execute();
//        prepare the camera activity launcher
        openCamera();
        loading.setVisibility(View.INVISIBLE);



//      initialize dialog class
        loadingLocationDialog myDialog = new loadingLocationDialog(Clock_in.this);


      openC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launches the activity


                if(ContextCompat.checkSelfPermission(Clock_in.this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED){

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                try {
                    photoFile = getPhotoFile(imagename);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoFile);
                 Uri pfile =  FileProvider.getUriForFile(Clock_in.this,"com.example.monitoring_app.fileprovider",photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,pfile);
                someActivityResultLauncher.launch(takePicture);


            }
            else{

                    requestCameraPermission();
                }

            }



      });



;

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify.setEnabled(false);


                if (ContextCompat.checkSelfPermission(Clock_in.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission
                            (Clock_in.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {




                            //initializies the location class
                            mylocation = new FindLocation(Clock_in.this);
                            verify.setEnabled(false);
                            if (mylocation.location_enabled) {


                                if (imgVerification != null) {

                                    if (!id.getText().toString().trim().equals("")) {
                                        myDialog.startLoading();

                                        //grts the user id from the edit text field
                                        int Uid = Integer.parseInt(id.getText().toString());
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                lon = mylocation.getLon();
                                                lat = mylocation.getLat();

                                                if (mylocation.getCurrent_adress() != null) {

                                                    adress.setText(mylocation.getCurrent_adress().get(0).getAddressLine(0));

                                                }
                                                longitude.setText(String.format(Locale.US, "Lon: %9f \n Lat: %9f", lon, lat));


                                                mylocation.verify(6.499712, 3.190435, lat, lon);

                                                myDialog.dismissDialog();


                                                if (mylocation.locationSRoute) {

                                                    //send the image verification process to a different thread
                                                    AsyncTaskRunnerVerifyImgClock_in  verfiyClock_in = new AsyncTaskRunnerVerifyImgClock_in();
                                                    verfiyClock_in.execute();


                                                } else {
                                                    messageDialog("Out Of Premises", "You are " + mylocation.getDistance() + " from your expected Location " +
                                                            "Please come closer to your expected location");
                                                    verify.setEnabled(true);

                                                }

                                            }
                                        }, 10000);


                                    } else {
                                        messageDialog("No Id Entered", "Please enter your user Id");
                                        verify.setEnabled(true);

                                    }
                                } else {
                                    messageDialog("No Image captured", "Please capture Image");
                                    verify.setEnabled(true);



                                }
                            }
//                            else { enable loaction}
                        }


                else{

                 requestLocationPermission();
                }



            }



        });



    }


    public void requestLocationPermission() {

        int granted = PackageManager.PERMISSION_GRANTED;
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        granted  && ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) ==
                        granted && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        granted)
        {



        } else {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 200);

        }


    }

//generate a file wher the photo is to be saved
    public File getPhotoFile(String filename) throws IOException {

        File storagedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(filename,".jpg",storagedir);

        imagepath =image.getAbsolutePath();

        return image;
    }



    public void deletefile(String filename) throws IOException {

        File fdelete =new File(photoFile.getAbsolutePath());
        if (fdelete.exists()){
            if(fdelete.delete()){

                Toast.makeText(Clock_in.this,
                        "image file was deleted", Toast.LENGTH_LONG).show();
            }


        }else {

            Toast.makeText(Clock_in.this,
                    "image file was deleted", Toast.LENGTH_LONG).show();

        }


    }


//ask for camera permission
    public void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED )
        {




        } else {


             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                       Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
                     Manifest.permission.ACCESS_COARSE_LOCATION}, 200);

        }


    }


//    //ask for location permission
//    public void requestLocationPermission() {
//        if (
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
//                        PackageManager.PERMISSION_GRANTED  &&
//                        ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) ==
//                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
//                        PackageManager.PERMISSION_GRANTED )
//        {
//
//
//
//        } else {
//
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
//                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
//
//        }
//
//
//    }




    //ask for internet permission
    public void requestInternetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED)
        {



        } else {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.INTERNET}, 200);

        }


    }






    public void openCamera() {
        // check if app is granted camera permission
        camera_permission =true;
        if (camera_permission) {


           someActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {


                                imgVerification = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                                imgVerification = mypic.setPicRotation(photoFile.getAbsolutePath());


                                correct_orientation = imgVerification;

                                user_image.setImageBitmap(correct_orientation);
//                                user_image.setImageBitmap(mypic.getImg());

                                AsyncTaskBase64 base64 = new AsyncTaskBase64();
                                base64.execute();



                            }
                        }


                    });


            //display error if app does not have camera permission

        }
        else{

            Toast.makeText( this,"cannot  access camera", Toast.LENGTH_SHORT).show();
        }


    }






public void messageDialog(String title, String message){

    MessageDialog myDialog = new MessageDialog();
    myDialog.showDialog(Clock_in.this,message,title);



}

    public void submitTour(View view) {

        if (tRoute == cRoute) {
            String g_message = guard_message.getText().toString().trim();

            if (g_message.equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Clock_in.this);
                alertDialog.setTitle("Submit Tour");
                alertDialog.setMessage("You have not Entered any message do you want to submit tour");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Clock_in.this, "Operation cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.create().show();


            }


        }
        String g_message = guard_message.getText().toString().trim();

        if(g_message.equals("")) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Clock_in.this);
            alertDialog.setTitle("Incomplete Tour");
            alertDialog.setMessage("Please Enter Message Before Submitting");
            alertDialog.setCancelable(true);
            alertDialog.create().show();
            guard_message.requestFocus();

        }else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Clock_in.this);
            alertDialog.setTitle("Incomplete Tour");
            alertDialog.setMessage("Do you want to submit tour?\nthis action cannot be reverted");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(Clock_in.this, "Operation cancelled", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setCancelable(true);
            alertDialog.create().show();




        }





    }



    private class AsyncTaskRunnerVerifyImgClock_in extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {

                mypic.VerifyImage(Clock_in.this, user_id, lat, lon,
                        loading,image1.toString(), image2.toString(), verify, photoFile);


            int status;
            do {
                status = mypic.getVerifiedstatus();
            } while (status < 1);


            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
            verify.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if(mypic.getVerifiedstatus() == 1) {
                messageDialog("Face Recognised", "Clock in successful");
                mypic.setVerifiedstatus(0);
                verify.setEnabled(true);
                Toast.makeText(Clock_in.this, " on post ran for image verified", Toast.LENGTH_SHORT).show();


                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(Clock_in.this,Profile.class);
                            startActivity(i);
                            finish();
                        }
                    },3000);



            }

            else if(mypic.getVerifiedstatus() == 2)
            {
                Toast.makeText(Clock_in.this, " on post ran for face not recognised", Toast.LENGTH_SHORT).show();

                mypic.setVerifiedstatus(0);
                messageDialog("Face not Recognised", "Face Recognition failed\nEnsure your face is " +
                        "clearly visible in the Image taken");
                verify.setEnabled(true);

            }else {
                mypic.setVerifiedstatus(0);
                messageDialog("Connection error", "Error communicating to server");
                Toast.makeText(Clock_in.this, " on post ran for sever error", Toast.LENGTH_SHORT).show();

                verify.setEnabled(true);
            }

        }
    }



private class AsyncTaskBase64 extends AsyncTask<Void,Void,Void>{


    @Override
    protected Void doInBackground(Void... voids) {


        image1 = mypic.getBase64(imgVerification);
        do {

            }while (mypic.uncompleteBase64);


        return null;

    }

    @Override
    protected void onPostExecute(Void img) {

        Toast.makeText(Clock_in.this, " enabled camera and button", Toast.LENGTH_SHORT).show();
        verify.setEnabled(true);
        openC.setEnabled(true);

        super.onPostExecute(img);




    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(Clock_in.this, " disabled camera and button", Toast.LENGTH_SHORT).show();
        openC.setEnabled(false);
        verify.setEnabled(false);
    }
}

    private class DownloadImage extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {


            boolean mem =  mypic.loadBitmapByGlide("https://i.ibb.co/5c8hD5f/EBUSKY02.jpg", Clock_in.this);

            do {

            }while (!mem);

            image2 = mypic.getBase64(mypic.getImg());


            do {

            }while (mypic.uncompleteBase64);

            return null;
        }

        @Override
        protected void onPostExecute(Void img) {
            super.onPostExecute(img);

            Toast.makeText(Clock_in.this, "Bitmap 1 is ready", Toast.LENGTH_SHORT).show();



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Clock_in.this, " pre execute ran for base64", Toast.LENGTH_SHORT).show();

        }
    }
















}