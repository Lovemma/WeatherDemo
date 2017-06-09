package xyz.lovemma.weatherdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.lovemma.weatherdemo.db.City;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;
import xyz.lovemma.weatherdemo.ui.adapter.CityAdapter;

public class ChoiceCityActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.textView)
    TextView mTextView;
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;
    private List<City> mCityList = new ArrayList<>();
    private CityAdapter mAdapter;
    private static final String TAG = "ChoiceCityActivity";


    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        ButterKnife.bind(this);
        mDataBaseHelper = new MyDataBaseHelper(this, "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();

        mAdapter = new CityAdapter(mCityList);
        mAdapter.setOnItemClickListener(new CityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
//                Toast.makeText(ChoiceCityActivity.this, "position:" + mCityList.get(pos).getCityZh(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoiceCityActivity.this, MainActivity.class);
                intent.putExtra("choice_city", mCityList.get(pos));
                setResult(RESULT_OK, intent);
                ContentValues values = new ContentValues();
                values.put("city", mCityList.get(pos).getCityZh());
                values.put("cond", "");
                db.insert("MutiliCity", null, values);
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
                Log.d(TAG, "onQueryTextSubmit: " + query);
                queryFromDB(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                queryFromDB(newText);
                return false;
            }
        });
        return true;
    }

    private void queryFromDB(String city) {
        if (city.length() == 0) {
            return;
        }
        mAdapter.clearData();
        Cursor cursor = db.query("City", null, "cityZh like ? or cityEn like ?", new String[]{"%" + city + "%", "%" + city + "%"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String cityEn = cursor.getString(cursor.getColumnIndex("cityEn"));
                String cityZh = cursor.getString(cursor.getColumnIndex("cityZh"));
                String countryCode = cursor.getString(cursor.getColumnIndex("countryCode"));
                String countryEn = cursor.getString(cursor.getColumnIndex("countryEn"));
                String countryZh = cursor.getString(cursor.getColumnIndex("countryZh"));
                String provinceEn = cursor.getString(cursor.getColumnIndex("provinceEn"));
                String provinceZh = cursor.getString(cursor.getColumnIndex("provinceZh"));
                String leaderEn = cursor.getString(cursor.getColumnIndex("leaderEn"));
                String leaderZh = cursor.getString(cursor.getColumnIndex("leaderZh"));
                float lat = cursor.getFloat(cursor.getColumnIndex("lat"));
                float lon = cursor.getFloat(cursor.getColumnIndex("lon"));

                mCityList.add(new City(id, cityEn, cityZh, countryCode, countryEn, countryZh, provinceEn, provinceZh, leaderEn, leaderZh, lat, lon));
            } while (cursor.moveToNext());
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
        }
        cursor.close();
        mAdapter.notifyDataSetChanged();
    }
}