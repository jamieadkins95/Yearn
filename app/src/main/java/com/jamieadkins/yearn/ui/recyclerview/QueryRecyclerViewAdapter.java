package com.jamieadkins.yearn.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.Yearn;
import com.jamieadkins.yearn.ui.QueryFragment;

import java.util.Calendar;
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
    private static final int INDEX_ADJUSTMENT = 3;

    private List<Yearn> mContextualYearns;
    private List<Yearn> mGeneralYearns;

    private String mCurrentWeekday;
    private String mCurrentTimeOfDay;

    private String mWeatherDescription;

    private ContextualYearnRecyclerViewAdapter mInnerAdapter;

    public QueryRecyclerViewAdapter(List<Yearn> contextualYearns,
                                    List<Yearn> generalYearns) {
        mContextualYearns = contextualYearns;
        mGeneralYearns = generalYearns;
    }

    public void setCurrentTime(String currentWeekday, String currentTimeOfDay) {
        mCurrentWeekday = currentWeekday;
        mCurrentTimeOfDay = currentTimeOfDay;
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
                // Inner recycler view contain contextual yearns.
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
        mInnerAdapter = new ContextualYearnRecyclerViewAdapter(mContextualYearns);
        contextualYearnViewHolder.getRecyclerView().setAdapter(mInnerAdapter);
    }

    private void configureGeneralYearnViewHolder(final YearnViewHolder yearnViewHolder,
                                                 int position) {
        int adjustedPosition = getAdjustedPosition(position);
        yearnViewHolder.bindYearn(mGeneralYearns.get(adjustedPosition));
    }

    private int getAdjustedPosition(int position) {
        return position - INDEX_ADJUSTMENT;
    }

    private void configureHeaderViewHolder(HeaderViewHolder headerViewHolder, int position) {
        Context context = headerViewHolder.getPrimaryText().getContext();

        if (position == CONTEXTUAL_HEADER_POSITION) {
            String dayHeader;
            if (mWeatherDescription != null) {
                dayHeader = String.format(context.getString(R.string.day_header_with_weather),
                        mCurrentWeekday, mCurrentTimeOfDay, mWeatherDescription);
            } else {
                dayHeader = String.format(context.getString(R.string.day_header),
                        mCurrentWeekday, mCurrentTimeOfDay);
            }
            headerViewHolder.getPrimaryText().setText(dayHeader);
            headerViewHolder.getSecondaryText().setText(context.getString(R.string.im_yearning_for));
        } else if (position == SOMETHING_ELSE_HEADER_POSITION) {
            headerViewHolder.getPrimaryText().setText(context.getString(R.string.something_else));
            headerViewHolder.getSecondaryText().setText("");
        } else {
            throw new RuntimeException("HeaderViewHolder not implemented!");
        }
    }

    @Override
    public int getItemCount() {
        return mGeneralYearns.size() + INDEX_ADJUSTMENT;
    }

    public void updateWithWeatherStatus(String weatherDescription) {
        mWeatherDescription = weatherDescription;
        notifyDataSetChanged();
    }
}
