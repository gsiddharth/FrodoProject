package com.applications.frodo.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by siddharth on 10/10/13.
 */
public class PictureDownloader extends AsyncTask<String, Integer, Bitmap[]>{

    private static String TAG=PictureDownloader.class.toString();

    private static final LruCache<String, Bitmap> cache=new LruCache<String, Bitmap>((int)(Runtime.getRuntime().maxMemory() / 1024/8)){
        @Override
        protected int sizeOf(String key, Bitmap bitmap){
            return bitmap.getByteCount()/1024;
        }
    };

    private PictureDownloaderListener listener;

    public PictureDownloader(PictureDownloaderListener listener){
        this.listener=listener;
    }

    @Override
    protected Bitmap[] doInBackground(String... urls) {
        if(urls.length>0){
            Bitmap[] bitmaps=new Bitmap[urls.length];
            for(int i=0;i<urls.length;i++){
                String urlstr=urls[i];
                try{
                    Bitmap bitmap=cache.get(urlstr);
                    if(bitmap==null){
                        URL url=new URL(urlstr);
                        InputStream in=new BufferedInputStream(url.openStream());
                        bitmap= BitmapFactory.decodeStream(in);
                        cache.put(urlstr,bitmap);
                    }

                    bitmaps[i]=bitmap;
                    publishProgress((int) (i/((float) urls.length)*100));

                }catch(Exception e){
                    bitmaps[i]=null;
                    Log.e(TAG,"Error while downloading a file from url "+urlstr,e);
                }
            }
            return bitmaps;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress){

    }

    @Override
    protected void onPostExecute(Bitmap[] results){
        for(Bitmap result:results){
            listener.onPictureDownload(result);
        }
    }

    public static interface PictureDownloaderListener{
        public void onPictureDownload(Bitmap bitmap);
    }

}
