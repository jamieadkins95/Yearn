package com.jamieadkins.yearn.ui.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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

public class QueryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0;
    private static final int CONTEXTUAL_YEARN = 1;
    private static final int GENERAL_YEARN = 2;

    private static final int CONTEXTUAL_HEADER_POSITION = 0;
    private static final int CONTEXTUAL_YEARN_LIST_POSITION = 1;
    private static final int SOMETHING_ELSE_HEADER_POSITION = 2;

    // Adjustment required so that getting yearns from the list is correct.
    // 2 headers + 1 horizontal yearn list = 3.
    // 2 headers + 1 horizontal yearn list = 3.
    private static final int INDEX_ADJUSTMENT = 3;

    private RecyclerViewHeader mContextualHeader;
    private RecyclerViewHeader mSomethingElseHeader;
    private List<Yearn> mContextualYearns;
    private List<Yearn> mGeneralYearns;

    public QueryRecyclerViewAdapter(RecyclerViewHeader contextualHeader,
                                    RecyclerViewHeader somethingElseHeader,
                                    List<Yearn> contextualYearns,
                                    List<Yearn> generalYearns) {
        mContextualHeader = contextualHeader;
        mSomethingElseHeader = somethingElseHeader;
        mContextualYearns = contextualYearns;
        mGeneralYearns = generalYearns;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case CONTEXTUAL_HEADER_POSITION:
            case SOMETHING_ELSE_HEADER_POSITION:
                return HEADER;
            case CONTEXTUAL_YEARN_LIST_POSITION:
                return CONTEXTUAL_YEARN;
            default:
                return GENERAL_YEARN;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HEADER:
                View headerView = inflater.inflate(R.layout.item_header, parent, false);
                viewHolder = new HeaderViewHolder(headerView);
                break;
            case CONTEXTUAL_YEARN:
                // Inner recycler view contain contextual yearns
                View contextualYearns = inflater.inflate(R.layout.item_horizontal_yearn_recycler_view, parent, false);
                viewHolder = new InnerRecyclerViewViewHolder(contextualYearns);
                break;
            case GENERAL_YEARN:
                View generalYearns = inflater.inflate(R.layout.item_simple_yearn, parent, false);
                viewHolder = new YearnViewHolder(generalYearns);
                break;
            default:
                throw new RuntimeException("View type not implemented!");
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                configureHeaderViewHolder(headerViewHolder, position);
                break;
            case CONTEXTUAL_YEARN:
                InnerRecyclerViewViewHolder contextualYearnViewHolder = (InnerRecyclerViewViewHolder) viewHolder;
                configureInnerRecyclerViewViewHolder(contextualYearnViewHolder, position);
                break;
            case GENERAL_YEARN:
                YearnViewHolder generalYearnViewHolder = (YearnViewHolder) viewHolder;
                configureGeneralYearnViewHolder(generalYearnViewHolder, position);
                break;
            default:
                throw new RuntimeException("View type not implemented!");
        }
    }

    private void configureInnerRecyclerViewViewHolder(
            InnerRecyclerViewViewHolder contextualYearnViewHolder, int position) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                contextualYearnViewHolder.getRecyclerView().getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        contextualYearnViewHolder.getRecyclerView().setLayoutManager(layoutManager);
        ContextualYearnRecyclerViewAdapter contextualYearnRecyclerViewAdapter =
                new ContextualYearnRecyclerViewAdapter(mContextualYearns);
        contextualYearnViewHolder.getRecyclerView().setAdapter(contextualYearnRecyclerViewAdapter);
    }

    private void configureGeneralYearnViewHolder(final YearnViewHolder generalYearnViewHolder,
                                                 int position) {
        int adjustedPosition = getAdjustedPosition(position);
        Context context = generalYearnViewHolder.getOverallView().getContext();
        generalYearnViewHolder.getTextView().setText(mGeneralYearns.get(adjustedPosition).getTitleId());

        // TODO: Should potentially use glide here to handle image.
        generalYearnViewHolder.getImageView().setImageDrawable(
                ContextCompat.getDrawable(
                        context, mGeneralYearns.get(adjustedPosition).getDrawable()));

        generalYearnViewHolder.getOverallView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ResultActivity.class);
                // Viewholder needs a bound query to access here.

                context.startActivity(intent);
            }
        });
    }

    private int getAdjustedPosition(int position) {
        return position - INDEX_ADJUSTMENT;
    }

    private void configureHeaderViewHolder(HeaderViewHolder headerViewHolder, int position) {
        if (position == CONTEXTUAL_HEADER_POSITION) {
            headerViewHolder.getPrimaryText().setText(mContextualHeader.getPrimaryText());
            headerViewHolder.getSecondaryText().setText(mContextualHeader.getSecondaryText());
        } else if (position == SOMETHING_ELSE_HEADER_POSITION) {
            headerViewHolder.getPrimaryText().setText(mSomethingElseHeader.getPrimaryText());
            headerViewHolder.getSecondaryText().setText(mSomethingElseHeader.getSecondaryText());
        } else {
            throw new RuntimeException("HeaderViewHolder not implemented!");
        }
    }

    @Override
    public int getItemCount() {
        return mGeneralYearns.size() + INDEX_ADJUSTMENT;
    }
}
