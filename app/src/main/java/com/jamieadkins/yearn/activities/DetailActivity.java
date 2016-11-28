package com.jamieadkins.yearn.activities;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ui.DetailFragment;
import com.jamieadkins.yearn.utils.OnPlaceFoundListener;
import com.jamieadkins.yearn.utils.PlaceFragment;
import com.jamieadkins.yearn.utils.PlaceProvider;
import com.jamieadkins.yearn.utils.SnapshotFragment;

public class DetailActivity extends AppCompatActivity
        implements OnPlaceFoundListener, PlaceProvider {
    private final String TAG = getClass().getSimpleName();
    protected static final String TAG_PLACE_FRAGMENT = SnapshotFragment.class.getSimpleName();
    private String mPlaceId;
    private Place mPlace;

    private PlaceFragment mPlaceFragment;
    private OnPlaceFoundListener mUiListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, new DetailFragment())
                    .commit();
        }

        loadBackdrop();

        mPlaceId = getIntent().getStringExtra(ResultActivity.EXTRA_PLACE_ID);

        mPlaceFragment = (PlaceFragment) fragmentManager.findFragmentByTag(TAG_PLACE_FRAGMENT);

        // If we haven't retained the place fragment, then create it.
        if (mPlaceFragment == null) {
            mPlaceFragment = PlaceFragment.newInstance(mPlaceId);
            mPlaceFragment.setOnPlaceFoundListener(this);
            fragmentManager.beginTransaction()
                    .add(mPlaceFragment, TAG_PLACE_FRAGMENT)
                    .commit();
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
        super.onStart();
        mPlaceFragment.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUiListener = null;
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
    public void onPlaceFound(Place place) {
        mPlace = place;
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(place.getName());

        if (mUiListener != null) {
            mUiListener.onPlaceFound(place);
        }
    }

    @Override
    public void setOnPlaceFoundListener(OnPlaceFoundListener onPlaceFoundListener) {
        mUiListener = onPlaceFoundListener;

        if (mPlace != null) {
            mUiListener.onPlaceFound(mPlace);
        }
    }
}
