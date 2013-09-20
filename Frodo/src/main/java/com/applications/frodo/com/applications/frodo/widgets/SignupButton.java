package com.applications.frodo.com.applications.frodo.widgets;

/**
 * Created by siddharth on 20/09/13.
 */

import android.view.View;



import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by siddharth on 20/09/13.
 */
public class SignupButton extends Button{

    public SignupButton (Context context) {
        super(context);
        finishInit();
    }

    public SignupButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        finishInit();
    }

    public SignupButton (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        finishInit();
    }

    private void finishInit(){
    }

    private class SignupClickListener implements OnClickListener {

        @Override
        public void onClick(View v){
            //TODO: Load the new activity for handling signup by user name and password

        }

    }
}
