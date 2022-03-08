package com.learning.weatherapidemo;

import android.os.Handler;
import android.os.Looper;

public class WeatherLooperThread extends Thread{
    public Handler mHandler;

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new Handler();
        Looper.loop();
    }
}
