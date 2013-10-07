package com.applications.frodo.utils;

import com.applications.frodo.blocks.ILocation;
import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.Location;
import com.applications.frodo.blocks.User;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by siddharth on 26/09/13.
 */
public class Convertors {

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


}
