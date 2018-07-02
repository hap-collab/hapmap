package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Avishay Peretz on 12/04/2017.
 */

public class Comment implements Parcelable {
    private String commentId;
    private String uid;
    private String eventId;
    private String comment;
    private String image;

    public String getId() {
        return commentId;
    }

    public String getUid() {
        return uid;
    }

    public String getEventId() {
        return eventId;
    }

    public String getComment() {
        return comment;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commentId);
        dest.writeString(this.uid);
        dest.writeString(this.eventId);
        dest.writeString(this.comment);
        dest.writeString(this.image);

    }

    public Comment(String commentId, String uid, String eventId, String comment, String image) {
        this.commentId = commentId;
        this.uid = uid;
        this.eventId = eventId;
        this.comment = comment;
        this.image = image;
    }

    //public Comment() {
    //}

    protected Comment(Parcel in) {
        this.commentId = in.readString();
        this.uid = in.readString();
        this.eventId = in.readString();
        this.comment = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
