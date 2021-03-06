package com.applications.frodo.views.home;

import java.util.Locale;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.applications.frodo.R;
import com.applications.frodo.blocks.CurrentEventFactory;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.widgets.EventPhotosFragment;

public class ApplicationActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey("current_view")){
                mViewPager.setCurrentItem(savedInstanceState.getInt("current_view"));
            }
        }
    }

    public void setView(int position){
        mViewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.application, menu);
        return true;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if(outState!=null){
            outState.putInt("current_view", mViewPager.getCurrentItem());
        }
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
            Fragment fragment = null;
            switch(position){
                case 0:
                    return new CameraFragment();
                case 1: return new MenuFragment();
                case 2:
                    return new EventPhotosFragment(FacebookEvents.EventPhotosOf.ALL,new CurrentEventFactory(),3);
                case 3:
                    return new EventPhotosFragment(FacebookEvents.EventPhotosOf.NETWORK,new CurrentEventFactory(),3);
                case 4:
                    return new EventPhotosFragment(FacebookEvents.EventPhotosOf.MY, new CurrentEventFactory(),2);
            }
            if(fragment!=null){
                Bundle args = new Bundle();
                args.putInt(MenuFragment.ARG_SECTION_NUMBER, position + 1);
                fragment.setArguments(args);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "CAMERA";
                case 1:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }
}
