package com.applications.frodo.views.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.socialnetworks.ISocialNetworkEvents;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.widgets.EventSummaryView;
import com.applications.frodo.widgets.EventSummaryWithPhotosView;

import java.util.List;

/**
 * Created by siddharth on 02/11/13.
 */
public class MyEventsFragment extends Fragment {

    private static final String TAG=MyEventsFragment.class.toString();

    private List<IEvent> events;
    private View rootView;

    public MyEventsFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_events, container, false);

        init();

        return rootView;
    }

    /**
     * This function sets up the view of the events. It fetches the events from facebook and lists them
     */
    private void init(){
        FacebookEvents.getInstance().getMyEvents(new ISocialNetworkEvents.Callback() {
            @Override
            public void onGetEvent(List<IEvent> eventList) {
                events = eventList;
                setupEventList(eventList);
            }
        });
    }

    private void setupEventList(List<IEvent> eventList){

        LinearLayout layout=(LinearLayout) rootView.findViewById(R.id.eventlistlayout);
        EventSummaryWithPhotosView.EventSummaryWithPhotosListAdaptor adaptor=new EventSummaryWithPhotosView.
                EventSummaryWithPhotosListAdaptor(eventList,getActivity().getBaseContext());
        for(int i=eventList.size()-1;i>=0;i--){
            View view=adaptor.getView(i,null,null);
            layout.addView(view);
        }
    }
}
