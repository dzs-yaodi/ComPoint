package com.xw.customrecycler.pagegrid;

import android.util.Log;

public class PagerConfig {

    private static final String TAG = "PagerGrid";
    private static boolean sShowLog = false;
    private static int sFlingThreshold = 1000;
    private static float sMillisecondsPreInch = 60.0F;

    public PagerConfig() {
    }

    public static boolean isShowLog() {
        return sShowLog;
    }

    public static void setShowLog(boolean showLog) {
        sShowLog = showLog;
    }

    public static int getFlingThreshold() {
        return sFlingThreshold;
    }

    public static void setFlingThreshold(int flingThreshold) {
        sFlingThreshold = flingThreshold;
    }

    public static float getMillisecondsPreInch() {
        return sMillisecondsPreInch;
    }

    public static void setMillisecondsPreInch(float millisecondsPreInch) {
        sMillisecondsPreInch = millisecondsPreInch;
    }

    public static void Logi(String msg) {
        if (isShowLog()) {
            Log.i("PagerGrid", msg);
        }
    }

    public static void Loge(String msg) {
        if (isShowLog()) {
            Log.e("PagerGrid", msg);
        }
    }
}
