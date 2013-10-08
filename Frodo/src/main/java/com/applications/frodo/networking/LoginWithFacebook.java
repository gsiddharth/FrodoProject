package com.applications.frodo.networking;

import android.util.Log;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.utils.Convertors;
import com.facebook.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharth on 30/09/13.
 */
public class LoginWithFacebook implements ILogin {

    private static final String TAG=LoginWithFacebook.class.getName();
    private LoginStatus loginStatus=LoginStatus.UNKNOWN;

    public void login(Map<String, String> params, ILoginCallback loginCallback){
        JSONObject obj=new JSONObject(params);
        this.login(obj.toString(),loginCallback);
    }

    public void login(String json, final ILoginCallback loginCallback){
        Log.i(TAG,"Trying to send the request to the backend");

        HttpConnectionHandler.getInstance().sendJson(
                BackendRequestParameters.getInstance().getLoginQuery()+"?access_token="+Session.getActiveSession().getAccessToken(),
                null, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {

                StatusLine status = httpResponse.getStatusLine();
                if (status.getStatusCode() != 200) {
                    throw new IOException("Invalid response from server: " + status.toString());
                }
                String result=Convertors.getString(httpResponse);

                try{
                    JSONObject resultObject=new JSONObject(result);
                    System.out.println("=====>>>>"+resultObject);
                    if(resultObject.get("status").toString().equalsIgnoreCase("success")){
                        System.out.println("======>>>>>"+resultObject.get("username"));
                        if(!resultObject.isNull("username")){
                            GlobalParameters.getInstance().getUser().setUsername(resultObject.get("username").toString());
                        }
                        loginStatus=LoginStatus.SUCCESS;
                        loginCallback.onLogin(LoginStatus.SUCCESS);
                    }else{
                        loginStatus=LoginStatus.FAILED;
                        loginCallback.onLogin(LoginStatus.FAILED);
                    }
                }catch(Exception e){
                    Log.e(TAG, "Error in result object: Expected JSON. Result => "+result, e);
                }
                return null;
            }
        });
    }

    public LoginStatus getLoginStatus(){
        return loginStatus;
    }
}
