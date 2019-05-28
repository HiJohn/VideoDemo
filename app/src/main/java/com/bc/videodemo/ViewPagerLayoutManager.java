package com.bc.videodemo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerLayoutManager extends LinearLayoutManager {


    public ViewPagerLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
        init();
    }

    public ViewPagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    public ViewPagerLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private String TAG = "ViewPagerLayoutManager";

    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener = null;
    private int mDrift = 0;//位移，用来判断移动方向

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (getChildCount() == 1) {
                int postion = getPosition(view);
                mOnViewPagerListener.onPageSelected(view,postion, postion == getChildCount() - 1);
                view.setTag(true);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            boolean tag = view.getTag() != null && (boolean)view.getTag();
            if (tag) {
                mOnViewPagerListener.onPageRelease(view,mDrift >= 0, getPosition(view));
                view.setTag(false);
            }
        }
    };

    private void init() {
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        view.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    /**
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     *
     * @param state 滑动状态
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        mOnViewPagerListener.onPageScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                {
                    View viewIdle = mPagerSnapHelper.findSnapView(this);
                    if (viewIdle != null) {
                        int positionIdle = getPosition(viewIdle);
                        boolean tag = viewIdle.getTag() != null && (boolean)viewIdle.getTag();
                        if (getChildCount() == 1 && !tag) {
                            mOnViewPagerListener.onPageSelected(viewIdle,positionIdle, positionIdle == getItemCount() - 1);
                            viewIdle.setTag(true);
                        }
                    }
                }
            break;
            case  RecyclerView.SCROLL_STATE_DRAGGING :
            {
                View viewIdle = mPagerSnapHelper.findSnapView(this);
                if (viewIdle != null) {
                    int positionIdle = getPosition(viewIdle);
                    if (positionIdle == 0) {
                        boolean tag = viewIdle.getTag() != null && (boolean)viewIdle.getTag();
                        if (getChildCount() == 2 && !tag) {
                            mOnViewPagerListener.onPageSelected(viewIdle,positionIdle, positionIdle == getItemCount() - 1);
                            viewIdle.setTag(true);
                        }
                    }
                }
            }
            break;
            case RecyclerView.SCROLL_STATE_SETTLING :
                break;
        }
    }

    /**
     * 布局完成后调用
     *
     * @param state state
     */
    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }

    /**
     * 监听竖直方向的相对偏移量
     *
     * @param dy       dy
     * @param recycler recycler
     * @param state    state
     * @return 竖直方向的相对偏移量
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    /**
     * 监听水平方向的相对偏移量
     *
     * @param dx       dx
     * @param recycler recycler
     * @param state    state
     * @return 水平方向的相对偏移量
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    /**
     * 设置监听
     *
     * @param listener 滑动监听
     */
    public void setOnViewPagerListener(OnViewPagerListener listener) {
        this.mOnViewPagerListener = listener;
    }

}
