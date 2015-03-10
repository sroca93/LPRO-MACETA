package com.anaroc.anaro.myapplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import contenedores.Parametro;


public class CameraActivity extends Activity {
    protected static final int MEDIA_TYPE_IMAGE = 1;
    protected static final String TAG = "tag";
    private Camera mCamera;
    private CameraPreview mPreview;
    private static Context context;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;

    int idPlanta;
    private static int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = getApplicationContext();

        idPlanta = getIntent().getIntExtra("idPlanta", 0);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        findViewById(R.id.imageView1).bringToFront();
        findViewById(R.id.ImageView01).bringToFront();
        findViewById(R.id.ImageView02).bringToFront();


        final PictureCallback mPicture = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null) {
                    Log.d(TAG, "Error creating media file, check storage permissions: ");
                    return;
                }

                try {
                    /*
                    String imagepath = pictureFile.getAbsolutePath();

                    BitmapFactory.Options options0 = new BitmapFactory.Options();
                    options0.inSampleSize = 2;
                    // options.inJustDecodeBounds = true;
                    options0.inScaled = false;
                    options0.inDither = false;
                    options0.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Bitmap bmp = BitmapFactory.decodeFile(imagepath);

                    ByteArrayOutputStream baos0 = new ByteArrayOutputStream();

                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos0);
                    byte[] imageBytes0 = baos0.toByteArray();

                    ImageView image = null;
                    image.setImageBitmap(bmp);

                    String encodedImage = Base64.encodeToString(imageBytes0, Base64.DEFAULT);

                    new SendPhotoTask().execute(new Parametro("image", encodedImage), new Parametro("idPlanta", "3"));

                     */
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // messageText.setText("uploading started.....");
                                }
                            });
                            imgPath = pictureFile.getAbsolutePath();
                            Log.d("imgPath", imgPath);
                            uploadImage();
                            //uploadFile(pictureFile.getAbsolutePath());

                        }
                    }).start();


                    onBackPressed();

                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
                recreate();
            }
        };
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
    }

    public void uploadImage() {
        // When Image is selected from Gallery

            encodeImagetoString();

    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                if (bitmap == null) Log.d("bitmap", "null");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                //prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                params.put("plantID", Integer.toString(idPlanta));

                Log.d("idPlanta", Integer.toString(idPlanta));
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        //prgDialog.setMessage("Invoking Php");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.

        client.post("http://193.146.210.69/uploadImage.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //prgDialog.hide();
                        Toast.makeText(getApplicationContext(), response,
                                Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        //prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        Toast.makeText(context, "File created: " + mediaFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

        return mediaFile;
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    protected void onRestart() {
        super.onRestart();
        try {
            mCamera = getCameraInstance();
            //mCamera.setPreviewCallback(null);
            mPreview = new CameraPreview(this, mCamera);
            RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        } catch (Exception e) {
            Log.d("error", "Error starting camera preview: " + e.getMessage());
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }



}
