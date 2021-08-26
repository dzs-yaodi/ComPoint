package com.xw.customrecycler.pagegrid;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class PagerGridSnapHelper extends SnapHelper {

    private RecyclerView mRecyclerView;

    public PagerGridSnapHelper() {
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    @Nullable
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int pos = layoutManager.getPosition(targetView);
        PagerConfig.Loge("findTargetSnapPosition, pos = " + pos);
        int[] offset = new int[2];
        if (layoutManager instanceof PagerGridLayoutManager) {
            PagerGridLayoutManager manager = (PagerGridLayoutManager)layoutManager;
            offset = manager.getSnapOffset(pos);
        }

        return offset;
    }

    @Override
    @Nullable
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof PagerGridLayoutManager) {
            PagerGridLayoutManager manager = (PagerGridLayoutManager)layoutManager;
            return manager.findSnapView();
        } else {
            return null;
        }
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int target = -1;
        PagerConfig.Loge("findTargetSnapPosition, velocityX = " + velocityX + ", velocityY" + velocityY);
        if (null != layoutManager && layoutManager instanceof PagerGridLayoutManager) {
            PagerGridLayoutManager manager = (PagerGridLayoutManager)layoutManager;
            if (manager.canScrollHorizontally()) {
                if (velocityX > PagerConfig.getFlingThreshold()) {
                    target = manager.findNextPageFirstPos();
                } else if (velocityX < -PagerConfig.getFlingThreshold()) {
                    target = manager.findPrePageFirstPos();
                }
            } else if (manager.canScrollVertically()) {
                if (velocityY > PagerConfig.getFlingThreshold()) {
                    target = manager.findNextPageFirstPos();
                } else if (velocityY < -PagerConfig.getFlingThreshold()) {
                    target = manager.findPrePageFirstPos();
                }
            }
        }

        PagerConfig.Loge("findTargetSnapPosition, target = " + target);
        return target;
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        } else {
            RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
            if (adapter == null) {
                return false;
            } else {
                int minFlingVelocity = PagerConfig.getFlingThreshold();
                PagerConfig.Loge("minFlingVelocity = " + minFlingVelocity);
                return (Math.abs(velocityY) > minFlingVelocity || Math.abs(velocityX) > minFlingVelocity) && this.snapFromFling(layoutManager, velocityX, velocityY);
            }
        }
    }

    private boolean snapFromFling(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return false;
        } else {
            RecyclerView.SmoothScroller smoothScroller = this.createSnapScroller(layoutManager);
            if (smoothScroller == null) {
                return false;
            } else {
                int targetPosition = this.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                if (targetPosition == -1) {
                    return false;
                } else {
                    smoothScroller.setTargetPosition(targetPosition);
                    layoutManager.startSmoothScroll(smoothScroller);
                    return true;
                }
            }
        }
    }

    @Override
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        return !(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new PagerGridSmoothScroller(this.mRecyclerView);
    }

    public void setFlingThreshold(int threshold) {
        PagerConfig.setFlingThreshold(threshold);
    }
}
