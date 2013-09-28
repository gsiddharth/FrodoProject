package com.applications.frodo.networking;

/**
 * Created by siddharth on 28/09/13.
 */
public class BackendRequestParameters {
    private static BackendRequestParameters ourInstance = new BackendRequestParameters();

    private String ip;
    private int port;
    private String singupQuery;
    private String getUserDataQuery;
    private int timeout;

    public static BackendRequestParameters getInstance() {
        return ourInstance;
    }

    private BackendRequestParameters() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSingupQuery() {
        return singupQuery;
    }

    public void setSingupQuery(String singupQuery) {
        this.singupQuery = singupQuery;
    }

    public String getGetUserDataQuery() {
        return getUserDataQuery;
    }

    public void setGetUserDataQuery(String getUserDataQuery) {
        this.getUserDataQuery = getUserDataQuery;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
