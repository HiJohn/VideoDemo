package com.bc.videodemo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class VideoListFragment extends Fragment {

    private ArrayList<VideoInfo> videoInfos = new ArrayList<>();

    private RecyclerView videoRv ;

    private VideoListAdapter videoListAdapter = new VideoListAdapter();


    private ViewPagerLayoutManager layoutManager;


    public void setVideoInfos(ArrayList<VideoInfo> data) {
        this.videoInfos = data;
        videoListAdapter.setData(videoInfos);
        videoListAdapter.notifyDataSetChanged();
    }

    public static VideoListFragment getInstance(){
        return new VideoListFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            videoInfos = getArguments().getParcelableArrayList(MeUtils.DATA_TAG);
        }
        layoutManager = new ViewPagerLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false);
        layoutManager.setOnViewPagerListener(pagerListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list,container,false);
        videoRv = view.findViewById(R.id.video_rv);
        videoRv.setLayoutManager(layoutManager);
        videoRv.setAdapter(videoListAdapter);
        return view;
    }


    private OnViewPagerListener pagerListener = new OnViewPagerListener() {
        @Override
        public void onPageRelease(View itemView, boolean isNext, int position) {
            VideoHolder videoHolder = (VideoHolder) videoRv.getChildViewHolder(itemView);
            videoHolder.pause();
        }

        @Override
        public void onPageSelected(View itemView, int position, boolean isBottom) {
            VideoHolder videoHolder = (VideoHolder) videoRv.getChildViewHolder(itemView);
            videoHolder.play();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
