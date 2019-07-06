package com.bc.videodemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class VerticalAdapter extends FragmentStateAdapter {


    private ArrayList<VideoInfo> videoInfos = new ArrayList<>();

    public VerticalAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public VerticalAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public VerticalAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setVideoInfos(ArrayList<VideoInfo> videoInfos) {
        this.videoInfos = videoInfos;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        VideoInfo videoInfo = videoInfos.get(position);
        return VideoSingleFragment.newInstance(videoInfo);
    }

    @Override
    public int getItemCount() {
        return videoInfos.size();
    }
}
