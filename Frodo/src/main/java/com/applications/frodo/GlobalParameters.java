package com.applications.frodo;
import android.util.Log;

import com.applications.frodo.blocks.Event;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.IUser;
import com.applications.frodo.db.PersistanceMap;

/**
 * Created by siddharth on 26/09/13.
 */
public class GlobalParameters {
    private static String TAG=GlobalParameters.class.toString();
    private static GlobalParameters ourInstance = new GlobalParameters();
    private IUser user;
    private IEvent checkedInEvent;
    private String rootDir;
    private boolean hasAllFBWritePermissions;

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

    public void setCheckedInEvent(IEvent checkedInEvent){
        try {
            PersistanceMap.getInstance().putString("checked_in_event", checkedInEvent.toJSON());
        } catch (PersistanceMap.PersistanceMapUninitializedException e) {
            Log.e(TAG, "", e);
        };
        this.checkedInEvent=checkedInEvent;
    }

    public String getCheckedInEventID(){
        if(this.checkedInEvent==null ){
            try {
                this.checkedInEvent= Event.getEvent(PersistanceMap.getInstance().getString("checked_in_event", null));
            } catch (PersistanceMap.PersistanceMapUninitializedException e) {
                Log.e(TAG, "", e);
            }
        }
        if(this.checkedInEvent!=null)
            return this.checkedInEvent.getId();
        else{
            return "";
        }
    }

    public IEvent getCheckedInEvent(){
        if(this.checkedInEvent==null ){
            try {
                this.checkedInEvent= Event.getEvent(PersistanceMap.getInstance().getString("checked_in_event", null));
            } catch (PersistanceMap.PersistanceMapUninitializedException e) {
                Log.e(TAG, "", e);
            }
        }
        return this.checkedInEvent;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        System.out.println(rootDir);
        this.rootDir = rootDir;
    }

    public boolean isHasAllFBWritePermissions() {
        return hasAllFBWritePermissions;
    }

    public void setHasAllFBWritePermissions(boolean hasAllFBWritePermissions) {
        this.hasAllFBWritePermissions = hasAllFBWritePermissions;
    }
}
