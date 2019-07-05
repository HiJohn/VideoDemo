package com.bc.videodemo;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;


public class ExoPlayerView extends PlayerView {


    public static final String TAG = "ExoPlayerView";

    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private Uri uri;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackSelection.Factory trackSelectionFactory;
    private DefaultRenderersFactory renderersFactory;
    private DefaultTrackSelector trackSelector;
    private CacheDataSourceFactory dataSourceFactory;

    public ExoPlayerView(Context context) {
        this(context,null);
    }

    public ExoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

    }


    public void setVideoUri(Uri uri) {
        this.uri = uri;
    }

    public void setVideoUri(String filePath) {
        this.uri = Uri.parse(filePath);
    }

    public void initPlayer() {
        if (uri == null) {
            LogUtils.i(TAG, " uri is empty ");
            return;
        }
        if (trackSelectionFactory == null) {
            trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        }
        if (renderersFactory == null) {
            renderersFactory = new DefaultRenderersFactory(getContext());
        }
        if (trackSelector == null) {
            trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        }
        if (trackSelectorParameters == null) {
            trackSelectorParameters =
                    new DefaultTrackSelector.ParametersBuilder().build();
        }
        trackSelector.setParameters(trackSelectorParameters);


        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(getContext(), renderersFactory, trackSelector);
            player.setRepeatMode(Player.REPEAT_MODE_ONE);
//            player.setPlayWhenReady(startAutoPlay);
        }
        setPlayer(player);
        buildMediaSource();
    }

    public void buildMediaSource() {
        dataSourceFactory = VideoApp.getApp().getCacheDataSourceFactory();
//        DefaultDataSourceFactory defaultDataSourceFactory = VideoApp.getApp().getUpstreamFactory();

        mediaSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        boolean haveStartPosition = startWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }
        player.prepare(mediaSource, !haveStartPosition, false);
    }


    public void releasePlayer() {
        updateTrackSelectorParameters();
        updateStartPosition();
        if (player!=null) {
            player.release();
            player = null;
        }
        trackSelector = null;
        mediaSource = null;
    }

    //==================================================================================================

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.i(TAG, " onAttachedToWindow  ");
        play();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.i(TAG, " onDetachedFromWindow  ");
        pause();
    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
    }

    public void pause(){
        onPause();
        if (player!=null){
            player.setPlayWhenReady(false);
        }
    }
    public void play(){
        onResume();
        if (player!=null){
            player.setPlayWhenReady(true);
        }
    }

    //==============================================================================================

    private boolean startAutoPlay = true;
    private int startWindow = 0;
    private long startPosition = 0;

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        LogUtils.i(TAG," onSaveInstanceState ");
        Parcelable parcelable = super.onSaveInstanceState();
        updateTrackSelectorParameters();
        updateStartPosition();
        return new SavedState(parcelable,startAutoPlay,startWindow,startPosition,
                trackSelectorParameters);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // not called
        LogUtils.i(TAG," onRestoreInstanceState ");
        super.onRestoreInstanceState(state);
        if (state==null){
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }else {
            SavedState ss = (SavedState)state;
            startAutoPlay = ss.startAutoPlay;
            startWindow = ss.startWindow;
            startPosition = ss.startPosition;
            trackSelectorParameters = ss.trackSelectorParameters;
        }

    }

    public static class SavedState extends BaseSavedState {
        private boolean startAutoPlay;
        private int startWindow;
        private long startPosition;
        private DefaultTrackSelector.Parameters trackSelectorParameters;

        public SavedState(Parcel source) {
            super(source);
        }

        public SavedState(Parcelable parcel, boolean startAutoPlay, int startWindow,
                          long startPosition
                , DefaultTrackSelector.Parameters parameters) {
            super(parcel);
            this.startAutoPlay = startAutoPlay;
            this.startWindow = startWindow;
            this.startPosition = startPosition;
            trackSelectorParameters = parameters;

        }
    }

}
