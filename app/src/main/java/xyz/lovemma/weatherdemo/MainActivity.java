package xyz.lovemma.weatherdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.ui.adapter.CityPagerAdapter;
import xyz.lovemma.weatherdemo.ui.adapter.HomeAdapter;
import xyz.lovemma.weatherdemo.ui.fragment.CityFragment;
import xyz.lovemma.weatherdemo.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private HomeAdapter mHomeAdapter;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private CityPagerAdapter mPagerAdapter;
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;
    private static final String TAG = "MainActivity";

    public static final int MULTI_CITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initIcon();
        initDrawer();
        initBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBroadcastManager.unregisterReceiver(mReceive);
    }


    private IntentFilter mIntentFilter;
    private ViewPagerChangeReceive mReceive;
    private LocalBroadcastManager mBroadcastManager;

    private void initBroadcast() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("xyz.lovemma.WeatherDemo.ViewPager_Change");
        mReceive = new ViewPagerChangeReceive();
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastManager.registerReceiver(mReceive, mIntentFilter);
    }

    private void initView() {
        setSupportActionBar(toolbar);

        mPagerAdapter = new CityPagerAdapter(getSupportFragmentManager());
        loadCityFromDB();
        viewPager.setAdapter(mPagerAdapter);
    }

    private void loadCityFromDB() {
        mDataBaseHelper = new MyDataBaseHelper(this, "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Cursor cursor = db.query("MutiliCity", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String city = cursor.getString(cursor.getColumnIndex("city"));
                        e.onNext(city);
                    } while (cursor.moveToNext());
                    e.onComplete();
                }
                cursor.close();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mPagerAdapter.addFragment(CityFragment.newInstance(s));
                    }
                })
                .subscribe();

    }

    private void showWeatherInfo(Weather weather) {
        setNotification(weather.getHeWeather5().get(0));
    }

    private void setNotification(HeWeather5 weather) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(weather.getBasic().getCity())
                .setContentText(String.format("%s 当前温度: %s℃ ", weather.getNow().getCond().getTxt(), weather.getNow().getTmp()))
                // 这里部分 ROM 无法成功
                .setSmallIcon((int) sharedPreferencesUtil.get(weather.getNow().getCond().getTxt(), R.drawable.ic_unknow))
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // tag和id都是可以拿来区分不同的通知的
        manager.notify(1, notification);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initIcon() {
        sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext());
        if (!(boolean) sharedPreferencesUtil.get("IconInit", false)) {
            sharedPreferencesUtil.put("晴", R.drawable.sunny);
            sharedPreferencesUtil.put("多云", R.drawable.cloudy);
            sharedPreferencesUtil.put("少云", R.drawable.cloudy);
            sharedPreferencesUtil.put("晴间多云", R.drawable.cloudy);
            sharedPreferencesUtil.put("阴", R.drawable.cloud);
            sharedPreferencesUtil.put("有风", R.drawable.windy);
            sharedPreferencesUtil.put("微风", R.drawable.windy);
            sharedPreferencesUtil.put("和风", R.drawable.windy);
            sharedPreferencesUtil.put("清风", R.drawable.windy);
            sharedPreferencesUtil.put("强风/劲风", R.drawable.windy);
            sharedPreferencesUtil.put("疾风", R.drawable.windy);
            sharedPreferencesUtil.put("大风", R.drawable.windy);
            sharedPreferencesUtil.put("烈风", R.drawable.windy);
            sharedPreferencesUtil.put("风暴", R.drawable.windy);
            sharedPreferencesUtil.put("狂爆风", R.drawable.windy);
            sharedPreferencesUtil.put("飓风", R.drawable.windy);
            sharedPreferencesUtil.put("龙卷风", R.drawable.windy);
            sharedPreferencesUtil.put("热带风暴", R.drawable.windy);
            sharedPreferencesUtil.put("阵雨", R.drawable.shower);
            sharedPreferencesUtil.put("强阵雨", R.drawable.shower);
            sharedPreferencesUtil.put("雷阵雨", R.drawable.rainstorm);
            sharedPreferencesUtil.put("强雷阵雨", R.drawable.rainstorm);
            sharedPreferencesUtil.put("雷阵雨伴有冰雹", R.drawable.rainstorm);
            sharedPreferencesUtil.put("小雨", R.drawable.rain);
            sharedPreferencesUtil.put("中雨", R.drawable.rain);
            sharedPreferencesUtil.put("大雨", R.drawable.rain);
            sharedPreferencesUtil.put("极端降雨", R.drawable.rain);
            sharedPreferencesUtil.put("毛毛雨/细雨", R.drawable.rain);
            sharedPreferencesUtil.put("暴雨", R.drawable.rain);
            sharedPreferencesUtil.put("大暴雨", R.drawable.rain);
            sharedPreferencesUtil.put("特大暴雨", R.drawable.rain);
            sharedPreferencesUtil.put("小雪", R.drawable.snow);
            sharedPreferencesUtil.put("中雪", R.drawable.snow);
            sharedPreferencesUtil.put("大雪", R.drawable.snow);
            sharedPreferencesUtil.put("暴雪", R.drawable.snow);
            sharedPreferencesUtil.put("雨夹雪", R.drawable.snow);
            sharedPreferencesUtil.put("雨雪天气", R.drawable.snow);
            sharedPreferencesUtil.put("阵雨夹雪", R.drawable.snow);
            sharedPreferencesUtil.put("阵雪", R.drawable.snow);
            sharedPreferencesUtil.put("薄雾", R.drawable.fog);
            sharedPreferencesUtil.put("雾", R.drawable.fog);
            sharedPreferencesUtil.put("霾", R.drawable.fog);
            sharedPreferencesUtil.put("IconInit", true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case MULTI_CITY:
                    int position = data.getIntExtra("position", 0);
                    viewPager.setCurrentItem(position);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_location_city:
                intent = new Intent(this, MulitiCityActivity.class);
                startActivityForResult(intent, MULTI_CITY);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class ViewPagerChangeReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra("change_viewpager", 0)) {
                case 1:
                    mPagerAdapter.addFragment(CityFragment.newInstance(intent.getStringExtra("city")));
                    break;
                case 2:
                    mPagerAdapter.removeFragment(intent.getIntExtra("position", -1));
                    break;
                default:
                    break;
            }
        }
    }
}
