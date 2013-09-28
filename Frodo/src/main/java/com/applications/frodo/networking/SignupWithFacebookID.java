package com.applications.frodo.networking;

import com.applications.frodo.blocks.IUser;

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
public class SignupWithFacebookID implements ISignup{

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
    public SignupWithFacebookID(IUser user, String ip, int port, String singupQuery, String getUserDataQuery){
        this(user,ip, port,singupQuery,getUserDataQuery,60000);
    }

    public SignupWithFacebookID(IUser user, String ip, int port, String singupQuery, String getUserDataQuery, int timeout){
        this.user=user;
        this.ip=ip;
        this.port=port;
        this.singupQuery = singupQuery;
        this.getUserDataQuery=getUserDataQuery;
        this.timeout=timeout;
    }


    @Override
    public boolean shouldSignup(Map<String, String> params) {

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);

        HttpClient client=new DefaultHttpClient(httpParams);

        String facebookId=params.get("facebookid");
        singupQuery = singupQuery.replaceAll("[?]facebookid", facebookId);

        HttpGet request= new HttpGet("http://"+ip+":"+port+"/"+ getUserDataQuery);

        try{
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new IOException("Invalid response from server: " + status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            // Read response into a buffered stream
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            // Return result from buffered stream
            String dataAsString = new String(content.toByteArray());
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
        String facebookId=params.get("facebookid");

        HttpClient client=new DefaultHttpClient();

        String query =this.singupQuery.replaceAll("[?]username", username);
        query = query.replaceAll("[?]email", email);
        query = query.replaceAll("[?]facebookid", facebookId);

        HttpGet request= new HttpGet("http://"+ip+":"+port+"/"+ query);

        try{
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new IOException("Invalid response from server: " + status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            // Read response into a buffered stream
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            // Return result from buffered stream
            String dataAsString = new String(content.toByteArray());
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

        }catch(Exception e){
            return SingupStatus.FAILURE;
        }
    }
}
