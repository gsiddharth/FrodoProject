package com.applications.frodo.socialnetworks.facebook;

import android.os.Bundle;
import android.util.Log;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.User;
import com.applications.frodo.socialnetworks.ISocialNetworkFriends;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharth on 16/10/13.
 */
public class FacebookUserFriends implements ISocialNetworkFriends{

    private static FacebookUserFriends instance=new FacebookUserFriends();
    private static String TAG=FacebookUserFriends.class.toString();

    private FacebookUserFriends(){

    }

    public static FacebookUserFriends getInstance(){
        return instance;
    }

    @Override
    public void getFriends(String userId, final Callback callback) {
        String fqlQuery = "SELECT uid, first_name, last_name FROM user WHERE uid IN \n" +
                "(SELECT uid2 FROM friend WHERE uid1 = "+GlobalParameters.getInstance().getUser().getFacebookId()+")";

        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session
                .getActiveSession();

        Request request = new Request(session,
                "/fql", params, HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(
                            Response response) {

                        GraphObject graphObject = response
                                .getGraphObject();
                        toFriendsList(graphObject,callback);

                    }
                });
        Request.executeBatchAsync(request);

    }

    private void toFriendsList(GraphObject graphObject, Callback callback){
        try{
            if(graphObject!=null){
                JSONObject dataJSon=graphObject.getInnerJSONObject();
                JSONArray arr=dataJSon.getJSONArray("data");

                Map<String, IUser> friends=new HashMap<String, IUser>();

                for(int i=0;i<arr.length();i++){
                    JSONObject obj=arr.getJSONObject(i);
                    IUser user=new User();
                    user.setId(obj.getInt("uid")+"");
                    user.setFirstName(obj.getString("first_name"));
                    user.setFirstName(obj.getString("last_name"));
                    friends.put(user.getId(),user);
                }

                callback.onUserFriendsGet(friends);
            }
        }catch(Exception e){
            Log.e(TAG,"",e);
        }
    }
}
