package com.xw.compoint.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoADManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.xw.compoint.R;
import com.xw.compoint.UITools;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = VideoActivity.class.getName();
    private VideoPlayer videoPlayer;
    private ImageView mPlayerCover;
    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private OrientationUtils mOrientationUtils;
    private String imagePath = "https://qhmediaoss.dmqhyadmin.com/162935752574323413";
    private String videoPath = "https://qhmediaoss.dmqhyadmin.com/162935752574423413";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        videoPlayer = findViewById(R.id.videoPlayer);
        
        mPlayerCover = new ImageView(this);
        mPlayerCover.setScaleType(ImageView.ScaleType.FIT_XY);
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();

        if (mOrientationUtils == null) {
            mOrientationUtils = new OrientationUtils(CommonUtil.scanForActivity(this), videoPlayer);
            mOrientationUtils.setRotateWithSystem(false);
            mOrientationUtils.setEnable(false);
        }

        if (mPlayerCover.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) mPlayerCover.getParent();
            viewGroup.removeView(mPlayerCover);
        }
        setListener();

        Glide.with(mPlayerCover).load(imagePath).into(mPlayerCover);
        gsyVideoOptionBuilder
                .setThumbImageView(mPlayerCover)
                .setIsTouchWiget(true)
                .setIsTouchWigetFull(true)
                .setCacheWithPlay(false)
                .setPlayTag(TAG)
                .setShowFullAnimation(true)
                .setUrl(videoPath)
                .setDismissControlTime(3000)
                .build(videoPlayer);

    }

    private void setListener() {
        videoPlayer.getFullscreenButton().setOnClickListener(v -> {
            //开启旋转
            mOrientationUtils.setEnable(true);
            videoPlayer.startWindowFullscreen(this, false, true);
            videoPlayer.showBackIcon();
        });

        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                mOrientationUtils.backToProtVideo();
                mOrientationUtils.setEnable(false);
                videoPlayer.showBackIcon();
                super.onQuitFullscreen(url, objects);
            }
        });
    }

    @Override
    protected void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        GSYVideoADManager.releaseAllVideos();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
//        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        GSYVideoManager.onResume(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (videoPlayer.getScreenState()) {
                videoPlayer.onBackFullscreen();
                return true;
            }  else {
                GSYVideoManager.releaseAllVideos();
                GSYVideoADManager.releaseAllVideos();
                return super.onKeyDown(keyCode, event);
            }
        }
        return false;
    }
}