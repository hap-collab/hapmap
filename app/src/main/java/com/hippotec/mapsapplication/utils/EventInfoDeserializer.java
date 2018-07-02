package com.hippotec.mapsapplication.utils;

import com.hippotec.mapsapplication.api.JsonKeys;
import com.hippotec.mapsapplication.model.EventDetails;
import com.hippotec.mapsapplication.model.EventAge;
import com.hippotec.mapsapplication.model.EventInfo;
import com.hippotec.mapsapplication.model.EventStats;
import com.hippotec.mapsapplication.model.EventUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yariv on 06/02/2018.
 */

public class EventInfoDeserializer {
    private static final String KEY_USER_LIKED = "userLiked";
    private static final String KEY_USER_DISLIKED = "userDisliked";

    private static final String KEY_MALE_COUNT = "maleCount";
    private static final String KEY_FEMALE_COUNT = "femaleCount";
    private static final String KEY_TOTAL_COUNT = "totalCount";
    private static final String KEY_COUNT_LIKE = "like";
    private static final String KEY_COUNT_DISSLIKE = "disslike";
    private static final String KEY_AGE = "age";
    private static final String KEY_AGE_MIN = "min";
    private static final String KEY_AGE_MAX = "max";

    public static EventInfo fromJson(int version, JSONObject eventJson) throws JSONException {
        EventDetails eventDetails = EventSerializer.fromJson(version, eventJson);

        final JSONObject statsJson = eventJson.getJSONObject(JsonKeys.KEY_STATS);
        int maleCount = statsJson.optInt(KEY_MALE_COUNT, 0);
        int femaleCount = statsJson.optInt(KEY_FEMALE_COUNT, 0);
        int totalCount = statsJson.optInt(KEY_TOTAL_COUNT, 0);
        int eventLike = statsJson.optInt(KEY_COUNT_LIKE, 0);
        int disslike = statsJson.optInt(KEY_COUNT_DISSLIKE, 0);
        JSONObject ageJson = statsJson.optJSONObject(KEY_AGE);
        int minAge = (ageJson == null) ? -1 : ageJson.optInt(KEY_AGE_MIN, -1);
        int maxAge = (ageJson == null) ? -1 : ageJson.optInt(KEY_AGE_MAX, 999);
        EventStats stats = new EventStats(maleCount, femaleCount, totalCount, new EventAge(minAge, maxAge), eventLike, disslike);

        final JSONObject userInfoJson = eventJson.getJSONObject(JsonKeys.KEY_USER_INFO);
        boolean userLiked = userInfoJson.optBoolean(KEY_USER_LIKED, false);
        boolean userDisiked = userInfoJson.optBoolean(KEY_USER_DISLIKED, false);
        EventUserInfo userInfo = new EventUserInfo(userLiked, userDisiked);

        return new EventInfo(eventDetails, stats, userInfo);
    }
}
