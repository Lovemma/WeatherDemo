package xyz.lovemma.weatherdemo.utils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

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
 * Created by OO on 2017/6/25.
 */

public class CityListTask extends AsyncTask<Void, Integer, Boolean> {
    private SQLiteDatabase db;
    private MyDataBaseHelper mDataBaseHelper;
    private Context mContext;
    private ProgressDialog mDialog;

    public CityListTask(Context context) {
        mContext = context;
        mDialog = new ProgressDialog(mContext);
        mDataBaseHelper = new MyDataBaseHelper(mContext, "City.db", null, 1);
        db = mDataBaseHelper.getWritableDatabase();
    }

    @Override
    protected void onPreExecute() {
        mDialog.setTitle("初始化数据库中");
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMax(3181);
        mDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    mContext.getAssets().open("china-city-list.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            return false;
        }
        Type type = new TypeToken<List<City>>() {
        }.getType();
        List<City> cities = new Gson().fromJson(stringBuilder.toString(), type);
        ContentValues values = new ContentValues();
        for (int i = 0; i < cities.size(); i++) {
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
            publishProgress(1);
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mDialog.incrementProgressBy(1);
    }
}
