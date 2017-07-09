package xyz.lovemma.weatherdemo.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.api.RetrofitUtils;
import xyz.lovemma.weatherdemo.db.MutiliCity;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.ui.adapter.HomeAdapter;
import xyz.lovemma.weatherdemo.utils.NetUtil;

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
                        loadData();
                    }
                }, 1000);
            }
        });
        loadData();
    }

    private int timeDiff = 1800000;

    private void loadData() {
        List<MutiliCity> cities = DataSupport.where("city = ?", mCity)
                .find(MutiliCity.class);
        MutiliCity city = cities.get(0);
        long time = System.currentTimeMillis();
        if (city.getTime() == 0) {
            loadDataFromNet();
        } else if (time - city.getTime() > timeDiff) {
            if (NetUtil.isConnected(getContext())) {
                loadDataFromNet();
            } else {
                Toast.makeText(getContext(), "连接网络才能加载最新的天气哦(●ˇ∀ˇ●)", Toast.LENGTH_SHORT).show();
                loadDataFromDB(city);
            }
        } else {
            loadDataFromDB(city);
        }
    }

    public void loadDataFromDB(MutiliCity city) {
        HeWeather5 weather = new Gson().fromJson(city.getJson(), HeWeather5.class);
        mAdapter.setData(weather);
        mAdapter.notifyItemRangeChanged(0, 4);
        mRefreshLayout.setRefreshing(false);
    }

    public void loadDataFromNet() {
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

                        String json = new Gson().toJson(weather5);
                        MutiliCity mutiliCity = new MutiliCity();
                        mutiliCity.setCity(mCity);
                        mutiliCity.setJson(json);
                        mutiliCity.setTime(System.currentTimeMillis());
                        mutiliCity.saveOrUpdate("city like ?", "%" + weather5.getBasic().getCity() + "%");
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
