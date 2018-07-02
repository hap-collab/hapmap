package com.hippotec.mapsapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BaseActivity;
import com.hippotec.mapsapplication.activities.base.SwitchableScreen;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivity;
//import com.hippotec.mapsapplication.api.APIManager;
//import com.hippotec.mapsapplication.api.JsonKeys;
//import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.adapters.EventsBottomSheetAdapter;
import com.hippotec.mapsapplication.adapters.SimpleTextAdapter;
import com.hippotec.mapsapplication.api.UserSignupApiMethods;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
//import com.hippotec.mapsapplication.model.Gender;
//import com.hippotec.mapsapplication.model.User;
//import com.hippotec.mapsapplication.utils.CredentialsManager;

//import org.json.JSONException;
//import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgeSelectionScreen implements SwitchableScreen {
    SwitchableScreenActivity m_activity = null;

    //@BindView(R.id.birth_years_recycler_view)
    RecyclerView m_birthYearRecyclerView;
    SimpleTextAdapter m_birthYearAdapter;

    public AgeSelectionScreen(SwitchableScreenActivity activity) {
        m_activity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        initializeViews();

        //permissionsChecker = new PermissionsChecker(LoginActivity.this);
        //permissionsChecker.isPhoneStateAvailable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        assert(false); // should never be reached
    }

    private void assignAge(int age) {
        m_activity.nextScreen(UserSignupApiMethods.KEY_AGE, Integer.toString(age));
    }

    private class RecyclerViewItemClickedHandler implements SimpleTextAdapter.ValueClickedListener
    {
        public void onClick(int val) {
            m_activity.nextScreen(UserSignupApiMethods.KEY_AGE, Integer.toString(val));
        }
    }

    private void initializeViews() {
        ButterKnife.bind(m_activity);

        m_birthYearRecyclerView = m_activity.m_viewSwitcher.findViewById(R.id.birth_years_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(m_activity);
        m_birthYearRecyclerView.setLayoutManager(layoutManager);

        m_birthYearAdapter = new SimpleTextAdapter(m_activity, 1950, 2002, 1998);
        m_birthYearAdapter.registerCallback(new RecyclerViewItemClickedHandler());
        m_birthYearRecyclerView.setAdapter(m_birthYearAdapter);

        // how do I set up on-click for recycled views?
    }

}
