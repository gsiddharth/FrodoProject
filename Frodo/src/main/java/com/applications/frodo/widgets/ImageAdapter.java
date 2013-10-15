package com.applications.frodo.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.networking.PictureDownloader;
import java.util.List;


/**
 * Created by siddharth on 14/10/13.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> thumnails;
    private List<String> images;

    public ImageAdapter(Context c, List<String> thumnails, List<String> images) {
        mContext = c;
        this.thumnails=thumnails;
        this.images=images;
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return images.get(position);
    }

    public long getItemId(int position) {
        return thumnails.get(position).hashCode();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            int width= GlobalParameters.getInstance().getScreenWidth(mContext);
            width=width/2-16;
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        PictureDownloader downloader=new PictureDownloader(new PictureDownloader.PictureDownloaderListener() {
            @Override
            public void onPictureDownload(Bitmap bitmap) {
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setImageBitmap(bitmap);
            }
        });


        downloader.execute(thumnails.get(position));
        return imageView;
    }

}
