package com.hippotec.mapsapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */

public class TimeUtils {

    private static final String TAG = "TimeUtils";

    public static long MINUTE = 1000*60;

    public static Date convertStringToDate(String str){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(str);
        } catch (ParseException e) {
           Logger.e(TAG, e.toString());
        }
       return convertedDate;
    }

    public static Date convertStringToTime(String str){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date convertedTime = new Date();
        try {
            convertedTime = dateFormat.parse(str);
        } catch (ParseException e) {
            Logger.e(TAG, e.toString());
        }
        return convertedTime;
    }


    public static Date convertStringToTimeDate(String date, String time){
        String str =  date+ "T" + time+"Z";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm'Z'");
        Date convertedTime = new Date();
        try {
            convertedTime = dateFormat.parse(str);
        } catch (ParseException e) {
            Logger.e(TAG, e.toString());
        }
        return convertedTime;
    }

    public static String getFixDatedStr(int dayOfMonth, int monthOfYear, int year) {
        String d = "" + dayOfMonth;
        if (dayOfMonth < 10)
            d = 0 + d;
        String m = "" + (monthOfYear + 1);
        if (monthOfYear < 10)
            m = 0 + m;

        return d + "/" + m + "/" + year;
    }


    public static String getFixTimeStr(int hourOfDay, int minute) {
        String h = "" + hourOfDay;
        String m = "" + minute;
        if (hourOfDay < 10)
            h = 0 + h;
        if (minute < 10)
            m = 0 + m;
        return h + ":" + m;

    }

    public static int[] getRemainingTime(Date date, Date date1) {
        int[] rt = new int[2];
        long diff = date1.getTime() - date.getTime();
        long diffHours = diff / (60 * 60 * 1000);
        long diffMinutes = diff / (60 * 1000) % 60;
        rt[0] = (int) diffHours;
        rt[1] = (int) diffMinutes;
        return   rt;
    }
}
