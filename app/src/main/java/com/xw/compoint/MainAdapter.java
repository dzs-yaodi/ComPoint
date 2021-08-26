package com.xw.compoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

    private List<String> stringList = new ArrayList<>();

    public MainAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_main_layout,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.btn.setText(stringList.get(position));

        holder.btn.setOnClickListener(view -> {
            JumpUtils.JumpToIndex(holder.itemView.getContext(), position);
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        private Button btn;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            btn = itemView.findViewById(R.id.btn);
        }
    }
}
