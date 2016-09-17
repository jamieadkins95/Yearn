package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ResultActivity;

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
    public void onResult(PlacesSearchResult[] result) {
        mPlaceResultView.setAdapter(new ResultsRecyclerViewAdapter(result));
    }

    public static class ResultsRecyclerViewAdapter
            extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

        PlacesSearchResult[] mResults;

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

        public PlacesSearchResult getValueAt(int position) {
            return mResults[position];
        }

        public ResultsRecyclerViewAdapter(PlacesSearchResult[] items) {
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
            holder.mPlaceName.setText(mResults[position].name);
            holder.mPlaceUri.setText(mResults[position].formattedAddress);

            /*if (mResults[position].photos.length > 0) {
                Log.d("JAMIEA", "pictures");
                Glide.with(holder.mPlaceImage.getContext())
                        .load(mResults[position].icon)
                        .fitCenter()
                        .into(holder.mPlaceImage);
            }*/
        }

        @Override
        public int getItemCount() {
            return mResults.length;
        }
    }
}
