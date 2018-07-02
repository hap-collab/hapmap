package com.hippotec.mapsapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BaseActivity;
import com.hippotec.mapsapplication.activities.base.SwitchableScreen;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivity;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivityResultHandler;
import com.hippotec.mapsapplication.api.APIManager;
import com.hippotec.mapsapplication.api.FacebookAuthenticator;
import com.hippotec.mapsapplication.api.JsonKeys;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.UserSignupApiMethods;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.utils.CredentialsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookScreen implements SwitchableScreen, SwitchableScreenActivityResultHandler {
    private final String TAG = "FacebookScreen";

    private SwitchableScreenActivity m_activity;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    public FacebookScreen(SwitchableScreenActivity activity) {
        m_activity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        initFBBtn();
        LoginManager.getInstance().logOut();

        //if (getIntent().getBooleanExtra(Const.KEY_SIGN_FB, false)) {
        //    fromMapsActivity = true;
        //    loginButton.callOnClick();
        //}

        //permissionsChecker = new PermissionsChecker(LoginActivity.this);
        //permissionsChecker.isPhoneStateAvailable();
    }

    private void initFBBtn() {
        loginButton = (LoginButton) m_activity.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(
                callbackManager,
                new FacebookAuthenticator(
                        TAG,
                        "id,name,email,gender,birthday, picture.type(large)",
                        new FacebookAuthenticator.AutenticationCompletedListener() {
                            public void onAuthenticated(User user) {
                                APIManager.getInstance().postRequest(m_activity, URLs.FB_SIGN_UP, UserSignupApiMethods.externalSignupJson(user).toString(), new FBSignupRequestListener(user));
                            }
                        }
                )
        );
    }

    private class FBSignupRequestListener extends BaseRequestListener {
        private final User user;

        public FBSignupRequestListener(User user) {
            this.user = user;
        }

        @Override
        public void onRequestFailure(String err) {
            super.onRequestFailure(err);
            LoginManager.getInstance().logOut();
            m_activity.stopProgress();
        }

        @Override
        public void onRequestSuccess(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.optBoolean(Const.KEY_SUCCESS, false)) {
                    user.setId(jsonObject.getString(JsonKeys.KEY_USER_ID));
                    if (!TextUtils.isEmpty(jsonObject.optString(Const.KEY_TOKEN,"unknown"))) {
                        CredentialsManager.getInstance(m_activity).setToken(jsonObject.optString(Const.KEY_TOKEN,"unknown"));
                        CredentialsManager.getInstance(m_activity).setUser(user);
                        m_activity.startActivityFinish(FacebookScreen.this, new Intent(m_activity, MapsActivity.class));
                    } else {
                        //TODO avishay handle it...
                        // maybe "already exist popup.
                        CredentialsManager.getInstance(m_activity).setUser(user);
                        m_activity.startActivityFinish(FacebookScreen.this, new Intent(m_activity, MapsActivity.class));
                    }
                } else {
                    LoginManager.getInstance().logOut();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            m_activity.stopProgress();
        }
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
