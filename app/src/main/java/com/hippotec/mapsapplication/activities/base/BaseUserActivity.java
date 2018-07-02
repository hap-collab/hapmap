package com.hippotec.mapsapplication.activities.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.utils.CredentialsManager;
import com.hippotec.mapsapplication.utils.PermissionsChecker;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */

public abstract class BaseUserActivity extends BaseActivity {
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUser();
    }

    protected boolean initUser() {
//        user = getLoginData().getUser();
        user = CredentialsManager.getInstance(this).getUser();
        return user != null;
    }

    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
