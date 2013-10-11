package com.applications.frodo.views.checkin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.socialnetworks.ISocialNetworkEvents;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.widgets.EventSummary;

import java.util.List;

/**
 * Created by siddharth on 09/10/13.
 */
public class EventCheckinFragment  extends Fragment {

    private static final String TAG=EventCheckinFragment.class.toString();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_checkin,
                container, false);
        init();
        return view;
    }

    /**
     * This function sets up the view of the events. It fetches the events from facebook and lists them
     */
    private void init(){
        FacebookEvents.getInstance().getEvents(new ISocialNetworkEvents.Callback() {
            @Override
            public void onGetEvent(List<IEvent> events) {
                setupEventList(events);
            }
        });
    }

    /**
     * THis function is used to change render the events into the view
     * @param events
     */
    public void setupEventList(List<IEvent> events){
        EventSummary.EventSummaryListAdaptor eventAdaptor=new EventSummary.EventSummaryListAdaptor(events,this.getActivity().getBaseContext());
        ListView eventListView= (ListView) this.getActivity().findViewById(R.id.eventListView);
        eventListView.setAdapter(eventAdaptor);
    }
}