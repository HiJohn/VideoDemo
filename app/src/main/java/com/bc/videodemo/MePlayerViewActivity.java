package com.bc.videodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.File;

import leakcanary.LeakSentry;

public class MePlayerViewActivity extends AppCompatActivity {


    ExoPlayerView playerView;

    private VideoInfo videoInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_player_view);

        if (getIntent()!=null){
            videoInfo = getIntent().getParcelableExtra(MeUtils.VIDEO_INFO_TAG);
            if (videoInfo==null){
                finish();
            }
        }

        playerView = findViewById(R.id.me_player_view);
        playerView.setVideoUri(videoInfo.path);
        playerView.initPlayer();
    }


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playerView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LeakSentry.INSTANCE.getRefWatcher().watch(this);
    }
}
