package xyz.lovemma.weatherdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.lovemma.weatherdemo.entity.Weather;

/**
 * Created by OO on 2017/5/21.
 */

public interface Api {
    @GET("/v5/weather")
    Call<Weather> getWeatherList(@Query("city")  String c,@Query("key")String key);
}
