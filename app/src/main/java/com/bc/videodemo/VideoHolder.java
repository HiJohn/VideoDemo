package com.bc.videodemo;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


/**
 *
 */
public class VideoHolder extends RecyclerView.ViewHolder {


    public static final String TAG = "VideoHolder";

    private ExoPlayerView playerView;

    private VideoInfo videoInfo;


    public VideoHolder(View itemView) {
        super(itemView);
        playerView = itemView.findViewById(R.id.player_view);
    }

    public void bind(VideoInfo data) {
        this.videoInfo = data;
        playerView.setVideoUri(videoInfo.path);
        playerView.initPlayer();
    }

    public void releasePlayer() {
        playerView.releasePlayer();
    }

    public void pause() {
        playerView.pause();
    }

    public void play() {
        playerView.play();
    }

    public String getVideoName(){
        if (videoInfo!=null){
            return videoInfo.name;
        }
        return "";
    }
}
