package com.applications.frodo.views.home;

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
 * Created by siddharth on 12/10/13.
 */
public class EventPhotosFragment  extends Fragment implements FacebookEvents.FacebookCallbacks, PullToRefreshBase.OnRefreshListener2<GridView>{

    private IEvent event;
    private PullToRefreshGridView eventPhotoGridView;
    private Context context;
    private FacebookEvents.EventPhotosOf who;
    private List<IPhoto> photos=new ArrayList<IPhoto>();
    private Set<String> imageSet=new HashSet<String>();
    private ImageAdapter adapter;
    private TextView nameView;
    private String eventID;
    private int imageAdaptorID=-1;

    public EventPhotosFragment(){
    }

    public EventPhotosFragment(FacebookEvents.EventPhotosOf who){
        this.who=who;
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
        }

        this.eventPhotoGridView=(PullToRefreshGridView) rootView.findViewById(R.id.eventPhotoGridView);
        this.nameView=(TextView) rootView.findViewById(R.id.cameraPhotoCheckedInEventName);
        this.event=GlobalParameters.getInstance().getCheckedInEvent();
        this.context=this.getActivity().getBaseContext();
        this.adapter= ImageAdapter.ImageAdapterFactory.getAdapter(this.getActivity(),context,photos,this.imageAdaptorID);
        this.imageAdaptorID=this.adapter.getID();
        setupGrid();

        return rootView;
    }

    private void setupGrid(){
        eventPhotoGridView.getRefreshableView().setAdapter(adapter);
        eventPhotoGridView.setOnRefreshListener(this);
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
        if(this.eventID==null || !this.eventID.equals(this.event.getId()) || this.event!=GlobalParameters.getInstance().getCheckedInEvent()){
            adapter.reset();
            imageSet.clear();
            event=GlobalParameters.getInstance().getCheckedInEvent();
            eventPhotoGridView.invalidate();
            eventPhotoGridView.requestLayout();
            downloadPhotoData();
            setName();
            this.eventID=this.event.getId();
        }else{
            setName();
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
        outState.putString("who", this.who.toString());
        outState.putInt("imageAdaptorID", this.imageAdaptorID);
        if(this.event!=null){
            outState.putString("eventid", this.event.getId());
        }
    }

    private void setName(){
        if(this.event!=null && this.event.getName()!=null){
            this.nameView.setText(this.event.getName());
        }
    }
}