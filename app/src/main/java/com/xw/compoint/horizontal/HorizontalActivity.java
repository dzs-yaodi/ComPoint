package com.xw.compoint.horizontal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.xw.compoint.R;
import com.xw.customrecycler.pagegrid.PagerGridLayoutManager;
import com.xw.customrecycler.pagegrid.PagerGridSnapHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HorizontalActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private MagicIndicator magicIndicator;
    private DotNavigator navigator;
    private int[] mImages = {R.mipmap.image1,R.mipmap.image2,R.mipmap.image3,R.mipmap.image4,
            R.mipmap.image5,R.mipmap.image6,R.mipmap.image7,
            R.mipmap.image8,R.mipmap.image9,R.mipmap.image10,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);

        recycler = findViewById(R.id.recycler);
        magicIndicator = findViewById(R.id.magic_indicator);

        //set indicator
        navigator = new DotNavigator(this);
        navigator.setCircleColor(Color.parseColor("#CD1F1F"));
        magicIndicator.setNavigator(navigator);

        //set layoutManager
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(2, 4, PagerGridLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(layoutManager);
        //set snap helper
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(recycler);

        //set listener
        layoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                magicIndicator.onPageSelected(pageIndex);
            }
        });

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < mImages.length; i++) {
            integers.add(mImages[i]);
        }

        HorizontalAdapter adapter = new HorizontalAdapter(integers);
        recycler.setAdapter(adapter);

        //设置指示器
        int size = integers.size();
        int page = (size + 7) / 8;
        if (page > 1) {
            navigator.setCircleCount(page);
//            navigator.onPageSelected(item.discoverPageIndex);
            magicIndicator.setVisibility(View.VISIBLE);
        } else {
            magicIndicator.setVisibility(View.GONE);
        }
    }
}