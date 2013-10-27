package com.applications.frodo.blocks;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharth on 09/10/13.
 */
public class Event implements IEvent{

    private static final String TAG=Event.class.toString();

    private String id;
    private String name;
    private String summary;
    private Calendar startTime;
    private Calendar endTime;
    private ILocation location;
    private Bitmap icon;
    private String iconPath;

    private Bitmap image;
    private String imagePath;


    public Event(JSONObject json) throws JSONException {
        setParams(json);
    }

    public Event(Parcel in){
        String jsonString=in.readString();
        try {
            JSONObject json = new JSONObject(jsonString);
            setParams(json);
        } catch (JSONException e) {
            Log.e(TAG,"",e);
        }
    }

    private void setParams(JSONObject json) throws JSONException {
        this.id=json.getString("id");
        this.name=json.getString("name");

        if(json.has("summary"))
            this.summary=json.getString("summary");

        if(json.has("starttime")){
            this.startTime=new GregorianCalendar();
            this.startTime.setTimeInMillis(json.getLong("starttime"));
        }

        if(json.has("endtime")){
            this.endTime=new GregorianCalendar();
            this.endTime.setTimeInMillis(json.getLong("endtime"));
        }

        if(json.has("location"))
            this.location=new Location(json.getJSONObject("location"));

        if(json.has("imagepath"))
            this.imagePath=json.getString("imagepath");

        if(json.has("iconpath"))
            this.iconPath=json.getString("iconpath");

    }

    public Event(String id, String name, String summary, Calendar startTime, Calendar endTime,
                 String iconPath, String imagePath, ILocation location) {
        this.id = id;
        this.name = name;
        this.startTime=startTime;
        this.endTime=endTime;
        this.summary=summary;
        this.iconPath =iconPath;
        this.imagePath=imagePath;
        this.location=location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Bitmap image) {
        this.icon =image;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public void setSummary(String summary) {
        this.summary=summary;
    }

    @Override
    public ILocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(ILocation location) {
        this.location=location;
    }

    @Override
    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    @Override
    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String imagePath) {
        this.iconPath = imagePath;
    }

    public JSONObject toJSON(){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("id", getId());
        map.put("name", getName());
        map.put("summary", getSummary());
        if(getStartTime()!=null)
            map.put("starttime", getStartTime().getTimeInMillis());
        if(getEndTime()!=null)
            map.put("endtime", getEndTime().getTimeInMillis());
        if(getLocation()!=null)
            map.put("location",getLocation().toJSON());
        map.put("imagepath", getImagePath());
        map.put("iconpath", getIconPath());

        JSONObject obj=new JSONObject(map);
        return obj;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toJSON().toString());
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public Bitmap getImage() {
        return image;
    }

    @Override
    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
