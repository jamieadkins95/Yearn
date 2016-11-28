package com.jamieadkins.yearn.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

/**
 * Background fragment that retrieves location.
 */
public class PlaceFragment extends GoogleApiClientFragment implements PlaceProvider {
    private final String TAG = getClass().getSimpleName();

    private OnPlaceFoundListener mPlaceListener;
    private String mPlaceId;
    private Place mPlace;

    public static PlaceFragment newInstance(String placeId) {
        PlaceFragment placeFragment = new PlaceFragment();
        placeFragment.mPlaceId = placeId;
        return placeFragment;
    }

    @Override
    protected void addApis(GoogleApiClient.Builder builder) {
        builder.addApi(Places.GEO_DATA_API);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPlaceListener = null;
    }

    @Override
    protected void doGoogleApiWork() {
        if (mPlace != null) {
            onPlaceFound(mPlace);
        } else {
            Places.GeoDataApi.getPlaceById(getGoogleApiClient(), mPlaceId)
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

    private void onPlaceFound(Place place) {
        mPlace = place.freeze();
        Log.i(TAG, "Place found: " + mPlace.getName());

        if (mPlaceListener != null) {
            mPlaceListener.onPlaceFound(mPlace);
        }
    }

    @Override
    public void setOnPlaceFoundListener(OnPlaceFoundListener onPlaceFoundListener) {
        mPlaceListener = onPlaceFoundListener;
    }
}
