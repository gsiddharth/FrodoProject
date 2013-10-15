package com.applications.frodo;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;

import com.applications.frodo.blocks.Event;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.User;
import com.applications.frodo.db.PersistanceMap;

/**
 * Created by siddharth on 26/09/13.
 */
public class GlobalParameters {
    private static String TAG=GlobalParameters.class.toString();
    private static GlobalParameters ourInstance = new GlobalParameters();

    public static final String CURRENT_USER_KEY="current_usre";
    public static final String CHECKED_IN_EVENT="checked_in_event";

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
        if(this.user==null){
            try {
                this.user=(IUser) PersistanceMap.getInstance().getObject(CURRENT_USER_KEY,null,User.class);
            } catch (PersistanceMap.PersistanceMapUninitializedException e) {
                Log.e(TAG, "", e);
            }
        }
        return user;
    }

    public void setUser(IUser user) {
        this.user=user;
        try {
            PersistanceMap.getInstance().putObject(CURRENT_USER_KEY, this.user);
        } catch (PersistanceMap.PersistanceMapUninitializedException e) {
            Log.e(TAG, "", e);
        };

    }

    public void setCheckedInEvent(IEvent checkedInEvent){
        try {
            Log.i(TAG,"Setting check in event");
            PersistanceMap.getInstance().putObject("checked_in_event", checkedInEvent);
            Log.i(TAG,"Set check in event");
        } catch (PersistanceMap.PersistanceMapUninitializedException e) {
            Log.e(TAG, "", e);
        };
        this.checkedInEvent=checkedInEvent;
    }

    public IEvent getCheckedInEvent(){
        if(this.checkedInEvent==null ){
            try {
                Log.i(TAG,"getting check in event");
                this.checkedInEvent= (IEvent) PersistanceMap.getInstance().getObject("checked_in_event", null, Event.class);
                Log.i(TAG,"got check in event "+this.checkedInEvent.getId());
            } catch (PersistanceMap.PersistanceMapUninitializedException e) {
                Log.e(TAG, "PersistanceMap not Inititalized", e);
            }
        }
        return this.checkedInEvent;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public boolean isHasAllFBWritePermissions() {
        return hasAllFBWritePermissions;
    }

    public void setHasAllFBWritePermissions(boolean hasAllFBWritePermissions) {
        this.hasAllFBWritePermissions = hasAllFBWritePermissions;
    }

    public int getScreenWidth(Context context){
        DisplayMetrics mat=context.getResources().getDisplayMetrics();
        return mat.widthPixels;
    }

    public int getScreenHeight(Context context){
        DisplayMetrics mat=context.getResources().getDisplayMetrics();
        return mat.heightPixels;
    }
}
