package com.applications.frodo;

import com.applications.frodo.blocks.IUser;

/**
 * Created by siddharth on 26/09/13.
 */
public class GlobalParameters {
    private static GlobalParameters ourInstance = new GlobalParameters();
    private IUser user;

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

}
