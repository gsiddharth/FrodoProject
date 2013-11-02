package com.applications.frodo.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.networking.PictureDownloader;
import com.applications.frodo.utils.Convertors;
import com.applications.frodo.utils.GeneralUtils;
import com.applications.frodo.views.checkin.EventCheckinFragment;
import com.applications.frodo.views.event.EventActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The view of the event in the event list box
 * Created by siddharth on 08/10/13.
 */
public class EventSummaryView extends View implements PictureDownloader.PictureDownloaderListener{

    private static final String TAG=EventSummaryView.class.toString();

    private IEvent event;

    private static final int HEIGHT=170;
    private static final int IMAGE_PADDING=40;
    private static final int TITLE_TEXT_SIZE =28;
    private static final int LOCATION_TEXT_SIZE =22;
    private static final int DATE_TEXT_SIZE =22;
    private int SELECTED_LEFT_PADDING=35;
    private int leftPadding=0;
    private int screenWidth;
    private int borderWidth=10;

    private Bitmap defaultImage=null;
    private GestureDetector detector;
    private EventCheckinFragment eventCheckinFragment;

    public EventSummaryView(Context context, AttributeSet attrs, int defStyle, EventCheckinFragment eventCheckinFragment) {
        super(context, attrs, defStyle);
        this.eventCheckinFragment=eventCheckinFragment;
        this.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundColor(Color.WHITE);
        this.setClickable(true);
        TypedArray a= context.getTheme().obtainStyledAttributes(attrs, R.styleable.EventSummaryView,0,0);
        defaultImage= BitmapFactory.decodeResource(getResources(),R.drawable.no_image_icon);
        defaultImage=getScaledBitmap(defaultImage, HEIGHT - IMAGE_PADDING);
        DisplayMetrics mat=context.getResources().getDisplayMetrics();
        screenWidth=mat.widthPixels;

        detector=new GestureDetector(context,new EventSummaryGestureListener());

        a.recycle();
    }

    private float lastFocusX;

    @Override
    public boolean onTouchEvent(MotionEvent ev){

        final int action = ev.getAction();
        final boolean pointerUp =
                (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP;
        final int skipIndex = pointerUp ? ev.getActionIndex() : -1;

        float sumX = 0;
        final int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) continue;
            sumX += ev.getX(i);
        }
        final int div = pointerUp ? count - 1 : count;
        final float focusX = sumX / div;

        switch(action){
            case MotionEvent.ACTION_DOWN:
                lastFocusX=focusX;
                break;
            case MotionEvent.ACTION_MOVE:
                if(focusX-lastFocusX>10){
                    getRootView().playSoundEffect(SoundEffectConstants.CLICK);
                    checkIn();
                    return false;
                }
        }

        detector.onTouchEvent(ev);
        return true;
    }

    private Bitmap getScaledBitmap(Bitmap src, int height){
        if(src==null)
            return null;
        else{
            int imageH=src.getHeight();
            int imageW=src.getWidth();
            int newH=height;
            int newW=(int)(height*imageW/(double) imageH);
            return Bitmap.createScaledBitmap(src,newW,newH,false);
        }
    }


    @Override
    public void onDraw(Canvas canvas){

        if(event!=null){
            Bitmap image=null;

            if(event.getIcon()!=null){
                image=getScaledBitmap(event.getIcon(),HEIGHT-IMAGE_PADDING);
            }else{
                image=defaultImage;
            }

            int newW=image.getWidth()+IMAGE_PADDING;

            //draws border
            Paint borderRect1Paint=new Paint();
            borderRect1Paint.setColor(Color.parseColor("#cfcfcf"));
            canvas.drawRect(0,0,screenWidth,HEIGHT,borderRect1Paint);

            Paint borderRect2Paint=new Paint();
            borderRect2Paint.setColor(Color.WHITE);
            canvas.drawRect(borderWidth,borderWidth/2,screenWidth-borderWidth,HEIGHT-borderWidth/2,borderRect2Paint);

            //move the card to the right
            if(this.leftPadding>0){
                Paint leftPaddingRectPaint=new Paint();
                leftPaddingRectPaint.setColor(Color.parseColor("#cfcfcf"));
                canvas.drawRect(0.0f,0.0f,(float) leftPadding,(float) HEIGHT, leftPaddingRectPaint);
            }
            canvas.drawBitmap(image,leftPadding+IMAGE_PADDING/2,IMAGE_PADDING/2,new Paint());

            //draw the title
            TextPaint titlePaint=new TextPaint();
            titlePaint.setTextSize(TITLE_TEXT_SIZE);
            titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
            titlePaint.setUnderlineText(true);
            titlePaint.setColor(Color.parseColor("#5B5B5B"));
            if(event.getName().length()>(screenWidth-newW)/TITLE_TEXT_SIZE*2){
                canvas.drawText(event.getName().substring(0,(int) ((screenWidth-newW)/TITLE_TEXT_SIZE*1.8))+"...",leftPadding+newW+10,35, titlePaint);
            }else{
                canvas.drawText(event.getName(),leftPadding+newW+10,30, titlePaint);
            }

            //draw the location
            List<String> locationText= GeneralUtils.wrapText(event.getLocation().getName(),(screenWidth-newW)/LOCATION_TEXT_SIZE*2,2);
            int i=0;
            for(String text:locationText){
                TextPaint locationPaint=new TextPaint();
                locationPaint.setTextSize(LOCATION_TEXT_SIZE);
                locationPaint.setTypeface(Typeface.DEFAULT_BOLD);
                locationPaint.setColor(Color.parseColor("#5B5B5B"));
                canvas.drawText(text, leftPadding+newW + 10, 30 + TITLE_TEXT_SIZE + 10+(LOCATION_TEXT_SIZE+10)*i, locationPaint);
                i++;
            }

            //draw the date
            TextPaint datePaint=new TextPaint();
            datePaint.setTextSize(DATE_TEXT_SIZE);
            datePaint.setTypeface(Typeface.DEFAULT_BOLD);
            datePaint.setColor(Color.parseColor("#5B5B5B"));
            canvas.drawText(Convertors.toString(event.getStartTime(),"EEE, dd MMM - h:mm a", "EEE, dd MMM"),
                    leftPadding+newW+10,30 + TITLE_TEXT_SIZE + 10+(LOCATION_TEXT_SIZE+10)*2, datePaint);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minW = getSuggestedMinimumWidth()- (getPaddingLeft() + getPaddingRight());
        int w = resolveSizeAndState(minW, widthMeasureSpec, 1);

        int minH = HEIGHT;
        int h = resolveSizeAndState(minH, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    public IEvent getEvent() {
        return event;
    }

    public void setEvent(IEvent event) {
        this.event = event;

        IEvent checkedInEvent=GlobalParameters.getInstance().getCheckedInEvent();

        if(checkedInEvent!=null){
            if(checkedInEvent.getId()!=null && checkedInEvent.getId().equals(event.getId())){
                leftPadding=SELECTED_LEFT_PADDING;
            }
        }

        if(event.getIcon()==null){
            PictureDownloader downloader=new PictureDownloader(this);
            downloader.execute(event.getIconPath());
        }
    }

    @Override
    public void onPictureDownload(Bitmap bitmap) {
        this.event.setIcon(bitmap);
        invalidate();
        requestLayout();
    }

    public void resetToNotCheckedIn(){
        if(this.leftPadding>0){
            this.leftPadding=0;
            invalidate();
            requestLayout();
        }
    }

    public void setToCheckedIn(){
        this.leftPadding=SELECTED_LEFT_PADDING;
        invalidate();
        requestLayout();
    }


    public void checkIn(){
        GlobalParameters.getInstance().setCheckedInEvent(this.event);
        eventCheckinFragment.resetViewToNotCheckedIn();
    }

    public void startEventActivity(){
        Intent intent=new Intent(getContext(), EventActivity.class);
        intent.putExtra("event", event);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }


    public static class EventSummaryListAdaptor extends BaseAdapter{

        private final List<IEvent> events;
        private final Map<Integer, EventSummaryView> eventSummariesMap;
        private final Context context;
        private final EventCheckinFragment eventCheckinFragment;

        public EventSummaryListAdaptor(List<IEvent> events, Context context, EventCheckinFragment eventCheckinFragment){
            this.eventCheckinFragment=eventCheckinFragment;
            this.eventSummariesMap =new HashMap<Integer, EventSummaryView>();
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
            EventSummaryView existingEventView= eventSummariesMap.get(i);
            if(existingEventView!=null && existingEventView.event==event){
                return existingEventView;
            }else{
                EventSummaryView eventSummary=new EventSummaryView(context,null,0, eventCheckinFragment);
                eventSummary.setEvent(event);
                eventSummariesMap.put(i, eventSummary);
                return eventSummary;
            }
        }
    }


    private class EventSummaryGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 20;
        private static final int SWIPE_MAX_OFF_PATH = HEIGHT;

        @Override
        public boolean onDown(MotionEvent e){
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            startEventActivity();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e){
            //checkIn();
            return true;
        }
    }
}
