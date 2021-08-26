package com.xw.compoint.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.IntDef;

import com.xw.compoint.UITools;

import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * 圆角矩形指示器
 */
public class RectangleNavigator extends View implements IPagerNavigator {

    //圆点靠左
    public static final int START = 1;
    //圆点靠右
    public static final int END = 2;
    //圆点居中
    public static final int CENTER = 3;

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @IntDef({START, END, CENTER})
    @interface RectangleGravity {
    }

    private int mRadius;
    private int mSelectColor;
    private static final int mNormalColor = Color.LTGRAY;
    private int mSpacing;
    private int mSpacing1;
    private int mCount;

    private int mCurrentIndex;
    private List<PointF> mPoints = new ArrayList<>();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //表示圆点开始的位置
    private int dotGravity = CENTER;
    private Context mContext;

    public RectangleNavigator(Context context) {
        super(context);
        this.mContext = context;
        mRadius = UIUtil.dip2px(context, 2);
        mSpacing = UIUtil.dip2px(context, 6);
        mSpacing1 = UIUtil.dip2px(context, 10);
    }

    /**
     * 设置方向
     */
    public void setDotGravity(@RectangleGravity int gravity) {
        this.dotGravity = gravity;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    private void prepareCirclePoints() {
        mPoints.clear();
        if (mCount > 0) {
            int y = getHeight() / 2;
            int measureWidth = mCount * mRadius * 2 + (mCount - 1) * mSpacing;
            int centerSpacing = mRadius * 2 + mSpacing;
            int startX;
            if (dotGravity == START) {
                startX = mRadius;
            } else if (dotGravity == END) {
                if (mCount > 9){
                    startX = (getWidth() - measureWidth - (5 * mCount));
                }else if (mCount >= 4){
                    startX = (getWidth() - measureWidth - (10 * mCount));
                }else {
                    startX = (getWidth() - measureWidth - (mSpacing1 * mCount));
                }
            } else {
                startX = (getWidth() - measureWidth) / 2 + mRadius;
            }
            for (int i = 0; i < mCount; i++) {
                PointF pointF = new PointF(startX, y);
                mPoints.add(pointF);
                startX += centerSpacing;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDeselected(canvas);
        drawSelected(canvas);
    }

    private void drawDeselected(Canvas canvas) {
        mPaint.setColor(mNormalColor);
        for (int i = 0, j = mPoints.size(); i < j; i++) {
            PointF pointF = mPoints.get(i);
            canvas.drawRoundRect(new RectF(pointF.x, pointF.y,pointF.x + UITools.dip2px(mContext,20),
                    pointF.y + UITools.dip2px(mContext,2)),
                    UITools.dip2px(mContext, 1), UITools.dip2px(mContext, 1),mPaint);
        }
    }

    private void drawSelected(Canvas canvas) {
        mPaint.setColor(mSelectColor);
        mPaint.setStyle(Paint.Style.FILL);
        if (mPoints.size() > 0) {
            float selectedCircleX = mPoints.get(mCurrentIndex).x;
            canvas.drawRoundRect(new RectF(selectedCircleX, getHeight() / 2,selectedCircleX + UITools.dip2px(mContext,20),
                            getHeight() / 2 + UITools.dip2px(mContext,2)),
                    UITools.dip2px(mContext, 1), UITools.dip2px(mContext, 1),mPaint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttachToMagicIndicator() {

    }

    @Override
    public void onDetachFromMagicIndicator() {

    }

    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        invalidate();
    }

    public void setSelectColor(int circleColor) {
        mSelectColor = circleColor;
        invalidate();
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
        prepareCirclePoints();
        invalidate();
    }

    public int getCircleSpacing() {
        return mSpacing;
    }

    public void setSpacing(int circleSpacing) {
        mSpacing = circleSpacing;
        prepareCirclePoints();
        invalidate();
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public int getCircleCount() {
        return mCount;
    }

    /**
     * notifyDataSetChanged应该紧随其后调用
     *
     * @param circleCount
     */
    public void setCircleCount(int circleCount) {
        mCount = circleCount;
    }
}
