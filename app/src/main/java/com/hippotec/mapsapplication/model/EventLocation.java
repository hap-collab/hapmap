package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Avishay Peretz on 04/04/2017.
 */

/*
public class EventLocation implements Parcelable {
    private LatLng coordinates;

    @SerializedName("type")
    private String type;

    public EventLocation() {
    }

    public EventLocation(double latitude, double longitude) {
        this.coordinates = new LatLng(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public JSONObject toJson() {
        JSONObject json=new JSONObject();
        try {
            json.put(Const.KEY_LATITUDE, latitude);
            json.put(Const.KEY_LONGITUDE, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.type);
        dest.writeDoubleArray(this.coordinates);
    }

    protected EventLocation(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.type = in.readString();
        this.coordinates = in.createDoubleArray();
    }

    public static final Parcelable.Creator<EventLocation> CREATOR = new Parcelable.Creator<EventLocation>() {
        @Override
        public EventLocation createFromParcel(Parcel source) {
            return new EventLocation(source);
        }

        @Override
        public EventLocation[] newArray(int size) {
            return new EventLocation[size];
        }
    };
}
*/
