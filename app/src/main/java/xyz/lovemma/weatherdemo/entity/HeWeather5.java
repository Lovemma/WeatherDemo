package xyz.lovemma.weatherdemo.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by OO on 2017/5/19.
 */

public class HeWeather5 {
    @SerializedName("aqi")
    private Aqi aqi;
    @SerializedName("basic")
    private Basic basic;
    @SerializedName("now")
    private Now now;
    @SerializedName("status")
    public String status;
    @SerializedName("suggestion")
    private Suggestion suggestion;
    @SerializedName("daily_forecast")
    private List<DailyForecast> daily_forecast;
    @SerializedName("hourly_forecast")
    private List<HourlyForecast> hourly_forecast;

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public List<DailyForecast> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecast> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public List<HourlyForecast> getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(List<HourlyForecast> hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }
}
