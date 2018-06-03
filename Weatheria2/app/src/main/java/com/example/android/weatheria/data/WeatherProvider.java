package com.example.android.weatheria.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.weatheria.utils.dateutils;

import static com.example.android.weatheria.data.WeatherProvider.buildUriMatcher;

/**
 * Created by dayalkhandelwal on 04/02/18.
 */

public class WeatherProvider extends ContentProvider {

    private WeatherDbHelper database;
    private UriMatcher sUriMatcher=buildUriMatcher();
    public static final int CODE_WEATHER=100;
    public static final int CODE_WEATHER_WITH_DATE=101;

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH,CODE_WEATHER);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY,WeatherContract.PATH+"/#",CODE_WEATHER_WITH_DATE);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        database=new WeatherDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase sql=database.getWritableDatabase();
        int rowsinserted=0;
        switch (sUriMatcher.match(uri)) {
            case CODE_WEATHER:
                sql.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long weatherDate =
                                value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
                        if (!dateutils.isDateNormalized(weatherDate)) {
                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }
                        long rownums = sql.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (rownums != -1)
                            rowsinserted++;
                    }
                    sql.setTransactionSuccessful();
                }
                finally {
                    sql.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return rowsinserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor=null;
        SQLiteDatabase sqldb=database.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        switch(match)
        {
            case CODE_WEATHER:
                cursor=sqldb.query(WeatherContract.WeatherEntry.TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            case CODE_WEATHER_WITH_DATE:
                String id=uri.getLastPathSegment();
                String[] selectionArguments = new String[]{id};

                cursor = sqldb.query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        strings,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        s1);
                break;
            default:
                throw new UnsupportedOperationException("This was a unknown uri "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int rowsdeleted=0;
        SQLiteDatabase sql=database.getWritableDatabase();
        if(s==null)
            s="1";
        switch (sUriMatcher.match(uri))
        {
            case CODE_WEATHER:
                break;
            case CODE_WEATHER_WITH_DATE:
                String date=uri.getLastPathSegment();
                rowsdeleted=sql.delete(WeatherContract.WeatherEntry.TABLE_NAME,s,strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsdeleted!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rowsdeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
