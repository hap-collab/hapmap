package com.hippotec.mapsapplication.api;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LikelyPlacesFinder {
    public static class LikelyPlace
    {
        public LikelyPlace(String name, String address, float likelyhood) {
            this.name = name; this.address = address; this.likelyhood = likelyhood;
        }
        public String getName() { return name; }
        public String getAddress() { return address; }
        public float getLikelyhood() { return likelyhood; }

        private String name;
        private String address;
        private float likelyhood;
    }

    public interface ResultListener {
        void onLikelyPlaces(LikelyPlacesFuture currentFuture, TreeSet<LikelyPlace> placesByLiklyhood);
    }

    public static class LikelyPlacesFuture implements Future<TreeSet<LikelyPlace>> {
        private boolean m_isDone = false;
        private PendingResult<PlaceLikelihoodBuffer> m_pendingResult;

        LikelyPlacesFuture(PendingResult<PlaceLikelihoodBuffer> pendingResult, final ResultListener onResult) {
            m_pendingResult = pendingResult;
            m_pendingResult.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    m_isDone = true;
                    TreeSet<LikelyPlace> result = processPlacesBuffer(likelyPlaces);
                    onResult.onLikelyPlaces(LikelyPlacesFuture.this, result);
                }
            });
        }

        @Override
        public boolean cancel(boolean ignoredMayInterruptIfExecuting) {
            m_pendingResult.cancel();
            return isCancelled();
        }

        @Override
        public boolean isCancelled() {
            return m_pendingResult.isCanceled();
        }

        @Override
        public boolean isDone() {
            return m_isDone;
        }

        @Override
        public TreeSet<LikelyPlace> get() throws InterruptedException, ExecutionException {
            PlaceLikelihoodBuffer buffer = m_pendingResult.await();
            m_isDone = true;
            return processPlacesBuffer(buffer);
        }

        @Override
        public TreeSet<LikelyPlace> get(long l, @NonNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            PlaceLikelihoodBuffer buffer = m_pendingResult.await(l, timeUnit);
            m_isDone = true;
            return processPlacesBuffer(buffer);
        }
    }

    public static LikelyPlacesFuture findPlaces(AppCompatActivity activity, GoogleApiClient googleApiClient, final ResultListener onResult) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        // Get the likely places - that is, the businesses and other points of interest that
        // are the best match for the device's current location.
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
        return new LikelyPlacesFuture(result, onResult);
//        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
//            @Override
//            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
//                TreeSet<LikelyPlace> result = processPlacesBuffer(likelyPlaces);
//                onResult.onLikelyPlaces(future, result);
//            }
//        });
            /*PlaceDetectionClient mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
            @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace().getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });*/
    }

    private static TreeSet<LikelyPlace> processPlacesBuffer(PlaceLikelihoodBuffer likelyPlaces) {
        int count = likelyPlaces.getCount();

        final TreeSet<LikelyPlace> filteredLikelyPlaces = new TreeSet<LikelyPlace>(new Comparator<LikelyPlace>() {
            @Override
            public int compare(LikelyPlace p1, LikelyPlace p2) {
                return (int)(p1.getLikelyhood() * 100.0) - (int)(p2.getLikelyhood() * 100.0);
            }
        });

        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
            // Build a list of likely places to show the user.
            Place place = placeLikelihood.getPlace();
            String name = (String) place.getName();
            String address = (String) place.getAddress();
            LatLngBounds bounds = place.getViewport();
            LatLng location = place.getLatLng();
            Locale locale = place.getLocale();
            List<Integer> placeTypes = place.getPlaceTypes();
            float likelyhood = placeLikelihood.getLikelihood();
            likelyhood *= getLiklyhoodFactorByPlaceType(placeTypes);
            if (likelyhood > 0.0) {
                filteredLikelyPlaces.add(new LikelyPlace(name, address, likelyhood));
            }
        }

        likelyPlaces.release();
        return filteredLikelyPlaces;
    }

    private static float getLiklyhoodFactorByPlaceType(List<Integer> placeTypes) {
        return 1.0f;
    }

//    private static final int M_MAX_ENTRIES = 5;
//    private String[] mLikelyPlaceNames;
//    private String[] mLikelyPlaceAddresses;
//    private String[] mLikelyPlaceAttributions;
//    private LatLng[] mLikelyPlaceLatLngs;
//
//    private void openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // The "which" argument contains the position of the selected item.
//                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
//                String markerSnippet = mLikelyPlaceAddresses[which];
//                if (mLikelyPlaceAttributions[which] != null) {
//                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                }
//
//                /*// Add a marker for the selected place, with an info window
//                // showing information about that place.
//                mMap.addMarker(new MarkerOptions()
//                        .title(mLikelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet));
//
//                // Position the map's camera at the location of the marker.
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                        DEFAULT_ZOOM));*/
//            }
//        };
//
//        // Display the dialog.
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Places")
//                .setItems(mLikelyPlaceNames, listener)
//                .show();
//    }
}
