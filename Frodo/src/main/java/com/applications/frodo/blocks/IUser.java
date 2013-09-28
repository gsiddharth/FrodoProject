package com.applications.frodo.blocks;

/**
 * Created by siddharth on 26/09/13.
 */
public interface IUser {

    /**
     * Returns the ID of the user.
     * @return the ID of the user
     */
    public String getId();
    /**
     * Sets the ID of the user.
     * @param id the ID of the user
     */
    public void setId(String id);


    /**
     * Returns the Facebook ID of the user.
     * @return the Facebook ID of the user
     */
    public String getFacebookId();
    /**
     * Sets the Facebook ID of the user.
     * @param id the Facebook ID of the user
     */
    public void setFacebookId(String id);




    /**
     * Returns the name of the user.
     * @return the name of the user
     */
    public String getName();
    /**
     * Sets the name of the user.
     * @param name the name of the user
     */
    public void setName(String name);

    /**
     * Returns the first name of the user.
     * @return the first name of the user
     */
    public String getFirstName();
    /**
     * Sets the first name of the user.
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName);

    /**
     * Returns the middle name of the user.
     * @return the middle name of the user
     */
    public String getMiddleName();
    /**
     * Sets the middle name of the user.
     * @param middleName the middle name of the user
     */
    public void setMiddleName(String middleName);

    /**
     * Returns the last name of the user.
     * @return the last name of the user
     */
    public String getLastName();
    /**
     * Sets the last name of the user.
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName);

    /**
     * Returns the Facebook URL of the user.
     * @return the Facebook URL of the user
     */
    public String getLink();
    /**
     * Sets the Facebook URL of the user.
     * @param link the Facebook URL of the user
     */
    public void setLink(String link);

    /**
     * Returns the Facebook username of the user.
     * @return the Facebook username of the user
     */
    public String getUsername();
    /**
     * Sets the Facebook username of the user.
     * @param username the Facebook username of the user
     */
    public void setUsername(String username);

    /**
     * Returns the birthday of the user.
     * @return the birthday of the user
     */
    public String getBirthday();
    /**
     * Sets the birthday of the user.
     * @param birthday the birthday of the user
     */
    public void setBirthday(String birthday);

    /**
     * Returns the current city of the user.
     * @return the current city of the user
     */
    public ILocation getLocation();
    /**
     * Sets the current city of the user.
     * @param location the current city of the user
     */
    public void setLocation(ILocation location);

}
