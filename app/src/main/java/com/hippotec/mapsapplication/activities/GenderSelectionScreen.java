package com.hippotec.mapsapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BaseActivity;
import com.hippotec.mapsapplication.activities.base.SwitchableScreen;
import com.hippotec.mapsapplication.activities.base.SwitchableScreenActivity;
//import com.hippotec.mapsapplication.api.APIManager;
//import com.hippotec.mapsapplication.api.JsonKeys;
//import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.UserSignupApiMethods;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.Gender;
//import com.hippotec.mapsapplication.model.User;
//import com.hippotec.mapsapplication.utils.CredentialsManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenderSelectionScreen implements SwitchableScreen, View.OnClickListener {
    SwitchableScreenActivity m_activity = null;

    private View male, female;
    //    private TextView tvMale, tvFemale;
    private ImageView maleV, femaleV;

    //@BindView(R.id.tv_male)
    TextView tvMale;
    //@BindView(R.id.tv_female)
    TextView tvFemale;

    public GenderSelectionScreen(SwitchableScreenActivity activity) {
        m_activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initializeViews();

        //permissionsChecker = new PermissionsChecker(LoginActivity.this);
        //permissionsChecker.isPhoneStateAvailable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        assert(false); // should never be reached
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.male_btn:
            case R.id.male_v:
                setMaleSelected();
                break;
            case R.id.female_btn:
            case R.id.female_v:
                setFemaleSelected();
                break;
        }
    }

    private void initializeViews() {
        assert(m_activity != null);
        assert(m_activity.m_viewSwitcher != null);

        ButterKnife.bind(m_activity.m_viewSwitcher);

        male = m_activity.m_viewSwitcher.findViewById(R.id.male_btn);
        female = m_activity.m_viewSwitcher.findViewById(R.id.female_btn);
        maleV = m_activity.m_viewSwitcher.findViewById(R.id.male_v);
        femaleV = m_activity.m_viewSwitcher.findViewById(R.id.female_v);
        tvMale = m_activity.m_viewSwitcher.findViewById(R.id.tv_male);
        tvFemale = m_activity.m_viewSwitcher.findViewById(R.id.tv_female);

        male.setOnClickListener(this);
        female.setOnClickListener(this);
        maleV.setOnClickListener(this);
        femaleV.setOnClickListener(this);
    }

    private void setFemaleSelected() {
        tvMale.setVisibility(View.VISIBLE);
        tvFemale.setVisibility(View.GONE);

        maleV.setVisibility(View.GONE);
        femaleV.setVisibility(View.VISIBLE);

        assignGender(Gender.FEMALE);
    }


    private void setMaleSelected() {
        tvMale.setVisibility(View.GONE);
        tvFemale.setVisibility(View.VISIBLE);

        maleV.setVisibility(View.VISIBLE);
        femaleV.setVisibility(View.GONE);

        assignGender(Gender.MALE);
    }

    private void assignGender(String gender) {
        m_activity.nextScreen(UserSignupApiMethods.KEY_GENDER, gender);
    }
}
