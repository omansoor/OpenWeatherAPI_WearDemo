package com.learning.weatherapidemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.learning.weatherapidemo.databinding.ActivityMainBinding;
import com.learning.weatherapidemo.json.Forecast;
import com.learning.weatherapidemo.json.WeatherItem;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends Activity {

    private Button mButtonStart;
    private TextView mTextView;
    private ActivityMainBinding binding;
    private List<WeatherItem> weatherItems;
    WeatherLooperThread t;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mButtonStart = binding.buttonStart;
        mTextView = binding.textview;
        t = new WeatherLooperThread();
        t.start();
    }

    public void startGETLoop(View view) {
        t.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    if (!t.isInterrupted())
                        GETLoop();
                    SystemClock.sleep(60000);
                }
            }
        },0);

//        GETLoop();
    }

    private void GETLoop(){

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
                if(response.isSuccessful()) {
                    Forecast fr = response.body();
                    if (fr != null) {
                        weatherItems = fr.getList();
                    }
                    if(weatherItems!=null && !weatherItems.isEmpty())
                        Log.d(TAG, "onResponse: " + weatherItems.get(0).getDtTxt());
                    else
                        Log.d(TAG, "onResponse: Successfully completed GET");
                }
                else
                    Log.d(TAG, "onResponse: Failure");
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void stopGETLoop(View view) {
        t.interrupt();
    }
}