package com.xw.customrecycler.slide;


import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemTouchHelperCallback<T> extends ItemTouchHelper.Callback {

    private final RecyclerView.Adapter adapter;
    private List<T> dataList;
    private OnSlideListener<T> mListener;

    public ItemTouchHelperCallback(@NonNull RecyclerView.Adapter adapter, @NonNull List<T> dataList) {
        this.adapter = (RecyclerView.Adapter)this.checkIsNull(adapter);
        this.dataList = (List)this.checkIsNull(dataList);
    }

    public ItemTouchHelperCallback(@NonNull RecyclerView.Adapter adapter, @NonNull List<T> dataList, OnSlideListener<T> listener) {
        this.adapter = (RecyclerView.Adapter)this.checkIsNull(adapter);
        this.dataList = (List)this.checkIsNull(dataList);
        this.mListener = listener;
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        } else {
            return t;
        }
    }

    public void setOnSlideListener(OnSlideListener<T> mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int slideFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof SlideLayoutManager) {
            slideFlags = 12;
        }

        return makeMovementFlags(dragFlags, slideFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        viewHolder.itemView.setOnTouchListener((View.OnTouchListener)null);
        int layoutPosition = viewHolder.getLayoutPosition();
        T remove = this.dataList.remove(layoutPosition);
        this.adapter.notifyDataSetChanged();
        if (this.mListener != null) {
            this.mListener.onSlided(viewHolder, remove, direction == 4 ? 1 : 4);
        }

        if (this.adapter.getItemCount() == 0 && this.mListener != null) {
            this.mListener.onClear();
        }

    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        if (actionState == 1) {
            float ratio = dX / this.getThreshold(recyclerView, viewHolder);
            if (ratio > 1.0F) {
                ratio = 1.0F;
            } else if (ratio < -1.0F) {
                ratio = -1.0F;
            }

            itemView.setRotation(ratio * 15.0F);
            int childCount = recyclerView.getChildCount();
            int position;
            int index;
            View view;
            if (childCount > 3) {
                for(position = 1; position < childCount - 1; ++position) {
                    index = childCount - position - 1;
                    view = recyclerView.getChildAt(position);
                    view.setScaleX(1.0F - (float)index * 0.1F + Math.abs(ratio) * 0.1F);
                    view.setScaleY(1.0F - (float)index * 0.1F + Math.abs(ratio) * 0.1F);
                    view.setTranslationY(((float)index - Math.abs(ratio)) * (float)itemView.getMeasuredHeight() / 14.0F);
                }
            } else {
                for(position = 0; position < childCount - 1; ++position) {
                    index = childCount - position - 1;
                    view = recyclerView.getChildAt(position);
                    view.setScaleX(1.0F - (float)index * 0.1F + Math.abs(ratio) * 0.1F);
                    view.setScaleY(1.0F - (float)index * 0.1F + Math.abs(ratio) * 0.1F);
                    view.setTranslationY(((float)index - Math.abs(ratio)) * (float)itemView.getMeasuredHeight() / 14.0F);
                }
            }

            if (this.mListener != null) {
                if (ratio != 0.0F) {
                    this.mListener.onSliding(viewHolder, ratio, ratio < 0.0F ? 4 : 8);
                } else {
                    this.mListener.onSliding(viewHolder, ratio, 1);
                }
            }
        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0.0F);
    }

    private float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return (float)recyclerView.getWidth() * this.getSwipeThreshold(viewHolder);
    }
}
