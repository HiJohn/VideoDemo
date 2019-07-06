package com.bc.videodemo;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.google.android.exoplayer2.ui.PlayerView;


/**
 *
 */
public class VideoHolder extends RecyclerView.ViewHolder {


    public static final String TAG = "VideoHolder";

    public PlayerView playerView;


    public VideoHolder(View itemView) {
        super(itemView);
        playerView = itemView.findViewById(R.id.player_view);
    }

}
