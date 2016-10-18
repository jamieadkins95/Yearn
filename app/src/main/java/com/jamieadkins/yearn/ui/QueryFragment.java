package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
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
import com.jamieadkins.yearn.ResultActivity;
import com.jamieadkins.yearn.Yearn;
import com.jamieadkins.yearn.ui.recyclerview.QueryRecyclerViewAdapter;
import com.jamieadkins.yearn.ui.recyclerview.RecyclerViewHeader;
import com.jamieadkins.yearn.utils.LocationFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment implements LocationFragment.LocationFetchListener {
    private QueryActivity mActivity;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mYearnAdapter;

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

        // Get current day and time for the contextual header.
        Calendar rightNow = Calendar.getInstance();
        final String weekday = getWeekdayFromCalendar(getActivity(), rightNow.get(Calendar.DAY_OF_WEEK));
        final String timeOfDay = getTimeFromCalendar(getActivity(), rightNow.get(Calendar.HOUR_OF_DAY));
        RecyclerViewHeader contextualHeader = new RecyclerViewHeader(
                String.format(getString(R.string.day_header), weekday, timeOfDay),
                getString(R.string.im_yearning_for));

        // Simple header that says "Something Else"
        RecyclerViewHeader somethingElseHeader = new RecyclerViewHeader(
                getString(R.string.something_else), "");

        // Get the context specific yearns.
        List<Yearn> contextualYearns = Yearn.getContextualYearns(rightNow.get(Calendar.DAY_OF_WEEK),
                Yearn.getTimeFromHourOfDay(rightNow.get(Calendar.HOUR_OF_DAY)));

        // Organise all of the general yearns.
        ArrayList<Yearn> generalYearns = new ArrayList<>();

        TypedArray yearnTypes = getResources().obtainTypedArray(R.array.yearn_types);
        TypedArray icons = getResources().obtainTypedArray(R.array.yearn_icons);
        for (int i = 0; i < icons.length(); i++) {
            generalYearns.add(new Yearn(yearnTypes.getResourceId(i, -1), icons.getResourceId(i, -1)));
        }

        // Recycle typed array.
        yearnTypes.recycle();
        icons.recycle();

        // Give it all to the adapter.
        mYearnAdapter = new QueryRecyclerViewAdapter(contextualHeader,
                somethingElseHeader,
                contextualYearns, generalYearns);
        recyclerView.setAdapter(mYearnAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (QueryActivity) context;
        mActivity.registerListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onLocationFound(Location location) {
        //mYearnAdapter.setLocation(location);
    }

    private static String getWeekdayFromCalendar(Context context, int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return context.getString(R.string.monday);
            case Calendar.TUESDAY:
                return context.getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return context.getString(R.string.thursday);
            case Calendar.FRIDAY:
                return context.getString(R.string.friday);
            case Calendar.SATURDAY:
                return context.getString(R.string.saturday);
            case Calendar.SUNDAY:
                return context.getString(R.string.sunday);
            default:
                throw new RuntimeException("Received a weekday that wasn't a day of the week!");
        }
    }

    private static String getTimeFromCalendar(Context context, int hourOfDay) {
        switch (Yearn.getTimeFromHourOfDay(hourOfDay)) {
            case MORNING:
                return context.getString(R.string.morning);
            case AFTERNOON:
                return context.getString(R.string.afternoon);
            case EVENING:
                return context.getString(R.string.evening);
            default:
                throw new RuntimeException("Time of day not implemented!");
        }
    }
}
