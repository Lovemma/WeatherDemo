package xyz.lovemma.weatherdemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import xyz.lovemma.weatherdemo.api.RetrofitUtils;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.ui.adapter.MutiliCityAdapter;

public class MulitiCityActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;
    private List<HeWeather5> mWeatherList = new ArrayList<>();
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

        mDataBaseHelper = new MyDataBaseHelper(this, "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();
        loadDataFromNetwork();
        mAdapter = new MutiliCityAdapter(mWeatherList);
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
                startActivityForResult(intent, ADD_CITY);
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
                    db.delete("MutiliCity", "city = ?", new String[]{mWeatherList.get(position).getBasic().getCity()});
                    mWeatherList.remove(position);

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

    private void addData(String city) {
        RetrofitUtils.getInstance().fetchWeather(city)
                .subscribe(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        mWeatherList.add(weather.getHeWeather5().get(0));
                        mAdapter.notifyItemInserted(mWeatherList.size());
                    }
                });
        Intent intent = new Intent("xyz.lovemma.WeatherDemo.ViewPager_Change");
        intent.putExtra("change_viewpager", 1);
        intent.putExtra("city", city);
        mBroadcastManager.sendBroadcast(intent);
    }

    public static final int ADD_CITY = 1;


    private void loadDataFromNetwork() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Cursor cursor = null;
                try {
                    cursor = db.query("MutiliCity", null, null, null, null, null, null);
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
                .filter(new Predicate<Weather>() {
                    @Override
                    public boolean test(Weather weather) throws Exception {
                        return weather.getHeWeather5().get(0).getStatus().equals("ok");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        mWeatherList.add(weather.getHeWeather5().get(0));
                        mAdapter.notifyItemRangeChanged(0, mWeatherList.size());
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe();
    }

}
