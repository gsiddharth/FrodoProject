package com.applications.frodo.views.event;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.networking.PictureDownloader;
import com.applications.frodo.utils.Convertors;

/**
 * Need to save event details in the bundle
 * Created by siddharth on 26/10/13.
 */
public class EventDetailFragment extends Fragment implements PictureDownloader.PictureDownloaderListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "1";
    public static final String EVENT_KEY="eventkey";

    private IEvent event;
    private View rootView;

    public EventDetailFragment(){
    }

    public EventDetailFragment(IEvent event) {
        this.event=event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.event_details, container, false);
        readSavedState(savedInstanceState);
        setupDetails();
        setupImage();
        return rootView;
    }

    private void setupImage(){
        ImageView imageView=(ImageView) rootView.findViewById(R.id.image);
        if(event.getImage()==null){
            if(event.getImagePath()!=null && !"".equals(event.getImagePath())){
                PictureDownloader downloader=new PictureDownloader(this);
                downloader.execute(event.getImagePath());
            }
        }else{
            Bitmap bm= Bitmap.createScaledBitmap(event.getImage(),imageView.getWidth(), event.getImage().getHeight()*imageView.getWidth()/event.getImage().getWidth(),false);
            imageView.setImageBitmap(bm);
        }
    }

    @Override
    public void onPictureDownload(Bitmap bitmap) {
        if(bitmap!=null){
            event.setImage(bitmap);
            ImageView imageView=(ImageView) rootView.findViewById(R.id.image);
            Bitmap bm= Bitmap.createScaledBitmap(event.getImage(),imageView.getWidth(), event.getImage().getHeight()*imageView.getWidth()/event.getImage().getWidth(),false);
            imageView.setImageBitmap(bm);
        }
    }

    private void setupDetails(){

        TextView eventname=(TextView) rootView.findViewById(R.id.eventname);
        eventname.setText(event.getName());

        TextView location=(TextView) rootView.findViewById(R.id.eventlocation);
        location.setText(event.getLocation().toString());

        TextView eventdate=(TextView)rootView.findViewById(R.id.eventdate);

        String from=Convertors.toString(event.getStartTime(),"EEE, dd MMM - h:mm a", "EEE, dd MMM");
        String to=Convertors.toString(event.getEndTime(),"EEE, dd MMM - h:mm a", "EEE, dd MMM");

        if(to!=null && !to.trim().equals("")){
            eventdate.setText(from+ " to "+to);
        }else if (from!=null && !from.trim().equals("")){
            eventdate.setText(from);
        }

        TextView eventabout=(TextView)rootView.findViewById(R.id.eventabout);
        eventabout.setText(event.getSummary());
    }

    private void setupFriendList(){

    }

    private void readSavedState(Bundle savedInstanceState){
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(EVENT_KEY)){
                this.event=savedInstanceState.getParcelable(EVENT_KEY);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        if(bundle!=null){
            bundle.putParcelable(EVENT_KEY, event);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public void onPause(){
        super.onPause();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}