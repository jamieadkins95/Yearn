package com.jamieadkins.yearn.ui.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.utils.PhotoUtils;

/**
 * ViewHolder for general yearns
 */

public class ResultViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private final ImageView mPlaceImage;
    private final TextView mPlaceName;
    private final TextView mPlaceUri;

    private PlacesSearchResult mBoundPlace;
    private String mPhotoUrl;

    public ResultViewHolder(View view) {
        super(view);
        mView = view;
        mPlaceImage = (ImageView) view.findViewById(R.id.placeImage);
        mPlaceName = (TextView) view.findViewById(R.id.placeName);
        mPlaceUri = (TextView) view.findViewById(R.id.placeUri);
    }

    public void bindPlace(PlacesSearchResult place) {
        mBoundPlace = place;

        mPlaceName.setText(mBoundPlace.name);
        mPlaceUri.setText(mBoundPlace.vicinity);

        // Reset the drawable so that images for other places are not recycled and used.
        mPlaceImage.setImageDrawable(
                ContextCompat.getDrawable(mPlaceImage.getContext(), R.drawable.yearn));

        if (mBoundPlace.photos != null) {
            if (mBoundPlace.photos.length == 0) {
                return;
            }

            mPhotoUrl = PhotoUtils.getBestPhoto(mBoundPlace.photos);

            if (mPhotoUrl != null) {
                Glide.with(mPlaceImage.getContext())
                        .load(mPhotoUrl)
                        .fitCenter()
                        .placeholder(R.drawable.yearn)
                        .into(mPlaceImage);
            }
        }

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" +
                        Uri.encode(mBoundPlace.name + " " + mBoundPlace.vicinity));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mPlaceName.getText();
    }
}
