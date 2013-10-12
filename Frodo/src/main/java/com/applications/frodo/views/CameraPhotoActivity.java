package com.applications.frodo.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;

import com.applications.frodo.R;

public class CameraPhotoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo);
        Intent intent = getIntent();
        Bitmap image=intent.getParcelableExtra("image");
        ImageView imageView = (ImageView) findViewById(R.id.cameraActivityPhoto);
        imageView.setImageBitmap(image);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera_photo, menu);
        return true;
    }
    
}
