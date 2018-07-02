package com.hippotec.mapsapplication.utils;

import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yariv on 01/02/2018.
 */

public class UserSerializer {
    private static final String TAG = "UserSerializer";
    private static final String KEY_VERSION = "v";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_EXTLOGIN_METHOD = "extlogin";
    private static final String KEY_EXTLOGIN_ID = "extid";

    public static JSONObject toJson(User s) {
        JSONObject json = new JSONObject();
        try {
            //json.put("user", (Object)this);
            json.put(KEY_VERSION, 1);
            json.put(KEY_ID, s.getId());
            json.put(KEY_NAME, s.getName());
            json.put(KEY_GENDER, s.getGender());
            json.put(KEY_BIRTHDAY, s.getBirthday());
            json.put(KEY_PICTURE, s.getImagePath());
            json.put(KEY_EXTLOGIN_METHOD, s.getId());
            json.put(KEY_EXTLOGIN_ID, s.getId());
        }
        catch (JSONException e) {
            assert("User.toJson()" == null);
            Logger.e(TAG, "toJson()");
        }
        return json;
    }

    public static User fromJson(JSONObject jsUserData) {
        User user = new User();
        int version = jsUserData.optInt(KEY_VERSION);
        user.setId(jsUserData.optString(KEY_ID));
        user.setName(jsUserData.optString(KEY_NAME));
        user.setGender(jsUserData.optString(KEY_GENDER));
        user.setBirthday(jsUserData.optString(KEY_BIRTHDAY));
        user.setImagePath(jsUserData.optString(KEY_PICTURE));
        if (version >= 1) {
            String extloginMethod = jsUserData.optString(KEY_EXTLOGIN_METHOD);
            String extloginId = jsUserData.optString(KEY_EXTLOGIN_ID);
            user.setExternalId(extloginMethod, extloginId);
        }
        return user;
    }
}
