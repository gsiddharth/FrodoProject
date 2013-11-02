package com.applications.frodo.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.blocks.IPhoto;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.socialnetworks.facebook.FacebookPermissions;
import com.applications.frodo.utils.FileStorage;
import com.applications.frodo.views.home.ApplicationActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CameraPhotoActivity extends Activity {

    private static String TAG=CameraPhotoActivity.class.toString();
    private Uri fileUri=null;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo);

        final Button shareButton=(Button) findViewById(R.id.cameraPhotoShareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            private boolean clicked=false;
            @Override
            public void onClick(View v) {
                if(!clicked){
                    share();
                    Toast toast=Toast.makeText(getBaseContext(),"Sharing", Toast.LENGTH_SHORT);
                    toast.show();
                    clicked=true;
                }
            }
        });


        Button cameraButton=(Button) findViewById(R.id.cameraPhotoCameraButton);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri= FileStorage.getOutputMediaFileUri(FileStorage.MEDIA_TYPE_IMAGE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                startActivityForResult(takePictureIntent, 10101);
            }
        });

        Intent intent = getIntent();
        String fileUri=intent.getStringExtra("image");
        File file=new File(fileUri);

        if(file.exists()){
            try{
                InputStream inputStream=new FileInputStream(file);
                InputStream in=new BufferedInputStream(inputStream);
                image= BitmapFactory.decodeStream(in);

                ImageView imageView = (ImageView) findViewById(R.id.cameraPhotoPhoto);
                imageView.setImageBitmap(image);

                TextView eventNameView = (TextView) findViewById(R.id.cameraPhotoCheckedInEventName);
                if(GlobalParameters.getInstance().getCheckedInEvent()!=null){
                    eventNameView.setText(GlobalParameters.getInstance().getCheckedInEvent().getName());
                }
            }catch(FileNotFoundException e){
                Log.e(TAG, "",e);
            }
        }
    }

    public void shareHelp(){
        if(image!=null){
            FacebookEvents.getInstance().sharePhoto(image, new FacebookEvents.FacebookCallbacks() {
                @Override
                public void onPhotoShareComplete() {
                    final Button shareButton=(Button) findViewById(R.id.cameraPhotoShareButton);
                    Toast toast=Toast.makeText(getBaseContext(),"Shared", Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void onEventPhotosDownloadComplete(List<IPhoto> photos) {
                }
            });
        }
    }

    public void share(){

        if(GlobalParameters.getInstance().isHasAllFBWritePermissions()){
            shareHelp();
        }else{
            String[] writePermissions=getResources().getString(R.string.facebook_write_permissions).split("[,]");
            List<String> writePermissionList=new ArrayList<String>();
            Collections.addAll(writePermissionList,writePermissions);

            if(!FacebookPermissions.requestWritePermissions(writePermissionList, this, 1023)){
                shareHelp();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera_photo, menu);
        return true;
    }

    private void handleCameraPhoto(Intent intent) {

        File file=new File(fileUri.getPath());
        if(file.exists()){
            Intent cameraIntent = new Intent(this.getBaseContext(), CameraPhotoActivity.class);
            cameraIntent.putExtra("image", fileUri.getPath());
            startActivity(cameraIntent);
        }else{
            Log.d(TAG,"No File of exists at the path"+fileUri.getPath());
            Intent mainIntent=new Intent(this, ApplicationActivity.class);
            startActivity(mainIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 10101) {
            handleCameraPhoto(data);
        }else if(requestCode==1023){
            GlobalParameters.getInstance().setHasAllFBWritePermissions(true);
            shareHelp();
        }
    }

}
