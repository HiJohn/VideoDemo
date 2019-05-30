package com.bc.videodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.exoplayer2.C;
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
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class VideoPlayActivity extends AppCompatActivity {

    public static final String TAG = "VideoPlayActivity";

    // Saved instance state keys.
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";

    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;


    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }



    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackSelection.Factory trackSelectionFactory;
    private DefaultRenderersFactory renderersFactory;
    private  DefaultTrackSelector trackSelector;

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private MediaSource mediaSource;
    private VideoInfo videoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.activity_video_play);


        if (getIntent()!=null){
            videoInfo = getIntent().getParcelableExtra(MeUtils.VIDEO_INFO_TAG);
            if (videoInfo==null){
                finish();
            }
        }

        playerView = findViewById(R.id.act_play_view);

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }

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
        boolean haveStartPosition = startWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }
        player.setPlayWhenReady(startAutoPlay);
        player.prepare(mediaSource,!haveStartPosition, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

        LogUtils.i(TAG,"onResume ");
        if (Util.SDK_INT <= 23 || player == null) {
            initPlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }


    private void initPlayer() {
        if (trackSelectionFactory==null) {
            trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        }
        if (renderersFactory==null) {
            renderersFactory = new DefaultRenderersFactory(this);
        }
        if (trackSelector==null) {
            trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        }
        if (trackSelectorParameters==null) {
            trackSelectorParameters =
                    new DefaultTrackSelector.ParametersBuilder().build();
            trackSelector.setParameters(trackSelectorParameters);
        }
        if (player == null) {

            player = ExoPlayerFactory.newSimpleInstance(this, renderersFactory, trackSelector);
            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                    LogUtils.i(TAG, " play state changed , playwhenready:" + playWhenReady
//                            + ", playbackstate :" + playbackState);
                    if (playbackState == Player.STATE_ENDED) {
                        player.seekTo(0,0);
                        playerView.onResume();
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

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateTrackSelectorParameters();
        updateStartPosition();
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);

        LogUtils.i(TAG,"  onSaveInstanceState ");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }
        LogUtils.i(TAG,"  onRestoreInstanceState ");
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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        if (player!=null) {
            updateTrackSelectorParameters();
            updateStartPosition();
            player.release();
            player = null;
            mediaSource = null;
        }
    }
}
