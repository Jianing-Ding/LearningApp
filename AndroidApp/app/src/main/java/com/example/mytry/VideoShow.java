package com.example.mytry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class VideoShow extends AppCompatActivity {

    SimpleVideoView videoView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

  //      this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //得到当前界面的装饰视图
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置让应用主题内容占据状态栏和导航栏
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏和导航栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置全屏
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.video_show);
        videoView1=findViewById(R.id.videoView);
        videoView1.setFullScreen();
        videoView1.setInitPicture(R.drawable.class_pic);
        videoView1.play();
        //videoView1.setVideoProgress(1000,true);
        //videoView1.setFullScreenListener(SimpleVideoView.onClickListener);
        videoView1.setVideoUri(Uri.parse(getIntent().getStringExtra("uri")),getIntent().getStringExtra("uriPath"));

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        videoView1.suspend();
    }

    @Override
    public void onBackPressed(){
        if(videoView1.isFullScreen()){
            videoView1.setNoFullScreen();
        }else{
            super.onBackPressed();
        }
    }

}