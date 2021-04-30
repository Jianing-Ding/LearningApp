package com.example.mytry.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mytry.R;
import com.example.mytry.SimpleVideoView;
import com.example.mytry.VideoShow;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public static View layout;
    SimpleVideoView videoView2;
    SimpleVideoView videoView1;
    SimpleVideoView videoView3;
    SimpleVideoView videoView4;

    private int videoProcess;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        videoView1=root.findViewById(R.id.videoView1);
        videoView2=root.findViewById(R.id.videoview2);
        videoView3=root.findViewById(R.id.videoView3);
        videoView4=root.findViewById(R.id.videoView4);
        videoView1.setInitPicture(R.drawable.class_pic);
        videoView2.setInitPicture(R.drawable.class_pic);
        videoView3.setInitPicture(R.drawable.class_pic);
        videoView4.setInitPicture(R.drawable.class_pic);
        String path="http://172.22.106.11/test.mp4";
        String path1="http://172.22.106.11/framework.mp4";
        videoView1.setVideoUri(Uri.parse(path),path);
        videoView2.setVideoUri(Uri.parse(path),path);
        videoView3.setVideoUri(Uri.parse(path1),path);
        videoView4.setVideoUri(Uri.parse(path1),path);
        //加载指定的视频文件


        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleVideoView videoView=(SimpleVideoView)v.getParent().getParent().getParent();
                if (videoView.isPlaying()){
                    videoView.suspend();
                }
                Intent intent=new Intent(getActivity(), VideoShow.class);
                Uri uri=videoView.getVideoUri();
                intent.putExtra("uri",uri.toString());
                startActivity(intent);
            }
        };

        videoView1.setFullScreenListener(listener);
        videoView2.setFullScreenListener(listener);
        videoView3.setFullScreenListener(listener);
        videoView4.setFullScreenListener(listener);
        return root;
    }

}