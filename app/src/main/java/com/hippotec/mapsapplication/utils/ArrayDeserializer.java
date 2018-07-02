package com.hippotec.mapsapplication.utils;
import com.hippotec.mapsapplication.model.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yariv on 07/02/2018.
 */

public class ArrayDeserializer {
    public interface ItemDeserializer<T> {
        T fromJson(int protocolVersion, JSONObject json) throws JSONException;
    }

    public static <T> boolean fromJson(int protocolVersion, JSONObject generalJson, String key, List<T> outList, ItemDeserializer<T> itemDeserializer) throws JSONException {
        JSONArray arrElementsJson =  generalJson.optJSONArray(key);
        int numElements = (arrElementsJson == null) ? 0 : arrElementsJson.length();
        if (numElements == 0) {
            return false;
        }
        for (int i = 0 ; i < arrElementsJson.length() ; ++i) {
            outList.add(itemDeserializer.fromJson(protocolVersion, arrElementsJson.getJSONObject(i)));
        }
        return true;
    }
}
