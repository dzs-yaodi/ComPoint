package com.xw.customrecycler.slide;

import androidx.recyclerview.widget.RecyclerView;

public interface OnSlideListener<T> {
    void onSliding(RecyclerView.ViewHolder var1, float var2, int var3);

    void onSlided(RecyclerView.ViewHolder var1, T var2, int var3);

    void onClear();
}
