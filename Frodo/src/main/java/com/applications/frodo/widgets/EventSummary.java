package com.applications.frodo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.applications.frodo.R;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.networking.PictureDownloader;

import java.util.List;

/**
 * Created by siddharth on 08/10/13.
 */
public class EventSummary extends View implements PictureDownloader.PictureDownloaderListener{

    private IEvent event;

    public EventSummary(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        TypedArray a= context.getTheme().obtainStyledAttributes(attrs, R.styleable.EventSummary,0,0);
        a.recycle();
    }


    @Override
    public void onDraw(Canvas canvas){
        int width=this.getWidth();
        int height=this.getHeight();

        int photoheight=(height*3)/4;
        int margin=height/8;

        if(event!=null){
            if(event.getImage()!=null){
                canvas.drawBitmap(event.getImage(),0,0,new Paint());
                canvas.drawText(event.getName(),event.getImage().getWidth()+10,10, new Paint());
            }else{
                canvas.drawText(event.getName(),10,10, new Paint());
            }
            ///canvas.drawText(summary,height/8+height/8+this.bitmap.getWidth(),5*height/8, new Paint());
            setMeasuredDimension(100,100);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getLayoutParams().width- (getPaddingLeft() + getPaddingRight());
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        int minh = 150;
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    public IEvent getEvent() {
        return event;
    }

    public void setEvent(IEvent event) {
        this.event = event;
        PictureDownloader.getBitmap(event.getImagePath(), this);
        invalidate();
        requestLayout();
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
