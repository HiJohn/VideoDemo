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

    public void setData(ArrayList<VideoInfo> data){
        videoInfos = data;
    }


    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.item_grid_video,viewGroup,false);


        return new GridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridHolder gridHolder, int i) {
        VideoInfo videoInfo = videoInfos.get(i);

        Glide.with(gridHolder.imageView).load(Uri.fromFile(new File(videoInfo.path))).into(gridHolder
        .imageView);
    }

    @Override
    public int getItemCount() {
        return videoInfos.size();
    }

    class GridHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public GridHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.grid_img);
        }
    }
}
