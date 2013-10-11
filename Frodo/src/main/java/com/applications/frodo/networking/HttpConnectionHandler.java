package com.applications.frodo.networking;

import android.os.AsyncTask;
import android.util.Log;

import com.applications.frodo.utils.Convertors;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;


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

    private HttpConnectionHandler(){
        this.ip= BackendRequestParameters.getInstance().getIp();
        this.port=BackendRequestParameters.getInstance().getPort();
        HttpConnectionParams.setConnectionTimeout(httpParams, BackendRequestParameters.getInstance().getTimeout());
        client=new DefaultHttpClient(httpParams);
    }

    public static HttpConnectionHandler getInstance(){
        return instance;
    }


    public void sendJson(String query,JSONObject json, ReponseCallBack responseHandler){
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
            RequestExecutor executor=new RequestExecutor(responseHandler);
            executor.execute(new RequestResponse(httpPost,responseHandler));

        }catch (Exception e){
            Log.e(TAG,"Connection Error",e);
        }
    }

    public void sendGetRequest(String query, ReponseCallBack responseHandler){
        HttpGet httpGet= new HttpGet("http://"+ip+":"+port+"/"+ query);

        try{
            RequestExecutor executor=new RequestExecutor(responseHandler);
            executor.execute(new RequestResponse(httpGet,responseHandler));
        }catch (Exception e){
            Log.e(TAG,"Connection Error",e);
        }
    }


    private class RequestExecutor extends AsyncTask<RequestResponse, Integer, HttpResponse[]> {

        private String TAG=RequestExecutor.class.toString();

        private ReponseCallBack responseHandler;

        public RequestExecutor(ReponseCallBack responseHandler){
            this.responseHandler=responseHandler;
        }

        @Override
        protected HttpResponse[] doInBackground(RequestResponse... requestResponses) {

            HttpResponse[] result=new HttpResponse[requestResponses.length];
            for(int i=0;i<requestResponses.length;i++){
                RequestResponse reqRes=requestResponses[i];
                if(reqRes.postRequest!=null){
                    try{
                        org.apache.http.HttpResponse response=client.execute(reqRes.postRequest);
                        HttpResponse hResponse=new HttpResponse(response.getStatusLine().getStatusCode(), Convertors.getString(response));
                        result[i]=hResponse;
                    }catch(Exception e){
                        Log.e(TAG,"Client request error "+reqRes.getRequest.getURI(),e);
                    }
                }else if(reqRes.getRequest!=null){
                    try{
                        org.apache.http.HttpResponse response=client.execute(reqRes.postRequest);
                        HttpResponse hResponse=new HttpResponse(response.getStatusLine().getStatusCode(), Convertors.getString(response));
                        result[i]=hResponse;
                    }catch(Exception e){
                        Log.e(TAG,"Client request error "+reqRes.getRequest.getURI(),e);
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(HttpResponse[] result){
            if(result!=null){
                for(HttpResponse response:result){
                    try{
                        responseHandler.onHttpResponse(response.getStatus(),response.getResult());
                    }catch(Exception e){
                        Log.e(TAG,"Error while handling response",e);
                    }
                }
            }
        }
    }

    private class RequestResponse {

        private HttpPost postRequest=null;
        private HttpGet getRequest=null;
        private ReponseCallBack responseHandler;

        public RequestResponse(HttpPost post, ReponseCallBack responseHandler){
            this.postRequest=post;
            this.responseHandler=responseHandler;
        }

        public RequestResponse(HttpGet get, ReponseCallBack responseHandler){
            this.getRequest=get;
            this.responseHandler=responseHandler;
        }
    }

    public static interface ReponseCallBack{
        public void onHttpResponse(int status, String response);
    }

    private class HttpResponse{
        private int status;
        private String result;

        public HttpResponse(int status, String result){
            this.status=status;
            this.result=result;
        }


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}


