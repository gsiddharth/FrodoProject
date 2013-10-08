package com.applications.frodo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.View;

import com.applications.frodo.R;

/**
 * Created by siddharth on 08/10/13.
 */
public class EventSummary extends View {

    private String summary;
    private Bitmap bitmap;
    private String datetime;
    private String title;

    public EventSummary(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a= context.getTheme().obtainStyledAttributes(attrs, R.styleable.EventSummary,0,0);
        try{
            this.summary=a.getString(R.styleable.EventSummary_summary);
            this.datetime=a.getString(R.styleable.EventSummary_datetime);
            this.title=a.getString(R.styleable.EventSummary_title);
        }finally{
            a.recycle();
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        int width=this.getWidth();
        int height=this.getHeight();

        int photoheight=(height*3)/4;
        int margin=height/8;

        canvas.drawBitmap(this.bitmap,height/8,7*height/8,new Paint());
        canvas.drawText(title,height/8+height/8+this.bitmap.getWidth(),7*height/8, new Paint());
        canvas.drawText(summary,height/8+height/8+this.bitmap.getWidth(),5*height/8, new Paint());
    }



    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
        invalidate();
        requestLayout();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
        requestLayout();
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
        invalidate();
        requestLayout();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
