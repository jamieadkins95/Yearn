package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.activities.ResultActivity;
import com.jamieadkins.yearn.utils.PhotoUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements ResultActivity.PlacesQueryResultListener {

    private RecyclerView mPlaceResultView;
    private SwipeRefreshLayout mRefreshContainer;
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;

    private ResultsRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        mRefreshContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshContainer);
        mRefreshContainer.setOnRefreshListener(mRefreshListener);
        mRefreshContainer.setColorSchemeResources(R.color.colorAccent);
        mRefreshContainer.setRefreshing(true);

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
        mRefreshListener = (SwipeRefreshLayout.OnRefreshListener) getActivity();
    }

    @Override
    public void onResult(PlacesSearchResult[] result) {
        if (mAdapter == null) {
            mAdapter = new ResultsRecyclerViewAdapter(result);
            mPlaceResultView.setAdapter(mAdapter);
        } else {
            mAdapter.onNewResult(result);
        }

        mRefreshContainer.setRefreshing(false);
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
            holder.mPlaceUri.setText(mResults[position].vicinity);

            // Reset the drawable so that images for other places are not recycled and used.
            holder.mPlaceImage.setImageDrawable(
                    ContextCompat.getDrawable(holder.mPlaceImage.getContext(), R.drawable.yearn));

            if (mResults[position].photos != null) {
                if (mResults[position].photos.length == 0) {
                    return;
                }

                String url = PhotoUtils.getBestPhoto(mResults[position].photos);

                if (url != null) {
                    Glide.with(holder.mPlaceImage.getContext())
                            .load(url)
                            .fitCenter()
                            .placeholder(R.drawable.yearn)
                            .into(holder.mPlaceImage);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mResults.length;
        }

        public void onNewResult(PlacesSearchResult[] newResult) {
            mResults = newResult;
            notifyDataSetChanged();
        }
    }
}
