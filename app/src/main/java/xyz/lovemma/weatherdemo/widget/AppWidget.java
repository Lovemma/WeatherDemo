package xyz.lovemma.weatherdemo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.List;

import io.reactivex.functions.Consumer;
import xyz.lovemma.weatherdemo.MainActivity;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.api.RetrofitUtils;
import xyz.lovemma.weatherdemo.db.MutiliCity;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.utils.NetUtil;
import xyz.lovemma.weatherdemo.utils.SharedPreferencesUtil;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    private RemoteViews views;

    void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.app_widget, pendingIntent);
        List<MutiliCity> cities = DataSupport
                .where("id = ?", "1")
                .find(MutiliCity.class);
        if (cities.size() != 0) {
            if (NetUtil.isConnected(context)) {
                MutiliCity city = cities.get(0);
                RetrofitUtils.getInstance().fetchWeather(city.getCity())
                        .subscribe(new Consumer<Weather>() {
                            @Override
                            public void accept(Weather weather) throws Exception {
                                HeWeather5 weather5 = weather.getHeWeather5().get(0);
                                String json = new Gson().toJson(weather5);
                                MutiliCity mutiliCity = new MutiliCity();
                                mutiliCity.setCity(weather5.getBasic().getCity()+"市");
                                mutiliCity.setJson(json);
                                mutiliCity.setTime(System.currentTimeMillis());
                                mutiliCity.saveOrUpdate("id = ?", "1");
                                views.setTextViewText(R.id.city, weather5.getBasic().getCity() + "");
                                String cond_txt = weather5.getNow().getCond().getTxt() + "  "
                                        + weather5.getAqi().getCity().getQlty() + "  "
                                        + weather5.getAqi().getCity().getPm25();
                                views.setTextViewText(R.id.cond_txt, cond_txt);
                                views.setTextViewText(R.id.temp, weather5.getNow().getTmp() + "°");
                                SharedPreferencesUtil preferencesUtil = new SharedPreferencesUtil(context);
                                int srcId = (int) preferencesUtil.get(weather5.getNow().getCond().getTxt(), R.drawable.ic_unknow);
                                views.setImageViewResource(R.id.cond_img, srcId);
                            }
                        });
            }
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

