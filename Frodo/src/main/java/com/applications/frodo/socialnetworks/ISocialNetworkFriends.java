package com.applications.frodo.socialnetworks;

import com.applications.frodo.blocks.IUser;

import java.util.Map;

/**
 * Created by siddharth on 16/10/13.
 */
public interface ISocialNetworkFriends {

    public void getFriends(String userId, Callback callback);

    public interface Callback{
        public void onUserFriendsGet(Map<String, IUser> friends);
    }

}
