package xyz.lovemma.weatherdemo;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import xyz.lovemma.weatherdemo.db.City;
import xyz.lovemma.weatherdemo.db.MyDataBaseHelper;

/**
 * Created by OO on 2017/6/5.
 */

public class App extends Application {
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mDataBaseHelper = new MyDataBaseHelper(this, "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setData(getJson("china-city-list.json"));
                }
            }).start();
        }
        cursor.close();
    }

    private String getJson(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    getAssets().open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void setData(String str) {
        Type type = new TypeToken<List<City>>() {
        }.getType();
        List<City> cities = new Gson().fromJson(str, type);

        ContentValues values = new ContentValues();
        for (int i = 0; i < cities.size(); i++) {
//            Log.d(TAG, "setData: " + cities.get(i).getCityZh());
            values.put("id", cities.get(i).getId());
            values.put("cityEn", cities.get(i).getCityEn());
            values.put("cityZh", cities.get(i).getCityZh());
            values.put("countryCode", cities.get(i).getCountryCode());
            values.put("countryEn", cities.get(i).getCountryEn());
            values.put("countryZh", cities.get(i).getCountryZh());
            values.put("provinceEn", cities.get(i).getProvinceEn());
            values.put("provinceZh", cities.get(i).getProvinceZh());
            values.put("leaderEn", cities.get(i).getLeaderEn());
            values.put("leaderZh", cities.get(i).getLeaderZh());
            values.put("lat", cities.get(i).getLat());
            values.put("lon", cities.get(i).getLon());
            db.insert("City", null, values);
            values.clear();
        }
    }
}
