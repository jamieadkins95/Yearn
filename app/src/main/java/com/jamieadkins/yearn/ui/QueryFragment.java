package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.common.api.ResultCallback;
import com.jamieadkins.yearn.activities.QueryActivity;
import com.jamieadkins.yearn.R;
import com.jamieadkins.yearn.Yearn;
import com.jamieadkins.yearn.ui.recyclerview.QueryRecyclerViewAdapter;
import com.jamieadkins.yearn.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment implements ResultCallback<WeatherResult> {
    private QueryRecyclerViewAdapter mYearnAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((QueryActivity) context).registerWeatherListener(this);
    }

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

        // Get the context specific yearns.
        List<Yearn> contextualYearns = Yearn.getContextualYearns(rightNow.get(Calendar.DAY_OF_WEEK),
                Yearn.getTimeFromHourOfDay(rightNow.get(Calendar.HOUR_OF_DAY)));

        // Organise all of the general yearns.
        ArrayList<Yearn> generalYearns = new ArrayList<>();
        for (Yearn yearn : Yearn.GENERAL_YEARNS) {
            generalYearns.add(yearn);
        }

        // Give it all to the adapter.
        mYearnAdapter = new QueryRecyclerViewAdapter(contextualYearns, generalYearns);
        mYearnAdapter.setCurrentTime(
                getWeekdayFromCalendar(getActivity(), rightNow.get(Calendar.DAY_OF_WEEK)),
                getTimeFromCalendar(getActivity(), rightNow.get(Calendar.HOUR_OF_DAY)));
        recyclerView.setAdapter(mYearnAdapter);
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

    @Override
    public void onResult(@NonNull WeatherResult weatherResult) {
        mYearnAdapter.updateWithWeatherStatus(
                WeatherUtils.getWeatherDescription(getActivity(),
                        weatherResult.getWeather().getConditions()));
    }
}
