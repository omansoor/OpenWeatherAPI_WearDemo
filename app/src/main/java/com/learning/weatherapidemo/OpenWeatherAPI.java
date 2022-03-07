package com.learning.weatherapidemo;

import com.learning.weatherapidemo.json.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherAPI {
    @GET("forecast")
    Call<Forecast> getFiveDayThreeHoursForecast(@Query("id") int cityId, @Query("appid") String apiKey);
}
