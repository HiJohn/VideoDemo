package com.bc.videodemo;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MeUtils {


    public static final String DATA_TAG = "data";
    public static final String USER_AGENT = "christopher";


    public static ArrayList<VideoInfo> queryVideoInfo(Context context) {

        ArrayList<VideoInfo> videoInfos = new ArrayList<>();

        VideoInfo videoInfo ;

        Cursor cursor =
                context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        try {

            if (cursor != null) {
                cursor.moveToFirst();
                do {

                    videoInfo = new VideoInfo();
                    videoInfo.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media
                            .DATA));
                    videoInfo.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video
                            .Media.DISPLAY_NAME));
                    videoInfo.duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video
                            .Media.DURATION));

                    videoInfos.add(videoInfo);

                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return videoInfos;
    }
}
