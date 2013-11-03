package com.applications.frodo.views.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.applications.frodo.R;
import com.applications.frodo.utils.FileStorage;
import com.applications.frodo.views.CameraPhotoActivity;
import com.applications.frodo.widgets.CameraPreview;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by siddharth on 03/11/13.
 */
public class CameraFragment extends Fragment implements Camera.PictureCallback{

    private static final String TAG=CameraFragment.class.toString();

    private Camera mCamera;
    private CameraPreview mPreview;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera, container, false);
        setup();
        return rootView;
    }

    private int getCameraId(){
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras=Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return 0;
    }

    private void setup(){
        if(mCamera==null){
            mCamera = getCameraInstance();
            if(mCamera!=null){
                setCameraDisplayOrientation(this.getActivity(),getCameraId(),mCamera);
                mPreview = new CameraPreview(this.getActivity(), mCamera);
                FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
                preview.addView(mPreview);

                Button captureButton = (Button) rootView.findViewById(R.id.button_capture);

                final Camera.PictureCallback mPicture=this;

                captureButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get an image from the camera
                                mCamera.takePicture(null,null,mPicture);
                            }
                        }
                );
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setup();
    }

    @Override
    public void onPause(){
        super.onPause();
        releaseCamera();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        releaseCamera();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
            preview.removeAllViews();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){

        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            Log.e(TAG,"Camera unavailable", e);
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        releaseCamera();
        File pictureFile = FileStorage.getOutputMediaFile(FileStorage.MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }

        try {

            Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);

            FileOutputStream fos = new FileOutputStream(pictureFile);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();

            Intent cameraIntent = new Intent(this.getActivity().getBaseContext(), CameraPhotoActivity.class);
            Uri fileUri= Uri.fromFile(pictureFile);
            cameraIntent.putExtra("image", fileUri.getPath());
            startActivity(cameraIntent);

        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
}
