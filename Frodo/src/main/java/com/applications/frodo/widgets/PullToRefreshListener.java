package com.applications.frodo.widgets;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by siddharth on 15/10/13.
 */
public class PullToRefreshListener implements View.OnTouchListener {
    private float lastY;
    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        System.out.println(ev.getAction()+" "+lastY+" "+ev.getRawY());
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastY = ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float newY = ev.getRawY();
            boolean drag=((newY - lastY) > 0);
            lastY = newY;
            return drag;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            lastY = 0;
        }
        return false;
    }

}
