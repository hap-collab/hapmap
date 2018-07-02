package com.hippotec.mapsapplication.utils;

import android.util.Log;

import com.hippotec.mapsapplication.Const;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */

public class Logger {

    private static final String TAG = "Logger";

    public static void i(String msg) {
        if (Const.DEBUG)
            Log.i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (Const.DEBUG)
            Log.i(tag, msg);
    }

    public static void e(String msg) {
        if (Const.DEBUG)
            Log.e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (Const.DEBUG)
            Log.e(tag, msg);
    }

}
