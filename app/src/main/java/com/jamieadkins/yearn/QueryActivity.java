package com.jamieadkins.yearn;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jamieadkins.yearn.secret.ApiKeys;

import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.GooglePlacesInterface;
import se.walkercrou.places.Place;

public class QueryActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        ExampleTask task = new ExampleTask();
        task.execute();
    }

    private class ExampleTask extends AsyncTask<Void, Void, List<Place>> {

        @Override
        protected List<Place> doInBackground(Void... voids) {
            GooglePlaces client = new GooglePlaces(ApiKeys.GOOGLE_PLACES_API_WEB);

            return client.getPlacesByQuery("Empire State", GooglePlacesInterface.DEFAULT_RESULTS);
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            for (Place place : places) {
                Log.d(TAG, place.getName());
            }
        }
    }
}
