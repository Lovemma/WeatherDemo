package xyz.lovemma.weatherdemo.api;

import xyz.lovemma.weatherdemo.entity.Weather;

/**
 * Created by OO on 2017/6/8.
 */

public interface WeatherCallBack {
    void showWeatherInfo(Weather weather);

    void showError(String e);
}
