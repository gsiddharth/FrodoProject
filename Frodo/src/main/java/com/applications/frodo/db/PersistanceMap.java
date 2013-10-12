package com.applications.frodo.db;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by siddharth on 12/10/13.
 */
public class PersistanceMap {

    private static PersistanceMap instance=new PersistanceMap();
    private static String TAG=PersistanceMap.class.toString();
    private SharedPreferences sharedPreferences;
    private boolean initialized =false;

    private PersistanceMap(){
    }

    public static PersistanceMap getInstance(){
        return instance;
    }

    public void init(SharedPreferences sharedPreferences){
        if(sharedPreferences!=null){
            this.sharedPreferences=sharedPreferences;
            this.initialized=true;
        }
    }

    public void putString(String key, String value) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void putStringSet(String key, Set<String> values) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }


    public void putInt(String key, int value) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public void putLong(String key, long value) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void remove(String key) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor=editor.remove(key);
        editor.commit();
    }

    public void clear() throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor=editor.clear();
        editor.commit();
    }


    public Map<String, ?> getAll() throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.getAll();
    }

    public String getString(String key, String defValue) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.getString(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValues) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.getStringSet(key, defValues);
    }

    public int getInt(String key, int defValue) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.getLong(key, defValue);
    }


    public float getFloat(String key, float defValue) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.getBoolean(key, defValue);
    }

    public boolean contains(String key) throws PersistanceMapUninitializedException {
        if(!initialized)
            throw new PersistanceMapUninitializedException();
        return sharedPreferences.contains(key);
    }


    public static class PersistanceMapUninitializedException extends Exception{
        public PersistanceMapUninitializedException(){
            super("PersistanceMap.init function has not been called or SharedPreferences passed to the function was invalid");
        }
    }
}
