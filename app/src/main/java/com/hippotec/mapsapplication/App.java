//package com.hippotec.mapsapplication;
//
//import android.app.Application;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDexApplication;
//import android.util.Base64;
//import android.util.Log;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
///**
// * Created by Avishay Peretz on 09/04/2017.
// */
//
//public class App extends MultiDexApplication { // extends Application
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        MultiDex.install(this);
//
//
//
//
////        // Add code to print out the key hash
////        try {
////            PackageInfo info = getPackageManager().getPackageInfo(
////                    "com.hippotec.mapsapplication",
////                    PackageManager.GET_SIGNATURES);
////            for (Signature signature : info.signatures) {
////                MessageDigest md = MessageDigest.getInstance("SHA");
////                md.update(signature.toByteArray());
////                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
////            }
////        } catch (PackageManager.NameNotFoundException e) {
////
////        } catch (NoSuchAlgorithmException e) {
////
////        }
//    }
//}
