package com.bc.videodemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoHolder> {

    public static final String TAG = "VideoListAdapter";

    private ArrayList<VideoInfo> data = new ArrayList<>();


    public void setData(ArrayList<VideoInfo> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_player,
                viewGroup,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder videoHolder, int i) {
        int adapterPosition = videoHolder.getAdapterPosition();
        VideoInfo videoInfo = data.get(adapterPosition);
        videoHolder.bind(videoInfo);
    }




    @Override
    public int getItemCount() {
        return data.size();
    }

    //=====================================================================================

    @Override
    public void onViewRecycled(@NonNull VideoHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LogUtils.i(TAG,"  attach to rv ");
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        LogUtils.i(TAG,"  detach from rv ");
    }

    //=======================================================================================
    @Override
    public void onViewAttachedToWindow(@NonNull VideoHolder holder) {
        super.onViewAttachedToWindow(holder);
        LogUtils.i(TAG," attach to window :"+holder.getAdapterPosition());
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoHolder holder) {
        super.onViewDetachedFromWindow(holder);
        LogUtils.i(TAG," detach from window :"+holder.getAdapterPosition());

    }

    //=======================================================================================



}
