package com.xw.compoint;

import android.content.Context;

public class UITools {

    /**
     * dp转px单位
     *
     * @param context context
     * @param dpValue dp的值
     * @return dp转化成px后的值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
