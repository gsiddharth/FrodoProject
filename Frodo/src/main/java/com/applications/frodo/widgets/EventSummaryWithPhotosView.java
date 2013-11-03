package com.applications.frodo.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.IPhoto;
import com.applications.frodo.networking.PictureDownloader;
import com.applications.frodo.socialnetworks.facebook.FacebookEvents;
import com.applications.frodo.utils.Convertors;
import com.applications.frodo.views.checkin.EventCheckinFragment;
import com.applications.frodo.views.event.EventActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by siddharth on 02/11/13.
 */
public class EventSummaryWithPhotosView extends View {

    private static final String TAG=EventSummaryWithPhotosView.class.toString();

    private static final int DEFAULT_HEIGHT=100;
    private static final int DEFAULT_NUM_OF_ROWS=2;
    private static final int DEFAULT_NUM_OF_COLUMNS=4;
    private static final int DEFAULT_BORDER_WIDTH=10;
    private static final int DEFAULT_BORDER_COLOR=Color.parseColor("#cfcfcf");
    private static final int DEFAULT_BACKGROUND_COLOR=Color.WHITE;
    private static final int DEFAULT_TAP_BACKGROUND_COLOR=Color.parseColor("#cfcfcf");
    private static final int DEFAULT_TITLE_SIZE=35;
    private static final int DEFAULT_TITLE_COLOR=Color.parseColor("#5B5B5B");
    private static final int DEFAULT_DATE_SIZE=25;
    private static final int DEFAULT_DATE_COLOR=Color.parseColor("#5B5B5B");
    private static final int DEFAULT_PADDING=10;
    private static final int DEFAULT_IMAGE_PADDING=10;


    private int height=DEFAULT_HEIGHT;
    private int numOfRows=DEFAULT_NUM_OF_ROWS;
    private int numOfColumns=DEFAULT_NUM_OF_COLUMNS;
    private int borderWidth=DEFAULT_BORDER_WIDTH;
    private int borderColor = DEFAULT_BORDER_COLOR;
    private int titleSize=DEFAULT_TITLE_SIZE;
    private int titleColor=DEFAULT_TITLE_COLOR;
    private int dateSize=DEFAULT_DATE_SIZE;
    private int dateColor=DEFAULT_DATE_COLOR;
    private int padding=DEFAULT_PADDING;
    private int imagePadding=DEFAULT_IMAGE_PADDING;
    private int backgroundColor=DEFAULT_BACKGROUND_COLOR;
    private int tapBackgroundColor=DEFAULT_TAP_BACKGROUND_COLOR;

    private int screenWidth;
    private int numberOfImagesDrawn=0;
    private int imageWidth=0;
    private int imageHeight=0;
    private int maxImages=0;


    private List<Bitmap> images=new ArrayList<Bitmap>();

    private IEvent event;

    private GestureDetector detector;

    public EventSummaryWithPhotosView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DisplayMetrics mat=context.getResources().getDisplayMetrics();
        screenWidth=mat.widthPixels;
        imageWidth=(screenWidth-padding*2-borderWidth*2-(numOfColumns+1)*imagePadding)/numOfColumns;
        imageHeight=imageWidth;
        maxImages=numOfColumns*numOfRows;

        detector=new GestureDetector(context,new EventSummaryWithPhotoGestureListener());

    }

    public void setEvent(IEvent event){
        this.event=event;

        FacebookEvents.getInstance().getAllPhotosOfEvent(event.getId(), new FacebookEvents.FacebookCallbacks() {
            @Override
            public void onPhotoShareComplete() {

            }

            @Override
            public void onEventPhotosDownloadComplete(List<IPhoto> photos) {
                setEventPhotos(photos);
            }
        });

        this.onMeasure(getMeasuredWidth(),getMeasuredHeight());
        this.invalidate();
        this.requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){

        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            setTapBackgroundColor();
        }else{
            resetToDefaultBackgroundColor();
        }

        detector.onTouchEvent(ev);
        return true;
    }


    @Override
    public void onDraw(final Canvas canvas){
        if(event!=null){
            numberOfImagesDrawn=0;
            drawBorder(canvas);
            drawTitle(canvas);
            drawImages(canvas);
            drawDate(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minW = getSuggestedMinimumWidth()- (getPaddingLeft() + getPaddingRight());
        int w = resolveSizeAndState(minW, widthMeasureSpec, 1);

        int minH = Math.max((int)(borderWidth*2+padding*2+titleSize+(Math.ceil(images.size()/(double) numOfColumns))*(imageWidth+imagePadding*2)), DEFAULT_HEIGHT);
        int h = resolveSizeAndState(minH, heightMeasureSpec, 0);
        height=h;
        setMeasuredDimension(w, h);
    }


    private void drawBorder(Canvas canvas){
        Paint borderRect1Paint=new Paint();
        borderRect1Paint.setColor(borderColor);
        canvas.drawRect(0,0,screenWidth,height,borderRect1Paint);

        Paint borderRect2Paint=new Paint();
        borderRect2Paint.setColor(backgroundColor);
        canvas.drawRect(borderWidth,borderWidth/2,screenWidth-borderWidth,height-borderWidth/2,borderRect2Paint);
    }

    public void setTapBackgroundColor(){
        this.backgroundColor=this.tapBackgroundColor;
        invalidate();
        requestLayout();
    }

    public void resetToDefaultBackgroundColor(){
        this.backgroundColor=DEFAULT_BACKGROUND_COLOR;
        invalidate();
        requestLayout();
    }

    private void drawTitle(Canvas canvas){

        TextPaint titlePaint=new TextPaint();
        titlePaint.setTextSize(titleSize);
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        titlePaint.setColor(titleColor);
        if(event.getName().length()>screenWidth/titleSize*1.7){
            canvas.drawText(StringUtils.capitalize(event.getName()).substring(0,(int) (screenWidth/titleSize*1.5))+"...",borderWidth+padding,borderWidth+padding+titleSize,titlePaint);
        }else{
            canvas.drawText(StringUtils.capitalize(event.getName()),borderWidth+padding,borderWidth+padding+titleSize, titlePaint);
        }
    }

    private void drawDate(Canvas canvas){
        TextPaint datePaint=new TextPaint();
        datePaint.setTextSize(dateSize);
        datePaint.setTypeface(Typeface.DEFAULT_BOLD);
        datePaint.setColor(dateColor);
        datePaint.setTextAlign(Paint.Align.RIGHT);
        String date= Convertors.toString(event.getStartTime(), "EEE, dd MMM");
        canvas.drawText(date,screenWidth-2*borderWidth-padding,borderWidth+padding+titleSize, datePaint);
    }


    private void drawImages(final Canvas canvas){
        for(Bitmap bm:images){
            addImage(bm, canvas);
        }
    }



    private synchronized void addImage(Bitmap bitmap, Canvas canvas){
        int row=(numberOfImagesDrawn)/numOfColumns+1;
        int col=(numberOfImagesDrawn)%numOfColumns+1;
        Bitmap newBitmap;

        if(bitmap.getWidth()<bitmap.getHeight()){
            newBitmap=Bitmap.createScaledBitmap(bitmap,imageWidth,bitmap.getHeight()*imageWidth/bitmap.getWidth(),false);
        }else{
            newBitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()*imageHeight/bitmap.getHeight(),imageHeight,false);
        }

        Bitmap thumbnail=Bitmap.createBitmap(newBitmap,0,0,imageWidth,imageHeight);
        canvas.drawBitmap(thumbnail,borderWidth+imagePadding*col+imageWidth*(col-1),
                borderWidth+padding*2+titleSize+imagePadding*row+imageHeight*(row-1),new Paint());
        numberOfImagesDrawn++;

    }


    private void setEventPhotos(final List<IPhoto> photos){

        final String[] thumbnails=new String[Math.min(maxImages,photos.size())];

        for(int i=0;i<thumbnails.length;i++){
            thumbnails[i]=photos.get(i).getThumbnail().getURL();
        }

        for(String thumbnail:thumbnails){
            PictureDownloader downloader=new PictureDownloader(new PictureDownloader.PictureDownloaderListener() {
                @Override
                public void onPictureDownload(Bitmap bitmap) {
                    images.add(bitmap);
                    invalidate();
                    requestLayout();

                }
            });

            downloader.execute(thumbnail);
        }
    }

    public void startEventActivity(){
        Intent intent=new Intent(getContext(), EventActivity.class);
        intent.putExtra("event", event);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private class EventSummaryWithPhotoGestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onDown(MotionEvent e){
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent ev){
        }


        @Override
        public boolean onSingleTapUp(MotionEvent ev){
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e){
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            getRootView().playSoundEffect(SoundEffectConstants.CLICK);
            startEventActivity();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e){
            return true;
        }
    }

    public static class EventSummaryWithPhotosListAdaptor extends BaseAdapter {

        private final List<IEvent> events;
        private final Map<Integer, EventSummaryWithPhotosView> eventSummariesMap;
        private final Context context;

        public EventSummaryWithPhotosListAdaptor (List<IEvent> events, Context context){
            this.eventSummariesMap =new HashMap<Integer, EventSummaryWithPhotosView>();
            this.events=events;
            this.context=context;
        }


        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int i) {
            return events.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            IEvent event=events.get(i);
            EventSummaryWithPhotosView existingEventView= eventSummariesMap.get(i);
            if(existingEventView!=null && existingEventView.event==event){
                return existingEventView;
            }else{
                EventSummaryWithPhotosView eventSummary=new EventSummaryWithPhotosView (context,null,0);
                eventSummary.setEvent(event);
                eventSummariesMap.put(i, eventSummary);
                return eventSummary;
            }
        }
    }

}