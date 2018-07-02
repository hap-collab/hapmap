package com.hippotec.mapsapplication.utils;

import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.api.GoogleApiClient;
import com.hippotec.mapsapplication.activities.MapsActivity;
import com.hippotec.mapsapplication.api.LikelyPlacesFinder;

import java.util.TreeSet;
import java.util.concurrent.Future;

public class GooglePlacesAsyncFetcher {
    private AppCompatActivity m_activity;
    private GoogleApiClient mGoogleApiClient;
    private Future m_futureResult = null;
    private TreeSet<LikelyPlacesFinder.LikelyPlace> m_placesByLiklyhood = null;
    private Spinner m_spinner = null;

    public GooglePlacesAsyncFetcher(AppCompatActivity activity, GoogleApiClient googleApiClient) {
        m_activity = activity;
        m_futureResult = LikelyPlacesFinder.findPlaces(activity, googleApiClient, new LikelyPlacesFinder.ResultListener() {
            @Override
            public void onLikelyPlaces(LikelyPlacesFinder.LikelyPlacesFuture currentFuture, TreeSet<LikelyPlacesFinder.LikelyPlace> placesByLiklyhood) {
                if (m_futureResult != currentFuture) {
                    return; // this result is no longer valid
                }
                m_futureResult = null;
                synchronized(this) {
                    m_placesByLiklyhood = placesByLiklyhood;
                    // fill combobox
                    if (m_spinner != null) {
                        fillSpinner(placesByLiklyhood);
                    }
                }
            }
        });

    }

    public void cancel() {

        if (m_futureResult == null) {
            return;
        }
        m_futureResult.cancel(true);
        m_futureResult = null;
    }

    public boolean isValidPlace(long idx) {
        if (m_placesByLiklyhood == null) {
            return false;
        }
        if ((idx < 0) || (idx > m_placesByLiklyhood.size())) {
            return false;
        }
        return true;
    }

    private void fillSpinner(TreeSet<LikelyPlacesFinder.LikelyPlace> places) {
        final String[] placesArr = new String[places.size() + 1];
        int i = 0;
        for (LikelyPlacesFinder.LikelyPlace place : places) {
            placesArr[i++] = place.getName() != null ? place.getName() : place.getAddress();
        }
        placesArr[i] = "None of these places";

        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_activity, android.R.layout.simple_spinner_item, placesArr);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                m_spinner.setAdapter(adapter);
            }
        });
    }

    public TreeSet<LikelyPlacesFinder.LikelyPlace> getPlaces() { return m_placesByLiklyhood; }
    public synchronized void setSpinner(Spinner spinner) {
        m_spinner = spinner;
        if (m_placesByLiklyhood != null) {
            fillSpinner(m_placesByLiklyhood);
        }
    }
}
