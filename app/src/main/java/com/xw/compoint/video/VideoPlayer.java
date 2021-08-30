package com.xw.compoint.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.xw.compoint.R;

import moe.codeest.enviews.ENDownloadView;

public class VideoPlayer extends StandardGSYVideoPlayer {

    private PlayerLoadingView loadingView;

    public VideoPlayer(Context context) {
        super(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fc_community_layout_player;
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        loadingView = findViewById(R.id.loading_constom);
    }

    public void showBackIcon() {

        ImageView imageView = getBackButton();
        if (imageView != null) {
            if (mIfCurrentIsFullscreen) {
                imageView.setVisibility(VISIBLE);
            } else {
                imageView.setVisibility(GONE);
            }
        }
    }

    /**
     * 判断屏幕状态
     */
    public boolean getScreenState() {
        return mIfCurrentIsFullscreen;
    }

    private void showLoading() {
        hideLoading();
        if (loadingView != null) {
            loadingView.setVisibility(VISIBLE);
            loadingView.start();
        }
    }

    private void hideLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(INVISIBLE);
            loadingView.stop();
        }
    }

    @Override
    public void clickStartIcon() {
        if (getCurrentState() != CURRENT_STATE_PLAYING) {
            addSlowVideoPleyNums();
        }
        super.clickStartIcon();
    }

    /**
     * 播放统计
     */
    @SuppressLint("HardwareIds")
    private void addSlowVideoPleyNums() {
//        if (moduleInfo != null) {
//            String url = TmOkClient.SAAS_V2 + "/live/api/index/addClick/" + moduleInfo.id;
//            String uuid = "";
//            if (ServerConfig.getUserId(context) > 0) {
//                uuid = String.valueOf(ServerConfig.getUserId(context));
//            } else {
//                uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            }
//
//            Disposable disposable = Api2.getService().addNums(url, config.header, uuid)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(baseResult -> {
//                    }, throwable -> throwable.printStackTrace());
//        }
    }

    @Override
    protected void changeUiToPreparingShow() {

        showLoading();

        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, VISIBLE);
        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, VISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView) mLoadingProgressBar;
            if (enDownloadView.getCurrentState() == ENDownloadView.STATE_PRE) {
                ((ENDownloadView) mLoadingProgressBar).start();
            }
        }
    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        hideLoading();
        setViewShowState(mBottomProgressBar, VISIBLE);
    }

    @Override
    protected void changeUiToClear() {
        super.changeUiToClear();
        hideLoading();
    }

    @Override
    protected void changeUiToPrepareingClear() {
        super.changeUiToPrepareingClear();
        hideLoading();
    }

    @Override
    protected void changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear();
        showLoading();
    }

    @Override
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();
        hideLoading();
    }

    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        hideLoading();
        setViewShowState(mBottomProgressBar, VISIBLE);
    }

    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        hideLoading();
        setViewShowState(mBottomProgressBar, VISIBLE);
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        showLoading();
        setViewShowState(mBottomProgressBar, VISIBLE);
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        hideLoading();
        setViewShowState(mBottomProgressBar, VISIBLE);
    }

    @Override
    protected void changeUiToError() {
        super.changeUiToError();
        hideLoading();
        setViewShowState(mBottomProgressBar, INVISIBLE);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {

        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        if (gsyBaseVideoPlayer instanceof VideoPlayer) {
            VideoPlayer player = (VideoPlayer) gsyBaseVideoPlayer;
            cloneMyParam(player);
        }

        return gsyBaseVideoPlayer;
    }

    /**
     * 全屏功能时,复制参数到全屏播放器.
     *
     * @param newPlayer 全屏播放器
     */
    private void cloneMyParam(VideoPlayer newPlayer) {
//        newPlayer.setParams(videoPojo);
        newPlayer.mListItemRect = mListItemRect;
        newPlayer.mListItemSize = mListItemSize;
    }
}

