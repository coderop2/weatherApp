package com.example.android.weatheria;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static android.R.attr.id;
import static android.R.attr.start;
import static android.R.attr.x;
import static android.provider.LiveFolders.INTENT;

/**
 * Created by dayalkhandelwal on 29/01/18.
 */

public class single_detailed_weather extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView textView;
    private String s;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        textView=(TextView)findViewById(R.id.text_1);
        Intent getIntent=getIntent();
        if(getIntent.hasExtra(Intent.EXTRA_TEXT)) {
            s = getIntent.getStringExtra(Intent.EXTRA_TEXT);
            textView.setText(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        MenuItem meniitem=menu.findItem(R.id.share);
        Intent intent= ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(s+FORECAST_SHARE_HASHTAG)
                .getIntent();
        meniitem.setIntent(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.go_to_settings2:
                Intent intent=new Intent(this, settingsActivity.class);
                startActivity(intent);
        }
        return true;
    }
}
