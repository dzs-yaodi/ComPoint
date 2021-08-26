package com.xw.compoint.slide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.compoint.R;
import com.xw.customrecycler.slide.ItemConfig;
import com.xw.customrecycler.slide.ItemTouchHelperCallback;
import com.xw.customrecycler.slide.OnSlideListener;
import com.xw.customrecycler.slide.SlideLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class SlideActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SmileView smileView;

    private int mLikeCount = 50;
    private int mDisLikeCount = 50;
    private List<SlideBean> mList = new ArrayList<>();
    private MyAdapter myAdapter;
    private ItemTouchHelperCallback itemTouchHelperCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        recyclerView = findViewById(R.id.recycler_view);
        smileView = findViewById(R.id.smile_view);

        smileView.setLike(mLikeCount);
        smileView.setDisLike(mDisLikeCount);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        addData();

        itemTouchHelperCallback = new ItemTouchHelperCallback(recyclerView.getAdapter(),mList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);

        SlideLayoutManager layoutManager = new SlideLayoutManager(recyclerView,itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        setListener();
    }

    private void setListener() {
        itemTouchHelperCallback.setOnSlideListener(new OnSlideListener() {
            @Override
            public void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {

            }

            @Override
            public void onSlided(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                if (direction == ItemConfig.SLIDED_LEFT) {
                    mDisLikeCount--;
                    smileView.setDisLike(mDisLikeCount);
                    smileView.disLikeAnimation();
                } else if (direction == ItemConfig.SLIDED_LEFT){
                    mLikeCount++;
                    smileView.setLike(mLikeCount);
                    smileView.likeAnimation();
                }
                int position = viewHolder.getAdapterPosition();
                Log.e("info", "onSlided--position:" + position);
            }

            @Override
            public void onClear() {
                addData();
            }
        });
    }

    /**
     * 向集合中添加数据
     */
    private void addData(){
        int[] icons = {R.mipmap.header_icon_1, R.mipmap.header_icon_2, R.mipmap.header_icon_3,
                R.mipmap.header_icon_4, R.mipmap.header_icon_1, R.mipmap.header_icon_2};
        String[] titles = {"Acknowledging", "Belief", "Confidence", "Dreaming", "Happiness", "Confidence"};
        String[] says = {
                "Do one thing at a time, and do well.",
                "Keep on going never give up.",
                "Whatever is worth doing is worth doing well.",
                "I can because i think i can.",
                "Jack of all trades and master of none.",
                "Keep on going never give up.",
                "Whatever is worth doing is worth doing well.",
        };
        int[] bgs = {
                R.mipmap.img_slide_1,
                R.mipmap.img_slide_2,
                R.mipmap.img_slide_3,
                R.mipmap.img_slide_4,
                R.mipmap.img_slide_5,
                R.mipmap.img_slide_6
        };

        for (int i = 0; i < 6; i++) {
            mList.add(new SlideBean(bgs[i],titles[i],icons[i],says[i]));
        }
    }


    /**
     * 适配器
     */
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SlideBean bean = mList.get(position);
            holder.imgBg.setImageResource(bean.getItemBg());
            holder.tvTitle.setText(bean.getTitle());
            holder.userIcon.setImageResource(bean.getUserIcon());
            holder.userSay.setText(bean.getUserSay());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBg;
            ImageView userIcon;
            TextView tvTitle;
            TextView userSay;

            public ViewHolder(View itemView) {
                super(itemView);
                imgBg = itemView.findViewById(R.id.img_bg);
                userIcon = itemView.findViewById(R.id.img_user);
                tvTitle = itemView.findViewById(R.id.tv_title);
                userSay = itemView.findViewById(R.id.tv_user_say);
            }
        }
    }
}