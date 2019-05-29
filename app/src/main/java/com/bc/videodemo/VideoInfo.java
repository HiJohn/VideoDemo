package com.bc.videodemo;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoInfo implements Parcelable {

    public String name;
    public String path;
    public long duration;


    public VideoInfo(){

    }

    protected VideoInfo(Parcel in) {
        name = in.readString();
        path = in.readString();
        duration = in.readLong();
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeLong(duration);
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                '}';
    }
}
