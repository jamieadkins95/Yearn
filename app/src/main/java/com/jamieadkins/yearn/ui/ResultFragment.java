package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.maps.model.PlacesSearchResult;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.activities.ResultActivity;
import com.jamieadkins.yearn.ui.recyclerview.ResultViewHolder;

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

        // Disable refresh so that users can't refresh the same search.
        mRefreshContainer.setEnabled(false);
    }

    public static class ResultsRecyclerViewAdapter
            extends RecyclerView.Adapter<ResultViewHolder> {

        private PlacesSearchResult[] mResults;

        public PlacesSearchResult getValueAt(int position) {
            return mResults[position];
        }

        public ResultsRecyclerViewAdapter(PlacesSearchResult[] items) {
            mResults = items;
        }

        @Override
        public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_place_result, parent, false);
            return new ResultViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ResultViewHolder holder, int position) {
            holder.bindPlace(mResults[position]);
        }

        @Override
        public int getItemCount() {
            if (mResults == null) {
                return 0;
            } else {
                return mResults.length;
            }
        }

        public void onNewResult(PlacesSearchResult[] newResult) {
            mResults = newResult;
            notifyDataSetChanged();
        }
    }
}
