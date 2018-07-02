package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yariv on 06/02/2018.
 */

public class EventUserInfo implements Parcelable {
    //@SerializedName("userLiked")
    private boolean userLiked;

    //@SerializedName("userDisliked")
    private boolean userDisliked;

    public EventUserInfo(boolean userLiked, boolean userDisliked) {
        this.userLiked = userLiked;
        this.userDisliked = userDisliked;

    }

    public boolean isUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public boolean isUserDisliked() {
        return userDisliked;
    }

    public void setUserDisliked(boolean userDisliked) {
        this.userDisliked = userDisliked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(userLiked ? 1 : 0));
        dest.writeByte((byte)(userDisliked ? 1 : 0));
    }

    protected EventUserInfo(Parcel in) {
        this.userLiked = in.readByte() != 0;
        this.userDisliked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<EventUserInfo> CREATOR = new Parcelable.Creator<EventUserInfo>() {
        @Override
        public EventUserInfo createFromParcel(Parcel source) {
            return new EventUserInfo(source);
        }

        @Override
        public EventUserInfo[] newArray(int size) {
            return new EventUserInfo[size];
        }
    };
}
