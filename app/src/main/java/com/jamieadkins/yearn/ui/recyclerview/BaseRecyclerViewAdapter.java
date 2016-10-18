package com.jamieadkins.yearn.ui.recyclerview;

import android.location.Location;
import android.support.v7.widget.RecyclerView;

/**
 * Recycler View Adapter that contains methods used by other recycler view adapters.
 */

public abstract class BaseRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Location mLocation;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }
}
