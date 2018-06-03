package com.example.android.weatheria.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by dayalkhandelwal on 08/02/18.
 */

public class WeatherIntentService extends IntentService {

    WeatherIntentService()
    {
        super("this is a Intent Service");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action=intent.getAction();
        if(action!=null)
        {
            WeatherSyncTask.syncData(this);
        }
    }
}
