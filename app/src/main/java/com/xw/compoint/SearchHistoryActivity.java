package com.xw.compoint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.xw.flowlayout.HistoryFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryActivity extends AppCompatActivity {

    private List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        HistoryFlowLayout flowLayout = findViewById(R.id.flowLayout);
//        flowLayout.setTextColor("#333333");
//        flowLayout.setStrokeColor("#19A1DF");

        for (int i = 0; i < 30; i++) {
            if (i % 2 == 0) {
                stringList.add("测试数据免费发放" + i);
            } else {
                stringList.add("数据" + i);
            }
        }

        flowLayout.setmDatas(stringList);

        flowLayout.setOnTagClickListener((view, position) -> {
            Toast.makeText(this, "点击了" + stringList.get(position), Toast.LENGTH_SHORT).show();
        });
    }
}