package com.hippotec.mapsapplication;

import android.support.annotation.Nullable;

import com.wonderkiln.camerakit.Size;

import java.io.File;

public class PhotoResultHolder {

    private static byte[] image;
    private static File video;
    private static Size nativeCaptureSize;
    private static long timeToCallback;


    public static void setImage(@Nullable byte[] image) {
        PhotoResultHolder.image = image;
    }

    @Nullable
    public static byte[] getImage() {
        return image;
    }

    public static void setVideo(@Nullable File video) {
        PhotoResultHolder.video = video;
    }

    @Nullable
    public static File getVideo() {
        return video;
    }

    public static void setNativeCaptureSize(@Nullable Size nativeCaptureSize) {
        PhotoResultHolder.nativeCaptureSize = nativeCaptureSize;
    }

    @Nullable
    public static Size getNativeCaptureSize() {
        return nativeCaptureSize;
    }

    public static void setTimeToCallback(long timeToCallback) {
        PhotoResultHolder.timeToCallback = timeToCallback;
    }

    public static long getTimeToCallback() {
        return timeToCallback;
    }

    public static void dispose() {
        setImage(null);
        setNativeCaptureSize(null);
        setTimeToCallback(0);
    }

}
