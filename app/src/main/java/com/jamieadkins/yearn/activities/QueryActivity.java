package com.jamieadkins.yearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.maps.model.LatLng;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ui.QueryFragment;
import com.jamieadkins.yearn.utils.LocationProvider;
import com.jamieadkins.yearn.utils.WeatherUtils;

public class QueryActivity extends BaseActivity implements LocationProvider {
    private final String TAG = getClass().getSimpleName();
    private static final int PLACE_PICKER_CODE = 3294;
    private Place mLocationForYearn;

    private ResultCallback<WeatherResult> mWeatherListener;

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
    }

    private void launchPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_CODE);
        } catch (GooglePlayServicesRepairableException|GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "Couldn't load place picker", e);
            Toast.makeText(this, "Failed to load place picker", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_CODE) {
            if (resultCode == RESULT_OK) {
                mLocationForYearn = PlacePicker.getPlace(this, data);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWeatherListener = null;
    }

    @Override
    protected void onWeatherResult(WeatherResult weatherResult) {
        Log.d(TAG, "Weather: " + WeatherUtils.getWeatherDescription(this,
                weatherResult.getWeather().getConditions()));

        if (mWeatherListener != null) {
            mWeatherListener.onResult(weatherResult);
        }
    }

    @Override
    protected void onLocationResult(LocationResult locationResult) {
        Log.d(TAG, locationResult.getLocation().toString());
        // TODO add bottom sheet to pick location.
    }

    public void registerWeatherListener(ResultCallback<WeatherResult> weatherListener) {
        mWeatherListener = weatherListener;
    }

    @Override
    public LatLng getLocationForYearn() {
        if (mLocationForYearn != null) {
            return new LatLng(mLocationForYearn.getLatLng().latitude,
                    mLocationForYearn.getLatLng().longitude);
        } else {
            return null;
        }
    }
}
