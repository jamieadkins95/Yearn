package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ResultActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.walkercrou.places.Place;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements ResultActivity.PlacesQueryResultListener {

    RecyclerView mPlaceResultView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        mPlaceResultView = (RecyclerView) rootView.findViewById(R.id.results);
        setupRecyclerView(mPlaceResultView);

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((ResultActivity) getActivity()).registerListener(this);
    }

    @Override
    public void onResult(List<Place> result) {
        mPlaceResultView.setAdapter(new ResultsRecyclerViewAdapter(result));
    }

    public static class ResultsRecyclerViewAdapter
            extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

        List<Place> mResults;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mPlaceImage;
            public final TextView mPlaceName;
            public final TextView mPlaceUri;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPlaceImage = (ImageView) view.findViewById(R.id.placeImage);
                mPlaceName = (TextView) view.findViewById(R.id.placeName);
                mPlaceUri = (TextView) view.findViewById(R.id.placeUri);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mPlaceName.getText();
            }
        }

        public Place getValueAt(int position) {
            return mResults.get(position);
        }

        public ResultsRecyclerViewAdapter(List<Place> items) {
            mResults = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_place_result, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mPlaceName.setText(mResults.get(position).getName());
            holder.mPlaceUri.setText(mResults.get(position).getAddress());

            if (mResults.get(position).getPhotos().size() > 0) {
                Log.d("JAMIEA", "pictures");
                Glide.with(holder.mPlaceImage.getContext())
                        .load(mResults.get(position).getGoogleUrl())
                        .fitCenter()
                        .into(holder.mPlaceImage);
            }
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }
}
