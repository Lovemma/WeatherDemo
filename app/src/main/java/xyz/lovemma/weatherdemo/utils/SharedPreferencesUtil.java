package xyz.lovemma.weatherdemo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by OO on 2017/3/5.
 */

public class SharedPreferencesUtil {
    public static final String FILE_NAME = "sharePreferences_data";

    private SharedPreferences mSharedPreferences;

    public SharedPreferencesUtil(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void put(String key, Object object) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
//        editor.apply();
        SharedPreferencesCompat.apply(editor);
    }


    public Object get(String key, Object defaultObject) {

        if (defaultObject instanceof String) {
            return mSharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return mSharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return mSharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return mSharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return mSharedPreferences.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    public void clear(Context context) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    private static class SharedPreferencesCompat {

        //查看SharedPreferences是否有apply方法
        private static final Method sApplyMethod = findApply();

        private static Method findApply() {

            try {
                Class<SharedPreferences> cls = SharedPreferences.class;
                return cls.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static void apply(SharedPreferences.Editor editor) {

            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            editor.commit();
        }

    }
}
