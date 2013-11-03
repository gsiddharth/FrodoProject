package com.applications.frodo.views.event;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.ILocation;
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
    private Button checkInButton;

    public EventDetailFragment(){
    }

    public EventDetailFragment(IEvent event) {
        this.event=event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.event_details, container, false);
        checkInButton=(Button) rootView.findViewById(R.id.checkinButton);

        checkInButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(Color.parseColor("#222222"));
                    v.playSoundEffect(SoundEffectConstants.CLICK);
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    v.setBackgroundColor(Color.parseColor("#ffffff"));
                    onCheckInButtonClick(v);
                }else if(event.getAction()==MotionEvent.ACTION_CANCEL){
                    v.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                return false;
            }
        });

        readSavedState(savedInstanceState);
        setupDetails();
        setupImage();
        return rootView;
    }

    private void onCheckInButtonClick(View v){
        GlobalParameters.getInstance().setCheckedInEvent(this.event);
        Toast toast=Toast.makeText(getActivity().getBaseContext(),"Checked In", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void setupImage(){
        ImageView imageView=(ImageView) rootView.findViewById(R.id.image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(event.getImage()==null){
            if(event.getImagePath()!=null && !"".equals(event.getImagePath())){
                PictureDownloader downloader=new PictureDownloader(this);
                downloader.execute(event.getImagePath());
            }
        }else{
            imageView.setImageBitmap(event.getImage());
        }
    }

    @Override
    public void onPictureDownload(Bitmap bitmap) {
        if(bitmap!=null){
            event.setImage(bitmap);
            ImageView imageView=(ImageView) rootView.findViewById(R.id.image);
            imageView.setImageBitmap(event.getImage());
        }
    }

    private void setStyle(String st, SpannableStringBuilder ssb, Object style, String seperator){
        if(st!=null && !"".equals(st) && !"null".equals(st)){
            st=st+seperator;
            ssb.append(st);
            ssb.setSpan(style,ssb.length()-st.length(),ssb.length(),0);
        }
    }

    private void setupDetails(){

        TextView eventname=(TextView) rootView.findViewById(R.id.eventname);
        eventname.setText(event.getName());

        TextView location=(TextView) rootView.findViewById(R.id.eventlocation);

        ILocation loc=event.getLocation();
        if(loc!=null){
            SpannableStringBuilder ssb=new SpannableStringBuilder();
            setStyle(loc.getName(),ssb,new StyleSpan(Typeface.BOLD),"\n");
            setStyle(loc.getStreet(),ssb,new StyleSpan(Typeface.NORMAL)," ");
            setStyle(loc.getCity(),ssb,new StyleSpan(Typeface.NORMAL)," ");
            setStyle(loc.getCountry(),ssb,new StyleSpan(Typeface.NORMAL)," ");
            setStyle(loc.getZip(),ssb,new StyleSpan(Typeface.NORMAL)," ");
            if(ssb.length()>0){
                ssb=ssb.delete(ssb.length()-1,ssb.length());
            }
            location.setText(ssb);
        }else{
            location.setText("Unknown Location");
        }

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