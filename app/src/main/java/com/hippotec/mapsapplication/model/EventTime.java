package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Avishay Peretz on 04/04/2017.
 */

public class EventTime implements Parcelable {
    private long startSecsSince1970;
    private long endSecsSince1970;

    public EventTime(long start, long end) {
        this.startSecsSince1970 = start;
        this.endSecsSince1970 = end;
    }

    public EventTime(Date start, Date end) {
        this.startSecsSince1970 = start.getTime() / 1000;
        this.endSecsSince1970 = end.getTime() / 1000;
    }

    public long getStartSecsSince1970() { return startSecsSince1970; }
    public long getEndSecsSince1970() { return endSecsSince1970; }
    public Date getStart() {
        return new Date(startSecsSince1970 * 1000);
    }
    public Date getEnd() {
        return new Date(endSecsSince1970 * 1000);
    }

//    public JSONObject toJson() {
//        JSONObject json = new JSONObject();
//        try {
//            json.put(Const.KEY_START, startSecsSince1970);
//            json.put(Const.KEY_END, endSecsSince1970);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.startSecsSince1970);
        dest.writeLong(this.endSecsSince1970);
    }

    protected EventTime(Parcel in) {
        this.startSecsSince1970 = in.readLong();
        this.endSecsSince1970 = in.readLong();
    }

    public static final Parcelable.Creator<EventTime> CREATOR = new Parcelable.Creator<EventTime>() {
        @Override
        public EventTime createFromParcel(Parcel source) {
            return new EventTime(source);
        }

        @Override
        public EventTime[] newArray(int size) {
            return new EventTime[size];
        }
    };
}
