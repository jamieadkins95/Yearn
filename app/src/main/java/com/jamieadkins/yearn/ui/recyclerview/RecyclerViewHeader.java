package com.jamieadkins.yearn.ui.recyclerview;

/**
 * Holds text to been shown in the headers withing the yearn recycler view.
 */

public class RecyclerViewHeader {
    private String mPrimaryText;
    private String mSecondaryText;

    public RecyclerViewHeader(String primaryText, String secondaryText) {
        mPrimaryText = primaryText;
        mSecondaryText = secondaryText;
    }

    public String getPrimaryText() {
        return mPrimaryText;
    }

    public String getSecondaryText() {
        return mSecondaryText;
    }
}
