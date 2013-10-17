package com.applications.frodo.blocks;

import java.util.List;

/**
 * Created by siddharth on 16/10/13.
 */
public class Photo implements IPhoto {

    private String id;
    private IPhotoSource thumbnail;
    private IPhotoSource image;
    private List<IPhotoSource> sources;
    private String userId;
    private String userName;

    public Photo(String id, List<IPhotoSource> sources, String userid, String username){
        this.id=id;
        this.sources=sources;
        this.userId=userid;
        this.userName=username;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<IPhotoSource> getSources() {
        return sources;
    }

    public void setSources(List<IPhotoSource> sources) {
        this.sources = sources;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public IPhotoSource getThumbnail() {
        return thumbnail;
    }

    @Override
    public void setThumbnail(IPhotoSource thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public IPhotoSource getImage() {
        return image;
    }

    @Override
    public void setImage(IPhotoSource image) {
        this.image = image;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static class PhotoSource implements IPhotoSource{
        private String URL;
        private int width;
        private int height;

        public PhotoSource(String URL, int width, int height) {
            this.URL=URL;
            this.width=width;
            this.height=height;
        }

        @Override
        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        @Override
        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
