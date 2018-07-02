package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.utils.TimeUtils;

import java.util.Date;

/**
 * Created by Avishay Peretz on 04/04/2017.
 */

public class EventDetails implements Parcelable {
    private static final String TAG = "EventDetails";

    private String id;
    private String name;
    private String description;
    private String m_place;
    private EventTime eventTime;
    private LatLng eventLocation;

    //@SerializedName("age")
    private String image;

    public EventDetails(String id, String name, String description, String place,
                        EventTime eventTime, LatLng eventLocation,
                        String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.m_place = place;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.image = image;
    }

    public EventDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(EventTime eventTime) {
        this.eventTime = eventTime;
    }

    public LatLng getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(LatLng eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getPlace() {
        return m_place;
    }

    public void setPlace(String place) {
        this.m_place = place;
    }

    public String getImage() {
        return image;
    }

    public String getTimeRange() {
        Date startTime = eventTime.getStart();
        Date endTime = eventTime.getEnd();
        return (TimeUtils.getFixTimeStr(startTime.getHours(), startTime.getMinutes()) + " - " + TimeUtils.getFixTimeStr(endTime.getHours(), endTime.getMinutes()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(eventTime, flags);
        dest.writeParcelable(eventLocation, flags);
        dest.writeString(image);
    }

    protected EventDetails(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.eventTime = in.readParcelable(EventTime.class.getClassLoader());
        this.eventLocation = in.readParcelable(LatLng.class.getClassLoader());
        this.image = in.readString();
    }

    public static final Parcelable.Creator<EventDetails> CREATOR = new Parcelable.Creator<EventDetails>() {
        @Override
        public EventDetails createFromParcel(Parcel source) {
            return new EventDetails(source);
        }

        @Override
        public EventDetails[] newArray(int size) {
            return new EventDetails[size];
        }
    };
}