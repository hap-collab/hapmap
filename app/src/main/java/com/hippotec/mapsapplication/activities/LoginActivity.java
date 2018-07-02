package com.hippotec.mapsapplication.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BaseActivity;
import com.hippotec.mapsapplication.activities.base.BaseUserActivity;
import com.hippotec.mapsapplication.activities.base.SwitchableScreen;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivity;
import com.hippotec.mapsapplication.api.APIManager;
import com.hippotec.mapsapplication.api.FacebookAuthenticator;
import com.hippotec.mapsapplication.api.JsonKeys;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.UserSignupApiMethods;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.Gender;
import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.utils.CredentialsManager;
import com.hippotec.mapsapplication.utils.Logger;
import com.hippotec.mapsapplication.utils.PermissionsChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class LoginActivity extends BaseUserActivity {
    //private final String TAG = "LoginActivity";

    //private PermissionsChecker permissionsChecker;
    private boolean fromMapsActivity;
    //private SwitchableScreen m_facebookScreen;

    public LoginActivity() {
        super();
        //m_facebookScreen = new FacebookScreen()
        //addScreen(new FacebookScreen(this));
        //addScreen(new GenderSelectionScreen(this));
        //addScreen(new AgeSelectionScreen(this));
        //setFinalAction(new PerformSignup(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //if (getIntent().getBooleanExtra(Const.KEY_SIGN_FB, false)) {
        //    fromMapsActivity = true;
        //    loginButton.callOnClick();
        //}
    }

    /*@OnClick(R.id.skip_btn)
    public void moveToGenderScreen() {
        skipCurrentScreen();
    }*/

    /*@Override
    public void onBackPressed() {
        if (fromMapsActivity) {
            startActivityFinish(new Intent(this, MapsActivity.class));
        } else if (m_viewSwitcher.getDisplayedChild() == 0)
            super.onBackPressed();
        else
            m_viewSwitcher.showPrevious();
    }*/


    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PermissionsChecker.REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    moveToGenderScreen();
                    break;

            default:
                break;
        }
    }*/
}
