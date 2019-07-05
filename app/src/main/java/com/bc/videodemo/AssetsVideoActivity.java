package com.bc.videodemo;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bc.videodemo.databinding.ActivityAssetsVideoBinding;

import java.io.File;

import leakcanary.LeakSentry;

public class AssetsVideoActivity extends AppCompatActivity {
    String fileUri = "asset:///turkey.mp4";

    ActivityAssetsVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assets_video);
        Uri uri = Uri.parse(fileUri);
        binding.mePlayerView.setVideoUri(uri);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding.mePlayerView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding.mePlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LeakSentry.INSTANCE.getRefWatcher().watch(this);
    }
}
