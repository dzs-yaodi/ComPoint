package com.xw.compoint.horizontal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xw.compoint.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>{

    private List<Integer> integerList = new ArrayList<>();

    public HorizontalAdapter(List<Integer> integerList) {
        this.integerList = integerList;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HorizontalViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_horizontal_layout,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(integerList.get(position))
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return integerList.size();
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
