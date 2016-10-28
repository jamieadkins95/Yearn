package com.jamieadkins.yearn;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.jamieadkins.yearn.ui.QueryFragment;

public class QueryActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();

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

    @Override
    protected void onWeatherResult(WeatherResult weatherResult) {
        // TODO Add weather context cards to recycler view.
    }

    @Override
    protected void onLocationResult(LocationResult locationResult) {
        Log.d(TAG, locationResult.getLocation().toString());
        // TODO add bottom sheet to pick location.
    }
}
