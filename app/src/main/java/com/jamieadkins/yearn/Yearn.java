package com.jamieadkins.yearn;

/**
 * Basic model of a yearn.
 */

public class Yearn {
    private String mTitle;
    private int mDrawableId;

    public Yearn(String title, int drawable) {
        mTitle = title;
        mDrawableId = drawable;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getDrawable() {
        return mDrawableId;
    }
}
