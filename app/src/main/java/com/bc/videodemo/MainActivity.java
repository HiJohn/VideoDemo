package com.bc.videodemo;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    ViewPager viewPager ;

    MePagerAdapter pagerAdapter;
    String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    FragmentManager fragmentManager;

    RetainFragment retainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        if (retainFragment==null) {

            retainFragment = new RetainFragment();
            fragmentManager.beginTransaction().add(retainFragment,"retain").commit();

        }

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new MePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        checkPermission();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // pause all player
            LogUtils.i(TAG," onPageSelected :"+position);
            pagerAdapter.dispatchPageSelectedEvent(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void loadData(){

        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {

               final ArrayList<VideoInfo> data = MeUtils.queryVideoInfo(MainActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pagerAdapter.setData(data);
                        pagerAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    private void checkPermission(){

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            loadData();
            return;
        }


        if (ContextCompat.checkSelfPermission(this,permission)==
                PackageManager.PERMISSION_GRANTED){
            loadData();
        }else {
            requestPermissions(new String[]{permission},100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this,permission)==
                PackageManager.PERMISSION_GRANTED){
            loadData();
        }else {
            ToastUtils.showLong("not granted");
        }
    }
}
