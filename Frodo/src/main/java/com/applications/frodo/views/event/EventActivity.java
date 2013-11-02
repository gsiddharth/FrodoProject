package com.applications.frodo.views.event;

import java.util.Locale;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applications.frodo.R;
import com.applications.frodo.blocks.CurrentEventFactory;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.SingleEventFactory;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.utils.GeneralUtils;
import com.applications.frodo.widgets.EventPhotosFragment;

public class EventActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private IEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        event=getIntent().getParcelableExtra("event");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        if(event!=null){
            System.out.println(event.getName());
            setTitle(GeneralUtils.wrapText(event.getName(), 20, 1).get(0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }
    
    

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new EventDetailFragment(event);
                case 1:
                    return new EventPhotosFragment(FacebookEvents.EventPhotosOf.ALL,new SingleEventFactory(event),3);
                case 2:
                    return new EventPhotosFragment(FacebookEvents.EventPhotosOf.NETWORK,new SingleEventFactory(event),3);
                case 3:
                    return new EventPhotosFragment(FacebookEvents.EventPhotosOf.MY, new SingleEventFactory(event),2);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "EVENT";
                case 1:
                    return "ALL PICS";
                case 2:
                    return "NETWORK PICS";
                case 3:
                    return "MY PICS";
            }
            return null;
        }
    }

}
