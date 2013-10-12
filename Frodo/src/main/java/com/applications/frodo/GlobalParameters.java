package com.applications.frodo;

import android.content.res.Resources;
import android.util.Log;

import com.applications.frodo.blocks.IUser;
import com.applications.frodo.db.PersistanceMap;

/**
 * Created by siddharth on 26/09/13.
 */
public class GlobalParameters {
    private static String TAG=GlobalParameters.class.toString();
    private static GlobalParameters ourInstance = new GlobalParameters();
    private IUser user;
    private  String checkedInEventID;
    public static GlobalParameters getInstance() {
        return ourInstance;
    }

    private GlobalParameters() {
    }


    public IUser getUser() {

        return user;
    }

    public void setUser(IUser user) {
        this.user=user;
    }

    public void setCheckedInEventID(String checkedInEventID){
        try {
            PersistanceMap.getInstance().putString("checked_in_event",checkedInEventID);
        } catch (PersistanceMap.PersistanceMapUninitializedException e) {
            Log.e(TAG, "", e);
        };

        this.checkedInEventID=checkedInEventID;
    }

    public String getCheckedInEventID(){
        if(this.checkedInEventID==null || this.checkedInEventID.equals("")){
            try {
                this.checkedInEventID= PersistanceMap.getInstance().getString("checked_in_event",null);
            } catch (PersistanceMap.PersistanceMapUninitializedException e) {
                Log.e(TAG, "", e);
            }
        }
        return this.checkedInEventID;
    }

}
