package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yariv on 06/02/2018.
 */

public class EventStats implements Parcelable {
    private int maleCount;
    private int femaleCount;
    private int totalCount;
    private EventAge eventAge;
    //@SerializedName("like")
    private int eventLike;
    //@SerializedName("disslike")
    private int disslike;

    public EventStats(int maleCount, int femaleCount, int totalCount,
                 EventAge eventAge, int eventLike, int disslike) {
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.totalCount = totalCount;
        this.eventAge = eventAge;
        this.eventLike = eventLike;
        this.disslike = disslike;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getEventLike() {
        return eventLike;
    }

    public void setEventLike(int eventLike) {
        this.eventLike = eventLike;
    }

    public int getDisslike() {
        return disslike;
    }

    public void setDisslike(int disslike) {
        this.disslike = disslike;
    }

    public EventAge getEventAge() {
        return eventAge;
    }

    public void setEventAge(EventAge eventAge) {
        this.eventAge = eventAge;
    }

    public String getMalePercent() {
        if (totalCount > 0)
            return (maleCount * 100)/ totalCount + "%";
        return "0%";
    }

    public String getFemalePercent() {
        if (totalCount > 0)
            return (femaleCount * 100) / totalCount + "%";
        return "0%";
    }

    public String getTotalAgeLine(String totalStr, String ageStr) {
        int minAge =0;
        int maxAge =0;
        if (eventAge != null)
        {
            minAge = eventAge.getMin();

            maxAge = eventAge.getMax();
        }
        String total = totalStr + ": " + totalCount;

        String age = "";
        if (totalCount != 0) {
            age += " | " + ageStr + ": ";
            if (minAge == 0 && maxAge == 0) {
                age += "?";
            } else if (minAge == 0 || minAge == maxAge) {
                age += maxAge;
            } else {
                age += minAge + "-" + maxAge;
            }
        }

        return total + age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maleCount);
        dest.writeInt(femaleCount);
        dest.writeInt(totalCount);
        dest.writeParcelable(eventAge, flags);
        dest.writeInt(eventLike);
        dest.writeInt(disslike);
    }

    protected EventStats(Parcel in) {
        this.maleCount = in.readInt();
        this.femaleCount = in.readInt();
        this.totalCount = in.readInt();
        this.eventAge = in.readParcelable(EventAge.class.getClassLoader());
        this.eventLike = in.readInt();
        this.disslike = in.readInt();
    }

    public static final Parcelable.Creator<EventStats> CREATOR = new Parcelable.Creator<EventStats>() {
        @Override
        public EventStats createFromParcel(Parcel source) {
            return new EventStats(source);
        }

        @Override
        public EventStats[] newArray(int size) {
            return new EventStats[size];
        }
    };
}
