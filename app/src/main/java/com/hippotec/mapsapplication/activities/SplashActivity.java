package com.hippotec.mapsapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BaseUserActivity;


/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class SplashActivity extends BaseUserActivity {

    private final long SPLASH_SECONDS = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startTimer();
    }

    private void startTimer() {
        CountDownTimer timer = new CountDownTimer(SPLASH_SECONDS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent;
                if (user != null) {
                    intent = new Intent(SplashActivity.this, MapsActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, ManualSignupActivity.class);
                }
                startActivity(intent);
            }
        };
        timer.start();
    }

}
