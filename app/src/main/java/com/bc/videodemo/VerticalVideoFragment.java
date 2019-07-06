package com.bc.videodemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bc.videodemo.databinding.FragmentVerticalVideoBinding;

import java.util.ArrayList;

import leakcanary.LeakSentry;

public class VerticalVideoFragment extends Fragment {

    public static final String TAG = "VerticalVideoFragment";

    FragmentVerticalVideoBinding binding;
    VerticalAdapter adapter;

    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    public VerticalVideoFragment(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogUtils.i(TAG,"  onAttach ");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG,"  onCreate ");

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        LogUtils.i(TAG,"  onViewStateRestored ");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_vertical_video,container,
                false);
        LogUtils.i(TAG,"  onCreateView ");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.i(TAG,"  onViewCreated ");

        adapter = new VerticalAdapter(this);
        binding.videoViewpager.setAdapter(adapter);
        binding.videoViewpager.setOffscreenPageLimit(3);
        binding.videoViewpager.registerOnPageChangeCallback(pageChangeCallback);
        loadData();
    }



    private void loadData(){
        if (getActivity()==null){
            return;
        }
        Activity activity = getActivity();
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                final ArrayList<VideoInfo> data = MeUtils.queryVideoInfo(activity);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setVideoInfos(data);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.i(TAG,"  onActivityCreated ");

    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.i(TAG,"  onStart ");

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG,"  onResume ");

    }



    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(TAG,"  onPause ");

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i(TAG,"  onStop ");

        binding.videoViewpager.unregisterOnPageChangeCallback(pageChangeCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i(TAG,"  onDestroyView ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG,"  onDestroy ");
        LeakSentry.INSTANCE.getRefWatcher().watch(this);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i(TAG,"  onDetach ");

    }
}
