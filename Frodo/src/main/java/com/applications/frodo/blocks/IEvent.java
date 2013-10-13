package com.applications.frodo.blocks;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharth on 09/10/13.
 */
public abstract class IEvent{

    public String toJSON(){
        Map<String, String> map=new HashMap<String, String>();
        map.put("id", getId());
        map.put("name", getName());
        JSONObject obj=new JSONObject(map);
        return obj.toString();

    }

    public static IEvent getEvent(String json){
        try {
            JSONObject obj=new JSONObject(json);
            String id=obj.getString("id");
            String name=obj.getString("name");
            return new Event(id,name,null,null,null,null,null);
        } catch (JSONException e) {
            return null;
        }
    }

    public abstract Calendar getStartTime();

    public abstract void setStartTime(Calendar startTime);

    public abstract Calendar getEndTime();

    public abstract void setEndTime(Calendar endTime);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getId();

    public abstract void setId(String id);

    public abstract Bitmap getImage();

    public abstract void setImage(Bitmap image);

    public abstract String getImagePath();

    public abstract void setImagePath(String path);

    public abstract String getSummary();

    public abstract void setSummary(String summary);

    public abstract ILocation getLocation();

    public abstract void setLocation(ILocation location);


    public static class EventComparator implements Comparator<IEvent> {
        @Override
        public int compare(IEvent lhs, IEvent rhs) {
            if(lhs.getStartTime()==null){
                return 1;
            }

            if(rhs.getStartTime()==null){
                return -1;
            }

            if(!lhs.getStartTime().equals(rhs.getStartTime())){
                return lhs.getStartTime().compareTo(rhs.getStartTime());
            }else{

                if(lhs.getEndTime()==null){
                    return 1;
                }

                if(rhs.getEndTime()==null){
                    return -1;
                }


                return lhs.getEndTime().compareTo(rhs.getEndTime());
            }
        }
    }

}
