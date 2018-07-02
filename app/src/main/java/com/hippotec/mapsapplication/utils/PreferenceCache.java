package com.hippotec.mapsapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class PreferenceCache {
    @SuppressWarnings("unused")
    private static final String TAG = "PreferenceCache";

    private final static String mFileName = "maps_app";


    private PreferenceCache() {
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getBoolean(key, false);
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(key, null);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getInt(key, -1);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.edit();
    }


    public static boolean clear(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        return editor.commit();
    }
}
