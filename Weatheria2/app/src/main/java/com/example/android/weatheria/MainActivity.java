package com.example.android.weatheria;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.weatheria.data.WeatherContract;
import com.example.android.weatheria.data.contants_funcs;
import com.example.android.weatheria.sync.WeatherUtilities;



public class MainActivity extends AppCompatActivity implements WeatherAdapter.OnListItemClickListener
        ,LoaderManager.LoaderCallbacks<Cursor>{
    private final String TAG = MainActivity.class.getSimpleName();

    public static final String[] PROJECTION_ARRAY = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHERID,
    };
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;
    TextView getTextView;
    ProgressBar pb;
    RecyclerView recyclerView;
    WeatherAdapter weatherAdapter;
    private static final int LOADER_NUMBER=1;
    private int mPosition=RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_instead_list);

        LinearLayoutManager llm=new LinearLayoutManager(this, LinearLayout.VERTICAL,false);

        recyclerView.setLayoutManager(llm);

        recyclerView.setHasFixedSize(true);

        weatherAdapter=new WeatherAdapter(this,this);

        recyclerView.setAdapter(weatherAdapter);

        pb=(ProgressBar)findViewById(R.id.progress_bar);
        getSupportLoaderManager().initLoader(LOADER_NUMBER,null,this);
        WeatherUtilities.initialize(this);
    }
    public void show()
    {
        recyclerView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
    }
    public void hide()
    {
        recyclerView.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void onlistitemclick(long weatherForDay) {
        Intent i=new Intent(MainActivity.this, single_detailed_weather.class);
        Uri urimade= WeatherContract.WeatherEntry.buildWeatherUriWithDate(weatherForDay);
        i.setData(urimade);
        startActivity(i);
    }

    private void openmaploc()
    {
        double[] coordinates = contants_funcs.getLocationCoordinates(this);
        String position_latitude = Double.toString(coordinates[0]);
        String position_Longitude = Double.toString(coordinates[1]);
        Uri geo_Location = Uri.parse("geo:" + position_latitude + "," + position_Longitude);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geo_Location);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "The following operation was not completed:-"
                    + geo_Location.toString() + ", no receiving apps installed!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.map:
                openmaploc();
                break;
            case R.id.go_to_settings:
                Intent in=new Intent(this,settingsActivity.class);
                startActivity(in);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id)
        {
            case LOADER_NUMBER:
                Uri uri = WeatherContract.WeatherEntry.CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this,
                        uri,
                        PROJECTION_ARRAY,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        weatherAdapter.swapcursor(data);
        if(mPosition==RecyclerView.NO_POSITION)mPosition=0;
        recyclerView.smoothScrollToPosition(mPosition);
        if(data.getCount()!=0)show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        weatherAdapter.swapcursor(null);
    }
}

