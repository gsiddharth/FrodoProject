package com.applications.frodo.socialnetworks;

import java.util.Map;

/**
 * Created by siddharth on 30/09/13.
 */
public interface ILogin {

    public void login(Map<String, String> params, ILoginCallback loginCallback);

    public void login(String json, ILoginCallback loginCallback);

    public LoginStatus getLoginStatus();

}
