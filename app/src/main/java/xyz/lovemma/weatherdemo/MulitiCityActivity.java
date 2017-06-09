package xyz.lovemma.weatherdemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;
import xyz.lovemma.weatherdemo.entity.MulitiCity;
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
    private List<MulitiCity> mCityList = new ArrayList<>();

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
        loadDataFromDB();
        MutiliCityAdapter adapter = new MutiliCityAdapter(mCityList);
        adapter.setOnMultiCityClickListener(new MutiliCityAdapter.onMultiCityClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MulitiCityActivity.this, MainActivity.class);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
    }

    private void loadDataFromDB() {
        Cursor cursor = db.query("MutiliCity", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String cond = cursor.getString(cursor.getColumnIndex("cond"));
                String temp = cursor.getString(cursor.getColumnIndex("temp"));
                mCityList.add(new MulitiCity(city, cond, temp));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
