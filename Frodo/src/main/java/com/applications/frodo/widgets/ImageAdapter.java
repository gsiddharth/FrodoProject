package com.applications.frodo.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.blocks.IPhoto;
import com.applications.frodo.blocks.Photo;
import com.applications.frodo.networking.PictureDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by siddharth on 14/10/13.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private Activity activity;
    private List<IPhoto> photos;
    private List<View> views;
    private int width;
    private int height;
    private int imagePadding=8;
    private static int staticID=1;
    private int id;

    public ImageAdapter(Activity activity, Context c, List<IPhoto> photos) {
        this.activity=activity;
        this.mContext = c;
        this.views=new ArrayList<View>();
        for(int i=0;i<photos.size();i++){
            this.views.add(null);
        }

        width= GlobalParameters.getInstance().getScreenWidth(mContext);
        height= GlobalParameters.getInstance().getScreenHeight(mContext);
        width=width/2-imagePadding*2;
        height=height/2-imagePadding*2-100;
        this.photos=photos;
        this.id=staticID++;
    }

    public int getID(){
        return id;
    }



    public void addNewItemAtFront(IPhoto photo){

        for(View v:views){
            if(v!=null){
                v.setId(v.getId()+1);
            }
        }
        this.photos.add(0,photo);
        this.views.add(0,null);
    }

    public void reset(){
        this.photos.clear();
        this.views.clear();
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return photos.get(position);
    }

    public long getItemId(int position) {
        return photos.get(position).hashCode();
    }

    // create a new ImageView for each item referenced by the Adapter
    public synchronized View getView(final int position, View convertView, ViewGroup parent) {

        if(views.get(position)!=null){
            return views.get(position);
        }else{

            final ImageView imageView;

            boolean createNewView=false;

            if(convertView!=null){
                if(convertView.getId()!=position)
                    createNewView=true;
            }else{
                createNewView=true;
            }

            if (createNewView) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(width, height));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
                imageView.setId(position);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(activity, PhotoDetailsActivity.class);
                        intent.putStringArrayListExtra("images", toImageURLList(photos));
                        intent.putExtra("current_image_position", position);
                        activity.startActivity(intent);
                    }

                    private ArrayList<String> toImageURLList(List<IPhoto> photos){
                        ArrayList<String> urls=new ArrayList<String>();
                        for(IPhoto photo:photos){
                            urls.add(photo.getImage().getURL());
                        }
                        return urls;
                    }
                });

                PictureDownloader downloader=new PictureDownloader(new PictureDownloader.PictureDownloaderListener() {
                    @Override
                    public void onPictureDownload(Bitmap bitmap) {
                        if(bitmap.getWidth()/(double) bitmap.getHeight()>width/(double) height){
                            bitmap=Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*height/(double) bitmap.getHeight()), height,false);
                            bitmap=Bitmap.createBitmap(bitmap,(bitmap.getWidth()-width)/2,0,width,height);
                        }else{
                            bitmap=Bitmap.createScaledBitmap(bitmap,width, (int)(bitmap.getHeight()*width/(double) bitmap.getWidth()),false);
                            bitmap=Bitmap.createBitmap(bitmap,0,(bitmap.getHeight()-height)/2,width,height);
                        }

                        bitmap= Bitmap.createScaledBitmap(bitmap,Math.min(width,bitmap.getWidth()),height,false);
                        imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        imageView.setImageBitmap(bitmap);

                    }
                });

                downloader.execute(photos.get(position).getThumbnail().getURL());
                views.set(position, imageView);

            } else {
                imageView = (ImageView) convertView;
            }
            return imageView;
        }
    }

    public static class ImageAdapterFactory{
        private static Map<Integer, ImageAdapter> adaptors=new HashMap<Integer, ImageAdapter>();

        public static ImageAdapter getAdapter(Activity activity, Context c, List<IPhoto> photos, int id){
            if(adaptors.containsKey(id)){
                return adaptors.get(id);
            }else{
                ImageAdapter adapter=new ImageAdapter(activity,c,photos);
                adaptors.put(adapter.getID(),adapter);
                return adapter;
            }
        }

        public static ImageAdapter getAdapter(int id){
            if(adaptors.containsKey(id)){
                return adaptors.get(id);
            }else{
                return null;
            }
        }
    }
}
