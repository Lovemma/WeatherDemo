package xyz.lovemma.weatherdemo.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.RemoteViews;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import xyz.lovemma.weatherdemo.MainActivity;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.api.RetrofitUtils;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.utils.SharedPreferencesUtil;

public class WidgetService extends Service {
    private RemoteViews mRemoteViews;
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;

    public WidgetService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDataBaseHelper = new MyDataBaseHelper(getApplicationContext(), "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();
        mRemoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.app_widget);
        updateAppWidget();
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.app_widget, pendingIntent);

        return START_STICKY;
    }

    private void updateAppWidget() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Cursor cursor = null;
                try {
                    cursor = db.query("MutiliCity", null, "id = ?", new String[]{"1"}, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String city = cursor.getString(cursor.getColumnIndex("city"));
                            emitter.onNext(city);
                        } while (cursor.moveToNext());
                        emitter.onComplete();
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .concatMap(new Function<String, ObservableSource<Weather>>() {
                    @Override
                    public ObservableSource<Weather> apply(String s) throws Exception {
                        return RetrofitUtils.getInstance().fetchWeather(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        HeWeather5 weather5 = weather.getHeWeather5().get(0);
                        mRemoteViews.setTextViewText(R.id.city, weather5.getBasic().getCity());
                        String cond_txt = weather5.getNow().getCond().getTxt() + "  "
                                + weather5.getAqi().getCity().getQlty() + "  "
                                + weather5.getAqi().getCity().getPm25();
                        mRemoteViews.setTextViewText(R.id.cond_txt, cond_txt);
                        mRemoteViews.setTextViewText(R.id.temp, weather5.getNow().getTmp() + "°");
                        SharedPreferencesUtil preferencesUtil = new SharedPreferencesUtil(getApplicationContext());
                        int srcId = (int) preferencesUtil.get(weather5.getNow().getCond().getTxt(), R.drawable.ic_unknow);
                        mRemoteViews.setImageViewResource(R.id.cond_img, srcId);
                        AppWidgetManager.getInstance(getApplicationContext())
                                .updateAppWidget(new ComponentName(getApplicationContext(), AppWidget.class), mRemoteViews);
                    }
                })
                .subscribe();
    }
}
