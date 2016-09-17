package com.jamieadkins.yearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.secret.ApiKeys;
import com.jamieadkins.yearn.ui.ResultFragment;

public class ResultActivity extends AppCompatActivity {
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

        String yearn = getIntent().getStringExtra(EXTRA_YEARN);
        setTitle(yearn);

        GetNearbyPlacesTask task = new GetNearbyPlacesTask(yearn);
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

        private String mYearn;

        private GetNearbyPlacesTask(String yearn) {
            mYearn = yearn;
        }

        @Override
        protected PlacesSearchResult[] doInBackground(String... strings) {
            GeoApiContext context = new GeoApiContext().setApiKey(ApiKeys.GOOGLE_PLACES_API_WEB);
            TextSearchRequest request = PlacesApi.textSearchQuery(context, mYearn + " near Rainham");

            try {
                PlacesSearchResult[] results = request.await().results;
                for (int i = 0; i < results.length; i++) {
                    Log.d("JAMIEA", results[i].name);
                }
                return results;
            } catch (Exception e) {
                Log.e("JAMIEA", "request failed", e);
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
