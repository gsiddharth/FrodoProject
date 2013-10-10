package com.applications.frodo.utils;

import android.util.Log;

import com.applications.frodo.blocks.ILocation;
import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.Location;
import com.applications.frodo.blocks.SocialNetworks;
import com.applications.frodo.blocks.User;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphUser;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
}
