package com.jamieadkins.yearn;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.awareness.state.Weather;
import com.google.maps.model.PlaceType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Basic model of a yearn.
 */

public class Yearn implements Parcelable {
    private static final int NO_QUERY_KEYWORD = -1;

    private static final int EVENING_END = 4;
    private static final int MORNING_END = 12;
    private static final int AFTERNOON_END = 17;

    private static final Yearn BREAKFAST = new Yearn(R.string.breakfast, R.drawable.ic_local_dining, R.string.breakfast, PlaceType.CAFE);
    private static final Yearn COFFEE = new Yearn(R.string.coffee, R.drawable.ic_local_cafe, R.string.coffee, PlaceType.CAFE);
    private static final Yearn LUNCH = new Yearn(R.string.lunch, R.drawable.ic_local_dining, PlaceType.RESTAURANT);
    private static final Yearn AFTERNOON_DRINKS = new Yearn(R.string.afternoon_drinks, R.drawable.ic_local_bar, PlaceType.BAR);
    private static final Yearn DINNER = new Yearn(R.string.dinner, R.drawable.ic_local_dining, PlaceType.RESTAURANT);
    private static final Yearn TAKEAWAY = new Yearn(R.string.takeaway, R.drawable.ic_local_dining, PlaceType.MEAL_TAKEAWAY);
    private static final Yearn BAR = new Yearn(R.string.bar, R.drawable.ic_local_bar, PlaceType.BAR);
    private static final Yearn SPORTS_BAR = new Yearn(R.string.sports_bar, R.drawable.ic_local_bar, R.string.sports_bar, PlaceType.BAR);
    private static final Yearn DRINKS_IN_THE_SUN = new Yearn(R.string.afternoon_drinks, R.drawable.ic_local_bar, PlaceType.BAR);
    private static final Yearn NIGHT_CLUB = new Yearn(R.string.night_club, R.drawable.ic_local_bar, PlaceType.NIGHT_CLUB);
    private static final Yearn PICNIC = new Yearn(R.string.picnic, R.drawable.ic_local_dining, PlaceType.PARK);
    private static final Yearn MOVIE = new Yearn(R.string.movie, R.drawable.ic_local_movies, PlaceType.MOVIE_THEATER);

    private static final Yearn SNOW = new Yearn(R.string.yearn_snow, R.drawable.ic_local_florist, PlaceType.PARK);
    private static final Yearn STAYING_INSIDE = new Yearn(R.string.staying_indoors, R.drawable.ic_hotel, PlaceType.MEAL_TAKEAWAY);

    public static final Yearn[] GENERAL_YEARNS = new Yearn[] {
            new Yearn(R.string.yearn_food, R.drawable.ic_local_dining, PlaceType.FOOD),
            new Yearn(R.string.yearn_drink, R.drawable.ic_local_drink, PlaceType.CAFE),
            COFFEE,
            new Yearn(R.string.yearn_park, R.drawable.ic_local_florist, PlaceType.PARK),
            new Yearn(R.string.yearn_atm, R.drawable.ic_local_atm, PlaceType.ATM),
            new Yearn(R.string.yearn_activity, R.drawable.ic_local_activity, PlaceType.AMUSEMENT_PARK),
            new Yearn(R.string.yearn_night_out, R.drawable.ic_local_bar, PlaceType.NIGHT_CLUB),
            new Yearn(R.string.yearn_movie, R.drawable.ic_local_movies, PlaceType.MOVIE_THEATER),
    };

    public enum TimeOfDay {
        MORNING,
        AFTERNOON,
        EVENING
    }

    private int mTitleId;
    private int mDrawableId;
    private int mQueryId = NO_QUERY_KEYWORD;
    private PlaceType mPlaceType;

    public Yearn(int titleId, int drawable, PlaceType placeType) {
        this(titleId, drawable, NO_QUERY_KEYWORD, placeType);
    }

    public Yearn(int titleId, int drawable, int queryId, PlaceType placeType) {
        mTitleId = titleId;
        mDrawableId = drawable;
        mQueryId = queryId;
        mPlaceType = placeType;
    }

    private Yearn(Parcel in) {
        mTitleId = in.readInt();
        mDrawableId = in.readInt();
        mQueryId = in.readInt();
        mPlaceType = (PlaceType) in.readSerializable();
    }

    public static final Parcelable.Creator<Yearn> CREATOR
            = new Parcelable.Creator<Yearn>() {
        public Yearn createFromParcel(Parcel in) {
            return new Yearn(in);
        }

        public Yearn[] newArray(int size) {
            return new Yearn[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mTitleId);
        parcel.writeInt(mDrawableId);
        parcel.writeInt(mQueryId);
        parcel.writeSerializable(mPlaceType);
    }

    public String getTitle(Context context) {
        return context.getString(mTitleId);
    }

    public int getDrawable() {
        return mDrawableId;
    }

    public PlaceType getPlaceType() {
        return mPlaceType;
    }

    public String getQueryKeyword(Context context) {
        if (mQueryId == NO_QUERY_KEYWORD) {
            return null;
        } else {
            return context.getString(mQueryId);
        }
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

    public static List<Yearn> getContextualYearns(int weather, float temperature) {
        ArrayList<Yearn> contextualYearns = new ArrayList<>();
        switch (weather) {
            case Weather.CONDITION_CLEAR:
                contextualYearns.add(DRINKS_IN_THE_SUN);
                break;
            case Weather.CONDITION_SNOWY:
                contextualYearns.add(SNOW);
                // Deliberate fall through to get the staying inside yearn.
            case Weather.CONDITION_ICY:
            case Weather.CONDITION_STORMY:
            case Weather.CONDITION_RAINY:
            case Weather.CONDITION_WINDY:
                contextualYearns.add(STAYING_INSIDE);
                break;
        }

        return contextualYearns;
    }

    public static TimeOfDay getTimeFromHourOfDay(int hourOfDay) {
        if (hourOfDay >= 0 && hourOfDay < EVENING_END) {
            return TimeOfDay.EVENING;
        } else if (hourOfDay >= EVENING_END && hourOfDay < MORNING_END) {
            return TimeOfDay.MORNING;
        } else if (hourOfDay >= MORNING_END && hourOfDay < AFTERNOON_END) {
            return TimeOfDay.AFTERNOON;
        } else if (hourOfDay >= AFTERNOON_END && hourOfDay < 24) {
            return TimeOfDay.EVENING;
        } else {
            throw new RuntimeException("Received an hour of the day that wasn't between 0 and 24");
        }
    }
}
