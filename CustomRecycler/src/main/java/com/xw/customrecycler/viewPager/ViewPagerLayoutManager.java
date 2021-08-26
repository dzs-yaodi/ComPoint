package com.xw.customrecycler.viewPager;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPagerLayoutManager extends LinearLayoutManager {

    private static final String TAG = "ViewPagerLayoutManager";
    private PagerSnapHelper mPageSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;
    private RecyclerView mRecyclerView;
    private int mDirft;
    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener;

    public ViewPagerLayoutManager(Context context,int orientation) {
        super(context,orientation,false);
        mChildAttachStateChangeListener = new NamelessClass_1();
        init();
    }

    public ViewPagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);

        mChildAttachStateChangeListener = new NamelessClass_1();
    }

    private void init() {
        mPageSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPageSnapHelper.attachToRecyclerView(view);
        mRecyclerView = view;
        mRecyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {

            case 0:
                View viewIdle = mPageSnapHelper.findSnapView(this);
                if (viewIdle != null) {
                    int positionIdle = getPosition(viewIdle);
                    if (mOnViewPagerListener != null && getChildCount() == 1) {
                        mOnViewPagerListener.onPageSelected(positionIdle, positionIdle == getItemCount() - 1);
                    }
                }
                break;
            case 1:
            case 2:
                View viewDrag = mPageSnapHelper.findSnapView(this);
                if (viewDrag != null) {
                    getPosition(viewDrag);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDirft = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDirft = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    class NamelessClass_1 implements RecyclerView.OnChildAttachStateChangeListener {

        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (mOnViewPagerListener != null && getChildCount() == 1) {
                mOnViewPagerListener.onInitComplete();
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            if (mDirft >= 0) {
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageRelease(true,getPosition(view));
                }
            } else if (mOnViewPagerListener != null) {
                mOnViewPagerListener.onPageRelease(false,getPosition(view));
            }
        }
    }

    public void setOnViewPagerListener(OnViewPagerListener mOnViewPagerListener) {
        this.mOnViewPagerListener = mOnViewPagerListener;
    }
}
