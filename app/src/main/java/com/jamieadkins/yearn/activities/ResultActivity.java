package com.jamieadkins.yearn.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.secret.ApiKeys;
import com.jamieadkins.yearn.ui.ResultFragment;

public class ResultActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    public static final String EXTRA_YEARN = "com.jamieadkins.yearn.YEARN";
    public static final String EXTRA_LATITUDE = "com.jamieadkins.yearn.LATITUDE";
    public static final String EXTRA_LONGITUDE = "com.jamieadkins.yearn.LONGITUDE";
    public static final String EXTRA_PLACE_TYPE = "com.jamieadkins.yearn.PLACE_TYPE";

    // -30,-30 is somewhere in the Atlantic Ocean.
    private static final double DEFAULT_LAT_LONG = -30.0;

    private LatLng mLocationForYearn;

    private PlacesQueryResultListener mResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ResultFragment())
                    .commit();
        }

        setTitle(getIntent().getStringExtra(EXTRA_YEARN));
    }

    @Override
    protected void initSnapshotFragment() {
        mLocationForYearn = new LatLng(getIntent().getDoubleExtra(EXTRA_LATITUDE, DEFAULT_LAT_LONG),
                getIntent().getDoubleExtra(EXTRA_LONGITUDE, DEFAULT_LAT_LONG));

        // Assume that the user will never want to yearn at this default location.
        // Nullify because it is more intuitive to understand when used elsewhere.
        if (mLocationForYearn.lat == DEFAULT_LAT_LONG
                && mLocationForYearn.lng == DEFAULT_LAT_LONG) {
            mLocationForYearn = null;
        }

        // Only fetch location if we haven't been given a location already.
        if (mLocationForYearn == null) {
            super.initSnapshotFragment();
        } else {
            startNearbyPlacesTask((PlaceType) getIntent().getSerializableExtra(EXTRA_PLACE_TYPE), mLocationForYearn);
        }
    }

    private void startNearbyPlacesTask(PlaceType placeType, LatLng location) {
        GetNearbyPlacesTask task = new GetNearbyPlacesTask(placeType, location);
        task.execute();
    }

    public void registerListener(PlacesQueryResultListener listener) {
        mResultListener = listener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResultListener = null;
    }

    @Override
    public void onLocationResult(@NonNull LocationResult locationResult) {
        Log.d(TAG, locationResult.getLocation().toString());
        mLocationForYearn = new LatLng(locationResult.getLocation().getLatitude(),
                locationResult.getLocation().getLongitude());
        startNearbyPlacesTask((PlaceType) getIntent().getSerializableExtra(EXTRA_PLACE_TYPE), mLocationForYearn);
    }

    @Override
    protected void onWeatherResult(WeatherResult weatherResult) {
        // Don't need to do anything here.
    }

    private class GetNearbyPlacesTask extends AsyncTask<String, Void, PlacesSearchResult[]> {
        private final String TAG = getClass().getSimpleName();
        private String mQueryKeyword;
        private LatLng mLocation;
        private PlaceType mPlaceType;

        private GetNearbyPlacesTask(String keyword, LatLng location) {
            mQueryKeyword = keyword;
            mLocation = location;
        }

        private GetNearbyPlacesTask(PlaceType placeType, LatLng location) {
            mPlaceType = placeType;
            mLocation = location;
        }

        @Override
        protected PlacesSearchResult[] doInBackground(String... strings) {
            GeoApiContext context = new GeoApiContext().setApiKey(ApiKeys.GOOGLE_PLACES_API_WEB);
            NearbySearchRequest request = PlacesApi.nearbySearchQuery(context, mLocation)
                    .radius(5000)
                    .type(mPlaceType);

            try {
                return request.await().results;
            } catch (Exception e) {
                Log.e(TAG, "Places query failed.", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(PlacesSearchResult[] places) {
            super.onPostExecute(places);
            if (mResultListener != null) {
                mResultListener.onResult(places);
            }
        }
    }

    public interface PlacesQueryResultListener {
        void onResult(PlacesSearchResult[] result);
    }
}