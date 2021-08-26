package com.xw.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFlowLayout extends ViewGroup {

    /**
     * 储存每一行的剩余空间
     */
    private List<Integer> lineSpaces = new ArrayList<>();

    /**
     * 存每一行的高度
     */
    private List<Integer> lineHieght = new ArrayList<>();
    /**
     * 每一行的view
     */
    private List<List<View>> lineView = new ArrayList<>();

    /**
     * 添加的view
     */
    private List<View> mChildrens = new ArrayList<>();
    /**
     * 每一行是否平分空间
     */
    private boolean isAverageInRow = false;
    /**
     * 每一列是否垂直居中
     */
    private boolean isAverageInColum = true;
    /**
     * 行数
     */
    private int mLineCount = 0;
    /**
     * 前两行里面view 的个数
     */
    private int mTwoLineViewCount = 0;
    /**
     * 展示时最多显示几行
     */
    private int mExpandLineCount = 5;
    /**
     * 展开时显示的view 个数
     */
    private int mExpandLineViewCount = 0;

    private OnTagClickListener onTagClickListener;
    //背景圆圈颜色
    private int strokeColor;
    //字体颜色
    private int textColor;

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = Color.parseColor(strokeColor);
    }

    public void setTextColor(String textColor) {
        this.textColor = Color.parseColor(textColor);
    }

    public HistoryFlowLayout(Context context) {
        this(context, null);
    }

    public HistoryFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HistoryFlowLayout);
        isAverageInRow = array.getBoolean(R.styleable.HistoryFlowLayout_xw_flow_AverageInRow, false);
        isAverageInColum = array.getBoolean(R.styleable.HistoryFlowLayout_xw_flow_AverageInColum, true);
        mExpandLineCount = array.getInteger(R.styleable.HistoryFlowLayout_xw_max_lines, 0);

        strokeColor = array.getColor(R.styleable.HistoryFlowLayout_xw_stroke_color,Color.parseColor("#33FFFFFF"));
        textColor = array.getColor(R.styleable.HistoryFlowLayout_xw_text_color,Color.parseColor("#FFFFFF"));

        array.recycle();
    }

    public int getLineCount() {
        return mLineCount;
    }

    public int getTwoLineViewCount() {
        return mTwoLineViewCount;
    }

    public int getExpandLineViewCount() {
        return mExpandLineViewCount;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    /**
     * 设置是否每列垂直居中
     */
    public void setAverageInColum(boolean averageInColum) {
        if (isAverageInColum != averageInColum) {
            isAverageInColum = averageInColum;
            requestLayout();
        }
    }

    /**
     * 设置是否每一行居中
     */
    public void setAverageInRow(boolean averageInRow) {
        if (isAverageInRow != averageInRow) {
            isAverageInRow = averageInRow;
            requestLayout();
        }
    }

    /**
     * 动态添加view
     *
     * @param childrens
     */
    public void setChildren(List<View> childrens) {
        if (childrens == null) return;

        this.mChildrens = childrens;
        mLineCount = 0;
        mTwoLineViewCount = 0;
        mExpandLineViewCount = 0;
        this.removeAllViews();

        for (int i = 0; i < mChildrens.size(); i++) {
            addView(mChildrens.get(i));
            if (mChildrens.get(i) instanceof TextView) {
                int finalI = i;
                mChildrens.get(i).setOnClickListener(view -> {
                    if (onTagClickListener != null) {
                        onTagClickListener.onTagClick(mChildrens.get(finalI), finalI);
                    }
                });
            }
        }
    }

    /**
     * 重写方法来获取子view的margin 值
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //清除记录数据
        lineSpaces.clear();
        lineHieght.clear();
        lineView.clear();

        //测量view的宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        //计算chuilden 的数量
        int count = getChildCount();
        //统计子view总共高度
        int childTotalHeight = 0;

        //一行中剩余的空间
        int lineLeftSpace = 0;
        int lineRealWidth = 0;
        int lineRealHeight = 0;

        List<View> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //不可见view不做处理
            if (child.getVisibility() == GONE) {
                continue;
            }

            //对子view进行测量
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //获取子view的间距
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            //获取view占据的空间大小
            int childViewWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childViewHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (childViewWidth + lineRealWidth <= viewWidth) {//一行
                //已占用空间
                lineRealWidth += childViewWidth;
                //剩余空间
                lineLeftSpace = viewWidth - lineRealWidth;
                //一行的最大高度
                lineRealHeight = Math.max(lineRealHeight, childViewHeight);
                //将一行中的view 加到同一个集合
                list.add(child);
            } else {//下一行
                if (list.size() != 0) {
                    //统计上一行的高度
                    childTotalHeight += lineRealHeight;
                    //上一行的高度
                    lineHieght.add(lineRealHeight);
                    //上一行剩余的空间
                    lineSpaces.add(lineLeftSpace);
                    //将上一行的元素保存起来
                    lineView.add(list);
                }
                //重置这一行中已占有的空间
                lineRealWidth = childViewWidth;
                //重置一行中剩余的空间
                lineLeftSpace = viewWidth - lineRealWidth;
                //重置一行中的高度
                lineRealHeight = childViewHeight;
                //更换心的集合存储下一行的元素
                list = new ArrayList<>();
                list.add(child);
            }

            //最后一个元素
            if (i == count - 1) {
                childTotalHeight += lineRealHeight;
                //将最后一行的信息存下来
                lineView.add(list);
                lineHieght.add(lineRealHeight);
                lineSpaces.add(lineLeftSpace);
            }
        }

        //宽度可以不用考虑，主要考虑高度
        if (heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(viewWidth, viewHeight);
        } else {
            setMeasuredDimension(viewWidth, childTotalHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //view最开始左边
        int viewLeft = 0;
        //view最开始上边
        int viewTop = 0;

        //每一个view layout的位置
        int vl;
        int vt;
        int vr;
        int vb;

        // 每一行中每一个view多平分的空间
        float averageInRow;
        // 每一列中每一个view距离顶部的高度
        float averageInColumn;

        int colums = lineView.size();
        mLineCount = colums;

        for (int i = 0; i < colums; i++) {
            //该行剩余的空间
            int lineSpace = lineSpaces.get(i);
            //该行的高度
            int lineHeight = lineHieght.get(i);
            //该行所有元素
            List<View> list = lineView.get(i);
            //每一行元素个数
            int row = list.size();
            if (i == 0 || i == 1) {
                mTwoLineViewCount = mTwoLineViewCount + row;
            }

            if (i < mExpandLineCount) {
                mExpandLineViewCount = mExpandLineViewCount + row;
            }

            //view Layout的位置
            //每一行中每个view 多平分的空间（一行只有一个就不管）
            if (isAverageInRow && row > 1) {
                averageInRow = lineSpace * 1.0f / (row + 1);
            } else {
                averageInRow = 0;
            }

            //获取view 的间距属性
            MarginLayoutParams params;
            for (int j = 0; j < row; j++) {
                //对应位置的view元素
                View child = list.get(j);
                params = (MarginLayoutParams) child.getLayoutParams();
                //是否计算每一列中的元素垂直居中的时候多出的距离
                if (isAverageInColum && row > 1) {
                    averageInColumn = (lineHeight - child.getMeasuredHeight() - params.topMargin - params.bottomMargin) / 2;
                } else {
                    averageInColumn = 0;
                }

                //左边位置= 起始位置+ view左间距+多平分的空间
                vl = (int) (viewLeft + params.leftMargin + averageInRow);
                //上边的位置 = 起始位置 + view上边的间距+多平分的空间
                vt = (int) (viewTop + params.topMargin + averageInColumn);
                vr = vl + child.getMeasuredWidth();
                vb = vt + child.getMeasuredHeight();
                child.layout(vl, vt, vr, vb);
                viewLeft += child.getMeasuredWidth() + params.leftMargin + params.rightMargin + averageInRow;
            }

            viewLeft = 0;
            viewTop += lineHeight;
        }
    }

    private List<String> mDatas;

    private List<View> mViewList = new ArrayList<>();

    public void setmDatas(List<String> mDatas) {
        this.mDatas = mDatas;
        initFlowLayout();
    }

    private void initFlowLayout() {
        mViewList.clear();
        addTextView(mDatas.size());
        setChildren(mViewList);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (mLineCount > 2) {  //默认展示2行，其余折叠收起
                    initIvClose(mTwoLineViewCount, mExpandLineViewCount);
                }
            }
        });
    }

    private void initIvClose(int twoLineViewCount, int expandLineViewCount) {
        mViewList.clear();
        addTextView(twoLineViewCount);
        addImageView(true, twoLineViewCount, expandLineViewCount);
        setChildren(mViewList);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (mLineCount > 2) {
                    initIvClose(twoLineViewCount - 1, expandLineViewCount);
                }
            }
        });

    }

    private void initIvOpen(int twoLineViewCount, int expandLineViewCount) {
        mViewList.clear();
        addTextView(expandLineViewCount);
        //收起按钮
        addImageView(false, twoLineViewCount, expandLineViewCount);
        setChildren(mViewList);
    }

    /**
     * 添加textView
     *
     * @param count
     */
    private void addTextView(int count) {
        for (int i = 0; i < count; i++) {
            TextView textView = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.xw_history_play_search_layout, this, false);
            textView.setText(mDatas.get(i));

            if (strokeColor != 0) {
                GradientDrawable drawable = (GradientDrawable) textView.getBackground();
                drawable.setStroke(2, strokeColor);
                textView.setBackground(drawable);
            }

            if (textColor != 0) {
                textView.setTextColor(textColor);
            }

            mViewList.add(textView);
        }
    }

    /**
     * 添加展开/收起按钮
     *
     * @param isDown
     */
    private void addImageView(boolean isDown, int twoLineViewCount, int expandLineViewCount) {
        ImageView imageView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.xw_history_play_search_down, this, false);
        if (isDown) {
            imageView.setImageResource(R.drawable.ic_xw_flow_layout_search);
            imageView.setOnClickListener(v -> initIvOpen(twoLineViewCount, expandLineViewCount));
        } else {
            //收起按钮
            imageView.setOnClickListener(v -> initIvClose(twoLineViewCount, expandLineViewCount));
            imageView.setImageResource(R.drawable.ic_xw_flow_layout_search_up);
        }
        if (strokeColor != 0) {
            GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
            drawable.setStroke(2, strokeColor);
            imageView.setBackground(drawable);
        }

        if (textColor != 0) {
            imageView.setColorFilter(textColor);
        }
        mViewList.add(imageView); //不需要的话可以不添加
    }
}
