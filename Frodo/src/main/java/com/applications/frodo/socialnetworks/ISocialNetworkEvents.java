package com.applications.frodo.socialnetworks;

import com.applications.frodo.blocks.IEvent;

import java.util.List;

/**
 * Created by siddharth on 11/10/13.
 */
public interface ISocialNetworkEvents {

    /**
     * This function is used to get events from the given social network
     * @param callback This is the interface used to callback once you get the events
     */
    public void getEvents(Callback callback);

    /**
     * Interface to implement event callbacks
     */
    public interface Callback{
        public void onGetEvent(List<IEvent> events);
    }
}
