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

public class ContextualYearnRecyclerViewAdapter extends BaseRecyclerViewAdapter {
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
        final YearnViewHolder yearnViewHolder = (YearnViewHolder) viewHolder;
        String text = yearnViewHolder.getTextView().getContext()
                .getString(mContextualYearns.get(position).getTitleId());
        mContextualYearns.get(position).setQueryKeyword(text);
        yearnViewHolder.setBoundYearn(mContextualYearns.get(position));
        yearnViewHolder.getTextView().setText(text);

        // Glide can't handle vector drawables so we have to do this manually.
        yearnViewHolder.getImageView().setImageDrawable(
                ContextCompat.getDrawable(
                        yearnViewHolder.getImageView().getContext(),
                        mContextualYearns.get(position).getDrawable()));

        yearnViewHolder.getOverallView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ResultActivity.class);

                intent.putExtra(ResultActivity.EXTRA_YEARN,
                        yearnViewHolder.getBoundYearn().getQueryKeyword());
                intent.putExtra(ResultActivity.EXTRA_LATITUDE, mLocation.getLatitude());
                intent.putExtra(ResultActivity.EXTRA_LONGITUDE, mLocation.getLongitude());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContextualYearns.size();
    }
}
