package xyz.lovemma.weatherdemo.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.lovemma.weatherdemo.entity.Weather;

/**
 * Created by OO on 2017/5/21.
 */

public interface Api {
    @GET("/v5/weather")
    Observable<Weather> getWeatherList(@Query("city")  String c, @Query("key")String key);
}
