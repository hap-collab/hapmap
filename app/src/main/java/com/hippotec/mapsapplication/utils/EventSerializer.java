package com.hippotec.mapsapplication.utils;

import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.model.EventDetails;
import com.hippotec.mapsapplication.model.EventTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by yariv on 06/02/2018.
 */

public class EventSerializer {
    private static final String TAG = "EventSerializer";
    private static final String KEY_VERSION = "v";
    private static int CUR_VERSION = 1;

    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "desc";
    private static final String KEY_PLACE = "place";
    //String KEY_TIME = "time";
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";
    private static final String KEY_IMAGE_URL = "image";

    public static JSONObject toJsonWithVersion(EventDetails eventDetails) {
        try {
            JSONObject json = new JSONObject();
            json.put(KEY_VERSION, CUR_VERSION);
            toJson(json, CUR_VERSION, eventDetails);
            return json;
        }
        catch (JSONException e) {
            assert ("EventSerializer.toJsonWithVersion()" == null);
            Logger.e(TAG, "toJson()");
        }
        return null;
    }

    public static JSONObject toJson(EventDetails eventDetails) {
        return toJson(CUR_VERSION, eventDetails);
    }

    public static JSONObject toJson(int version, EventDetails eventDetails) {
        try {
            JSONObject json = new JSONObject();
            toJson(json, CUR_VERSION, eventDetails);
            return json;
        }
        catch (JSONException e) {
            assert ("EventSerializer.toJson()" == null);
            Logger.e(TAG, "toJson()");
        }
        return null;
    }

    public static void toJson(JSONObject json, EventDetails eventDetails) throws JSONException {
        toJson(json, CUR_VERSION, eventDetails);
    }

    private static void toJson(JSONObject json, int version, EventDetails eventDetails) throws JSONException {
        json.put(Const.KEY_LATITUDE, eventDetails.getEventLocation().latitude);
        json.put(Const.KEY_LONGITUDE, eventDetails.getEventLocation().longitude);
        json.put(Const.KEY_NAME, eventDetails.getName());
        json.put(KEY_DESCRIPTION, eventDetails.getDescription());
        json.put(KEY_PLACE, eventDetails.getPlace());
        json.put(KEY_START, eventDetails.getEventTime().getStartSecsSince1970());
        json.put(KEY_END, eventDetails.getEventTime().getEndSecsSince1970());
    }

    public static EventDetails fromJson(int version, JSONObject json) throws JSONException {
        String eventId = json.getString(Const.KEY_EVENT_ID);
        double lat = json.getDouble(Const.KEY_LATITUDE);
        double longi = json.getDouble(Const.KEY_LONGITUDE);
        String name = json.optString(Const.KEY_NAME);
        if (name == null) {
            name = "";
        }
        String desc = json.optString(KEY_DESCRIPTION);
        if (desc == null) {
            desc = "";
        }
        String place = json.optString(KEY_PLACE);
        if (place == null) {
            place = "";
        }
        long start = json.optLong(KEY_START, 0);
        long end = json.optLong(KEY_END, 0);
        if (start == 0 || end == 0) {
            Date now = new Date();
            if (start == 0) {
                Date timeStart = new Date(now.getTime());
                timeStart.setHours(0);
                timeStart.setMinutes(0);
                timeStart.setSeconds(0);
                start = timeStart.getTime() / 1000;
            }
            if (end == 0) {
                Date timeEnd = new Date(now.getTime());
                timeEnd.setHours(23);
                timeEnd.setMinutes(59);
                timeEnd.setSeconds(59);
                end = timeEnd.getTime() / 1000;
            }
        }

        String imageUrl = json.optString(KEY_IMAGE_URL);
        if (imageUrl == null) {
            imageUrl = "";
        }
        //boolean userDisiked = json.getBoolean(KEY_USER_DISLIKED);
        //boolean userLiked = json.getBoolean(KEY_USER_LIKED);
        //long maleCount = json.getLong(KEY_MALE_COUNT);
        //long femaleCount = json.getLong(KEY_FEMALE_COUNT);
        //long totalCount = json.getLong(KEY_TOTAL_COUNT);
        return new EventDetails(eventId, name, desc, place, new EventTime(start, end), new LatLng(lat, longi), imageUrl);//, maleCount, femaleCount, totalCound, )
    }
}
