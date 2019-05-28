package com.bc.videodemo;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class VideoHolder extends RecyclerView.ViewHolder {

    PlayerView playerView;

    VideoInfo videoInfo;

    private SimpleExoPlayer player;

    private Context context;

    public VideoHolder(View itemView){
        super(itemView);
        context = itemView.getContext();
        playerView = itemView.findViewById(R.id.player_view);
    }

    public void bind(VideoInfo data){
        this.videoInfo = data;
        initPlayer();
        initPlayerView();
        buildMediaSource();
    }



    private void initPlayer(){
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context);

        DefaultTrackSelector trackSelector =new DefaultTrackSelector(trackSelectionFactory);

        DefaultTrackSelector.Parameters parameters =
                new DefaultTrackSelector.ParametersBuilder().build();

        trackSelector.setParameters(parameters);

        player = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }
        });
    }

    private void buildMediaSource(){
        DefaultDataSourceFactory upstreamFactory = new DefaultDataSourceFactory(context,
                new DefaultHttpDataSourceFactory(MeUtils.USER_AGENT));
        SimpleCache simpleCache =new SimpleCache(context.getCacheDir(),new NoOpCacheEvictor()
                ,new ExoDatabaseProvider(context));

        CacheDataSourceFactory cacheDataSourceFactory =new CacheDataSourceFactory(simpleCache,
                upstreamFactory,new FileDataSourceFactory(),null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,null);

        Uri uri = Uri.fromFile(new File(videoInfo.path));

        MediaSource mediaSource =
                new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
        player.prepare(mediaSource, false, false);
    }


    private void initPlayerView(){
        playerView.setPlayer(player);
        playerView.setPlaybackPreparer(new PlaybackPreparer() {
            @Override
            public void preparePlayback() {

            }
        });

    }

    public void releasePlayer(){
        player.release();
        player = null;
    }

    public void pause(){
        playerView.onPause();
        if (player!=null) {
            player.setPlayWhenReady(false);
        }
    }

    public void resume(){
        playerView.onResume();
        if (player!=null) {
            player.setPlayWhenReady(true);
        }
    }

    public void play(){

        if (player!=null) {
            player.setPlayWhenReady(true);
        }
    }
}
