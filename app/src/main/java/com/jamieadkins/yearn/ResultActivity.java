package com.jamieadkins.yearn;

import android.app.Fragment;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jamieadkins.yearn.secret.ApiKeys;
import com.jamieadkins.yearn.ui.ResultFragment;

import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.GooglePlacesInterface;
import se.walkercrou.places.Place;

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

    private class GetNearbyPlacesTask extends AsyncTask<String, Void, List<Place>> {

        private String mYearn;

        private GetNearbyPlacesTask(String yearn) {
            mYearn = yearn;
        }

        @Override
        protected List<Place> doInBackground(String... strings) {
            GooglePlaces client = new GooglePlaces(ApiKeys.GOOGLE_PLACES_API_WEB);

            return client.getPlacesByQuery(mYearn + " near trowbridge", GooglePlacesInterface.DEFAULT_RESULTS);
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            if (mResultListener != null) {
                mResultListener.onResult(places);
            }
        }
    }

    public interface PlacesQueryResultListener {
        void onResult(List<Place> result);
    }
}
