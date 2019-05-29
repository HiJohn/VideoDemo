package com.bc.videodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

    public static final String TAG = "VideoPlayActivity";

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private MediaSource mediaSource;
    private VideoInfo videoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        if (getIntent()!=null){
            videoInfo = getIntent().getParcelableExtra(MeUtils.VIDEO_INFO_TAG);
            if (videoInfo==null){
                finish();
            }
        }

        playerView = findViewById(R.id.act_play_view);

    }


    private void initPlayerView(){
        playerView.setPlayer(player);
        playerView.setPlaybackPreparer(new PlaybackPreparer() {
            @Override
            public void preparePlayback() {
                player.retry();
            }
        });
    }

    private void buildMediaSource(){
        CacheDataSourceFactory dataSourceFactory = VideoApp.getApp().getCacheDataSourceFactory();
//        DefaultDataSourceFactory defaultDataSourceFactory = VideoApp.getApp().getUpstreamFactory();
        Uri uri = Uri.fromFile(new File(videoInfo.path));
        mediaSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        player.prepare(mediaSource);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initPlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }


    private void initPlayer() {
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultTrackSelector.Parameters parameters =
                new DefaultTrackSelector.ParametersBuilder().build();
        trackSelector.setParameters(parameters);
        if (player == null) {

            player = ExoPlayerFactory.newSimpleInstance(this, renderersFactory, trackSelector);
            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    LogUtils.i(TAG, " play state changed , playwhenready:" + playWhenReady
                            + ", playbackstate :" + playbackState);
                    if (playbackState == Player.STATE_ENDED) {

                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    ToastUtils.showShort("player error :" + error.getMessage());
                }
            });
            player.setPlayWhenReady(true);
        }

        initPlayerView();
        buildMediaSource();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        releasePlayer();
    }

    private void releasePlayer(){
        player.release();
        player = null;
        mediaSource = null;
    }
}
