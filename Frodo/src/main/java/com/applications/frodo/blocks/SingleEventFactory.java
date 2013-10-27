package com.applications.frodo.blocks;

import android.os.Parcel;
import android.os.Parcelable;

import com.applications.frodo.blocks.IEvent;
import com.applications.frodo.blocks.IEventFactory;

/**
 * Created by siddharth on 24/10/13.
 */
public class SingleEventFactory implements IEventFactory {

    private IEvent event;
    public SingleEventFactory(IEvent event){
        this.event=event;
    }

    public SingleEventFactory(Parcel in) {
        this.event=in.readParcelable(null);
    }

    @Override
    public IEvent getEvent() {
        return event;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.event,0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SingleEventFactory createFromParcel(Parcel in) {
            return new SingleEventFactory(in);
        }

        public SingleEventFactory[] newArray(int size) {
            return new SingleEventFactory[size];
        }
    };
}
