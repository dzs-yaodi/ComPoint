package com.xw.compoint.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xw.compoint.R;
import com.xw.customrecycler.gallery.RecyclerGallery;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerGallery recyclerGallery = findViewById(R.id.recycler_gallery);
        GalleryAdapter galleryAdapter = new GalleryAdapter();
        recyclerGallery.setAdapter(galleryAdapter);

        galleryAdapter.setOnItemlickListener(recyclerGallery::scrollToPosition);
    }
}