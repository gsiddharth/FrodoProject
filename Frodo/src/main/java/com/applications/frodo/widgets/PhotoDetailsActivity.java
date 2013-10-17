package com.applications.frodo.widgets;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.applications.frodo.R;
import com.applications.frodo.networking.PictureDownloader;

public class PhotoDetailsActivity extends FragmentActivity {

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

    private List<String> imagesUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        Intent intent=getIntent();
        this.imagesUrls=intent.getStringArrayListExtra("images");
        int currentPosition=intent.getIntExtra("current_image_position", 0);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.photoDetailsPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentPosition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_details, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new PhotoViewFragment(imagesUrls.get(position),position);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if(imagesUrls==null){
                return 0;
            }else{
                return imagesUrls.size();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (1+position)+"";
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class PhotoViewFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        private String imageUrl;
        private int position;

        public PhotoViewFragment(String imageUrl, int position) {
            this.imageUrl=imageUrl;
            this.position=position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.photo_detail, container, false);
            final ImageView imageView = (ImageView) rootView.findViewById(R.id.singlePhotoView);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            PictureDownloader downloader=new PictureDownloader(new PictureDownloader.PictureDownloaderListener() {
                @Override
                public void onPictureDownload(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            });

            downloader.execute(imageUrl);

            return rootView;
        }
    }

}
