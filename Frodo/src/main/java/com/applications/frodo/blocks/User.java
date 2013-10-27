package com.applications.frodo.blocks;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharth on 26/09/13.
 */
public class User implements IUser {

    private static final String TAG=User.class.toString();

    private String id;
    private String facebookid;
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthday;
    private String username;
    private String link;
    private ILocation location;
    private Map<String, IUser> friends;

    public User(){

    }

    public User(JSONObject jsonParams) throws JSONException {
        setParams(jsonParams);
    }

    public User(Parcel in){
        String jsonString=in.readString();
        try {
            JSONObject json = new JSONObject(jsonString);
            setParams(json);
        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }

    }

    private void setParams(JSONObject jsonParams) throws JSONException {
        this.id=jsonParams.getString("id");
        this.facebookid=jsonParams.getString("facebookid");
        this.name=jsonParams.getString("name");
        this.firstName=jsonParams.getString("firstname");
        this.middleName=jsonParams.getString("middlename");
        this.lastName=jsonParams.getString("lastname");
        this.birthday=jsonParams.getString("birthday");
        this.username=jsonParams.getString("username");
        this.link=jsonParams.getString("link");
        this.location=new Location(jsonParams.getJSONObject("location"));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public String getFacebookId() {
        return facebookid;
    }

    @Override
    public void setFacebookId(String id) {
        this.facebookid=id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName=firstName;
    }

    @Override
    public String getMiddleName() {
        return this.middleName;
    }

    @Override
    public void setMiddleName(String middleName) {
        this.middleName=middleName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName=lastName;
    }

    @Override
    public String getLink() {
        return this.link;
    }

    @Override
    public void setLink(String link) {
        this.link=link;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username=username;
    }

    @Override
    public String getBirthday() {
        return this.birthday;
    }

    @Override
    public void setBirthday(String birthday) {
        this.birthday=birthday;
    }

    @Override
    public ILocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(ILocation location) {
        this.location=location;
    }

    @Override
    public Map<String, IUser> getFriends() {
        return friends;
    }

    @Override
    public void setFriends(Map<String, IUser> friends) {
        this.friends = friends;
    }


    @Override
    public JSONObject toJSON() {

        Map<String, Object> map=new HashMap<String, Object>();

        map.put("id", this.getId());
        map.put("facebookid", this.getFacebookId());
        map.put("name", this.getName());
        map.put("firstname", this.getName());
        map.put("middlename", this.getMiddleName());
        map.put("lastname", this.getLastName());
        map.put("birthday", this.getBirthday());
        map.put("username", this.getUsername());
        map.put("link", this.getLink());
        map.put("location", this.getLocation().toJSON());
        return new JSONObject(map);
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
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User [] newArray(int size) {
            return new User[size];
        }
    };
}