package com.example.guardtroldemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class user_picture {

    private final String url ="https://facerecog.guardtrol.com/verify";
//    private final String url ="https://staff-face-recog.herokuapp.com/verify";




    boolean uncompleteBase64 = true;
    boolean route;
    Bitmap img;

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    int  verifiedstatus;
    public String getUrl() {
        return url;
    }

    public int getVerifiedstatus() {
        return verifiedstatus;
    }

    public void setVerifiedstatus(int verifiedstatus) {
        this.verifiedstatus = verifiedstatus;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }

    public Bitmap setPic(int width, int height, String currentPhotoPath) {
        // Get the dimensions of the View
        int targetW = width;
        int targetH = height;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        return bitmap;

    }


    public Bitmap setPicRotation(String imagepath) {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagepath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(imagepath, opts);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Bitmap bp = setPic(500, 500,imagepath);



        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);

        return rotatedBitmap;
    }


    @SuppressLint("NewApi")
    public StringBuffer getBase64(Bitmap b){


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        StringBuffer my = new StringBuffer();
        my.append(Base64.encodeToString(byteArray,Base64.NO_WRAP));
        uncompleteBase64 = false;

        return my;


    }








    public void VerifyImage(Context m , int id ,
                            double latitude, double longitude,com.airbnb.lottie.LottieAnimationView loading,
                            String image1,String image2,
    Button verify,File photo){



            final  JSONObject jsonObject = new JSONObject();
            try {
//                jsonObject.put("staff_id",id);
//                jsonObject.put("longitude",longitude);
//                jsonObject.put("latitude",latitude);
                jsonObject.put("img1","data:image/jpeg;base64,"+image1);
                jsonObject.put("img2","data:image/jpeg;base64,"+image2);




            }catch (JSONException e){
                e.printStackTrace();


            }

            RequestQueue requestQueue = Volley.newRequestQueue(m);

            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.POST, getUrl(), jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    loading.setVisibility(View.INVISIBLE);

                    if(response !=null) {


                        JSONObject result = response.optJSONObject("pair_1");

                        if(result != null) {
//                                 Intent i = new Intent(m, Clock_in_History.class);
//                                 m.startActivity(i);
                           setVerifiedstatus(1);
//

                        }

                        else {

                            Toast.makeText(m,"Failed to clock in", Toast.LENGTH_LONG).show();
                            setVerifiedstatus(2);
                        }

//                            try {
//                                deletefile(photo, m);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    AlertDialog.Builder alertDialog= new AlertDialog.Builder(m);
//                    alertDialog.setTitle("Status");
//                    alertDialog.setMessage(error.toString());
//                    alertDialog.setCancelable(true);
//                    alertDialog.create().show();
                    Toast.makeText(m,"volley error", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.INVISIBLE);
                    setVerifiedstatus(3);
                    Log.e("volley error",error.toString());

                    try {
                        deletefile(photo,m);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


            );


            requestQueue.add(objectRequest).setRetryPolicy(new DefaultRetryPolicy(20 * 1000 ,2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));






          }


    public void deletefile(File ph, Context m) throws IOException {

        File fdelete =new File(ph.getAbsolutePath());
        if (fdelete.exists()){
            if(fdelete.delete()){

                Toast.makeText(m,
                        "image file was deleted", Toast.LENGTH_LONG).show();
            }


        }else {

            Toast.makeText(m,
                    "image file was deleted", Toast.LENGTH_LONG).show();

        }


    }




    public Bitmap getImageFromUr(String url,Context m)  {
        Bitmap bitmap = null;
//       bitmap = loadBitmapByGlide(url,m);

//        Toast.makeText(cm, bitmap.getWidth(), Toast.LENGTH_SHORT).show();

        return getResizedBitmap(bitmap,500,500);
    }



   // resize bitmap

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;

    }





 public boolean loadBitmapByGlide(String imageUrl, Context c) {
    Bitmap bt = null;
    boolean t = false;

     try {

        bt = Glide.with(c)
                 .asBitmap()
                 .load(imageUrl)
                 .submit(500,500)
                 .get();
        if(bt != null){

        t =true;

        }
     } catch (ExecutionException e) {
         e.printStackTrace();
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
    setImg(bt);


     return t;
 }





//    public void writeTofile(Context c ,String image2) {
//        File file = new File(c.getExternalFilesDir("/").getAbsolutePath() + "/output.txt");
//        FileOutputStream stream;
//
//        {
//            try {
//                stream = new FileOutputStream(file);
//                stream.write(image2.toString().getBytes());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }



}
