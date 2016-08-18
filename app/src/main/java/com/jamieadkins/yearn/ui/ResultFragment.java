package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ResultActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.results);
        setupRecyclerView(recyclerView);

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        List<String> yearnTypes = Arrays.asList(getResources().getStringArray(R.array.yearn_types));

        // Get all the icons and add them to a List.
        TypedArray icons = getResources().obtainTypedArray(R.array.yearn_icons);
        List<Integer> yearnIcons = new ArrayList<>();
        for (int i = 0; i < icons.length(); i++)
        {
            yearnIcons.add(icons.getResourceId(i, -1));
        }

        recyclerView.setAdapter(new ResultsRecyclerViewAdapter(yearnTypes));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static class ResultsRecyclerViewAdapter
            extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

        List<String> mResults;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mPlaceImage;
            public final TextView mPlaceName;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPlaceImage = (ImageView) view.findViewById(R.id.placeImage);
                mPlaceName = (TextView) view.findViewById(R.id.placeName);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mPlaceName.getText();
            }
        }

        public String getValueAt(int position) {
            return mResults.get(position);
        }

        public ResultsRecyclerViewAdapter(List<String> items) {
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
            holder.mBoundString = mResults.get(position);
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }
}
