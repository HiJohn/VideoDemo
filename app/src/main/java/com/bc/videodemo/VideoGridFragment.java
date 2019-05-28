package com.bc.videodemo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class VideoGridFragment extends Fragment {


    private ArrayList<VideoInfo> data = new ArrayList<>();


    public void setData(ArrayList<VideoInfo> videoInfos) {
        this.data = videoInfos;
        gridAdapter.setData(data);
        gridAdapter.notifyDataSetChanged();
    }

    public static VideoGridFragment getInstance(){
        return new VideoGridFragment();
    }


    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private VideoGridAdapter gridAdapter = new VideoGridAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid,container,false);
        recyclerView = view.findViewById(R.id.grid_rv);
        layoutManager = new GridLayoutManager(this.getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(gridAdapter);

        return view;
    }
}
