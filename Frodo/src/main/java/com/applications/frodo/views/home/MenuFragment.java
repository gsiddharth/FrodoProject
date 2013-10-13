package com.applications.frodo.views.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.applications.frodo.MainActivity;
import com.applications.frodo.R;
import com.applications.frodo.utils.FileStorage;
import com.applications.frodo.views.CameraPhotoActivity;
import com.applications.frodo.views.checkin.CheckinActivity;
import com.facebook.widget.LoginButton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;


/**
 * Created by siddharth on 20/09/13.
 */
public class MenuFragment extends Fragment{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static String TAG=MenuFragment.class.toString();
    public static final String ARG_SECTION_NUMBER = "1";
    private Uri fileUri=null;


    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu, container, false);
        Button checkinMenuButton=(Button) rootView.findViewById(R.id.checkinMenuButton);

        checkinMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckinButtonClick(view);
            }
        });

        Button cameraMenuButton=(Button) rootView.findViewById(R.id.cameraMenuButton);

        cameraMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCameraButtonClick(view);
            }
        });



        return rootView;
    }

    public void onCheckinButtonClick(View view){
        Intent intent=new Intent(this.getActivity(), CheckinActivity.class);
        startActivity(intent);
    }

    public void onCameraButtonClick(View view){
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri= FileStorage.getOutputMediaFileUri(FileStorage.MEDIA_TYPE_IMAGE);
        System.out.println(fileUri);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(takePictureIntent, 10101);
    }

    private void handleCameraPhoto(Intent intent) {

        File file=new File(fileUri.getPath());
        if(file.exists()){
            Intent cameraIntent = new Intent(this.getActivity().getBaseContext(), CameraPhotoActivity.class);
            cameraIntent.putExtra("image", fileUri.getPath());
            startActivity(cameraIntent);
        }else{
            Log.d(TAG,"No File of exists at the path"+fileUri.getPath());
            Intent mainIntent=new Intent(this.getActivity(), ApplicationActivity.class);
            startActivity(mainIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 10101) {
            handleCameraPhoto(data);
        }
    }
}