package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yariv on 06/02/2018.
 */

public class EventInfo implements Parcelable {
    private EventDetails details;
    private EventStats stats;
    private EventUserInfo userInfo;

    public EventInfo(EventDetails eventDetails, EventStats stats, EventUserInfo userInfo) {
        this.details = eventDetails;
        this.stats = stats;
        this.userInfo = userInfo;
    }

    public EventDetails getDetails() { return details; }
    public EventStats getStats() { return stats; }
    public EventUserInfo getUserInfo() { return userInfo; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(details, flags);
        dest.writeParcelable(stats, flags);
        dest.writeParcelable(userInfo, flags);
    }

    protected EventInfo(Parcel in) {
        this.details = in.readParcelable(EventDetails.class.getClassLoader());
        this.stats = in.readParcelable(EventStats.class.getClassLoader());
        this.userInfo = in.readParcelable(EventUserInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<EventInfo> CREATOR = new Parcelable.Creator<EventInfo>() {
        @Override
        public EventInfo createFromParcel(Parcel source) {
            return new EventInfo(source);
        }

        @Override
        public EventInfo[] newArray(int size) {
            return new EventInfo[size];
        }
    };
}
