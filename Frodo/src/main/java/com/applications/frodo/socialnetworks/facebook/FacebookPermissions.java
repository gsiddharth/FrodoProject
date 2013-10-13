package com.applications.frodo.socialnetworks.facebook;

import android.app.Activity;

import com.applications.frodo.GlobalParameters;
import com.facebook.Session;

import java.util.List;

/**
 * Created by siddharth on 12/10/13.
 */
public class FacebookPermissions {

    public static boolean requestWritePermissions(List<String> permissionList, Activity activity, int code){

        Session session=Session.getActiveSession();

        if(session!=null){
            List<String> allowedPermissions=session.getPermissions();

            if(!allowedPermissions.containsAll(permissionList)){
                if(session!=null){
                    Session.NewPermissionsRequest newPermissionsRequest=new Session.NewPermissionsRequest(activity, permissionList).setRequestCode(code);
                    session.requestNewPublishPermissions(newPermissionsRequest);
                }
                return true;
            }else{
                GlobalParameters.getInstance().setHasAllFBWritePermissions(true);
            }
            return false;
        }else{
            return true;
        }
    }
}
