package com.bc.videodemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bc.videodemo.databinding.FragmentSingleVideoBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import leakcanary.LeakSentry;

public class VideoSingleFragment extends Fragment {


    public static final String TAG = "VideoSingleFragment";

    private FragmentSingleVideoBinding binding;

    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private VideoInfo videoInfo;
    private String assetsUri = "asset:///turkey.mp4";//asset:///turkey.mp4
    private Uri uri;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackSelection.Factory trackSelectionFactory;
    private DefaultRenderersFactory renderersFactory;
    private DefaultTrackSelector trackSelector;
    private CacheDataSourceFactory dataSourceFactory;

    // Saved instance state keys.
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";

    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;

    static VideoSingleFragment newInstance(VideoInfo videoInfo){
        Bundle args = new Bundle();
        args.putParcelable(MeUtils.VIDEO_INFO_TAG,videoInfo);
        VideoSingleFragment singleFragment = new VideoSingleFragment();
        singleFragment.setArguments(args);
        return singleFragment;
    }

    static VideoSingleFragment newInstance(){
        return new VideoSingleFragment();
    }


    private void initPlayer(){
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
            trackSelectorParameters =
                    new DefaultTrackSelector.ParametersBuilder().build();
            trackSelector.setParameters(trackSelectorParameters);
        }
        if (player == null) {
            if (getActivity()!=null) {
                player = ExoPlayerFactory.newSimpleInstance(getActivity(), renderersFactory, trackSelector);
                player.setRepeatMode(Player.REPEAT_MODE_ONE);
                player.setPlayWhenReady(startAutoPlay);
            }
        }
        initPlayerView();
        buildMediaSource();
    }


    private void buildMediaSource(){
        DefaultDataSourceFactory dataSourceFactory = VideoApp.getApp().getUpstreamFactory();
        Uri uri = null;
        if (videoInfo == null) {
            uri = Uri.parse(assetsUri);
        } else {
//            uri = Uri.fromFile(new File(videoInfo.path));
            uri = Uri.parse(videoInfo.path);
        }
        mediaSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        boolean haveStartPosition = startWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }
        player.setPlayWhenReady(startAutoPlay);
        player.prepare(mediaSource, !haveStartPosition, false);
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


    private void releasePlayer() {
        if (player != null) {
            updateTrackSelectorParameters();
            updateStartPosition();
            player.release();
            binding.singlePlayerView.setPlayer(null);
            player = null;
            mediaSource = null;
        }
    }
    private void initPlayerView() {
        binding.singlePlayerView.setPlayer(player);
        binding.singlePlayerView.setPlaybackPreparer(new PlaybackPreparer() {
            @Override
            public void preparePlayback() {
                player.retry();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_video,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        updateTrackSelectorParameters();
        updateStartPosition();
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);

        LogUtils.i(TAG, "  onSaveInstanceState ");
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }
        LogUtils.i(TAG, "  onRestoreInstanceState ");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (binding.singlePlayerView != null) {
                binding.singlePlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume ");
        if (Util.SDK_INT <= 23 || player == null) {
            initPlayer();
            if (binding.singlePlayerView != null) {
                binding.singlePlayerView.onResume();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (binding.singlePlayerView != null) {
                binding.singlePlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer();
            if (binding.singlePlayerView != null) {
                binding.singlePlayerView.onResume();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i(TAG,"onDestroyView ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LeakSentry.INSTANCE.getRefWatcher().watch(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i(TAG,"onDetach ");
    }
}
