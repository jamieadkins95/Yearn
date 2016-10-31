package com.jamieadkins.yearn.utils;

import com.google.maps.model.LatLng;

/**
 * Classes that implement this provide a location that is not the users current location.
 * Used for yearning in other locations.
 */

public interface LocationProvider {
    LatLng getLocationForYearn();
}
