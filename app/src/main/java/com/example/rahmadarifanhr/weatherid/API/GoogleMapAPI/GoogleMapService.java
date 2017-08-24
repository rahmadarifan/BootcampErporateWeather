package com.example.rahmadarifanhr.weatherid.API.GoogleMapAPI.Model;

import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelForecast;
import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelWeatherToday;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.R.attr.key;

/**
 * Created by Ifan on 8/23/2017.
 */

public interface GoogleMapService {
    @GET("/maps/api/geocode/json")
    Call<DataModelGeoCode.Example> getGeocodeData(
            @Query("latlng") String latlng,
            @Query("key") String key
    );

    @GET("/maps/api/place/details/json")
    Call<DataModelPlace.Example> getPlaceData(
            @Query("placeid") String placeID,
            @Query("key") String key
    );
}
