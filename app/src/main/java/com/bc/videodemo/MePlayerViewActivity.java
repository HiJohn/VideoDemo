package com.bc.videodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import java.io.File;

public class MePlayerViewActivity extends AppCompatActivity {


    ExoPlayerView playerView;

    private VideoInfo videoInfo;

//    String uri = "asset:///turkey.mp4";

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
        Uri uri = Uri.fromFile(new File(videoInfo.path));
        playerView.setVideoUri(uri);
    }


    @Override
    protected void onStart() {
        super.onStart();
        playerView.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        playerView.pause();
    }
}
