package com.jamieadkins.yearn.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamieadkins.yearn.R;

/**
 * ViewHolder for general yearns
 */

public class YearnViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private final ImageView mImageView;
    private final TextView mTextView;

    public YearnViewHolder(View view) {
        super(view);
        mView = view;
        mImageView = (ImageView) view.findViewById(R.id.yearning_icon);
        mTextView = (TextView) view.findViewById(R.id.yearning_name);
    }

    public View getOverallView() {
        return mView;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTextView.getText();
    }
}