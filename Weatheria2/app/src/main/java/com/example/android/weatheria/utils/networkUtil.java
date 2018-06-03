package com.example.android.weatheria.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.weatheria.data.contants_funcs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Harshitkhandelwal on 20/09/17.
 */

public class networkUtil {
    private final static String CLASS_TAG= networkUtil.class.getSimpleName();
    private static final String DYNAMIC_WEATHER_URL = "https://andfun-weather.udacity.com/weather";

    private static final String STATIC_WEATHER_URL = "https://andfun-weather.udacity.com/staticweather";

    private static final String BASE_URL = STATIC_WEATHER_URL;
    private static final String format = "json";
    private static final String units = "metric";
    private static final int numDays = 21;

    final static String queryP = "q";
    final static String latitudeP = "lat";
    final static String longitudeP = "lon";
    final static String formatP = "mode";
    final static String unitsP = "units";
    final static String daysP = "cnt";

    public static URL getUrl(Context context) {
        if (contants_funcs.isLocationLatLonAvailable(context)) {
            double[] preferredCoordinates = contants_funcs.getLocationCoordinates(context);
            double latitude = preferredCoordinates[0];
            double longitude = preferredCoordinates[1];
            return buildUrlWithLatitudeLongitude(latitude, longitude);
        } else {
            String locationmadeurl = contants_funcs.getPreferredWeatherLocation(context);
            return buildUrlWithLocationQuery(locationmadeurl);
        }
    }
    private static URL buildUrlWithLocationQuery(String locationQuery) {
        Uri weatherQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(queryP, locationQuery)
                .appendQueryParameter(formatP, format)
                .appendQueryParameter(unitsP, units)
                .appendQueryParameter(daysP, Integer.toString(numDays))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(CLASS_TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static URL buildUrlWithLatitudeLongitude(Double latitude, Double longitude) {
        Uri weatherUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(latitudeP, String.valueOf(latitude))
                .appendQueryParameter(longitudeP, String.valueOf(longitude))
                .appendQueryParameter(formatP, format)
                .appendQueryParameter(unitsP, units)
                .appendQueryParameter(daysP, Integer.toString(numDays))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherUri.toString());
            Log.v(CLASS_TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static URL buildUrl(String locationQuery) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(queryP, locationQuery)
                .appendQueryParameter(formatP, format)
                .appendQueryParameter(unitsP, units)
                .appendQueryParameter(daysP, Integer.toString(numDays))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(CLASS_TAG, "This is the URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
