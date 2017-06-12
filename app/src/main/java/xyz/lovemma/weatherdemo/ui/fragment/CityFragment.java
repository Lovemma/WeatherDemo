package xyz.lovemma.weatherdemo.ui.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.api.RetrofitUtils;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.ui.adapter.HomeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {


    @BindView(R.id.weather_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private static final String CITY = "CITY";
    private String mCity;
    private HomeAdapter mAdapter;
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = getArguments().getString(CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        mDataBaseHelper = new MyDataBaseHelper(getContext(), "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();
        loadDataByNetWork();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public CityFragment() {
        // Required empty public constructor
    }

    public static CityFragment newInstance(String city) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putString(CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        mAdapter = new HomeAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        );
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDataByNetWork();
                    }
                }, 1000);
            }
        });
    }

    public void loadDataByNetWork() {
        RetrofitUtils.getInstance().fetchWeather(mCity)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRefreshLayout.setRefreshing(true);
                    }
                })
                .doOnNext(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        HeWeather5 weather5 = weather.getHeWeather5().get(0);
                        mAdapter.setData(weather5);
                        mAdapter.notifyItemRangeChanged(0, 4);
                        ContentValues values;
                        Cursor cursor = db.query("MutiliCity", null, "city like ?", new String[]{"%" + weather5.getBasic().getCity() + "%"}, null, null, null);
                        if (cursor.getCount() == 0) {
                            values = new ContentValues();
                            values.put("city", weather5.getBasic().getCity());
                            db.insert("MutiliCity", null, values);
                        }
                        cursor.close();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRefreshLayout.setRefreshing(false);
                    }
                })
                .subscribe();
    }
}
