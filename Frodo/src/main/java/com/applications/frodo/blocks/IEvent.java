package com.applications.frodo.blocks;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by siddharth on 09/10/13.
 */
public interface IEvent extends IJSONable{

    public Calendar getStartTime();

    public void setStartTime(Calendar startTime);

    public Calendar getEndTime();

    public void setEndTime(Calendar endTime);

    public String getName();

    public void setName(String name);

    public String getId();

    public void setId(String id);

    public Bitmap getImage();

    public void setImage(Bitmap image);

    public String getImagePath();

    public void setImagePath(String path);

    public String getSummary();

    public void setSummary(String summary);

    public ILocation getLocation();

    public void setLocation(ILocation location);


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
