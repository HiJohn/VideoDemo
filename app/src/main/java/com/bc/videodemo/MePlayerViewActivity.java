package com.bc.videodemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.File;

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
        Uri uri = Uri.fromFile(new File(videoInfo.path));
        playerView.setVideoUri(uri);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playerView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playerView.onPause();
    }
}
