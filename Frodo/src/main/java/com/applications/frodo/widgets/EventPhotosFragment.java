package com.applications.frodo.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.IPhoto;
import com.applications.frodo.blocks.IUser;
import com.applications.frodo.blocks.IEventFactory;
import com.applications.frodo.socialnetworks.ISocialNetworkFriends;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.socialnetworks.facebook.FacebookUserFriends;
import com.applications.frodo.widgets.ImageAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This fragment is used to view the pics of the users. This fragment contains a PullToRefreshGridView.
 * This fragment shows content specific to an event and fetches pics of the event based on the parameter "who<my, network, all>"
 * If the event a user is checked in changes than a new
 * Created by siddharth on 12/10/13.
 */
public class EventPhotosFragment  extends Fragment implements FacebookEvents.FacebookCallbacks, PullToRefreshBase.OnRefreshListener2<GridView>{

    private IEventFactory factory;
    private PullToRefreshGridView eventPhotoGridView;
    private Context context;
    private FacebookEvents.EventPhotosOf who;
    private List<IPhoto> photos=new ArrayList<IPhoto>();
    private Set<String> imageSet=new HashSet<String>();
    private ImageAdapter adapter;
    private TextView nameView;
    private String eventID;
    private int imageAdaptorID=-1;
    private int numOfColumns;

    public EventPhotosFragment(){
    }

    public EventPhotosFragment(FacebookEvents.EventPhotosOf who, IEventFactory factory, int numOfColumns){
        this.who=who;
        this.factory=factory;
        this.numOfColumns=numOfColumns;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View rootView = inflater.inflate(R.layout.event_photos, container, false);

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey("who")){
                this.who= FacebookEvents.EventPhotosOf.valueOf(savedInstanceState.getString("who"));
            }
            if(savedInstanceState.containsKey("eventid")){
                this.eventID= savedInstanceState.getString("eventid");
            }

            if(savedInstanceState.containsKey("imageAdapterID")){
                this.imageAdaptorID= savedInstanceState.getInt("imageAdapterID");
            }

            if(savedInstanceState.containsKey("factory")){
                this.factory= savedInstanceState.getParcelable("factory");
            }

            if(savedInstanceState.containsKey("numOfColumns")){
                this.numOfColumns=savedInstanceState.getInt("numOfColumns");
            }
        }

        this.nameView=(TextView) rootView.findViewById(R.id.cameraPhotoCheckedInEventName);
        this.eventPhotoGridView=(PullToRefreshGridView) rootView.findViewById(R.id.eventPhotoGridView);
        this.eventPhotoGridView.getRefreshableView().setNumColumns(numOfColumns);
        setupGrid();

        return rootView;
    }

    private void setupGrid(){
        this.context=this.getActivity().getBaseContext();
        setEmptyView();
        reset();
    }

    private void setEmptyView(){
        TextView tv = new TextView(getActivity());
        tv.setGravity(Gravity.CENTER);
        tv.setText("Empty View, Pull Down/Up to Add Items");
        eventPhotoGridView.setEmptyView(tv);
    }


    private void downloadPhotoData(){
        IEvent event=factory.getEvent();
        if(event!=null && event.getId()!=null){

            switch (who){
                case MY:
                    FacebookEvents.getInstance().getMyPhotosOfEvent(event.getId(),this);
                    break;
                case NETWORK:
                    if(GlobalParameters.getInstance().getUser().getFriends()==null){
                        FacebookUserFriends.getInstance().getFriends(GlobalParameters.getInstance().getUser().getId(),new ISocialNetworkFriends.Callback() {
                            @Override
                            public void onUserFriendsGet(Map<String, IUser> friends) {
                                GlobalParameters.getInstance().getUser().setFriends(friends);
                                downloadPhotoData();
                            }
                        });
                    }
                    FacebookEvents.getInstance().getMyNetworkPhotosOfEvent(event.getId(),this);
                case ALL:
                    FacebookEvents.getInstance().getAllPhotosOfEvent(event.getId(),this);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
        downloadPhotoData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
    }

    @Override
    public void onEventPhotosDownloadComplete(List<IPhoto> pictures) {
        boolean moreAdded=false;

        for(IPhoto photo:pictures){
            if(!imageSet.contains(photo.getThumbnail().getURL())){
                imageSet.add(photo.getThumbnail().getURL());
                adapter.addNewItemAtFront(photo);
                moreAdded=true;
            }
        }

        if(moreAdded){
            adapter.notifyDataSetChanged();
        }
        eventPhotoGridView.onRefreshComplete();

        if(moreAdded){
            eventPhotoGridView.refreshDrawableState();
            eventPhotoGridView.getRefreshableView().invalidate();
            eventPhotoGridView.getRefreshableView().requestLayout();
        }
    }

    @Override
    public void onPhotoShareComplete() {
    }

    private void reset(){
        IEvent event=factory.getEvent();
        if(event!=null){
            if(this.eventID==null || !this.eventID.equals(event.getId())){
                photos.clear();
                imageSet.clear();
                this.adapter= ImageAdapter.ImageAdapterFactory.getAdapter(this.getActivity(),context,photos, numOfColumns,-1);
                this.imageAdaptorID=this.adapter.getID();
                eventPhotoGridView.getRefreshableView().setAdapter(adapter);
                eventPhotoGridView.setOnRefreshListener(this);

                downloadPhotoData();
                setName();
                this.eventID=event.getId();
            }else{
                this.adapter= ImageAdapter.ImageAdapterFactory.getAdapter(this.getActivity(),context,photos,numOfColumns,this.imageAdaptorID);
                this.imageAdaptorID=this.adapter.getID();
                eventPhotoGridView.getRefreshableView().setAdapter(adapter);
                eventPhotoGridView.setOnRefreshListener(this);
                setName();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        reset();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(this.who!=null)
            outState.putString("who", this.who.toString());

        outState.putInt("imageAdaptorID", this.imageAdaptorID);
        if(this.factory!=null){

            IEvent event=factory.getEvent();
            if(event!=null){
                outState.putString("eventid", event.getId());
            }

            outState.putParcelable("factory", factory);
        }

        outState.putInt("numOfColumns", numOfColumns);

    }

    private void setName(){
        IEvent event=factory.getEvent();
        if(event!=null && event.getName()!=null){
            this.nameView.setText(event.getName());
        }
    }
}