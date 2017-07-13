package xyz.lovemma.weatherdemo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import xyz.lovemma.weatherdemo.db.City;

/**
 * Created by OO on 2017/6/25.
 */

public class CityListTask extends AsyncTask<Void, Integer, Boolean> {
    private Context mContext;
    private ProgressDialog mDialog;

    public CityListTask(Context context) {
        mContext = context;

        mDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        mDialog.setMessage("初始化数据库中");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setMax(100);
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
        Type type = new TypeToken<List<City>>() {}.getType();
        List<City> cities = new Gson().fromJson(stringBuilder.toString(), type);
        for (int i = 0; i < cities.size(); i++) {
            cities.get(i).save();
//            publishProgress(i);
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        mDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
//        int progress = (int) (values[0]*1.0/3181*100);
//        mDialog.incrementProgressBy(progress);
    }
}
