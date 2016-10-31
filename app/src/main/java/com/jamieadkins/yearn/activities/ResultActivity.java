package com.jamieadkins.yearn.activities;

import android.location.Location;
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
import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.secret.ApiKeys;
import com.jamieadkins.yearn.ui.ResultFragment;

public class ResultActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    public static final String EXTRA_YEARN = "com.jamieadkins.yearn.YEARN";

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

    private void startNearbyPlacesTask(String keyword, Location location) {
        GetNearbyPlacesTask task = new GetNearbyPlacesTask(keyword, location);
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
        startNearbyPlacesTask(getIntent().getStringExtra(EXTRA_YEARN), locationResult.getLocation());
    }

    @Override
    protected void onWeatherResult(WeatherResult weatherResult) {
        // Don't need to do anything here.
    }

    private class GetNearbyPlacesTask extends AsyncTask<String, Void, PlacesSearchResult[]> {
        private final String TAG = getClass().getSimpleName();
        private String mQueryKeyword;
        private LatLng mLocation;

        private GetNearbyPlacesTask(String keyword, Location location) {
            mQueryKeyword = keyword;
            mLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        protected PlacesSearchResult[] doInBackground(String... strings) {
            GeoApiContext context = new GeoApiContext().setApiKey(ApiKeys.GOOGLE_PLACES_API_WEB);
            NearbySearchRequest request = PlacesApi.nearbySearchQuery(context, mLocation)
                    .radius(3000)
                    .keyword(mQueryKeyword);

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
