package com.example.android.weatheria.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.android.weatheria.R;
import com.example.android.weatheria.data.WeatherContract;

/**
 * Created by dayalkhandelwal on 09/02/18.
 */

public class NotificationClass {
    public static final String[] WEATHER_NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_WEATHERID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
    };

    public static final int INDEX_WEATHER_ID = 0;
    public static final int INDEX_MAX_TEMP = 1;
    public static final int INDEX_MIN_TEMP = 2;
    private static final int WEATHER_NOTIFICATION_ID=1234;

    public static void notifyUserrsOfWeather(Context context)
    {
        Uri uri= WeatherContract.WeatherEntry.buildWeatherUriWithDate(dateutils.normalizeDate(System.currentTimeMillis()));
        Cursor cursor = context.getContentResolver().query(
                uri,
                WEATHER_NOTIFICATION_PROJECTION,
                null,
                null,
                null);
        if(cursor.moveToFirst())
        {
            int id=cursor.getInt(INDEX_WEATHER_ID);
            double max=cursor.getDouble(INDEX_MAX_TEMP);
            double min=cursor.getDouble(INDEX_MIN_TEMP);
            Resources resources = context.getResources();
            int largeArtResourceId = weathercontent
                    .getLargeArtResourceIdForWeatherCondition(id);

            Bitmap largeIcon = BitmapFactory.decodeResource(
                    resources,
                    largeArtResourceId);

            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = getNotificationText(context, id, max,min);
        }
    }
    private static String getNotificationText(Context context, int weatherId, double high, double low) {
        String shortDescription = weathercontent
                .getStringForWeatherCondition(context, weatherId);

        String notificationFormat = context.getString(R.string.format_notification);
        String notificationText = String.format(notificationFormat,
                shortDescription,
                weathercontent.formatTemperature(context, high),
                weathercontent.formatTemperature(context, low));

        return notificationText;
    }
}
