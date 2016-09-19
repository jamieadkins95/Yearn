package com.jamieadkins.yearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.secret.ApiKeys;
import com.jamieadkins.yearn.ui.ResultFragment;

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_YEARN = "com.jamieadkins.yearn.YEARN";
    public static final String EXTRA_LONGITUDE = "com.jamieadkins.yearn.LONGITUDE";
    public static final String EXTRA_LATITUDE = "com.jamieadkins.yearn.LATITUDE";

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

        String yearn = getIntent().getStringExtra(EXTRA_YEARN);
        setTitle(yearn);

        LatLng location = new LatLng(getIntent().getDoubleExtra(EXTRA_LATITUDE, 0.0),
                getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0.0));

        GetNearbyPlacesTask task = new GetNearbyPlacesTask(yearn, location);
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

    private class GetNearbyPlacesTask extends AsyncTask<String, Void, PlacesSearchResult[]> {
        private final String TAG = getClass().getSimpleName();
        private String mYearn;
        private LatLng mLocation;

        private GetNearbyPlacesTask(String yearn, LatLng location) {
            mYearn = yearn;
            mLocation = location;
        }

        @Override
        protected PlacesSearchResult[] doInBackground(String... strings) {
            GeoApiContext context = new GeoApiContext().setApiKey(ApiKeys.GOOGLE_PLACES_API_WEB);
            NearbySearchRequest request = PlacesApi.nearbySearchQuery(context, mLocation).radius(5000).keyword("restaurant");

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
