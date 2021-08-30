package com.xw.compoint;

import android.content.Context;
import android.content.Intent;

import com.xw.compoint.echelon.EchelonActivity;
import com.xw.compoint.gallery.GalleryActivity;
import com.xw.compoint.horizontal.HorizontalActivity;
import com.xw.compoint.slide.SlideActivity;
import com.xw.compoint.video.VideoActivity;
import com.xw.compoint.viewpager.ViewPagerManagerActivity;

public class JumpUtils {

    public static void JumpToIndex(Context context,int position) {

        Intent intent = new Intent();
        if (position == 0) {
            //跳转画廊
            intent.setClass(context, GalleryActivity.class);
        } else if (position == 1) {
            intent.setClass(context, HorizontalActivity.class);
        } else if (position == 2) {
            intent.setClass(context, EchelonActivity.class);
        } else if (position == 3) {
            intent.setClass(context, SlideActivity.class);
        } else if (position == 4) {
            intent.setClass(context, ViewPagerManagerActivity.class);
        } else if (position == 5) {
            intent.setClass(context,SearchHistoryActivity.class);
        } else if (position == 6) {
            intent.setClass(context, VideoActivity.class);
        }

        context.startActivity(intent);
    }
}
