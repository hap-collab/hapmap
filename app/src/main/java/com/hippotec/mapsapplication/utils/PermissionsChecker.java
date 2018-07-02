package com.hippotec.mapsapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.hippotec.mapsapplication.R;

/**
 * Util class to check permissions in Runtime for Android Marshmallow.
 * Created on 11.01.16.
 *
 * @author Evgenii Kanivets
 */

public class PermissionsChecker {
    private final Context context;
    public static final int REQUEST_PERMISSION_STORAGE = 32;
    public static final int REQUEST_PERMISSION_LOCATION = 33;
    public static final int REQUEST_PERMISSION_PHONE_STATE= 34;

    private AlertDialog storageDialog;
    private AlertDialog locationDialog;
    private AlertDialog  phoneStateDialog;

    public PermissionsChecker(Context context) {
        this.context = context;
        createLocationDialog();
        createStorageDialog();
    }

    private boolean needRequest(String... permissions) {
        for (String permission : permissions) {
            if (needRequest(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean needRequest(String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }

    public boolean attemptStoragePermission() {
        if (needRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (context instanceof Activity) {
                showStorageDialog();
            }
            return !needRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        else {
            return true;
        }
    }

    public boolean isStorageAvailable() {
        return !needRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public boolean isLocationAvailable() {
        if (needRequest(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (context instanceof Activity) {
                showLocationDialog();
            }
            return !needRequest(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else {
            return true;
        }
    }

    /*public boolean isPhoneStateAvailable() {
        if (needRequest(Manifest.permission.READ_PHONE_STATE)) {
            if (context instanceof Activity) {
                showPhoneStateDialog();
            }
            return false;
        } else {
            return true;
        }
    }*/

    private void createStorageDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(null);
        dialog.setMessage(context.getString(R.string.permission_storage_request));
        dialog.setCancelable(false);
        dialog.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        });
        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        storageDialog = dialog.create();
    }

    private void showStorageDialog() {
        if (!storageDialog.isShowing()) {
            storageDialog.show();
        }
    }

    private void createLocationDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(null);
        dialog.setMessage(context.getString(R.string.permission_gps_request));
        dialog.setCancelable(false);
        dialog.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(
                        (Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCATION);
            }
        });
        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        locationDialog = dialog.create();
    }

    private void showLocationDialog() {
        if (!locationDialog.isShowing()) {
            locationDialog.show();
        }
    }

    /*private void createPhoneStateDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(null);
        dialog.setMessage(context.getString(R.string.permission_phone_state_request));
        dialog.setCancelable(false);
        dialog.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(
                        (Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_PERMISSION_PHONE_STATE);
            }
        });
        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        phoneStateDialog = dialog.create();
    }

    private void showPhoneStateDialog() {
        if (!phoneStateDialog.isShowing()) {
            phoneStateDialog.show();
        }
    }*/
}