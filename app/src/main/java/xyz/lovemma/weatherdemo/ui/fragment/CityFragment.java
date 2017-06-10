package xyz.lovemma.weatherdemo.ui.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.api.RetrofitUtils;
import xyz.lovemma.weatherdemo.api.WeatherCallBack;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.entity.Weather;
import xyz.lovemma.weatherdemo.ui.adapter.HomeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment implements WeatherCallBack {


    @BindView(R.id.weather_recycler)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    private static final String CITY = "CITY";
    private String mCity;
    private HomeAdapter mAdapter;
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RetrofitUtils.fetchWeather(mCity, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showWeatherInfo(Weather weather) {
        HeWeather5 weather5 = weather.getHeWeather5().get(0);
        mAdapter = new HomeAdapter(weather5);
        mRecyclerView.setAdapter(mAdapter);
        mDataBaseHelper = new MyDataBaseHelper(getContext(), "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();
        ContentValues values;
        Cursor cursor = db.query("MutiliCity", null, "city like ?", new String[]{"%" + weather5.getBasic().getCity() + "%"}, null, null, null);
        if (cursor.getCount() == 0) {
            values = new ContentValues();
            values.put("city", weather5.getBasic().getCity());
            db.insert("MutiliCity", null, values);
        }
        cursor.close();
    }

    @Override
    public void showError(String e) {

    }

}
