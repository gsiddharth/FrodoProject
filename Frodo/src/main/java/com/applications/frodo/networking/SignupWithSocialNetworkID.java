package com.applications.frodo.networking;

import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.SocialNetworks;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by siddharth on 28/09/13.
 */
public class SignupWithSocialNetworkID implements ISignup{

    private SocialNetworks socialNetwork;
    private IUser user;
    private String ip;
    private int port;
    private int timeout;
    private String singupQuery;
    private String getUserDataQuery;

    /**
     * Constructor with default timeout for request set at 60sec
     * @param socialNetwork the social network whose id is being passed
     * @param user
     * @param ip
     * @param port
     * @param singupQuery
     * @param getUserDataQuery
     */
    public SignupWithSocialNetworkID(SocialNetworks socialNetwork, IUser user, String ip, int port, String singupQuery, String getUserDataQuery){
        this(socialNetwork, user,ip, port,singupQuery,getUserDataQuery,60000);
    }

    public SignupWithSocialNetworkID(SocialNetworks socialNetwork, IUser user, String ip, int port, String singupQuery, String getUserDataQuery, int timeout){
        this.socialNetwork=socialNetwork;
        this.user=user;
        this.ip=ip;
        this.port=port;
        this.singupQuery = singupQuery;
        this.getUserDataQuery=getUserDataQuery;
        this.timeout=timeout;
    }


    @Override
    public boolean shouldSignup(Map<String, String> params) {

        try{
            String socialnetId=params.get("socialnetid");
            singupQuery = singupQuery.replaceAll("[?]socialnetid", socialnetId);

            String dataAsString = HttpConnectionHandler.getInstance().sendRequest(singupQuery);
            dataAsString.trim();

            JSONObject jsonObject=new JSONObject(dataAsString);

            if(StringUtils.isBlank(dataAsString)){
                return true;
            } else{
                boolean signedUp=jsonObject.getBoolean("signedUp");
                if(signedUp){
                    String username=jsonObject.getString("username");
                    String userid=jsonObject.getString("userid");
                    this.user.setId(userid);
                    this.user.setUsername(username);
                    return true;
                }else{
                    return false;
                }
            }
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public SingupStatus singup(Map<String, String> params) {
        String username=params.get("username");
        String email=params.get("email");
        String socialNetworkId=params.get("socialnetid");
        String socialNetworkName=socialNetwork.toString().toLowerCase();

        String query =this.singupQuery.replaceAll("[?]username", username);
        query = query.replaceAll("[?]email", email);
        query = query.replaceAll("[?]socialnetid", socialNetworkId);
        query = query.replaceAll("[?]socialnetname", socialNetworkName);

        String dataAsString = HttpConnectionHandler.getInstance().sendRequest(query);
        dataAsString.trim();

        if(StringUtils.isBlank(dataAsString)){
            return SingupStatus.FAILURE;
        } else if(StringUtils.equalsIgnoreCase(dataAsString,"username taken")){
            return SingupStatus.USERNAME_TAKEN;
        }else if(StringUtils.equalsIgnoreCase(dataAsString,"email exists")){
            return SingupStatus.EMAIL_REGISTERED;
        }else{
            this.user.setId(dataAsString.trim());
            return SingupStatus.SUCCESS;
        }

    }
}
