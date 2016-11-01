package com.jamieadkins.yearn.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.yearn.R;
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
        final YearnViewHolder yearnViewHolder = (YearnViewHolder) viewHolder;
        yearnViewHolder.bindYearn(mContextualYearns.get(position));
    }

    @Override
    public int getItemCount() {
        return mContextualYearns.size();
    }

    protected void addYearns(List<Yearn> newYearns) {
        mContextualYearns.addAll(newYearns);
        notifyDataSetChanged();
    }
}
