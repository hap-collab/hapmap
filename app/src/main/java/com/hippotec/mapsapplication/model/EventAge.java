package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Avishay Peretz on 04/04/2017.
 */

public class EventAge implements Parcelable {
    private int min;
    private int max;

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.min);
        dest.writeInt(this.max);
    }

    public EventAge(int min, int max) {
        this.min = min;
        this.max = max;
    }

    protected EventAge(Parcel in) {
        this.min = in.readInt();
        this.max = in.readInt();
    }

    public static final Parcelable.Creator<EventAge> CREATOR = new Parcelable.Creator<EventAge>() {
        @Override
        public EventAge createFromParcel(Parcel source) {
            return new EventAge(source);
        }

        @Override
        public EventAge[] newArray(int size) {
            return new EventAge[size];
        }
    };
}
