package com.jamieadkins.yearn.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Background fragment that retrieves location.
 */
public class LocationFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = getClass().getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private LocationFetchListener mListener;
    private GetLocationTask mTask;
    private boolean mRunning = false;
    private boolean mStartRequested = false;

    private Location mLastLocation;

    public interface LocationFetchListener {
        void onLocationFound(Location location);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (LocationFetchListener) context;
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        cancel();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mStartRequested) {
            mTask = new GetLocationTask();
            mTask.execute();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Don't need to do anything.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google Play services failed.");
    }

    public void start() {
        mStartRequested = true;
       if (!mRunning && mGoogleApiClient.isConnected()) {
           mTask = new GetLocationTask();
           mTask.execute();
       }
    }

    /**
     * Cancel the background task.
     */
    private void cancel() {
        if (mRunning) {
            mTask.cancel(false);
            mTask = null;
            mRunning = false;
        }
    }

    private class GetLocationTask extends AsyncTask<Void, Void, Location> {
        @Override
        protected Location doInBackground(Void... voids) {
            return LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        @Override
        protected void onPreExecute() {
            mRunning = true;
            mStartRequested = false;
        }


        @Override
        protected void onCancelled() {
            mRunning = false;
        }


        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute(location);
            mRunning = false;
            mLastLocation = location;

            if (location != null) {
                Log.d(TAG, "Location found: " + location.toString());
            }

            if (mListener != null) {
                mListener.onLocationFound(location);
            }
        }
    }
}
