package xyz.lovemma.weatherdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.lovemma.weatherdemo.api.RetrofitUtils;
import xyz.lovemma.weatherdemo.db.MutiliCity;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.ui.adapter.MutiliCityAdapter;
import xyz.lovemma.weatherdemo.utils.CircularAnimUtil;

public class MulitiCityActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private List<MutiliCity> mCityList = new ArrayList<>();
    private MutiliCityAdapter mAdapter;
    private LocalBroadcastManager mBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muliti_city);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("多城市管理");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadData();
        mAdapter = new MutiliCityAdapter(mCityList);
        mAdapter.setOnMultiCityClickListener(new MutiliCityAdapter.onMultiCityClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MulitiCityActivity.this, MainActivity.class);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MulitiCityActivity.this, ChoiceCityActivity.class);
                CircularAnimUtil.startActivityForResult(MulitiCityActivity.this, intent, ADD_CITY, fab, R.color.colorAccent);
            }
        });
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != 0) {
                    DataSupport
                            .deleteAll(MutiliCity.class, "city = ?", mCityList.get(position).getCity());
                    mCityList.remove(position);

                    Intent intent = new Intent("xyz.lovemma.WeatherDemo.ViewPager_Change");
                    intent.putExtra("change_viewpager", 2);
                    intent.putExtra("position", position);
                    mBroadcastManager.sendBroadcast(intent);
                }
                mAdapter.notifyItemChanged(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CITY:
                    String city = data.getStringExtra("city");
                    addData(city);
                    break;
            }
        }
    }

    private void addData(final String city) {
        RetrofitUtils.getInstance().fetchWeather(city)
                .subscribe(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        HeWeather5 weather5 = weather.getHeWeather5().get(0);
                        String json = new Gson().toJson(weather5);
                        MutiliCity mutiliCity = new MutiliCity();
                        mutiliCity.setCity(weather5.getBasic().getCity());
                        mutiliCity.setJson(json);
                        mutiliCity.setTime(System.currentTimeMillis());
                        mutiliCity.save();
                        mCityList.add(mutiliCity);
                        mAdapter.notifyItemInserted(mCityList.size());
                        Intent intent = new Intent("xyz.lovemma.WeatherDemo.ViewPager_Change");
                        intent.putExtra("change_viewpager", 1);
                        intent.putExtra("city", city);
                        mBroadcastManager.sendBroadcast(intent);
                    }
                });
    }

    public static final int ADD_CITY = 1;


    private void loadData() {
        Observable.create(new ObservableOnSubscribe<MutiliCity>() {
            @Override
            public void subscribe(ObservableEmitter<MutiliCity> emitter) throws Exception {
                List<MutiliCity> cities = DataSupport.findAll(MutiliCity.class);
                for (MutiliCity city : cities) {
                    emitter.onNext(city);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<MutiliCity>() {
                    @Override
                    public void accept(MutiliCity city) throws Exception {
                        mCityList.add(city);
                        mAdapter.notifyItemRangeChanged(0, mCityList.size());
                    }
                })
                .subscribe();
    }

}
