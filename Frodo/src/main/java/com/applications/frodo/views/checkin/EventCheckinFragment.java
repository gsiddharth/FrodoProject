package com.applications.frodo.views.checkin;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.blocks.Event;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.ILocation;
import com.applications.frodo.blocks.SocialNetworks;
import com.applications.frodo.networking.PictureDownloader;
import com.applications.frodo.utils.Convertors;
import com.applications.frodo.widgets.EventSummary;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by siddharth on 09/10/13.
 */
public class EventCheckinFragment  extends Fragment {
    private static String TAG=EventCheckinFragment.class.toString();

    private String[] possibleDateFormats={"yyyy-MM-dd'T'HH:mm:ssZ","yyyy-MM-dd"};

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_checkin,
                container, false);
        init();
        return view;
    }

    /**
     * This function sets up the view of the events. It fetches the events from facebook and lists them
     */
    private void init(){

        String strPossibleDateFormats=(String)getResources().getString(R.string.possible_date_formats);
        if(strPossibleDateFormats!=null && !strPossibleDateFormats.equals("")){
            possibleDateFormats=strPossibleDateFormats.split("[|][|][|][|]");
        }

        String fqlQuery = "SELECT name, description, start_time, end_time, eid, venue, location, pic from event WHERE eid in (SELECT eid FROM event_member WHERE uid = "
                + GlobalParameters.getInstance().getUser().getFacebookId()+ ")";

        Log.i(TAG,"=====>>>>>>"+fqlQuery);

        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session
                .getActiveSession();

        Request request = new Request(session,
                "/fql", params, HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(
                            Response response) {

                        GraphObject graphObject = response
                                .getGraphObject();
                        setupEventList(graphObject);

                    }
                });
        Request.executeBatchAsync(request);
    }


    public void setupEventList(GraphObject graphObject) {

        if (graphObject != null) {
            JSONObject jsonObject = graphObject.getInnerJSONObject();
            final List<IEvent> events = new ArrayList<IEvent>();

            try {
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    try{
                    JSONObject object = (JSONObject) array.get(i);

                    String eventId="";
                    if(!object.isNull("eid")) eventId= object.getString("eid");

                    String eventName="";
                    if(!object.isNull("name")) eventName = object.getString("name");

                    Calendar eventStartTime=null;
                    if(!object.isNull("start_time")) eventStartTime= Convertors.toTime(object.getString("start_time"), possibleDateFormats);

                    Calendar eventEndTime=null;
                    if(!object.isNull("end_time")) eventEndTime = Convertors.toTime(object.getString("end_time"), possibleDateFormats);

                    String eventSummary="";
                    if(!object.isNull("description")) eventSummary=object.getString("description");

                    String image=null;
                    if(!object.isNull("pic")) image= object.getString("pic");

                    String locationname="";
                    if(!object.isNull("location")) locationname=object.getString("location");

                    ILocation location=null;
                    if(!object.isNull("venue")) location= Convertors.convertToLocation(object.getJSONObject("venue"),
                            SocialNetworks.FACEBOOK, locationname);

                    events.add(new Event(eventId,eventName,eventSummary,eventStartTime,eventEndTime,image,location));
                    }catch(JSONException e){
                        Log.e(TAG,"JSON => "+array.get(i).toString(),e);
                    }
                }

                Collections.sort(events, new IEvent.EventComparator());
                EventSummary.EventSummaryListAdaptor eventAdaptor=new EventSummary.EventSummaryListAdaptor(events,this.getActivity().getBaseContext());
                ListView eventListView= (ListView) this.getActivity().findViewById(R.id.eventListView);
                eventListView.setAdapter(eventAdaptor);

            } catch (JSONException e) {
                Log.e(TAG,"JSON Exception", e);
            }
        }
    }
}