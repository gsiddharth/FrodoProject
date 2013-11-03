package com.applications.frodo.views.checkin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.socialnetworks.ISocialNetworkEvents;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.widgets.EventSummaryView;

import java.util.List;

/**
 * Created by siddharth on 09/10/13.
 */
public class EventCheckinFragment  extends Fragment {

    private static final String TAG=EventCheckinFragment.class.toString();
    private ListView eventListView;
    private View rootView;
    private List<IEvent> events;
    private EventSummaryView.EventSummaryListAdaptor eventAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.event_checkin,
                container, false);
        init();
        return rootView;
    }

    /**
     * This function sets up the view of the events. It fetches the events from facebook and lists them
     */
    private void init(){
        FacebookEvents.getInstance().getCurrentEventsIamGoingTo(new ISocialNetworkEvents.Callback() {
            @Override
            public void onGetEvent(List<IEvent> eventList) {
                events = eventList;
                setupEventList(eventList);
            }
        });
    }

    /**
     * THis function is used to change render the events into the view
     * @param events
     */
    public void setupEventList(List<IEvent> events){

        LinearLayout layout=(LinearLayout) rootView.findViewById(R.id.eventlistlayout);
        eventAdaptor=new EventSummaryView.EventSummaryListAdaptor(events,getActivity().getBaseContext(), this);
        for(int i=0;i<events.size();i++){
            layout.addView(eventAdaptor.getView(i,null,null));
        }
    }

    public void resetViewToNotCheckedIn(){
        if(eventAdaptor!=null){
            int count=eventAdaptor.getCount();
            for(int i=0;i<count;i++){
                try{
                    if(!events.get(i).getId().equals(GlobalParameters.getInstance().getCheckedInEvent().getId())){
                        EventSummaryView summaryView=(EventSummaryView) eventAdaptor.getView(i, null, null);
                        summaryView.resetToNotCheckedIn();
                    }else{
                        EventSummaryView summaryView=(EventSummaryView) eventAdaptor.getView(i, null, null);
                        summaryView.setToCheckedIn();
                    }
                }catch(Exception e){
                    Log.e(TAG,"",e);
                }
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        resetViewToNotCheckedIn();
    }
}