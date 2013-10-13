package com.applications.frodo.views.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.networking.PictureDownloader;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;

import java.util.Map;

/**
 * Created by siddharth on 12/10/13.
 */
public class EventPhotosFragment  extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_photos, container, false);

        final GridView eventPhotoGridView=(GridView) rootView.findViewById(R.id.eventPhotoGridView);

        FacebookEvents.getInstance().getPhotosOfEvent(GlobalParameters.getInstance().getCheckedInEventID(),new FacebookEvents.FacebookCallbacks() {
            @Override
            public void onEventPhotosDownloadComplete(Map<String, String> pictures) {
                for(String thumbnail: pictures.keySet()){
                    PictureDownloader downloader=new PictureDownloader(new PictureDownloader.PictureDownloaderListener() {
                        @Override
                        public void onPictureDownload(Bitmap bitmap) {

                        }
                    });
                }

            }

            @Override
            public void onPhotoShareComplete() {
            }


        });

        return rootView;
    }


}
