package com.bc.videodemo;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

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

import java.io.File;


/**
 *
 */
public class VideoHolder extends RecyclerView.ViewHolder {


    public static final String TAG = "VideoHolder";

    private PlayerView playerView;

    private VideoInfo videoInfo;
    private MediaSource mediaSource;
    // fixme cause oom
//    private LoopingMediaSource loopingMediaSource;
    private Uri uri;

    private CacheDataSourceFactory cacheDataSourceFactory;

    private TrackSelection.Factory trackSelectionFactory;
    private DefaultRenderersFactory renderersFactory;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters parameters;
    private SimpleExoPlayer player;

    private Context context;


    public VideoHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        playerView = itemView.findViewById(R.id.player_view);
    }

    public void bind(VideoInfo data) {
        this.videoInfo = data;
        initPlayer();
        initPlayerView();
        buildMediaSource();
    }


    private void initPlayer() {
        if (trackSelectionFactory == null) {
            trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        }
        if (renderersFactory == null) {
            renderersFactory = new DefaultRenderersFactory(context);
        }
        if (trackSelector == null) {
            trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        }
        if (parameters == null) {
            parameters =
                    new DefaultTrackSelector.ParametersBuilder().build();
        }
        trackSelector.setParameters(parameters);

        if (player == null) {

            player = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector);

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
        }
    }

    private void buildMediaSource() {

        cacheDataSourceFactory = VideoApp.getApp().getCacheDataSourceFactory();

        if (uri == null) {
            uri = Uri.fromFile(new File(videoInfo.path));

        }

        if (mediaSource == null) {
            mediaSource =
                    new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
        }

//        if (loopingMediaSource==null){
//            loopingMediaSource = new LoopingMediaSource(mediaSource);
//        }

        if (player != null) {
            player.prepare(mediaSource, false, false);

        }

    }


    private void initPlayerView() {
        playerView.setPlayer(player);
        playerView.setPlaybackPreparer(new PlaybackPreparer() {
            @Override
            public void preparePlayback() {

            }
        });

    }

    public void releasePlayer() {
        player.release();
        player = null;
        releaseMediaSource();
    }

    private void releaseMediaSource(){
//        loopingMediaSource = null;
        mediaSource = null;
        uri = null;
    }

    public void pause() {
        LogUtils.i(TAG, " pause play in position :" + getAdapterPosition());
        playerView.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    public int getPlaybackState(){
        if (player!=null){
            return player.getPlaybackState();
        }
        return 0;
    }

    public void resume() {
        playerView.onResume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    public void play() {

        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }
}
