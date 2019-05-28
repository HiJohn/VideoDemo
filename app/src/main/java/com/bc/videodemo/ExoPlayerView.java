package com.bc.videodemo;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer2.ui.PlayerView;

public class ExoPlayerView extends PlayerView {
    public ExoPlayerView(Context context) {
        super(context);
    }

    public ExoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
