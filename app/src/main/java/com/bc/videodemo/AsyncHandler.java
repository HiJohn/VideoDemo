package com.bc.videodemo;

import android.os.Handler;
import android.os.HandlerThread;

public class AsyncHandler {
    private AsyncHandler(){}


    private static final String TAG= "AsyncHandler";
    private static Handler sHandler ;
    private static HandlerThread sHandlerThread = new HandlerThread(TAG);
    static {
        sHandlerThread.start();
        sHandler = new Handler(sHandlerThread.getLooper());
    }

    public static void post(Runnable run){
        sHandler.post(run);
    }

    public static void postDelayed(Runnable run,long delayedTime){
        sHandler.postDelayed(run,delayedTime);
    }
}
