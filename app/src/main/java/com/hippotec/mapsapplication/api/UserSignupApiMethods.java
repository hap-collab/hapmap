package com.hippotec.mapsapplication.api;

import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yariv on 28/01/2018.
 */

public class UserSignupApiMethods {
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXTERNAL_LOGIN_METHOD = "extlogin";
    private static final String KEY_EXTERNAL_ID = "extid";
    public static final String KEY_GENDER = "gender";
    private static final String KEY_PIC = "pic";
    public static final String KEY_AGE = "age";
    public static final String KEY_YEAR = "year";

    public static JSONObject signupJson(String gender, int year) {
        JSONObject json;
        try {
            json = new JSONObject();
            json.put(KEY_GENDER, gender);
            json.put(KEY_YEAR, year);
            return json;
        } catch (JSONException e) {
            assert("signupJson" == null);
            e.printStackTrace();
        }
        return null;
    }


    public static JSONObject externalSignupJson(User user) {
        JSONObject json;
        try {
            json = new JSONObject();
            if ((user.getId() != null) && !user.getId().equals("")) {
                json.put(JsonKeys.KEY_USER_ID, user.getId());
            }
            json.put(KEY_EXTERNAL_LOGIN_METHOD, user.getExternalLoginMethod());
            json.put(KEY_EXTERNAL_ID, user.getExternalId());
            json.put(KEY_GENDER, user.getGender());
            json.put(KEY_USERNAME, user.getName());
            json.put(KEY_PIC, user.getImagePath());
            json.put(KEY_AGE, user.getAge());
            return json;
        } catch (JSONException e) {
            assert("fbSignupJson" == null);
            e.printStackTrace();
        }
        return null;
    }
}
