package com.applications.frodo.networking;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by siddharth on 29/09/13.
 */
public class HttpConnectionHandler {

    private static final String TAG=HttpConnectionHandler.class.getName();
    private static HttpConnectionHandler instance=new HttpConnectionHandler();
    private final String ip;
    private final int port;
    private final HttpParams httpParams=new BasicHttpParams();
    private HttpClient client;
    private Queue<RequestResponse> queue=new ConcurrentLinkedQueue<RequestResponse>();
    private Executor executor=new ThreadPoolExecutor(10,20,100000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20));

    private HttpConnectionHandler(){
        Log.i(TAG, "Initializing==> IP="+BackendRequestParameters.getInstance().getIp()+
                ";Port=" +BackendRequestParameters.getInstance().getPort());
        this.ip= BackendRequestParameters.getInstance().getIp();
        this.port=BackendRequestParameters.getInstance().getPort();
        HttpConnectionParams.setConnectionTimeout(httpParams, BackendRequestParameters.getInstance().getTimeout());
        client=new DefaultHttpClient(httpParams);
    }

    public static HttpConnectionHandler getInstance(){
        return instance;
    }


    public void sendJson(String query,JSONObject json, ResponseHandler<String> responseHandler){
        HttpPost httpPost= new HttpPost("http://"+ip+":"+port+"/"+ query);

        try{
            if(json==null){
                json=new JSONObject();
            }
            StringEntity se = new StringEntity(json.toString(), "UTF8");
            httpPost.setHeader("accept", "application/json");
            httpPost.setHeader("content-type", "application/json");
            se.setContentType("application/json");
            httpPost.setEntity(se);
            executor.execute(new RequestExecutor(new RequestResponse(httpPost,responseHandler)));

        }catch (Exception e){
            Log.e(TAG,"Connection Error",e);
        }
    }

    public void sendGetRequest(String query, ResponseHandler<String> responseHandler){
        HttpGet request= new HttpGet("http://"+ip+":"+port+"/"+ query);

        try{
            queue.add(new RequestResponse(request,responseHandler));
        }catch (Exception e){
            Log.e(TAG,"Connection Error",e);
        }
    }


    private class RequestExecutor implements Runnable{

        private RequestResponse reqRes;

        public RequestExecutor(RequestResponse reqRes){
            this.reqRes=reqRes;
        }

        public void run(){
            if(reqRes.postRequest!=null){
                try{
                    client.execute(reqRes.postRequest,reqRes.responseHandler);
                }catch(Exception e){

                }
            }else if(reqRes.getRequest!=null){
                try{
                    client.execute(reqRes.getRequest,reqRes.responseHandler);
                }catch(Exception e){

                }
            }

        }
    }

    private class RequestResponse {

        private HttpPost postRequest=null;
        private HttpGet getRequest=null;
        private ResponseHandler<String> responseHandler;

        public RequestResponse(HttpPost post, ResponseHandler<String> responseHandler){
            this.postRequest=post;
            this.responseHandler=responseHandler;
        }

        public RequestResponse(HttpGet get, ResponseHandler<String> responseHandler){
            this.getRequest=get;
            this.responseHandler=responseHandler;
        }
    }

}


