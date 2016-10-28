package com.jamieadkins.yearn;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.maps.model.LatLng;
import com.jamieadkins.yearn.utils.LocationFragment;

public abstract class BaseActivity extends AppCompatActivity
        implements LocationFragment.LocationFetchListener {
    private final String TAG = getClass().getSimpleName();
    protected static final String TAG_LOCATION_FRAGMENT = LocationFragment.class.getSimpleName();
    protected static final int PERMISSIONS_REQUEST_FINE_LOCATION = 3294;

    protected LocationFragment mLocationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.snapshot_file_key), MODE_PRIVATE);

        LatLng location = new LatLng(
                sharedPreferences.getFloat(getString(R.string.snapshot_latitude), 0.0f),
                sharedPreferences.getFloat(getString(R.string.snapshot_longitude), 0.0f));

        long snapshotTimestamp =
                sharedPreferences.getLong(getString(R.string.snapshot_timestamp), 0);

        // If no location has been saved or the timestamp is older than 30 seconds ago.
        // Under the assumption that the user is never at 0,0.
        if ((location.lat == 0.0f && location.lng == 0.0f) ||
                snapshotTimestamp < System.currentTimeMillis() - 30000) {
            initLocationFragment();
        } else {
            onSnapshotReady(new YearnSnapshot(location));
        }
    }

    private void initLocationFragment() {
        Log.d(TAG, "Initialising location fragment");

        FragmentManager fm = getSupportFragmentManager();
        mLocationFragment = (LocationFragment) fm.findFragmentByTag(TAG_LOCATION_FRAGMENT);

        // If we haven't retained the location fragment, then create it.
        if (mLocationFragment == null) {
            mLocationFragment = new LocationFragment();
            fm.beginTransaction().add(mLocationFragment, TAG_LOCATION_FRAGMENT).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationFragment != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                mLocationFragment.start();
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
                    mLocationFragment.start();
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
    public void onLocationFound(Location location) {
        onSnapshotReady(
                new YearnSnapshot(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    protected abstract void onSnapshotReady(YearnSnapshot snapshot);
}
