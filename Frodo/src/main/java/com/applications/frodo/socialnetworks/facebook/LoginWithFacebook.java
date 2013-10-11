package com.applications.frodo.socialnetworks.facebook;

import android.util.Log;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.networking.BackendRequestParameters;
import com.applications.frodo.networking.HttpConnectionHandler;
import com.applications.frodo.socialnetworks.ILogin;
import com.applications.frodo.socialnetworks.ILoginCallback;
import com.applications.frodo.socialnetworks.LoginStatus;
import com.facebook.Session;

import org.json.JSONObject;

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
                null, new HttpConnectionHandler.ReponseCallBack(){

            @Override
            public void onHttpResponse(int status, String result) {
                try{
                    JSONObject resultObject=new JSONObject(result);
                    if(resultObject.get("status").toString().equalsIgnoreCase("success")){
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

            }
        });
    }

    public LoginStatus getLoginStatus(){
        return loginStatus;
    }
}
