package com.applications.frodo.networking;

import java.util.Map;

/**
 * Created by siddharth on 28/09/13.
 */
public interface ISignup {


    /**
     * This method returns if the user with the given params needs to signup or not
     * @param params
     *          list of credentials of the user
     * @return true
     *          if user needs to signup
     *         false
     *          otherwise
     */
    public boolean shouldSignup(Map<String, String> params);

    /**
     * This method needs to called to send the signup request
     * @param params
     *          a map of credential type and credential value
     * @return user id if the signup was successful
     */
    public SingupStatus singup(Map<String, String> params);

}
