package com.bc.videodemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class RetainFragment extends Fragment {


    private String meVideoTag = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void setMeVideoTag(String tag) {
        this.meVideoTag = tag;
    }

    public String getMeVideoTag() {
        return meVideoTag;
    }
}


