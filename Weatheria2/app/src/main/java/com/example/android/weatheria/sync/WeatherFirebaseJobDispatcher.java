package com.example.android.weatheria.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;


/**
 * Created by dayalkhandelwal on 07/02/18.
 */

public class WeatherFirebaseJobDispatcher extends JobService
{
    AsyncTask createAsyncTask;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

       createAsyncTask=new AsyncTask<Void, Void, Void>() {
           @Override
           protected Void doInBackground(Void... objects) {
               Context context = getApplicationContext();
               WeatherSyncTask.syncData(context);
               jobFinished(jobParameters, false);
               return null;
           }

           @Override
           protected void onPostExecute(Void aVoid) {
               jobFinished(jobParameters, false);
           }
       };
        createAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(createAsyncTask!=null)
            createAsyncTask.cancel(true);
        return true;
    }
}
