package com.hippotec.mapsapplication.model;

import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.R;

import java.util.Date;

/**
 * Created by Avishay Peretz on 06/04/2017.
 */

public class EventAndPeopleObject {

    private EntityType mEntityType;
    private Object mObject; // EventDetails \ People
    //private int position;

    public EventAndPeopleObject(EventInfo mObject /*, int position*/) {
        this.mEntityType = EntityType.EVENT;
        this.mObject = mObject;
        //this.position = position;
    }

    public EventAndPeopleObject(People mObject /*, int position*/) {
        this.mEntityType = EntityType.PEOPLE;
        this.mObject = mObject;
        //this.position = position;
    }

    public EntityType getType() {
        return mEntityType;
    }

    public EventAndPeopleObject setType(EntityType mEntityType) {
        this.mEntityType = mEntityType;
        return this;
    }

    public Object getObject() {
        return mObject;
    }

    public EventAndPeopleObject setObject(Object mObject) {
        this.mObject = mObject;
        return this;
    }

    //public int getPosition() {
    //    return position;
    //}

    //public EventAndPeopleObject setPosition(int position) {
    //    this.position = position;
    //    return this;
    //}

    public Date getEventStartTime() {
        Date date = null;
        switch (mEntityType) {
            case EVENT:
                date = ((EventInfo) mObject).getDetails().getEventTime().getStart();
                break;

        }
        return date;
    }

    public Date getEventEndTime() {
        Date date = null;
        switch (mEntityType) {
            case EVENT:
                date = ((EventInfo) mObject).getDetails().getEventTime().getEnd();
                break;

        }
        return date;
    }

    public String getId() {
        String id = "";
        switch (mEntityType) {
            case EVENT:
                id = ((EventInfo) mObject).getDetails().getId();
                break;

        }
        return id;
    }

    public LatLng getLocation() {
        switch (mEntityType) {
            case EVENT:
                return ((EventInfo) mObject).getDetails().getEventLocation();
            case PEOPLE:
                return ((People) mObject).getPeopleLocation();
        }
        return null;
    }

    public int getMarkerIcon() {
        switch (mEntityType) {
            case EVENT:
                return R.drawable.icn_flag;
            case PEOPLE:
                return R.drawable.icn_orange_pin;
        }
        return R.drawable.icn_orange_pin;
    }




}
