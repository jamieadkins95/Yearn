package com.jamieadkins.yearn.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamieadkins.yearn.R;

/**
 * ViewHolder that holds a recycler view. Used for a RecyclerView inside a RecyclerView.
 */

public class InnerRecyclerViewViewHolder extends RecyclerView.ViewHolder {
    private final RecyclerView mRecyclerView;

    public InnerRecyclerViewViewHolder(View view) {
        super(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
