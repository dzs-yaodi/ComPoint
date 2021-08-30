package com.xw.compoint.video;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.xw.compoint.R;

public class PlayerLoadingView extends AppCompatImageView {

    private AnimationDrawable animationDrawable;
    private int image_resouce = R.drawable.fc_community_player_loading_resouce;

    public PlayerLoadingView(Context context) {
        this(context, null);
    }

    public PlayerLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable drawable = ContextCompat.getDrawable(context, image_resouce).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
//        DrawableCompat.setTint(wrappedDrawable, themeColor);

        setBackground(wrappedDrawable);

    }

    public void start() {
        if (animationDrawable == null) {
            animationDrawable = getAnimationDrawable(getBackground());
        }
        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    public void stop() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    /**
     * 获取动画Drawable
     *
     * @param drawable AnimationDrawable
     * @return 如果为null, 未知情况.
     */
    private AnimationDrawable getAnimationDrawable(Drawable drawable) {
        Drawable d = DrawableCompat.unwrap(drawable);
        if (d instanceof AnimationDrawable) {
            return (AnimationDrawable) drawable;
        }
        return null;
    }

}
