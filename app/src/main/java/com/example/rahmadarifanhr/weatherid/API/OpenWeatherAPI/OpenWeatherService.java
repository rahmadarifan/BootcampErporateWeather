package com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI;

import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelForecast;
import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelWeatherToday;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ifan on 8/23/2017.
 */

public interface OpenWeatherService {
    @GET("weather")
    Call<DataModelWeatherToday.Example> getWeatherToday(
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("units") String units,
            @Query("appid") String appid);

    @GET("forecast")
    Call<DataModelForecast.Example> getForecast(
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("weather")
    Call<DataModelWeatherToday.Example> getWeatherTodayByCity(
            @Query("q") String cityname,
            @Query("units") String units,
            @Query("appid") String appid);
}
