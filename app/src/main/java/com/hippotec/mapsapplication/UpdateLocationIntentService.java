package com.hippotec.mapsapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.api.APIManager;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.utils.CredentialsManager;

import org.json.JSONObject;

/**
 * Created by Avishay Peretz on 03/04/2017.
 */

public class UpdateLocationIntentService extends IntentService implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private LatLng lastKnownLocation;

    public UpdateLocationIntentService() {
        super("UpdateLocationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //TODO connect to Google Location Service
        connectToGoogleApi();

    }

    private void connectToGoogleApi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationUpdateReceived(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void locationUpdateReceived(Location location) {
        if (location == null) return;
        lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());

        JSONObject json = APIManager.getInstance().updateLocationJson(
                CredentialsManager.getInstance(getBaseContext()).getUser().getId(),
                lastKnownLocation.latitude,
                lastKnownLocation.longitude
        );
        APIManager.getInstance().postRequestNoProgress(null, URLs.UPDATE_LOCATION, json.toString(), new BaseRequestListener());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
