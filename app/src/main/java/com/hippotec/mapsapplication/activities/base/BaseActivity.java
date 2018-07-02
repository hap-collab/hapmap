package com.hippotec.mapsapplication.activities.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.hippotec.mapsapplication.ui.CustomProgressDialog;
import com.hippotec.mapsapplication.utils.PermissionsChecker;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private PermissionsChecker permissionsChecker;
    private CustomProgressDialog mProgressDialog;

    protected PermissionsChecker getPermissionsChecker() {
        return permissionsChecker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionsChecker = new PermissionsChecker(this);
    }

    //protected abstract void initializeViews();

    protected void startActivityFinish(Intent intent){
        startActivity(intent);
        finish();
    }

    public void startProgress() {
//        if (getProgressBar() == null) return;
        if (getProgressBar()!=null && !getProgressBar().isShowing()) {
            getProgressBar().show();
        }
    }

    public void stopProgress() {
        if (getProgressBar() != null) {
            getProgressBar().dismiss();
        }
    }

    private ProgressDialog getProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgressDialog(this);
        }
        return mProgressDialog;
    }

    public static void hideSoftKeyboard(Activity activity){
        final InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}
