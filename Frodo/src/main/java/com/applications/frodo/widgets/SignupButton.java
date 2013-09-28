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
        setOnClickListener(new SignupClickListener());

    }

    private class SignupClickListener implements OnClickListener {

        private String ip;
        private int port;
        private String query;

        public SignupClickListener(){
            this.ip = getResources().getString(R.string.host_ip);
            try{
                this.port=Integer.parseInt(getResources().getString(R.string.host_port));
            }catch(Exception e){
                this.port=80;
            }

            this.query=getResources().getString(R.string.signup_query);
        }

        @Override
        public void onClick(View v){
            HttpClient client=new DefaultHttpClient();



            String query=this.query.replaceFirst("[?]username", "");
            HttpGet request= new HttpGet("http://"+ip+":"+port+"/"+query);

            try{
                HttpResponse response = client.execute(request);
                StatusLine status = response.getStatusLine();

                if (status.getStatusCode() != 200) {
                    throw new IOException("Invalid response from server: " + status.toString());
                }

                // Pull content stream from response
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                ByteArrayOutputStream content = new ByteArrayOutputStream();

                // Read response into a buffered stream
                int readBytes = 0;
                byte[] sBuffer = new byte[512];
                while ((readBytes = inputStream.read(sBuffer)) != -1) {
                    content.write(sBuffer, 0, readBytes);
                }

                // Return result from buffered stream
                String dataAsString = new String(content.toByteArray());


            }catch(Exception e){

            }

        }

    }
}
