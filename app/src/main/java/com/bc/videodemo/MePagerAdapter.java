package com.bc.videodemo;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MePagerAdapter extends FragmentStatePagerAdapter {

    private static String[] pagerTitle = {"list","grid"};
    private VideoListFragment listFragment = VideoListFragment.getInstance();
    private VideoGridFragment gridFragment = VideoGridFragment.getInstance();


    public MePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(ArrayList<VideoInfo> data) {
        listFragment.setVideoInfos(data);
        gridFragment.setData(data);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitle[position];
    }


    @Override
    public Fragment getItem(int i) {
        if (i==0){
            return listFragment;
        }else if (i==1){
            return gridFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return pagerTitle.length;
    }


}
