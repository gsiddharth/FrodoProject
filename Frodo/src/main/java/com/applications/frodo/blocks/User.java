package com.applications.frodo.blocks;

/**
 * Created by siddharth on 26/09/13.
 */
public class User implements IUser {

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
}