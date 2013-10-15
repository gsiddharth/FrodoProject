package com.applications.frodo.socialnetworks.facebook;

import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void sharePhoto(Bitmap image, final FacebookCallbacks fbCallback){
        Session session = Session.getActiveSession();

        if (session != null) {

            Bundle postParams = new Bundle();

            byte[] data;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            data = baos.toByteArray();

            postParams.putByteArray("source", data);

            Request.Callback callback = new Request.Callback() {
                public void onCompleted(Response response) {
                    fbCallback.onPhotoShareComplete();
                }
            };

            IEvent checkedInEvent=GlobalParameters.getInstance().getCheckedInEvent();
            if(checkedInEvent!=null && checkedInEvent.getId()!=null){
                Request request = new Request(session, checkedInEvent.getId()+ "/photos", postParams,
                        HttpMethod.POST, callback);

                RequestAsyncTask task = new RequestAsyncTask(request);
                task.execute();
            }
        }
    }

    public void checkForPermissions(){
        Session.isPublishPermission("");
    }

    public interface FacebookCallbacks{
        public void onPhotoShareComplete();

        public void onEventPhotosDownloadComplete(Map<String, String> pictures);

    }

    public void getPhotosOfEvent(String eventId, final FacebookCallbacks eventPhotosCallback){
        RequestExecutor executor=new RequestExecutor(eventPhotosCallback);
        System.out.println("Getting photos... for thv  e event "+eventId);
        executor.execute(eventId);
    }

    private class RequestExecutor extends AsyncTask<String, Integer, Map<String, String>> {

        private class Pair{
            String thumbnail;
            String picture;
        }

        private FacebookCallbacks eventPhotosCallback;

        public RequestExecutor(FacebookCallbacks eventPhotosCallback){
            this.eventPhotosCallback=eventPhotosCallback;
        }

        @Override
        protected Map<String, String> doInBackground(String... params) {
            Session session = Session.getActiveSession();

            if (session != null) {

                IEvent checkedInEvent=GlobalParameters.getInstance().getCheckedInEvent();

                if(checkedInEvent!=null && checkedInEvent.getId()!=null){
                    Request request = new Request(session, checkedInEvent.getId()+ "/photos", null,
                            HttpMethod.GET);

                    System.out.println("Getting photos... for the event "+checkedInEvent.getId());

                    Response response=request.executeAndWait();

                    System.out.println(response);

                    GraphObject graphObject=response.getGraphObject();
                    JSONObject json=graphObject.getInnerJSONObject();
                    Map<String, String> images=new HashMap<String, String>();
                    try {
                        JSONArray photoArray=json.getJSONArray("data");
                        for(int i=0;i<photoArray.length();i++){
                            JSONObject obj=photoArray.getJSONObject(i);
                            JSONArray arr=obj.getJSONArray("images");
                            String thum="";
                            String image="";
                            thum=arr.getJSONObject(arr.length()-5).getString("source");
                            image=arr.getJSONObject(arr.length()-7).getString("source");
                            images.put(thum,image);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*for(GraphObject photoObject:photoObjects){
                        thumbnails.put(photoObject.getProperty("picture").toString(), photoObject.getProperty("source").toString());
                    }*/
                    return images;
                }

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Map<String, String> result){
            if(result!=null){
                this.eventPhotosCallback.onEventPhotosDownloadComplete(result);
            }
        }
    }
}
