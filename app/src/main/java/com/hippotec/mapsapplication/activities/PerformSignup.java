package com.hippotec.mapsapplication.activities;

import android.content.Intent;

//import com.hippotec.mapsapplication.activities.base.SwitchableScreen;

import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivity;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivityResultHandler;
import com.hippotec.mapsapplication.api.APIManager;
import com.hippotec.mapsapplication.api.JsonKeys;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.UserSignupApiMethods;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.Gender;
import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.utils.CredentialsManager;
import com.hippotec.mapsapplication.utils.Toaster;

import org.json.JSONObject;
import org.json.JSONException;

public class PerformSignup implements Runnable {
    SwitchableScreenActivity m_activity = null;

    public PerformSignup(SwitchableScreenActivity activity) { m_activity = activity; }

    public void run() {
        String gender = m_activity.getSharedValue(Gender.TAG);
        int year = Integer.parseInt(m_activity.getSharedValue(UserSignupApiMethods.KEY_AGE));

        User user = new User();
        user.setGender(gender);
        user.setBirthYear(year);

        JSONObject json = UserSignupApiMethods.signupJson(gender, year);
        if (json != null) {
            m_activity.startProgress();

            SignupRequestListener signupResultListener = new SignupRequestListener(user);
            signupResultListener.onRequestSuccess("{ success: true, uid: 111 }");
            //APIManager.getInstance().postRequest(m_activity, URLs.SIGN_UP, json.toString(), signupResultListener);
        }
        else {
            // TODO: GO BACK TO PREV SCREEN
            m_activity.stopProgress();
        }
    }

    private class SignupRequestListener extends BaseRequestListener {
        private final User user;

        public SignupRequestListener(User user) {
            this.user = user;
        }

        @Override
        public void onRequestFailure(String err) {
            super.onRequestFailure(err);
            m_activity.stopProgress();
            Toaster.longToast(m_activity, "Signup failed!\nPlease try again later.");
        }

        @Override
        public void onRequestSuccess(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optBoolean(Const.KEY_SUCCESS, false)) {
                    user.setId(jsonObject.getString(JsonKeys.KEY_USER_ID));
                    CredentialsManager.getInstance(m_activity).setUser(user);
                    m_activity.startActivity(new Intent(m_activity, MapsActivity.class));
                }
                else {
                    //TODO avishay handle it...
                    // maybe "already exist popup
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            m_activity.stopProgress();
        }
    }
}
