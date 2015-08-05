package org.whut.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by baisu on 15-5-29.
 */
public class ToastHelper {

    public static boolean isShow = true;

    private ToastHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 长时间显示
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
    }

    /**
     * 自定义时间显示
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }
}
