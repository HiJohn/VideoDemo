package com.bc.videodemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.blankj.utilcode.util.UriUtils;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.util.UriUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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



    public void preCacheNext(int position){
        if (data.size()>position){
            LogUtils.i(TAG," pre cache next ");
            VideoInfo videoInfo = data.get(position);
            Uri uri = Uri.fromFile(new File(videoInfo.path));
            if (uri!=null){
                preCache(uri,0,100*1024,uri.getLastPathSegment());
            }
        }
    }

    private void preCache(Uri uri,long absoluteStreamPosition,long length,String key){

        DataSpec dataSpec = new DataSpec(uri,absoluteStreamPosition,length,key);
        try {
            CacheUtil.cache(dataSpec, VideoApp.getApp().getSimpleCache(),
                    CacheUtil.DEFAULT_CACHE_KEY_FACTORY,
                    VideoApp.getApp().getCacheDataSourceFactory().createDataSource(),
                    null,new AtomicBoolean(true));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // failed
        }
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
