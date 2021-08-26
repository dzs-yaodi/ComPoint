package com.xw.customrecycler.pagegrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * 请求外部ViewPager不拦截事件.（处理横向滑动的recyclerView 和 viewPager滑动不冲突）
 */
public class InnerRecyclerView extends RecyclerView {

    public InnerRecyclerView(@NonNull Context context) {
        super(context);
    }

    public InnerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewParent parent = getParent();
        // 循环查找ViewPager, 请求ViewPager不拦截触摸事件
        while (!(parent instanceof ViewPager)) {
            if (parent != null) {
                parent = parent.getParent();
            } else {
                return super.dispatchTouchEvent(ev);
            }
        }
        //请求viewPager 不拦截事件
        parent.requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
