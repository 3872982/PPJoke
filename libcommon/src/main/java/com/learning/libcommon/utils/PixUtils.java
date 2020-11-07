package com.learning.libcommon.utils;

import android.util.DisplayMetrics;

import com.learning.libcommon.globals.AppGlobal;

public class PixUtils {
    public static int dp2px(int dpValue) {
        DisplayMetrics metrics = AppGlobal.getApplication().getResources().getDisplayMetrics();
        return (int) (metrics.density * dpValue + 0.5f);
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = AppGlobal.getApplication().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = AppGlobal.getApplication().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
