package com.bc.videodemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoHolder> {


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
        VideoInfo videoInfo = data.get(videoHolder.getAdapterPosition());
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
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    //=======================================================================================
    @Override
    public void onViewAttachedToWindow(@NonNull VideoHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    //=======================================================================================



}
