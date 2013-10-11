package com.applications.frodo.socialnetworks.facebook;

import android.os.Bundle;
import android.util.Log;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.blocks.Event;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.ILocation;
import com.applications.frodo.blocks.Location;
import com.applications.frodo.socialnetworks.ISocialNetworkEvents;
import com.applications.frodo.utils.Convertors;

import com.applications.frodo.utils.GeneralUtils;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by siddharth on 11/10/13.
 */
public class FacebookEvents implements ISocialNetworkEvents{

    private static final FacebookEvents instance=new FacebookEvents();
    private static final String TAG=FacebookEvents.class.toString();
    private static String[] possibleDateFormats={"yyyy-MM-dd'T'HH:mm:ssZ","yyyy-MM-dd"};

    private FacebookEvents(){
    }

    public static FacebookEvents getInstance(){
        return instance;
    }

    @Override
    public void getEvents(final Callback callback) {
        String fqlQuery = "SELECT name, description, start_time, end_time, eid, venue, location, pic " +
                "from event WHERE eid in (SELECT eid FROM event_member WHERE uid = "
                + GlobalParameters.getInstance().getUser().getFacebookId()+ " and (rsvp_status = " +
                "'attending' or rsvp_status='unsure')) and start_time >='"+ GeneralUtils.getYesterdayDate()+"'";

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
                        toEventsList(graphObject, callback);

                    }
                });
        Request.executeBatchAsync(request);
    }

    /**
     * This functions converts the graph object returned by facebook to the application readable format
     * @param graphObject
     * @param callback
     */
    private void toEventsList(GraphObject graphObject, Callback callback) {

        if (graphObject != null) {
            JSONObject dataObject = graphObject.getInnerJSONObject();
            final List<IEvent> events = new ArrayList<IEvent>();

            try {
                JSONArray array = dataObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    try{
                        JSONObject eventJSONOject = (JSONObject) array.get(i);

                        String eventId="";
                        if(!eventJSONOject.isNull("eid")) eventId= eventJSONOject.getString("eid");

                        String eventName="";
                        if(!eventJSONOject.isNull("name")) eventName = eventJSONOject.getString("name");

                        Calendar eventStartTime=null;
                        if(!eventJSONOject.isNull("start_time")) eventStartTime= Convertors.toTime(eventJSONOject.getString("start_time"), possibleDateFormats);

                        Calendar eventEndTime=null;
                        if(!eventJSONOject.isNull("end_time")) eventEndTime = Convertors.toTime(eventJSONOject.getString("end_time"), possibleDateFormats);

                        String eventSummary="";
                        if(!eventJSONOject.isNull("description")) eventSummary=eventJSONOject.getString("description");

                        String image=null;
                        if(!eventJSONOject.isNull("pic")) image= eventJSONOject.getString("pic");

                        String locationName="";
                        if(!eventJSONOject.isNull("location")) locationName=eventJSONOject.getString("location");

                        ILocation location=null;
                        if(!eventJSONOject.isNull("venue")) location= convertToLocation(eventJSONOject.getJSONObject("venue"), locationName);

                        events.add(new Event(eventId,eventName,eventSummary,eventStartTime,eventEndTime,image,location));

                    }catch(JSONException e){
                        Log.e(TAG,"JSON => "+array.get(i).toString(),e);
                    }
                }

                Collections.sort(events, new IEvent.EventComparator());
                callback.onGetEvent(events);

            } catch (JSONException e) {
                Log.e(TAG,"JSON Exception", e);
            }
        }
    }

    /**
     * Convert a json object to an ILocation object
     * @param eventJSONObject
     *          json object representing event
     * @param name
     *          name of the event
     * @return ILocation Object
     */
    private ILocation convertToLocation(JSONObject eventJSONObject, String name){
        try{
            ILocation location = new Location();
            if(!eventJSONObject.isNull("latitude")){
                String latitude=eventJSONObject.getString("latitude");
                if(StringUtils.isNumeric(latitude)){
                    location.setLatitude(Double.parseDouble(latitude));
                }
            }

            if(!eventJSONObject.isNull("longitude")){
                String longitude=eventJSONObject.getString("longitude");
                if(StringUtils.isNumeric(longitude)){
                    location.setLongitude(Double.parseDouble(longitude));
                }
            }

            if(!eventJSONObject.isNull("state"))
                location.setState(eventJSONObject.getString("state"));
            if(!eventJSONObject.isNull("city"))
                location.setCity(eventJSONObject.getString("city"));
            if(!eventJSONObject.isNull("country"))
                location.setCountry(eventJSONObject.getString("country"));
            if(!eventJSONObject.isNull("street"))
                location.setStreet(eventJSONObject.getString("street"));
            if(!eventJSONObject.isNull("zip"))
                location.setZip(eventJSONObject.getString("zip"));

            location.setName(name);
            return location;

        }catch(Exception e){
            Log.e(TAG,"JSON Parse Exception, JSON="+eventJSONObject.toString(), e);
        }
        return null;
    }
}
