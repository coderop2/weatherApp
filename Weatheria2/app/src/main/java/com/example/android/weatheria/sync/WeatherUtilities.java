package com.example.android.weatheria.sync;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.drm.DrmInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.TimeUtils;

import com.example.android.weatheria.data.WeatherContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by dayalkhandelwal on 08/02/18.
 */

public class WeatherUtilities {

    private static final String TAG="Syncing-Weatheria";
    private static final int SERVICE_HOURS=3;
    private static final int SERVICE_HOURS_TO_SECONDS=(int) TimeUnit.HOURS.toSeconds(SERVICE_HOURS);
    private static final int SERVICE_SECONDS=SERVICE_HOURS_TO_SECONDS;
    private static boolean bool;
    public static void execute(Context mContext)
    {
        Driver driver=new GooglePlayDriver(mContext);
        FirebaseJobDispatcher dispatcher=new FirebaseJobDispatcher(driver);
        Job createdJob=dispatcher.newJobBuilder()
                .setTag(TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(SERVICE_HOURS_TO_SECONDS,SERVICE_HOURS_TO_SECONDS+SERVICE_SECONDS))
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        dispatcher.schedule(createdJob);
    }
    synchronized public static void initialize(@NonNull final Context context) {
        if (bool) return;

        bool = true;
        execute(context);
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String[] projectionColumns = {WeatherContract.WeatherEntry._ID};
                String selectionStatement = WeatherContract.WeatherEntry
                        .getSqlSelectForTodayOnwards();
                Cursor cursor = context.getContentResolver().query(
                        forecastQueryUri,
                        projectionColumns,
                        selectionStatement,
                        null,
                        null);
                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }
                cursor.close();
            }
        });
        checkForEmpty.start();
    }
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, WeatherIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
