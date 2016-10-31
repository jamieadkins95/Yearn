package com.jamieadkins.yearn.ui.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.maps.model.LatLng;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.activities.ResultActivity;
import com.jamieadkins.yearn.Yearn;
import com.jamieadkins.yearn.utils.LocationProvider;

/**
 * ViewHolder for general yearns
 */

public class YearnViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private final ImageView mImageView;
    private final TextView mTextView;

    private Yearn mBoundYearn;

    public YearnViewHolder(View view) {
        super(view);
        mView = view;
        mImageView = (ImageView) view.findViewById(R.id.yearning_icon);
        mTextView = (TextView) view.findViewById(R.id.yearning_name);
    }

    protected void bindYearn(Yearn yearn) {
        mBoundYearn = yearn;

        Context context = mView.getContext();
        String text = context.getString(mBoundYearn.getTitleId());
        mBoundYearn.setQueryKeyword(text);
        mTextView.setText(text);

        // Glide can't handle vector drawables so we have to do this manually.
        mImageView.setImageDrawable(ContextCompat.getDrawable(
                        context, mBoundYearn.getDrawable()));

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                // The context will be a instance of QueryActivity so this cast is safe.
                LocationProvider locationProvider = (LocationProvider) context;
                LatLng locationForYearn = locationProvider.getLocationForYearn();

                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra(ResultActivity.EXTRA_YEARN, mBoundYearn.getQueryKeyword());
                if (locationForYearn != null) {
                    intent.putExtra(ResultActivity.EXTRA_LATITUDE, locationForYearn.lat);
                    intent.putExtra(ResultActivity.EXTRA_LONGITUDE, locationForYearn.lng);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTextView.getText();
    }
}
