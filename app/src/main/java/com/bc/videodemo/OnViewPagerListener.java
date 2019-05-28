package com.bc.videodemo;

import android.view.View;

/**
 * description ：
 *
 * @author : xupengfei
 * @project ：s_video_bc
 * @date : 2019/4/24 11:09
 * 版权所有 2019 佰策科技。保留所有权利。
 */
public interface OnViewPagerListener {


    /*释放的监听*/
    void onPageRelease(View itemView, boolean isNext, int position);

    /*选中的监听以及判断是否滑动到底部*/
    void onPageSelected(View itemView, int position, boolean isBottom);

    /*布局完成的监听*/
    void onPageScrollStateChanged(int state);
}
