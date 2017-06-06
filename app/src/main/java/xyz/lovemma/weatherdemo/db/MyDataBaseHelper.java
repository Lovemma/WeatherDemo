package xyz.lovemma.weatherdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OO on 2017/6/4.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {
    /**
     * id : CN101010100
     * cityEn : beijing
     * cityZh : 北京
     * countryCode : CN
     * countryEn : China
     * countryZh : 中国
     * provinceEn : beijing
     * provinceZh : 北京
     * leaderEn : beijing
     * leaderZh : 北京
     * lat : 39.904989
     * lon : 116.405285
     */
    public static final String CREATE_CITY = "create table City ("
            + "id text , "
            + "cityEn text, "
            + "cityZh text,"
            + "countryCode text, "
            + "countryEn text, "
            + "countryZh text, "
            + "provinceEn text, "
            + "provinceZh text, "
            + "leaderEn text, "
            + "leaderZh text, "
            + "lat real, "
            + "lon real)";

    private Context mContext;
    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
