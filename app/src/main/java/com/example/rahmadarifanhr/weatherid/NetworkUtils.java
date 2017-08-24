package com.example.rahmadarifanhr.weatherid;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ifan on 8/22/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/forecast?";

    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;

    private static final String units = "metric";
    private static final String appid= "653b2fb992e6cfe8a01f9e59c9159ec4";
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String UNITS_PARAM = "units";
    final static String APP_ID = "appid";

    public static URL buildUrl(String lat, String lon) {

        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(LAT_PARAM, lat)
                .appendQueryParameter(LON_PARAM, lon)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(APP_ID, appid)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
}

