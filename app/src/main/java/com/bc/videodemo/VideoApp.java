package com.bc.videodemo;

import android.app.Application;

import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

import leakcanary.LeakSentry;

public class VideoApp extends Application {

    private static VideoApp sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        initDataSource();
        LeakSentry.INSTANCE.setConfig(LeakSentry.INSTANCE.getConfig().copy(true,true,true,true,2000));
    }

    public static VideoApp getApp() {
        return sApp;
    }


    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private DefaultDataSourceFactory upstreamFactory;
    private File cacheDir ;

    public void initDataSource(){
        if (upstreamFactory == null) {
            upstreamFactory = new DefaultDataSourceFactory(this,
                    new DefaultHttpDataSourceFactory(MeUtils.USER_AGENT));
        }

        if (cacheDir==null){
            cacheDir = new File(this.getExternalCacheDir(),"exocache");
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(cacheDir, new NoOpCacheEvictor()
                    , new ExoDatabaseProvider(this));
        }
        if (cacheDataSourceFactory==null) {
            cacheDataSourceFactory = new CacheDataSourceFactory(simpleCache,
                    upstreamFactory, new FileDataSourceFactory(), null,
                    CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
        }
    }


    public SimpleCache getSimpleCache() {
        return simpleCache;
    }

    public CacheDataSourceFactory getCacheDataSourceFactory() {
        return cacheDataSourceFactory;
    }


    public DefaultDataSourceFactory getUpstreamFactory(){
        return upstreamFactory;
    }
}
