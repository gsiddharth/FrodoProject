package com.applications.frodo.com.applications.frodo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


/**
 * Created by siddharth on 20/09/13.
 */
public class LoginButton extends Button{

    public LoginButton(Context context) {
        super(context);
        finishInit();
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        finishInit();
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        finishInit();
    }

    private void finishInit(){
    }

    private class LoginClickListener implements OnClickListener {

        @Override
        public void onClick(View v){
            //TODO: Load the new activity for handling login by user name and password

        }

    }
}
