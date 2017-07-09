package xyz.lovemma.weatherdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.lovemma.weatherdemo.db.City;
import xyz.lovemma.weatherdemo.db.MutiliCity;
import xyz.lovemma.weatherdemo.ui.adapter.CityAdapter;
import xyz.lovemma.weatherdemo.utils.NetUtil;

public class ChoiceCityActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.textView)
    TextView mTextView;
    private List<City> mCityList = new ArrayList<>();
    private CityAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        ButterKnife.bind(this);

        mAdapter = new CityAdapter(mCityList);
        mAdapter.setOnItemClickListener(new CityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (NetUtil.isConnected(ChoiceCityActivity.this)) {
                    List<MutiliCity> cities = DataSupport
                            .where("city like ?", "%" + mCityList.get(pos).getCityZh() + "%")
                            .find(MutiliCity.class);
                    if (cities.size() == 1) {
                        Toast.makeText(ChoiceCityActivity.this, "城市已存在", Toast.LENGTH_SHORT).show();
                    } else {
                        MutiliCity city = new MutiliCity();
                        city.setCity(mCityList.get(pos).getCityZh());
                        city.save();
                        Intent intent = new Intent(ChoiceCityActivity.this, MulitiCityActivity.class);
                        intent.putExtra("city", mCityList.get(pos).getCityZh());
                        setResult(RESULT_OK, intent);
                    }
                } else {
                    Toast.makeText(ChoiceCityActivity.this, "请检查网络后重试！", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.onActionViewExpanded();
        mSearchView.setQueryHint("城市名");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryFromDB(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryFromDB(newText);
                return false;
            }
        });
        return true;
    }

    private void queryFromDB(final String city) {
        if (city.length() == 0) {
            return;
        }
        mCityList.clear();
        Observable.create(new ObservableOnSubscribe<City>() {
            @Override
            public void subscribe(ObservableEmitter<City> e) throws Exception {
                List<City> cities = DataSupport
                        .where("cityZh like ? or cityEn like ?", "%" + city + "%", "%" + city + "%")
                        .find(City.class);
                for (City city1 : cities) {
                    e.onNext(city1);
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<City>() {
                    @Override
                    public void accept(City city) throws Exception {
                        mCityList.add(city);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (mCityList.size() == 0) {
                            mTextView.setVisibility(View.VISIBLE);
                        } else {
                            mTextView.setVisibility(View.GONE);
                        }
                    }
                })
                .subscribe();
    }
}