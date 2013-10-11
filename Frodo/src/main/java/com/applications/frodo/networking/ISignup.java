package com.applications.frodo.networking;

import org.apache.http.client.ResponseHandler;

import java.util.Map;

/**
 * Created by siddharth on 28/09/13.
 */
public interface ISignup {


    /**
     * This method returns if the user with the given params needs to signup or not
     * @param params
     *          list of credentials of the user
     */
    public void shouldSignup(Map<String, String> params, HttpConnectionHandler.ReponseCallBack responseHandler);

    /**
     * This method needs to called to send the signup request
     * @param params
     *          a map of credential type and credential value
     */
    public void singup(Map<String, String> params, HttpConnectionHandler.ReponseCallBack responseHandler);

}
