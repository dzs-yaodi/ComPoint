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
 * 圆形指示器
 */
public class DotNavigator extends View implements IPagerNavigator {

    //圆点靠左
    public static final int START = 1;
    //圆点靠右
    public static final int END = 2;
    //圆点居中
    public static final int CENTER = 3;

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @IntDef({START, END, CENTER})
    @interface DotGravity {
    }

    private int mRadius;
    private int mCircleColor;
    private static final int mNormalCircleColor = Color.LTGRAY;
    private int mCircleSpacing;
    private int mCircleCount;

    private int mCurrentIndex;
    private List<PointF> mCirclePoints = new ArrayList<>();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //表示圆点开始的位置
    private int dotGravity = CENTER;
    //圆角矩形需要中心点 - 间距
    private int widthMargin = 20;
    private int heightMargin;
    //是否使用圆角矩形
    private boolean isSelectRoundRect = false;

    public DotNavigator(Context context) {
        super(context);
        mRadius = UIUtil.dip2px(context, 3);
        mCircleSpacing = UIUtil.dip2px(context, 9);
        widthMargin = (int) (mCircleSpacing / 1.2);
        heightMargin = mRadius;
    }

    public void setSelectRoundRect(boolean selectRoundRect) {
        isSelectRoundRect = selectRoundRect;
    }

    /**
     * 设置方向
     */
    public void setDotGravity(@DotGravity int gravity) {
        this.dotGravity = gravity;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    private void prepareCirclePoints() {
        mCirclePoints.clear();
        if (mCircleCount > 0) {
            int y = getHeight() / 2;
            int measureWidth = mCircleCount * mRadius * 2 + (mCircleCount - 1) * mCircleSpacing;
            int centerSpacing = mRadius * 2 + mCircleSpacing;
            int startX;
            if (dotGravity == START) {
                startX = mRadius;
            } else if (dotGravity == END) {
                if (isSelectRoundRect) {
                    if (mCircleCount > 9) {
                        startX = (getWidth() - measureWidth - (5 * mCircleCount));
                    } else if (mCircleCount >= 4) {
                        startX = (getWidth() - measureWidth - (UITools.dip2px(getContext(), 2) * mCircleCount));
                    } else {
                        startX = (getWidth() - measureWidth) - mCircleSpacing;
                    }
                } else {
                    startX = (getWidth() - measureWidth) + mRadius;
                }
            } else {
                startX = (getWidth() - measureWidth) / 2 + mRadius;
            }
            for (int i = 0; i < mCircleCount; i++) {
                PointF pointF = new PointF(startX, y);
                mCirclePoints.add(pointF);
                startX += centerSpacing;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDeselectedCircles(canvas);
        drawSelectedCircle(canvas);
    }

    private void drawDeselectedCircles(Canvas canvas) {
        mPaint.setColor(mNormalCircleColor);
        for (int i = 0, j = mCirclePoints.size(); i < j; i++) {
            PointF pointF = mCirclePoints.get(i);
            if (isSelectRoundRect && mCurrentIndex != i){
                if (mCurrentIndex == 0) {
                    canvas.drawCircle(pointF.x + widthMargin -5, pointF.y, mRadius, mPaint);
                }else if (mCurrentIndex == mCirclePoints.size() - 1){
                    canvas.drawCircle(pointF.x - widthMargin + 5, pointF.y, mRadius, mPaint);
                }else{
                    if (i < mCurrentIndex){
                        canvas.drawCircle(pointF.x - widthMargin + 5, pointF.y, mRadius, mPaint);
                    }else{
                        canvas.drawCircle(pointF.x + widthMargin - 5, pointF.y, mRadius, mPaint);
                    }
                }
            }else {
                canvas.drawCircle(pointF.x, pointF.y, mRadius, mPaint);
            }
        }
    }

    private void drawSelectedCircle(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        mPaint.setStyle(Paint.Style.FILL);
        if (mCirclePoints.size() > 0) {
            float selectedCircleX = mCirclePoints.get(mCurrentIndex).x;
            int height = getHeight() / 2;
            if (isSelectRoundRect) {
                RectF rectF = new RectF(selectedCircleX - widthMargin, height - heightMargin,
                        selectedCircleX + widthMargin, height + heightMargin);
                canvas.drawRoundRect(rectF, 50, 50, mPaint);
            } else {
                canvas.drawCircle(selectedCircleX, height, mRadius, mPaint);
            }
        }
    }

    // 被添加到 magicindicator 时调用
    @Override
    public void onAttachToMagicIndicator() {
    }

    // 从 magicindicator 上移除时调用
    @Override
    public void onDetachFromMagicIndicator() {
    }

    // 当指示数目改变时调用
    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        invalidate();
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
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
        return mCircleSpacing;
    }

    public void setCircleSpacing(int circleSpacing) {
        mCircleSpacing = circleSpacing;
        widthMargin = (int) (mCircleSpacing / 1.1);
        prepareCirclePoints();
        invalidate();
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public int getCircleCount() {
        return mCircleCount;
    }

    /**
     * notifyDataSetChanged应该紧随其后调用
     *
     * @param circleCount
     */
    public void setCircleCount(int circleCount) {
        mCircleCount = circleCount;
    }
}
