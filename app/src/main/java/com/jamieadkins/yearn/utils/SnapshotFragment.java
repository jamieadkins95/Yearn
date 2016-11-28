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
public class SnapshotFragment extends GoogleApiClientFragment {
    private final String TAG = getClass().getSimpleName();

    private ResultCallback<WeatherResult> mWeatherListener;
    private ResultCallback<LocationResult> mLocationListener;

    @Override
    protected void addApis(GoogleApiClient.Builder builder) {
        builder.addApi(Awareness.API);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mWeatherListener = (ResultCallback<WeatherResult>) context;
        mLocationListener = (ResultCallback<LocationResult>) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mWeatherListener = null;
        mLocationListener = null;
    }

    @Override
    protected void doGoogleApiWork() {
        Awareness.SnapshotApi.getLocation(getGoogleApiClient()).setResultCallback(mLocationListener);
        Awareness.SnapshotApi.getWeather(getGoogleApiClient()).setResultCallback(mWeatherListener);
    }
}
