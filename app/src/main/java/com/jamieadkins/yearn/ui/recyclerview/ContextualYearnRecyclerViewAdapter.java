package com.jamieadkins.yearn.ui.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.ResultActivity;
import com.jamieadkins.yearn.Yearn;
import com.jamieadkins.yearn.ui.QueryFragment;

import java.util.List;

/**
 * RecyclerView adapter to show possible yearns in {@link QueryFragment}.
 */

public class ContextualYearnRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Yearn> mContextualYearns;

    public ContextualYearnRecyclerViewAdapter(List<Yearn> contextualYearns) {
        mContextualYearns = contextualYearns;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new YearnViewHolder(inflater.inflate(R.layout.item_contextual_yearn, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        YearnViewHolder yearnViewHolder = (YearnViewHolder) viewHolder;
        yearnViewHolder.getTextView().setText(mContextualYearns.get(position).getTitleId());

        // TODO: Should potentially use glide here to handle image.
        yearnViewHolder.getImageView().setImageDrawable(
                ContextCompat.getDrawable(
                        yearnViewHolder.getImageView().getContext(),
                        mContextualYearns.get(position).getDrawable()));

        yearnViewHolder.getOverallView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ResultActivity.class);

                intent.putExtra(ResultActivity.EXTRA_YEARN, "Lunch");
                intent.putExtra(ResultActivity.EXTRA_LATITUDE, 51.505203);
                intent.putExtra(ResultActivity.EXTRA_LONGITUDE, -0.224475);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContextualYearns.size();
    }
}
