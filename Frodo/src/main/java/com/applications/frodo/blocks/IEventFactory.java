package com.applications.frodo.blocks;

import android.os.Parcelable;

/**
 * Created by siddharth on 24/10/13.
 */
public interface IEventFactory extends Parcelable{
    public IEvent getEvent();
}
