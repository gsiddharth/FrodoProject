package com.applications.frodo.blocks;

import android.os.Parcel;
import android.os.Parcelable;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.IEventFactory;

/**
 * Created by siddharth on 24/10/13.
 */
public class CurrentEventFactory implements IEventFactory {

    public CurrentEventFactory() {

    }

    public CurrentEventFactory(Parcel in) {
    }

    @Override
    public IEvent getEvent() {
        return GlobalParameters.getInstance().getCheckedInEvent();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CurrentEventFactory createFromParcel(Parcel in) {
            return new CurrentEventFactory(in);
        }

        public CurrentEventFactory[] newArray(int size) {
            return new CurrentEventFactory[size];
        }
    };
}
