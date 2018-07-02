package com.hippotec.mapsapplication.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Avishay Peretz on 06/04/2017.
 */
public class People {
    private LatLng peopleLocation;
    private int count;

    public LatLng getPeopleLocation() {
        return peopleLocation;
    }

    public int getCount() {
        return count;
    }

    public double getLatitude() {
        if (peopleLocation != null)
            return peopleLocation.latitude;
        return 0;
    }

    public double getLongitude() {
        if (peopleLocation != null)
            return peopleLocation.longitude;
        return 0;
    }
}
