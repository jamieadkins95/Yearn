package com.jamieadkins.yearn.activities;

import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.jamieadkins.yearn.R;

public class DetailActivity extends AppCompatActivity {

    Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadBackdrop();
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

    private class GetPlaceTask extends AsyncTask<String, Void, Place> {

        @Override
        protected Place doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Place place) {
            super.onPostExecute(place);

            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(place.getName());
        }
    }
}
