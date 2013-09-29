package com.applications.frodo.widgets;

/**
 * Created by siddharth on 20/09/13.
 */

import android.view.View;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


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

        }

    }
}
