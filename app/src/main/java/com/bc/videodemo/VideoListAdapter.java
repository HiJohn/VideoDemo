package com.bc.videodemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



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
    public void onBindViewHolder(@NonNull VideoHolder videoHolder, int position) {
//        int adapterPosition = videoHolder.getAdapterPosition();
        VideoInfo videoInfo = data.get(position);
//        LogUtils.i(TAG," data :    "+videoInfo.toString());
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
        holder.releasePlayer();
//        LogUtils.i(TAG," on view recycled "+holder.getAdapterPosition());
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
        // item view 进入当前window
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoHolder holder) {
        super.onViewDetachedFromWindow(holder);
        LogUtils.i(TAG," detach from window :"+holder.getAdapterPosition());
        // item view 未显示在当前窗口

    }

    //=======================================================================================



}
