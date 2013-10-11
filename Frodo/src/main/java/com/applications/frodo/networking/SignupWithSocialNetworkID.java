package com.applications.frodo.networking;

import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.SocialNetworks;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by siddharth on 28/09/13.
 */
public class SignupWithSocialNetworkID implements ISignup{

    private IUser user;
    private String ip;
    private int port;
    private int timeout;
    private String singupQuery;
    private String getUserDataQuery;

    /**
     * Constructor with default timeout for request set at 60sec
     * @param user
     * @param ip
     * @param port
     * @param singupQuery
     * @param getUserDataQuery
     */
    public SignupWithSocialNetworkID(IUser user, String ip, int port, String singupQuery, String getUserDataQuery){
        this(user,ip, port,singupQuery,getUserDataQuery,60000);
    }

    public SignupWithSocialNetworkID( IUser user, String ip, int port, String singupQuery, String getUserDataQuery, int timeout){
        this.user=user;
        this.ip=ip;
        this.port=port;
        this.singupQuery = singupQuery;
        this.getUserDataQuery=getUserDataQuery;
        this.timeout=timeout;
    }


    @Override
    public void shouldSignup(Map<String, String> params, HttpConnectionHandler.ReponseCallBack responseHandler) {
        JSONObject jParams=new JSONObject(params);
        HttpConnectionHandler.getInstance().sendJson(BackendRequestParameters.getInstance().getShouldSignupQuery(),
                jParams, responseHandler);

    }

    @Override
    public void singup(Map<String, String> params, HttpConnectionHandler.ReponseCallBack responseHandler) {
        JSONObject jParams=new JSONObject(params);
        String signupQuery=BackendRequestParameters.getInstance().getSingupQuery();
        signupQuery=singupQuery.replace("?username", params.get("username"));
        HttpConnectionHandler.getInstance().sendJson(signupQuery,
                jParams, responseHandler);

    }
}
