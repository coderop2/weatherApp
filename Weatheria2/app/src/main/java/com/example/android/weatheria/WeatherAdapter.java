package com.example.android.weatheria;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import com.example.android.weatheria.utils.dateutils;
import com.example.android.weatheria.utils.weathercontent;

import static android.R.attr.description;


/**
 * Created by harshitkhandelwal on 29/01/18.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.DataViewHolder>
{
    private final OnListItemClickListener olicl;
    private Context mContext;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean bool;

    public WeatherAdapter(Context cox,OnListItemClickListener mOnListItemclickListener)
    {
        mContext=cox;
        olicl = mOnListItemclickListener;
        bool = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    public interface OnListItemClickListener
    {
        void onlistitemclick(long weather_String);
    }

    private Cursor cursor;

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_1;
                break;
            }

            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_view;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        int id = R.layout.list_item_view;
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position)
    {
        cursor.moveToPosition(position);
        long date=cursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        double max=cursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        double min=cursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
        int s=cursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        String highAndLowTemperature =
                weathercontent.formatHighLows(mContext, max, min);
        String description = weathercontent.getStringForWeatherCondition(mContext, s);
        String dateString = dateutils.getFriendlyDateString(mContext, date, false);
        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;
    }

    @Override
    public int getItemCount()
    {
        if (null == cursor) return 0;
        return cursor.getCount();
    }

    class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView textWeather;

        public DataViewHolder(View view) {
            super(view);
            textWeather = view.findViewById(R.id.date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            long dateinmillis=cursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            olicl.onlistitemclick(dateinmillis);
        }
    }

    public void swapcursor(Cursor mCursor)
    {
        cursor=mCursor;
        notifyDataSetChanged();
    }
}
