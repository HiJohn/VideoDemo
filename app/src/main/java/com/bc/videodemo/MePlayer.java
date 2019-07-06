package com.bc.videodemo;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;


public class MePlayer {

    public static final String TAG = "MePlayer";

    private SimpleExoPlayer player;

    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackSelection.Factory trackSelectionFactory;
    private DefaultRenderersFactory renderersFactory;
    private DefaultTrackSelector trackSelector;
    private MediaSource mediaSource;
    private Uri uri = null;

    private static class MePlayerHolder{
        private static final MePlayer player = new MePlayer();
    }
    private MePlayer(){}

    public static MePlayer getInstance(){
        return MePlayerHolder.player;
    }

    public void setVideoUri(Uri u){
        uri = u;
    }

    public void setVideoPath(String path){
        uri = Uri.parse(path);
    }

    public void setVideoUrl(String url){
        uri = Uri.parse(url);
    }

    public void initPlayer(Context context){
        if (trackSelectionFactory == null) {
            trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        }
        if (renderersFactory == null) {
            renderersFactory = VideoApp.getApp().buildRenderFactory();
        }
        if (trackSelector == null) {
            trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        }
        if (trackSelectorParameters == null) {
            trackSelectorParameters = trackSelector.getParameters();
//                    new DefaultTrackSelector.ParametersBuilder().build();
        }
        trackSelector.setParameters(trackSelectorParameters);
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
        }
    }

    public void initSimplePlayer(Context context){
        if (player==null){
            player = ExoPlayerFactory.newSimpleInstance(context);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
        }
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void buildMediaSource(){
        if (uri==null){
            return;
        }
        CacheDataSourceFactory dataSourceFactory = VideoApp.getApp().getCacheDataSourceFactory();
        mediaSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory,new Mp4ExtractorsFactory()).createMediaSource(uri);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    public void rebuildMediaSource(){
        mediaSource = null;
        // uri should been reset
        buildMediaSource();
    }

    public void addEventListener(Player.EventListener eventListener){
        if (player!=null){
            player.addListener(eventListener);
        }
    }

    public void removeEventListener(Player.EventListener eventListener){
        if (player!=null){
            player.removeListener(eventListener);
        }
    }


    public void setMute(boolean isMute){
        if (player!=null){
            player.setVolume(isMute?0f:1f);
        }
    }

    public void pause(){
        if (player!=null){
            player.setPlayWhenReady(false);
        }
    }

    public void play(){
        if (player!=null){
            player.setPlayWhenReady(true);
        }
    }

    public void stop(){
        if (player!=null){
            player.stop();
        }
    }

    public void retry(){
        if (player!=null){
            player.retry();
        }
    }

    public void release(){
        if (player!=null){
            player.release();
            player = null;
        }
    }

    public int getCurrentWindowIndex(){
        if (player!=null){

            return player.getCurrentWindowIndex();
        }

        return C.INDEX_UNSET;
    }

    public long startPosition(){
        if (player!=null) {
            return Math.max(0, player.getContentPosition());
        }
        return C.TIME_UNSET;
    }

    public boolean getPlayWhenReady(){
        if (player!=null){
            return player.getPlayWhenReady();
        }
        return false;
    }

    public DefaultTrackSelector.Parameters getTrackSelectorParameters() {
        return trackSelectorParameters;
    }

    public void setTrackSelectorParameters(DefaultTrackSelector.Parameters trackSelectorParameters) {
        this.trackSelectorParameters = trackSelectorParameters;
    }
}
