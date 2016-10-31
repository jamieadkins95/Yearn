package com.jamieadkins.yearn.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.awareness.state.Weather;
import com.jamieadkins.yearn.R;

/**
 * Utils class that deals with weather statuses.
 */

public class WeatherUtils {
    private static final String TAG = WeatherUtils.class.getSimpleName();

    public static String getWeatherDescription(Context context, int[] weatherConditions) {
        String description = "";

        for (int i = 0; i < weatherConditions.length; i++) {
            switch (weatherConditions[i]) {
                case Weather.CONDITION_CLEAR:
                    description += context.getString(R.string.clear);
                    break;
                case Weather.CONDITION_CLOUDY:
                    description += context.getString(R.string.cloudy);
                    break;
                case Weather.CONDITION_FOGGY:
                    description += context.getString(R.string.foggy);
                    break;
                case Weather.CONDITION_HAZY:
                    description += context.getString(R.string.hazy);
                    break;
                case Weather.CONDITION_ICY:
                    description += context.getString(R.string.icy);
                    break;
                case Weather.CONDITION_RAINY:
                    description += context.getString(R.string.rainy);
                    break;
                case Weather.CONDITION_SNOWY:
                    description += context.getString(R.string.snowy);
                    break;
                case Weather.CONDITION_STORMY:
                    description += context.getString(R.string.stormy);
                    break;
                case Weather.CONDITION_WINDY:
                    description += context.getString(R.string.windy);
                    break;
                default:
                    Log.e(TAG, "Don't recognise weather code " + weatherConditions[i]);
                    description += "";
                    break;
            }

            // Only add comma if we are not at the end.
            if (i + 1 != weatherConditions.length) {
                description += ", ";
            }
        }

        return description;
    }
}
