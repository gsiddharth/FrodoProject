package com.applications.frodo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.networking.PictureDownloader;
import com.applications.frodo.utils.Convertors;
import com.applications.frodo.utils.GeneralUtils;

import java.util.List;

/**
 * The view of the event in the event list box
 * Created by siddharth on 08/10/13.
 */
public class EventSummary extends View implements PictureDownloader.PictureDownloaderListener{

    private IEvent event;

    private static final int HEIGHT=150;
    private static final int TITLE_TEXT_SIZE =28;
    private static final int LOCATION_TEXT_SIZE =22;
    private static final int DATE_TEXT_SIZE =22;

    private Bitmap defaultImage=null;

    public EventSummary(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        TypedArray a= context.getTheme().obtainStyledAttributes(attrs, R.styleable.EventSummary,0,0);
        defaultImage= BitmapFactory.decodeResource(getResources(),R.drawable.no_image_icon);
        defaultImage=getScaledBitmap(defaultImage, HEIGHT);
        a.recycle();
    }


    private Bitmap getScaledBitmap(Bitmap src, int height){
        if(src==null)
            return null;
        else{
            int imageH=src.getHeight();
            int imageW=src.getWidth();
            int newH=HEIGHT;
            int newW=(int)(HEIGHT*imageW/(double) imageH);
            return Bitmap.createScaledBitmap(src,newW,newH,false);
        }
    }


    @Override
    public void onDraw(Canvas canvas){

        if(event!=null){
            Bitmap image=null;

            if(event.getImage()!=null){
                image=getScaledBitmap(event.getImage(),HEIGHT);
            }else{
                image=defaultImage;
            }

            int newW=image.getWidth();
            canvas.drawBitmap(image,0,0,new Paint());

            TextPaint titlePaint=new TextPaint();
            titlePaint.setTextSize(TITLE_TEXT_SIZE);
            titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText(event.getName(),newW+10,30, titlePaint);

            System.out.println("====>>>>>"+canvas.getMaximumBitmapWidth());

            List<String> locationText= GeneralUtils.wrapText(event.getLocation().getName(),getLayoutParams().width/LOCATION_TEXT_SIZE,2);

            int i=0;
            for(String text:locationText){
                TextPaint locationPaint=new TextPaint();
                locationPaint.setTextSize(LOCATION_TEXT_SIZE);
                locationPaint.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText(text, newW + 10, 30 + TITLE_TEXT_SIZE + 20+(LOCATION_TEXT_SIZE+10)*i, locationPaint);
            }

            TextPaint datePaint=new TextPaint();

            datePaint.setTextSize(DATE_TEXT_SIZE);
            datePaint.setTypeface(Typeface.DEFAULT_BOLD);

            setMeasuredDimension(100, 100);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minW = getLayoutParams().width- (getPaddingLeft() + getPaddingRight());
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
        PictureDownloader downloader=new PictureDownloader(this);
        downloader.execute(event.getImagePath());
    }

    @Override
    public void onPictureDownload(Bitmap bitmap) {
        this.event.setImage(bitmap);
        invalidate();
        requestLayout();
    }


    public static class EventSummaryListAdaptor extends BaseAdapter{

        private List<IEvent> events;
        private Context context;

        public EventSummaryListAdaptor(List<IEvent> events, Context context){
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
            EventSummary eventSummary=new EventSummary(context,null,0);
            eventSummary.setEvent(event);
            return eventSummary;
        }
    }
}
