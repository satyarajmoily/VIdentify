package com.howaboutthis.satyaraj.videntify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IdentifyActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_TAKE_PHOTO = 200;
    private static final int CLICK_IMAGE_REQUEST = 2;
    private static final String TAG = "IdentifyActivity";
    private RequestPermissionHandler mRequestPermissionHandler;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);

        mRequestPermissionHandler = new RequestPermissionHandler();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Visual Recognition");
        Button cameraButton = findViewById(R.id.camera);
        Button galleryButton = findViewById(R.id.gallery);

        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent intent = new Intent(IdentifyActivity.this, ProfileActivity.class);
            startActivity(intent);
        }else if(id == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.camera:
                takePicture();
                break;

            case R.id.gallery:
                pickFromGallery();
                break;
        }
    }

    private void pickFromGallery() {

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        mRequestPermissionHandler.requestPermission(this, PERMISSIONS, REQUEST_TAKE_PHOTO, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }

            @Override
            public void onFailed() {
                Toast.makeText(IdentifyActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void takePicture() {

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        mRequestPermissionHandler.requestPermission(this, PERMISSIONS, REQUEST_TAKE_PHOTO, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                dispatchTakePictureIntent();
            }
            @Override
            public void onFailed() {
                Toast.makeText(IdentifyActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String filePath = null;

            if (storageDir != null) {
                filePath = BitmapCompressor.with(IdentifyActivity.this)
                        .compress(String.valueOf(uri),storageDir,false);
            }

            Intent intent = new Intent(IdentifyActivity.this, detailedActivity.class);
            intent.putExtra("PATH", filePath);
            startActivity(intent);

        } else if (requestCode == CLICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            galleryAddPic();

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String filePath = null;

            if (storageDir != null) {
                filePath = BitmapCompressor.with(IdentifyActivity.this)
                        .compress(mCurrentPhotoPath,storageDir,true);
            }
            Intent intent = new Intent(IdentifyActivity.this, detailedActivity.class);
            intent.putExtra("PATH", filePath);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        mRequestPermissionHandler.onRequestPermissionsResult(requestCode,
                grantResults);
    }

    //Creating a path for the captured image to be saved. Using this path we can retrieve the original Image.
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG,"Error while creating the file");
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.howaboutthis.satyaraj.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CLICK_IMAGE_REQUEST);
            }
        }
    }

    // Adding the pic to the gallery so that it is visible to everyone
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}