package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.google.android.gms.location.places.Place;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.utils.OnPlaceFoundListener;
import com.jamieadkins.yearn.utils.PlaceProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements OnPlaceFoundListener {

    private PlaceProvider mPlaceProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPlaceProvider = (PlaceProvider) context;
        mPlaceProvider.setOnPlaceFoundListener(this);
    }

    @Override
    public void onPlaceFound(Place place) {
        // Update UI based on place.
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPlaceProvider = null;
    }
}
