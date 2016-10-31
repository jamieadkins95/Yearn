package com.jamieadkins.yearn.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.common.api.ResultCallback;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ui.QueryFragment;
import com.jamieadkins.yearn.utils.WeatherUtils;

public class QueryActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
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
}
