package com.applications.frodo.views.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.networking.PictureDownloader;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.widgets.ImageAdapter;

import java.util.ArrayList;
import java.util.List;
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

        IEvent event=GlobalParameters.getInstance().getCheckedInEvent();

        final Context context=this.getActivity().getBaseContext();

        System.out.println(event);

        if(event!=null && event.getId()!=null){

            FacebookEvents.getInstance().getPhotosOfEvent(event.getId(),new FacebookEvents.FacebookCallbacks() {
                @Override
                public void onEventPhotosDownloadComplete(Map<String, String> pictures) {
                    List<String> thumbnails=new ArrayList<String>();
                    List<String> images=new ArrayList<String>();

                    for(Map.Entry<String, String> entry:pictures.entrySet()){
                        thumbnails.add(entry.getKey());
                        images.add(entry.getKey());
                    }

                    eventPhotoGridView.setAdapter(new ImageAdapter(context,thumbnails, images));

                }

                @Override
                public void onPhotoShareComplete() {
                }


            });
        }

        return rootView;
    }


}
