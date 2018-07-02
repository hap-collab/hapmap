package com.hippotec.mapsapplication.utils;

import android.content.Context;
import android.widget.Toast;

import com.hippotec.mapsapplication.Const;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class Toaster {


    public static void shortToast(Context context, String msg) {
        if(Const.DEBUG)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String msg) {
        if(Const.DEBUG)
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
