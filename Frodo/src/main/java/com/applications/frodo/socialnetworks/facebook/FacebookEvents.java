package com.applications.frodo.socialnetworks.facebook;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.blocks.Event;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.ILocation;
import com.applications.frodo.blocks.IPhoto;
import com.applications.frodo.blocks.IUser;
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
    public void getCurrentEventsIamGoingTo(final Callback callback) {
        String fqlQuery = "SELECT name, description, start_time, end_time, eid, venue, location, pic_cover, pic_square " +
                "from event WHERE eid in (SELECT eid FROM event_member WHERE uid = "
                + GlobalParameters.getInstance().getUser().getFacebookId()+ " and (rsvp_status = " +
                "'attending' or rsvp_status='unsure')) and (start_time >='"+ GeneralUtils.getYesterdayDate()+
                "' or end_time >='"+GeneralUtils.getYesterdayDate()+"')";
        getEvents(fqlQuery, callback);
    }


    @Override
    public void getMyEvents(final Callback callback) {
        String fqlQuery = "SELECT name, description, start_time, end_time, eid, venue, location, pic_cover, pic_square " +
                "from event WHERE creator = " +GlobalParameters.getInstance().getUser().getId();

        getEvents(fqlQuery, callback);
    }


    private void getEvents(String fqlQuery, final Callback callback){

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

                        String icon=null;
                        if(!eventJSONOject.isNull("pic_square")) icon= eventJSONOject.getString("pic_square");

                        String image=null;
                        if(!eventJSONOject.isNull("pic_cover")) {
                            image= eventJSONOject.getJSONObject("pic_cover").getString("source");
                        }

                        String locationName="";
                        if(!eventJSONOject.isNull("location")) locationName=eventJSONOject.getString("location");

                        ILocation location=null;
                        if(!eventJSONOject.isNull("venue")){
                            try{
                            location= convertToLocation(eventJSONOject.getJSONObject("venue"), locationName);
                            }catch(Exception e){
                                location=null;
                            }
                        }

                        events.add(new Event(eventId,eventName,eventSummary,eventStartTime,eventEndTime,icon, image,location));

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
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
            System.out.println(data.length);

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

    public interface FacebookCallbacks{
        public void onPhotoShareComplete();

        public void onEventPhotosDownloadComplete(List<IPhoto> photos);

    }

    public void getAllPhotosOfEvent(String eventId, final FacebookCallbacks eventPhotosCallback){
        RequestExecutor executor=new RequestExecutor(eventPhotosCallback, EventPhotosOf.ALL);
        executor.execute(eventId);
    }

    public void getMyPhotosOfEvent(String eventId, final FacebookCallbacks eventPhotosCallback){
        RequestExecutor executor=new RequestExecutor(eventPhotosCallback,EventPhotosOf.MY);
        executor.execute(eventId);
    }

    public void getMyNetworkPhotosOfEvent(String eventId, final FacebookCallbacks eventPhotosCallback){
        RequestExecutor executor=new RequestExecutor(eventPhotosCallback,EventPhotosOf.MY);
        executor.execute(eventId);
    }

    private class RequestExecutor extends AsyncTask<String, Integer, List<IPhoto>> {

        private FacebookCallbacks eventPhotosCallback;
        private EventPhotosOf who;

        public RequestExecutor(FacebookCallbacks eventPhotosCallback, EventPhotosOf who){
            this.who=who;
            this.eventPhotosCallback=eventPhotosCallback;
        }

        @Override
        protected List<IPhoto> doInBackground(String... params) {
            Session session = Session.getActiveSession();

            if (session != null && params.length>0) {

                String eventID=params[0];

                if(eventID!=null ){
                    Request request = new Request(session, eventID+ "/photos", null,
                            HttpMethod.GET);

                    Response response=request.executeAndWait();

                    GraphObject graphObject=response.getGraphObject();
                    try{
                        List<IPhoto> images=Convertors.convertToPhotos(graphObject);
                        images=filter(images, who);
                        return images;
                    }catch(Exception e){
                        Log.e(TAG,"",e);
                    }

                    return null;
                }

                return null;
            }

            return null;
        }

        private List<IPhoto> filter(List<IPhoto> photos, EventPhotosOf who ){
            switch(who){
                case MY:
                    IUser user=GlobalParameters.getInstance().getUser();
                    List<IPhoto> newList=new ArrayList<IPhoto>();
                    for(IPhoto photo:photos){
                        if(photo.getUserId().equals(user.getId())){
                            newList.add(photo);
                        }
                    }
                    return newList;
                case NETWORK:
                    user=GlobalParameters.getInstance().getUser();
                    Map<String, IUser> friends=user.getFriends();
                    newList=new ArrayList<IPhoto>();
                    for(IPhoto photo:photos){
                        if(friends.containsKey(photo.getUserId()) || photo.getUserId().equals(user.getId())){
                            newList.add(photo);
                        }
                    }
                    return newList;

                case ALL:
                    return photos;
                default:
                    return photos;
            }
        }

        @Override
        protected void onPostExecute(List<IPhoto> photos){
            if(photos!=null){
                this.eventPhotosCallback.onEventPhotosDownloadComplete(photos);
            }
        }
    }

    public enum EventPhotosOf{
        MY, NETWORK, ALL
    }
}
