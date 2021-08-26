package com.xw.customrecycler.viewPager;

public interface OnViewPagerListener {

    void onInitComplete();

    void onPageRelease(boolean var1, int var2);

    void onPageSelected(int var1, boolean var2);
}
