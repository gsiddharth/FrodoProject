package com.applications.frodo.utils;

import android.util.Log;

import com.applications.frodo.blocks.ILocation;
import com.applications.frodo.blocks.IPhoto;
import com.applications.frodo.blocks.IPhotoSource;
import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.Location;
import com.applications.frodo.blocks.Photo;
import com.applications.frodo.blocks.SocialNetworks;
import com.applications.frodo.blocks.User;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by siddharth on 26/09/13.
 */
public class Convertors {

    private static String TAG=Convertors.class.toString();

    /**
     * This function converts a facebook user data representation to application specific user data representation
     * @param graphUser
     *              The object representing facebook graph user
     * @return The native representation of user data
     */
    public static IUser convertToUser(GraphUser graphUser){
        IUser user=new User();
        user.setId(graphUser.getId());
        user.setBirthday(graphUser.getBirthday());
        user.setFirstName(graphUser.getFirstName());
        user.setMiddleName(graphUser.getMiddleName());
        user.setLastName(graphUser.getLastName());
        user.setName(graphUser.getName());
        user.setLocation(convertToLocation(graphUser.getLocation()));
        user.setFacebookId(graphUser.getId());
        user.setUsername(graphUser.getUsername());
        return user;
    }



    /**
     * This function converts a facebook user location data representation to application specific user location data representation
     * @param graphLocation
     *              The object representing facebook graph user location
     * @return The native representation of user location data
     */
    public static ILocation convertToLocation(GraphLocation graphLocation){

        ILocation location = new Location();
        location.setStreet(graphLocation.getStreet());
        location.setCity(graphLocation.getCity());
        location.setState(graphLocation.getState());
        location.setCountry(graphLocation.getCountry());
        location.setZip(graphLocation.getZip());
        location.setLatitude(graphLocation.getLatitude());
        location.setLongitude(graphLocation.getLongitude());
        return location;
    }

    public static List<IPhoto> convertToPhotos(GraphObject photosObject) throws JSONException {
        List<IPhoto> photos=new ArrayList<IPhoto>();
        JSONObject outerJson=photosObject.getInnerJSONObject();
        JSONArray photosArray=outerJson.getJSONArray("data");

        for(int j=0;j<photosArray.length();j++){

            JSONObject json=photosArray.getJSONObject(j);
            String imageId=json.getString("id");
            JSONObject user=json.getJSONObject("from");
            if(user!=null){
                String userId=user.getString("id");
                String username=user.getString("name");

                List<IPhotoSource> sources=new ArrayList<IPhotoSource>();
                JSONArray sourceArray=json.getJSONArray("images");
                for(int i=0;i<sourceArray.length();i++){
                    JSONObject sourceObj=sourceArray.getJSONObject(i);
                    String sourceURL=sourceObj.getString("source");
                    int width=sourceObj.getInt("width");
                    int height=sourceObj.getInt("height");
                    sources.add(new Photo.PhotoSource(sourceURL,width,height));
                }

                IPhoto photo=new Photo(imageId,sources,userId,username);
                photo.setThumbnail(sources.get(3));
                photo.setImage(sources.get(2));
                photos.add(photo);
            }
        }

        return photos;
    }

    public static ILocation convertToLocation(JSONObject jsonObject, SocialNetworks socialnet, String name){

        if(socialnet==SocialNetworks.FACEBOOK){
            try{
                ILocation location = new Location();
                if(!jsonObject.isNull("latitude")){
                    String latitude=jsonObject.getString("latitude");
                    if(StringUtils.isNumeric(latitude)){
                        location.setLatitude(Double.parseDouble(latitude));
                    }
                }

                if(!jsonObject.isNull("longitude")){
                    String longitude=jsonObject.getString("longitude");
                    if(StringUtils.isNumeric(longitude)){
                        location.setLongitude(Double.parseDouble(longitude));
                    }
                }

                if(!jsonObject.isNull("state"))
                    location.setState(jsonObject.getString("state"));
                if(!jsonObject.isNull("city"))
                    location.setCity(jsonObject.getString("city"));
                if(!jsonObject.isNull("country"))
                    location.setCountry(jsonObject.getString("country"));
                if(!jsonObject.isNull("street"))
                    location.setStreet(jsonObject.getString("street"));
                if(!jsonObject.isNull("zip"))
                    location.setZip(jsonObject.getString("zip"));

                location.setName(name);
                return location;

            }catch(Exception e){
                Log.e(TAG,"JSON Parse Exception, JSON="+jsonObject.toString(), e);
            }
            return null;

        }else{
            return null;
        }

    }

    public static String getString(HttpResponse response) throws IOException{
        // Pull content stream from response
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Read response into a buffered stream
        int readBytes = 0;
        byte[] sBuffer = new byte[512];
        while ((readBytes = inputStream.read(sBuffer)) != -1) {
            content.write(sBuffer, 0, readBytes);
        }

        // Return result from buffered stream
        String dataAsString = new String(content.toByteArray());
        return dataAsString;
    }

    public static Calendar toTime(String time, String[] possibleFormats){

        for(String format:possibleFormats){
            try{
                SimpleDateFormat rawformat = new SimpleDateFormat(format);

                Date d = rawformat.parse(time);
                Calendar cal=new GregorianCalendar();
                cal.setTime(d);
                return cal;
            }catch(Exception e){
                Log.e(TAG, "Format tried to parse => "+format+", time=>"+time);
                continue;
            }
        }

        return null;
    }

    public static String toString(Calendar cal, String... formats){
        for(String format:formats){
            try{
                SimpleDateFormat fm=new SimpleDateFormat(format);
                return fm.format(cal.getTime());
            }catch(Exception e){
                Log.e(TAG,"Format tried to parse => "+format, e);
            }
        }
        return "";
    }
}
