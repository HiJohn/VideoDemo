package com.bc.videodemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.bc.videodemo.databinding.ActivityContainerBinding;

public class ContainerActivity extends AppCompatActivity {


    ActivityContainerBinding binding;

    VideoSingleFragment fragment;

    VerticalVideoFragment videoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_container);


        init();
//        init2();
    }

    private void init(){

        if (getIntent()==null){
            return;
        }
        VideoInfo videoInfo = null;
        if (getIntent().getParcelableExtra(MeUtils.VIDEO_INFO_TAG)!=null){
            videoInfo = getIntent().getParcelableExtra(MeUtils.VIDEO_INFO_TAG);
        }

        if (videoInfo==null){
            fragment = VideoSingleFragment.newInstance();
        }else {
            fragment = VideoSingleFragment.newInstance(videoInfo);
        }

        getSupportFragmentManager().beginTransaction().add(R.id.root_container,fragment).commit();

    }


    private void init2(){
        videoFragment = new VerticalVideoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.root_container,videoFragment).commit();

    }


}
