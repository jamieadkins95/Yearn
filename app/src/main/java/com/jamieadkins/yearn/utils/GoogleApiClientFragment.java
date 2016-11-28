package com.jamieadkins.yearn.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

/**
 * Background fragment that retrieves location.
 */
public abstract class GoogleApiClientFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = getClass().getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private boolean mStartRequested = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        GoogleApiClient.Builder googleApiClientBuilder;
        if (mGoogleApiClient == null) {
            googleApiClientBuilder = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this);

            // Add the APIs specific to what we need.
            addApis(googleApiClientBuilder);

            mGoogleApiClient = googleApiClientBuilder.build();
        }


    }

    protected abstract void addApis(GoogleApiClient.Builder builder);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mStartRequested) {
            doGoogleApiWork();
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
        if (mGoogleApiClient.isConnected()) {
            doGoogleApiWork();
        }
    }

    protected abstract void doGoogleApiWork();

    /**
     * Cancel the background task.
     */
    private void cancel() {
        mStartRequested = false;
    }

    protected GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}
