package com.hippotec.mapsapplication.api;

import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.LoginActivity;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by yariv on 30/01/2018.
 */

public class FacebookAuthenticator implements FacebookCallback<LoginResult> {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PICTURE = "picture";

    private String tag;
    private String fields;
    private AutenticationCompletedListener authCompleted;

    public interface AutenticationCompletedListener
    {
        void onAuthenticated(User user);
    }

    public FacebookAuthenticator(String tag, String fields, AutenticationCompletedListener authCompleted) {
        this.tag = tag;
        this.fields = fields;
        this.authCompleted = authCompleted;
    }

    public void onSuccess(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new FacebookAuthenticator.SuccessfulLoginHandler()
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", fields);
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        // App code
        Logger.i(tag,"FB onCancel");
    }

    @Override
    public void onError(FacebookException exception) {
        // App code
        Logger.i(tag, "FB onError: " + exception.toString());
    }

    private class SuccessfulLoginHandler implements GraphRequest.GraphJSONObjectCallback {
        @Override
        public void onCompleted(JSONObject jsonFromFb, GraphResponse response) {
            User user = new User();
            //try {
            user.setExternalId(User.EXTERNAL_METHOD_FACEBOOK, jsonFromFb.optString(KEY_ID)); // facebookId: jsonFromFb.optString(KEY_ID)
            user.setName(jsonFromFb.optString(KEY_NAME));
            user.setGender(jsonFromFb.optString(KEY_GENDER));
            user.setBirthday(jsonFromFb.optString(KEY_BIRTHDAY)); // 01/31/1980 format
            JSONObject pictureInfo = jsonFromFb.optJSONObject(KEY_PICTURE);
            if (pictureInfo != null) {
                JSONObject picData = pictureInfo.optJSONObject(Const.KEY_DATA);
                user.setImagePath(picData.optString(Const.KEY_URL));
            }
            authCompleted.onAuthenticated(user);
            //}
            //catch (JSONException e) {
            //    e.printStackTrace();
            //}
        }
    }
}
