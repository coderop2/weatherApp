package com.example.android.weatheria.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.weatheria.utils.dateutils;

/**
 * Created by dayalkhandelwal on 04/02/18.
 */

public class WeatherContract {
    public  final static String CONTENT_AUTHORITY="com.example.android.weatheria";
    public  final static Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public  final static String PATH="weather";

    public static final class WeatherEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();
        public static final String TABLE_NAME="weather";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_WEATHERID="weather_id";
        public static final String COLUMN_MIN_TEMP="min_temp";
        public static final String COLUMN_MAX_TEMP="max_temp";
        public static final String COLUMN_HUMIDIDTY="humidity";
        public static final String COLUMN_PRESSURE="pressure";
        public static final String COLUMN_WIND_SPEED="wind_speed";
        public static final String COLUMN_DEGREES="degrees";
        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }
        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = dateutils.normalizeDate(System.currentTimeMillis());
            return WeatherContract.WeatherEntry.COLUMN_DATE + " >= " + normalizedUtcNow;
        }
    }
}
