package com.jamieadkins.yearn.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.jamieadkins.yearn.utils.SnapshotFragment;

public abstract class BaseActivity extends AppCompatActivity
        implements ResultCallback {
    private final String TAG = getClass().getSimpleName();
    protected static final String TAG_SNAPSHOT_FRAGMENT = SnapshotFragment.class.getSimpleName();
    protected static final int PERMISSIONS_REQUEST_FINE_LOCATION = 3294;

    protected SnapshotFragment mSnapshotFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSnapshotFragment();
    }

    private void initSnapshotFragment() {
        Log.d(TAG, "Initialising location fragment");

        FragmentManager fm = getSupportFragmentManager();
        mSnapshotFragment = (SnapshotFragment) fm.findFragmentByTag(TAG_SNAPSHOT_FRAGMENT);

        // If we haven't retained the location fragment, then create it.
        if (mSnapshotFragment == null) {
            mSnapshotFragment = new SnapshotFragment();
            fm.beginTransaction().add(mSnapshotFragment, TAG_SNAPSHOT_FRAGMENT).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSnapshotFragment != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                mSnapshotFragment.start();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mSnapshotFragment.start();
                } else {
                    // Permission denied.
                }
                break;
            default:
                // Don't need to do anything here.
                break;
        }
    }

    @Override
    public void onResult(@NonNull Result result) {
        if (!result.getStatus().isSuccess()) {
            Log.e(TAG, "Couldn't get snapshot");
        }

        if (result instanceof LocationResult) {
            onLocationResult((LocationResult) result);
        } else if (result instanceof WeatherResult) {
            onWeatherResult((WeatherResult) result);
        }
    }

    protected abstract void onWeatherResult(WeatherResult weatherResult);

    protected abstract void onLocationResult(LocationResult locationResult);
}
