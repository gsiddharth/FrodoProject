package com.applications.frodo.blocks;

/**
 * Created by siddharth on 16/10/13.
 */
public interface IPhotoSource{
    public String getURL();
    public void setURL(String url);
    public int getWidth();
    public void setWidth(int width);
    public int getHeight();
    public void setHeight(int height);
}