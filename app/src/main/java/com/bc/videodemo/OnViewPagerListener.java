package com.bc.videodemo;

import android.view.View;


public interface OnViewPagerListener {


    /*释放的监听*/
    void onPageRelease(View itemView, boolean isNext, int position);

    /*选中的监听以及判断是否滑动到底部*/
    void onPageSelected(View itemView, int position, boolean isBottom);

    /*布局完成的监听*/
    void onPageScrollStateChanged(int state);
}
