package com.bc.videodemo;

import android.util.Log;


public class LogUtils {

    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final String TAG = "BaMe";


    private static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};


    private LogUtils() {

    }


    public static void v(String contents) {
        log(V, TAG, contents);
    }

    public static void v(String tag, String contents) {
        log(V, tag, contents);
    }


    public static void d(String contents) {
        log(D, TAG, contents);
    }

    public static void d(String tag, String contents) {
        log(D, tag, contents);
    }


    public static void i(String contents) {
        log(I, TAG, contents);
    }

    public static void i(String tag, String contents) {
        log(I, tag, contents);
    }


    public static void w(String contents) {
        log(W, TAG, contents);
    }

    public static void w(String tag, String contents) {
        log(W, tag, contents);
    }


    public static void e(String contents) {
        log(E, TAG, contents);
    }

    public static void e(String tag, String contents) {
        log(E, tag, contents);
    }


    private static void log(int priority, String tag, String msg) {
        if (!tag.equals(TAG)){
            tag = "{"+TAG+"}==>"+"["+tag+"]";
        }
        Log.println(priority, tag, msg);
    }
}
