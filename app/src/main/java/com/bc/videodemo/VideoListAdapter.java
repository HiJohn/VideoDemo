package com.bc.videodemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoHolder> {

    public static final String TAG = "VideoListAdapter";

    private ArrayList<VideoInfo> data = new ArrayList<>();


    private MePlayer player = MePlayer.getInstance();

    private Context mContext;

    public VideoListAdapter(){

    }

    public VideoListAdapter(Context context){
        mContext = context;
        player.initSimplePlayer(context);
    }

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
        LogUtils.i(TAG," onBindViewHolder "+videoHolder.getAdapterPosition());

        VideoInfo videoInfo = data.get(videoHolder.getAdapterPosition());
        player.setVideoPath(videoInfo.path);
        player.buildMediaSource();
        videoHolder.playerView.setPlayer(player.getPlayer());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //=====================================================================================

    @Override
    public void onViewRecycled(@NonNull VideoHolder holder) {
        super.onViewRecycled(holder);
//        holder.releasePlayer();
        LogUtils.i(TAG," on view recycled "+holder.getAdapterPosition());
    }
//
//    @Override
//    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        LogUtils.i(TAG,"  attach to rv ");
//    }
//
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
        if (holder.playerView.getPlayer()==null) {
            holder.playerView.setPlayer(player.getPlayer());
        }
    }

    public Uri getUriByPosition(int position){
        VideoInfo videoInfo = data.get(position);
        return Uri.parse(videoInfo.path);
    }

    public void rebuildMedia(Uri uri){
        player.setVideoUri(uri);
        player.rebuildMediaSource();
    }

    public void rebuildMediaSourceByPosition(int position){
        rebuildMedia(getUriByPosition(position));
    }

    public void resetMedia(){
        player.stop();
    }



    @Override
    public void onViewDetachedFromWindow(@NonNull VideoHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        LogUtils.i(TAG," detach from window :"+holder.getAdapterPosition());
        // item view 未显示在当前窗口
        holder.playerView.onPause();
        holder.playerView.setPlayer(null);

    }

    //=======================================================================================



}
