package xyz.lovemma.weatherdemo.api;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.lovemma.weatherdemo.entity.Weather;

/**
 * Created by OO on 2017/6/3.
 */

public class RetrofitUtils {
    private static RetrofitUtils INSTANCE;
    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit retrofit;
    private Api mApi;

    public static RetrofitUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitUtils();
                }
            }
        }
        return INSTANCE;
    }

    private RetrofitUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApi = retrofit.create(Api.class);
    }

    public static void fetchWeather(String city, final WeatherCallBack callBack) {
        RetrofitUtils.getInstance().mApi.getWeatherList(city, ApiConfig.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Weather>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Weather weather) {
                        String status = weather.getHeWeather5().get(0).getStatus();
                        if ("ok".equals(status)) {
                            callBack.showWeatherInfo(weather);
                        } else {
                            callBack.showError(status);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBack.showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
