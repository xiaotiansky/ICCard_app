package org.whut.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by baisu on 15-7-1.
 */
public class SPUtils {

    public static final String FILE_NAME = "share_data";

    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (value instanceof String) {
            editor.putString(key, (String)value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer)value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float)value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long)value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    public static Object get(Context context, String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);

        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)){
            return sharedPreferences.getInt(key, (Integer)defValue);
        }else if ("Boolean".equals(type)){
            return sharedPreferences.getBoolean(key, (Boolean)defValue);
        }else if ("String".equals(type)){
            return sharedPreferences.getString(key, (String)defValue);
        }else if ("Float".equals(type)){
            return sharedPreferences.getFloat(key, (Float)defValue);
        }else if ("Long".equals(type)){
            return sharedPreferences.getLong(key, (Long)defValue);
        }

        return null;
    }
}
