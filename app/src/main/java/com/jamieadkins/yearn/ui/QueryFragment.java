package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.content.Intent;
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

import com.jamieadkins.yearn.QueryActivity;
import com.jamieadkins.yearn.R;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_query, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.yearningList);
        setupRecyclerView(recyclerView);

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        List<String> yearnTypes = Arrays.asList(getResources().getStringArray(R.array.yearn_types));
        recyclerView.setAdapter(new YearningRecyclerViewAdapter(yearnTypes));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static class YearningRecyclerViewAdapter
            extends RecyclerView.Adapter<YearningRecyclerViewAdapter.ViewHolder> {

        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.yearning_icon);
                mTextView = (TextView) view.findViewById(R.id.yearning_name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public YearningRecyclerViewAdapter(List<String> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_yearning, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));
            holder.mImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                            holder.mImageView.getContext(), R.drawable.ic_local_bar));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Context context = v.getContext();
                    //Intent intent = new Intent(context, QueryActivity.class);
                    //intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);

                    //context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
