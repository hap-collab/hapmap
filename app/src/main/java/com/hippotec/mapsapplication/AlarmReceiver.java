package com.hippotec.mapsapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hippotec.mapsapplication.utils.Toaster;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toaster.shortToast(context, "onReceive");

        Intent msgIntent = new Intent(context, UpdateLocationIntentService.class);
        context.startService(msgIntent);
     }
}
