package com.xw.compoint.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xw.compoint.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{

    private int[] mImages = {R.mipmap.image1,R.mipmap.image2,R.mipmap.image3,R.mipmap.image4,
            R.mipmap.image5,R.mipmap.image6,R.mipmap.image7,
            R.mipmap.image8,R.mipmap.image9,R.mipmap.image10,};

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_gallery_layout,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(mImages[position])
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImages.length;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(view -> {
                if (onItemlickListener != null) {
                    onItemlickListener.onItemListener(getAdapterPosition());
                }
            });
        }
    }

    private OnItemlickListener onItemlickListener;

    public void setOnItemlickListener(OnItemlickListener onItemlickListener) {
        this.onItemlickListener = onItemlickListener;
    }

    public interface OnItemlickListener {
        void onItemListener(int position);
    }
}
