package com.xw.customrecycler.slide;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SlideLayoutManager extends RecyclerView.LayoutManager {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            RecyclerView.ViewHolder childViewHolder = SlideLayoutManager.this.mRecyclerView.getChildViewHolder(v);
            if (MotionEventCompat.getActionMasked(event) == 0) {
                SlideLayoutManager.this.mItemTouchHelper.startSwipe(childViewHolder);
            }

            return false;
        }
    };

    public SlideLayoutManager(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper itemTouchHelper) {
        this.mRecyclerView = (RecyclerView)this.checkIsNull(recyclerView);
        this.mItemTouchHelper = (ItemTouchHelper)this.checkIsNull(itemTouchHelper);
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        } else {
            return t;
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.detachAndScrapAttachedViews(recycler);
        int itemCount = this.getItemCount();
        int position;
        View view;
        int widthSpace;
        int heightSpace;
        if (itemCount > 3) {
            for(position = 3; position >= 0; --position) {
                view = recycler.getViewForPosition(position);
                this.addView(view);
                this.measureChildWithMargins(view, 0, 0);
                widthSpace = this.getWidth() - this.getDecoratedMeasuredWidth(view);
                heightSpace = this.getHeight() - this.getDecoratedMeasuredHeight(view);
                this.layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5, widthSpace / 2 + this.getDecoratedMeasuredWidth(view), heightSpace / 5 + this.getDecoratedMeasuredHeight(view));
                if (position == 3) {
                    view.setScaleX(1.0F - (float)(position - 1) * 0.1F);
                    view.setScaleY(1.0F - (float)(position - 1) * 0.1F);
                    view.setTranslationY((float)((position - 1) * view.getMeasuredHeight() / 14));
                } else if (position > 0) {
                    view.setScaleX(1.0F - (float)position * 0.1F);
                    view.setScaleY(1.0F - (float)position * 0.1F);
                    view.setTranslationY((float)(position * view.getMeasuredHeight() / 14));
                } else {
                    view.setOnTouchListener(this.mOnTouchListener);
                }
            }
        } else {
            for(position = itemCount - 1; position >= 0; --position) {
                view = recycler.getViewForPosition(position);
                this.addView(view);
                this.measureChildWithMargins(view, 0, 0);
                widthSpace = this.getWidth() - this.getDecoratedMeasuredWidth(view);
                heightSpace = this.getHeight() - this.getDecoratedMeasuredHeight(view);
                this.layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5, widthSpace / 2 + this.getDecoratedMeasuredWidth(view), heightSpace / 5 + this.getDecoratedMeasuredHeight(view));
                if (position > 0) {
                    view.setScaleX(1.0F - (float)position * 0.1F);
                    view.setScaleY(1.0F - (float)position * 0.1F);
                    view.setTranslationY((float)(position * view.getMeasuredHeight() / 14));
                } else {
                    view.setOnTouchListener(this.mOnTouchListener);
                }
            }
        }

    }
}
