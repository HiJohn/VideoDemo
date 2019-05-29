package com.bc.videodemo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class VideoGridAdapter extends RecyclerView.Adapter<VideoGridAdapter.GridHolder> {


    private ArrayList<VideoInfo> videoInfos = new ArrayList<>();

    public void setData(ArrayList<VideoInfo> data) {
        videoInfos = data;
    }

    private OnGridItemClickListener itemClickListener;

    public void setItemClickListener(OnGridItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.item_grid_video, viewGroup, false);


        return new GridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridHolder gridHolder, int i) {
        final VideoInfo videoInfo = videoInfos.get(i);

        Glide.with(gridHolder.imageView).load(Uri.fromFile(new File(videoInfo.path))).into(gridHolder
                .imageView);


        gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener!=null){
                    itemClickListener.onItemClick(videoInfo,gridHolder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoInfos.size();
    }

    class GridHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public GridHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.grid_img);
        }
    }
}
