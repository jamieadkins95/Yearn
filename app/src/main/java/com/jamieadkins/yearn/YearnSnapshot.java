package com.jamieadkins.yearn;

import com.google.maps.model.LatLng;

/**
 * Contains users current context.
 */

public class YearnSnapshot {
    private LatLng mLocation;

    public YearnSnapshot(LatLng location) {
        mLocation = location;
    }

    public LatLng getLocation() {
        return mLocation;
    }
}
