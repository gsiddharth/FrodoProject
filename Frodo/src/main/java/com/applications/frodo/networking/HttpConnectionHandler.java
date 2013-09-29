package com.applications.frodo.networking;

import com.applications.frodo.GlobalParameters;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by siddharth on 29/09/13.
 */
public class HttpConnectionHandler {

    private static HttpConnectionHandler instance=new HttpConnectionHandler();
    private final String ip;
    private final int port;
    private final HttpParams httpParams=new BasicHttpParams();
    private HttpClient client;

    private HttpConnectionHandler(){
        this.ip= BackendRequestParameters.getInstance().getIp();
        this.port=BackendRequestParameters.getInstance().getPort();
        HttpConnectionParams.setConnectionTimeout(httpParams, BackendRequestParameters.getInstance().getTimeout());
        client=new DefaultHttpClient(httpParams);
    }

    public static HttpConnectionHandler getInstance(){
        return instance;
    }

    public String sendRequest(String query){
        HttpGet request= new HttpGet("http://"+ip+":"+port+"/"+ query);

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
            return dataAsString;
        }catch (Exception e){
            return null;
        }
    }


}

