package com.applications.frodo.blocks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharth on 26/09/13.
 */
public class Location implements ILocation {

    private String name;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zip;
    private double latitude;
    private double longitude;

    public Location(){

    }

    public Location(JSONObject jsonParams) throws JSONException {
        this.name=jsonParams.getString("name");
        this.street=jsonParams.getString("street");
        this.city=jsonParams.getString("city");
        this.state=jsonParams.getString("state");
        this.country=jsonParams.getString("country");
        this.zip=jsonParams.getString("zip");
        this.latitude=jsonParams.getDouble("latitude");
        this.longitude=jsonParams.getDouble("longitude");
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String getZip() {
        return zip;
    }

    @Override
    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJSON() {

        Map<String, Object> map=new HashMap<String, Object>();

        map.put("name", this.name);
        map.put("street", this.street);
        map.put("city",this.city);
        map.put("state", this.state);
        map.put("country",this.country);
        map.put("zip",this.zip);
        map.put("latitude",this.latitude);
        map.put("longitude",this.longitude);

        return new JSONObject(map);
    }

    @Override
    public String toString(){

        StringBuilder sb=new StringBuilder();

        if(name!=null){
            sb.append(name);
            sb.append(",");
        }
        if(street!=null){
            sb.append(street);
            sb.append(",");
        }

        if(city!=null){
            sb.append(city);
            sb.append(",");
        }

        if(country!=null){
            sb.append(country);
            sb.append(",");
        }

        if(zip!=null){
            sb.append(zip);
        }

        return sb.toString();
    }
}