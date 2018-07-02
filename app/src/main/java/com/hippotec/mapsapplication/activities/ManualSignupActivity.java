package com.hippotec.mapsapplication.activities;

import android.os.Bundle;

import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivity;

import butterknife.OnClick;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class ManualSignupActivity extends SwitchableScreenActivity {
    //private final String TAG = "LoginActivity";

    //private PermissionsChecker permissionsChecker;
    //private boolean fromMapsActivity;

    public ManualSignupActivity() {
        super(R.layout.activity_manual_signup, R.id.manual_signup_viewswitcher);
        addScreen(new GenderSelectionScreen(this));
        addScreen(new AgeSelectionScreen(this));
        setFinalAction(new PerformSignup(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        //if (getIntent().getBooleanExtra(Const.KEY_SIGN_FB, false)) {
        //    fromMapsActivity = true;
        //    loginButton.callOnClick();
        //}
    }

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
