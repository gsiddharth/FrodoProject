package com.applications.frodo.blocks;

import java.util.List;

/**
 * Created by siddharth on 16/10/13.
 */
public interface IPhoto {
    public String getId();
    public void setId(String id);
    public List<IPhotoSource> getSources();
    public void setSources(List<IPhotoSource> sources);
    public IPhotoSource getThumbnail();
    public void setThumbnail(IPhotoSource source);
    public IPhotoSource getImage();
    public void setImage(IPhotoSource source);
    public String getUserId();
    public void setUserId(String userid);
    public String getUserName();
    public void setUserName(String userName);
}
