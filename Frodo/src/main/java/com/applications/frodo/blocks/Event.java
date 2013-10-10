package com.applications.frodo.blocks;

import android.graphics.Bitmap;

import com.applications.frodo.networking.PictureDownloader;

import java.util.Calendar;

/**
 * Created by siddharth on 09/10/13.
 */
public class Event implements IEvent{

    private String id;
    private String name;
    private String summary;
    private Calendar startTime;
    private Calendar endTime;
    private Bitmap image;
    private ILocation location;
    private String imagePath;

    public Event(String id, String name, String summary, Calendar startTime, Calendar endTime,
                 String imagePath, ILocation location) {
        this.id = id;
        this.name = name;
        this.startTime=startTime;
        this.endTime=endTime;
        this.summary=summary;
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
    public Bitmap getImage() {
        return image;
    }

    @Override
    public void setImage(Bitmap image) {
        this.image=image;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
