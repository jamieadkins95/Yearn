package com.jamieadkins.yearn.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jamieadkins.yearn.R;

/**
 * ViewHolder for the headers e.g. "It's Wednesday afternoon"
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView mPrimaryText;
    private TextView mSecondaryText;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        mPrimaryText = (TextView) itemView.findViewById(R.id.header_primary_text);
        mSecondaryText = (TextView) itemView.findViewById(R.id.header_secondary_text);
    }

    public TextView getPrimaryText() {
        return mPrimaryText;
    }

    public TextView getSecondaryText() {
        return mSecondaryText;
    }
}
