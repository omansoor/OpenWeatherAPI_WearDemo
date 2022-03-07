package com.learning.weatherapidemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.learning.weatherapidemo.databinding.ActivityMainBinding;
import com.learning.weatherapidemo.json.Forecast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTextView = binding.text;

        //TODO example API call documentation https://openweathermap.org/forecast5
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherAPI weatherAPIClient = retrofit.create(OpenWeatherAPI.class);
        Call<Forecast> weatherAPICall = weatherAPIClient.getFiveDayThreeHoursForecast(524901,
                "22702184df187b296cb176128ab3f461");

        weatherAPICall.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if(response.isSuccessful())
                    mTextView.setText("Success");
                else
                    mTextView.setText("Unauthorized!");
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                mTextView.setText(t.getMessage());
            }
        });

    }
}