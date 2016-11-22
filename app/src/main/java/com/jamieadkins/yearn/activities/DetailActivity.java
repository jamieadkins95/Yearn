package com.jamieadkins.yearn.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ui.DetailFragment;

public class DetailActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = getClass().getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private String mPlaceId;
    private Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new DetailFragment())
                    .commit();
        }

        loadBackdrop();

        mPlaceId = getIntent().getStringExtra(ResultActivity.EXTRA_PLACE_ID);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(DetailActivity.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        FloatingActionButton mapsButton = (FloatingActionButton) findViewById(R.id.btnMaps);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + mPlace.getName() + " " + mPlace.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void onPlaceFound(Place place) {
        mPlace = place.freeze();
        Log.i(TAG, "Place found: " + mPlace.getName());

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(place.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        String url = getIntent().getStringExtra(ResultActivity.EXTRA_PHOTO_URL);
        if (url != null) {
            Glide.with(this)
                    .load(url)
                    .fitCenter()
                    .placeholder(R.drawable.yearn)
                    .into(imageView);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mPlace == null) {
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, mPlaceId)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                onPlaceFound(places.get(0));
                            } else {
                                Log.e(TAG, "Place not found");
                            }
                            places.release();
                        }
                    });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Don't need to do anything.
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google Play services failed.");
    }
}
