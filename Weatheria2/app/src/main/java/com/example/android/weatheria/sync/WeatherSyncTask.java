package com.example.android.weatheria.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.example.android.weatheria.data.WeatherContract;
import com.example.android.weatheria.data.contants_funcs;
import com.example.android.weatheria.utils.NotificationClass;
import com.example.android.weatheria.utils.jsonresponse;
import com.example.android.weatheria.utils.networkUtil;
import com.example.android.weatheria.utils.weathercontent;

import java.net.URL;

/**
 * Created by dayalkhandelwal on 08/02/18.
 */

public class WeatherSyncTask {
    synchronized public static void syncData(Context context) {

        try {
            URL weatherRequestUrl = networkUtil.getUrl(context);
            String jsonWeatherResponse = networkUtil.getResponseFromHttpUrl(weatherRequestUrl);
            ContentValues[] weatherValues = jsonresponse
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);
            if (weatherValues != null && weatherValues.length != 0) {
                ContentResolver ContentResolver = context.getContentResolver();
                ContentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null);
                ContentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues);
                boolean notificationsEnabled = contants_funcs.areNotificationsEnabled(context);
                long timeSinceLastNotification = contants_funcs
                        .getEllapsedTimeSinceLastNotification(context);

                boolean oneDayPassedSinceLastNotification = false;
                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                    oneDayPassedSinceLastNotification = true;
                }
                if (notificationsEnabled && oneDayPassedSinceLastNotification) {
                    NotificationClass.notifyUserrsOfWeather(context);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
