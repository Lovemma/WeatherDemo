package xyz.lovemma.weatherdemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.lovemma.weatherdemo.db.City;
import xyz.lovemma.weatherdemo.db.MutiliCity;
import xyz.lovemma.weatherdemo.ui.activity.AboutActivity;
import xyz.lovemma.weatherdemo.ui.adapter.CityPagerAdapter;
import xyz.lovemma.weatherdemo.ui.fragment.CityFragment;
import xyz.lovemma.weatherdemo.utils.CityListTask;
import xyz.lovemma.weatherdemo.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AMapLocationListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private CityPagerAdapter mPagerAdapter;
    private ViewPagerChangeReceive mReceive;
    private LocalBroadcastManager mBroadcastManager;

    private AMapLocationClient mLocationClient = null;

    public static final int MULTI_CITY = 2;
    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initLocation();
        initData();
        initBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBroadcastManager.unregisterReceiver(mReceive);
        mLocationClient.onDestroy();
    }

    private void initView() {
        setSupportActionBar(toolbar);

        mPagerAdapter = new CityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTitle("" + mPagerAdapter.getItem(position).getArguments().getString("CITY"));
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("xyz.lovemma.WeatherDemo.ViewPager_Change");
        mReceive = new ViewPagerChangeReceive();
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastManager.registerReceiver(mReceive, intentFilter);
    }

    private void initData() {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext());
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

        DataSupport.findAllAsync(City.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                List<City> cities = (List<City>) t;
                if (cities.size() != 3181) {
                    new CityListTask(MainActivity.this).execute();
                }
            }
        });
    }

    private void initLocation() {
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setOnceLocationLatest(true);
        locationOption.setNeedAddress(true);
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);
        mLocationClient.setLocationOption(locationOption);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                loadCity();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_CODE);
            }
        } else {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationClient.startLocation();
                } else {
                    insertDefaultCityAndLoad("成都市");
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String city = aMapLocation.getCity();
                insertDefaultCityAndLoad(city);
                setTitle(city);
            } else {
                Snackbar.make(container, "定位失败,请检查网络后重试", Snackbar.LENGTH_LONG)
                        .setAction(R.string.alert_dialog_ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLocationClient.startLocation();
                            }
                        }).show();
                loadCity();
            }
        }
    }

    private void insertDefaultCityAndLoad(String city) {
        MutiliCity city1 = new MutiliCity();
        city1.setCity(city);
        city1.saveOrUpdate("id = ?", "1");
        loadCity();
    }

    private void loadCity() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                List<MutiliCity> cities = DataSupport.findAll(MutiliCity.class);
                for (MutiliCity city : cities) {
                    e.onNext(city.getCity());
                }
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
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(this, SettingActivity.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_location_city:
                intent = new Intent(this, MulitiCityActivity.class);
                startActivityForResult(intent, MULTI_CITY);
                break;
            case R.id.nav_info:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
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
