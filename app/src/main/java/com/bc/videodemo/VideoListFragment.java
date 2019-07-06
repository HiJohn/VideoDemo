package com.bc.videodemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;

import leakcanary.LeakSentry;

public class VideoListFragment extends Fragment  {

    public static final String TAG = "VideoListFragment";

    private ArrayList<VideoInfo> videoInfos = new ArrayList<>();

    private RecyclerView videoRv;

    private VideoListAdapter videoListAdapter = new VideoListAdapter();


    private ViewPagerLayoutManager layoutManager;


    public void setVideoInfos(ArrayList<VideoInfo> data) {
        this.videoInfos = data;
        videoListAdapter.setData(videoInfos);
        videoListAdapter.notifyDataSetChanged();
    }

    public static VideoListFragment getInstance() {
        return new VideoListFragment();
    }


    public void pauseOrPlay(boolean isVisibleToUser) {
        int po = layoutManager.findLastCompletelyVisibleItemPosition();
        VideoHolder videoHolder = (VideoHolder) videoRv.findViewHolderForLayoutPosition(po);
        if (isVisibleToUser) {
            if (videoHolder != null) {
                videoHolder.play();
            }
        } else {
            if (videoHolder != null) {
                videoHolder.pause();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(TAG,"onPause ");
        pauseOrPlay(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i(TAG,"onDetach ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        layoutManager.removeAllViews();
        LogUtils.i(TAG,"onDestroyView ");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG,"onResume ");
        if (getUserVisibleHint()) {
            pauseOrPlay(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.i(TAG," setUserVisibleHint: "+isVisibleToUser);
        if (layoutManager!=null){
            pauseOrPlay(isVisibleToUser);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG,"onCreate ");
        if (getArguments() != null) {
            videoInfos = getArguments().getParcelableArrayList(MeUtils.DATA_TAG);
        }
        layoutManager = new ViewPagerLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOnViewPagerListener(pagerListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        videoRv = view.findViewById(R.id.video_rv);
        videoRv.setLayoutManager(layoutManager);
        videoRv.setAdapter(videoListAdapter);
        return view;
    }


    private OnViewPagerListener pagerListener = new OnViewPagerListener() {
        @Override
        public void onPageRelease(View itemView, boolean isNext, int position) {

        }

        @Override
        public void onPageSelected(View itemView, int position, boolean isBottom) {

//            findRetain();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {

            }
        }
    };




    private String mVideoName = "";

    private void findRetain(){
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager!=null){
//            RetainFragment retainFragment =
//                    (RetainFragment) fragmentManager.findFragmentById(R.id.retain_fragment);
//            if (retainFragment==null){
//                retainFragment = (RetainFragment) fragmentManager.findFragmentByTag("retain");
//            }
            RetainFragment retainFragment = (RetainFragment) fragmentManager.findFragmentByTag("retain");

            if (retainFragment!=null){
                retainFragment.setMeVideoTag(mVideoName);
            }else {
                ToastUtils.showShort(" not find retain frag ");
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LeakSentry.INSTANCE.getRefWatcher().watch(this);
    }
}
