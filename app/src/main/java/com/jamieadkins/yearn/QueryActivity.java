package com.jamieadkins.yearn;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jamieadkins.yearn.ui.QueryFragment;
import com.jamieadkins.yearn.utils.LocationFragment;

public class QueryActivity extends AppCompatActivity implements LocationFragment.LocationFetchListener {
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 3294;
    private final String TAG = getClass().getSimpleName();
    private static final String TAG_LOCATION_FRAGMENT = LocationFragment.class.getSimpleName();

    private LocationFragment mLocationFragment;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new QueryFragment())
                    .commit();
        }

        initLocationFragment();
    }

    private void initLocationFragment() {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            mLocationFragment.start();
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
        mLastLocation = location;
    }
}
