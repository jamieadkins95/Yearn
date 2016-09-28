package com.jamieadkins.yearn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Basic model of a yearn.
 */

public class Yearn {
    private static final int EVENING_END = 4;
    private static final int MORNING_END = 12;
    private static final int AFTERNOON_END = 17;

    private static final Yearn BREAKFAST = new Yearn(R.string.breakfast, R.drawable.ic_local_dining);
    private static final Yearn COFFEE = new Yearn(R.string.coffee, R.drawable.ic_local_cafe);
    private static final Yearn LUNCH = new Yearn(R.string.lunch, R.drawable.ic_local_dining);
    private static final Yearn AFTERNOON_DRINKS = new Yearn(R.string.afternoon_drinks, R.drawable.ic_local_bar);
    private static final Yearn DINNER = new Yearn(R.string.dinner, R.drawable.ic_local_dining);
    private static final Yearn TAKEAWAY = new Yearn(R.string.takeaway, R.drawable.ic_local_dining);
    private static final Yearn BAR = new Yearn(R.string.bar, R.drawable.ic_local_bar);
    private static final Yearn SPORTS_BAR = new Yearn(R.string.sports_bar, R.drawable.ic_local_bar);
    private static final Yearn DRINKS_IN_THE_SUN = new Yearn(R.string.afternoon_drinks, R.drawable.ic_local_bar);
    private static final Yearn NIGHT_CLUB = new Yearn(R.string.night_club, R.drawable.ic_local_bar);
    private static final Yearn PICNIC = new Yearn(R.string.picnic, R.drawable.ic_local_dining);
    private static final Yearn MOVIE = new Yearn(R.string.movie, R.drawable.ic_local_movies);

    public enum TimeOfDay {
        MORNING,
        AFTERNOON,
        EVENING
    }

    private int mTitleId;
    private int mDrawableId;

    public Yearn(int titleId, int drawable) {
        mTitleId = titleId;
        mDrawableId = drawable;
    }

    public int getTitleId() {
        return mTitleId;
    }

    public int getDrawable() {
        return mDrawableId;
    }

    public static List<Yearn> getContextualYearns(int dayOfWeek, TimeOfDay timeOfDay) {
        ArrayList<Yearn> contextualYearns = new ArrayList<>();
        switch (dayOfWeek) {
            case Calendar.MONDAY:
            case Calendar.TUESDAY:
            case Calendar.WEDNESDAY:
            case Calendar.THURSDAY:
            case Calendar.FRIDAY:
                switch (timeOfDay) {
                    case MORNING:
                        contextualYearns.add(BREAKFAST);
                        contextualYearns.add(COFFEE);
                        contextualYearns.add(LUNCH);
                        break;
                    case AFTERNOON:
                        contextualYearns.add(LUNCH);
                        contextualYearns.add(COFFEE);
                        contextualYearns.add(AFTERNOON_DRINKS);
                        contextualYearns.add(DINNER);
                        break;
                    case EVENING:
                        contextualYearns.add(DINNER);
                        contextualYearns.add(TAKEAWAY);
                        contextualYearns.add(BAR);
                        break;
                }
                break;
            case Calendar.SATURDAY:
                switch (timeOfDay) {
                    case MORNING:
                        contextualYearns.add(BREAKFAST);
                        contextualYearns.add(COFFEE);
                        contextualYearns.add(LUNCH);
                        contextualYearns.add(SPORTS_BAR);
                        break;
                    case AFTERNOON:
                        contextualYearns.add(LUNCH);
                        contextualYearns.add(COFFEE);
                        contextualYearns.add(SPORTS_BAR);
                        contextualYearns.add(AFTERNOON_DRINKS);
                        contextualYearns.add(DINNER);
                        break;
                    case EVENING:
                        contextualYearns.add(DINNER);
                        contextualYearns.add(NIGHT_CLUB);
                        contextualYearns.add(BAR);
                        break;
                }
                break;
            case Calendar.SUNDAY:
                switch (timeOfDay) {
                    case MORNING:
                        contextualYearns.add(BREAKFAST);
                        contextualYearns.add(COFFEE);
                        contextualYearns.add(LUNCH);
                        contextualYearns.add(SPORTS_BAR);
                        break;
                    case AFTERNOON:
                        contextualYearns.add(LUNCH);
                        contextualYearns.add(COFFEE);
                        contextualYearns.add(SPORTS_BAR);
                        contextualYearns.add(AFTERNOON_DRINKS);
                        contextualYearns.add(PICNIC);
                        break;
                    case EVENING:
                        contextualYearns.add(DINNER);
                        contextualYearns.add(TAKEAWAY);
                        contextualYearns.add(MOVIE);
                        break;
                }
                break;
        }

        return contextualYearns;
    }

    public static TimeOfDay getTimeFromHourOfDay(int hourOfDay) {
        if (hourOfDay > 0 && hourOfDay < EVENING_END) {
            return TimeOfDay.EVENING;
        } else if (hourOfDay >= EVENING_END && hourOfDay < MORNING_END) {
            return TimeOfDay.MORNING;
        } else if (hourOfDay >= MORNING_END && hourOfDay < AFTERNOON_END) {
            return TimeOfDay.AFTERNOON;
        } else if (hourOfDay >= AFTERNOON_END && hourOfDay <= 24) {
            return TimeOfDay.EVENING;
        } else {
            throw new RuntimeException("Received an hour of the day that wasn't between 0 and 24");
        }
    }
}