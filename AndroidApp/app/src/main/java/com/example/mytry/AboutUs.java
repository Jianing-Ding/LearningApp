package com.example.mytry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        textView=findViewById(R.id.text);
        textView.setText("  该应用为一款学习app，希望帮助同学们更好地学习。\n  本应用共开发了在线课堂，在线测试，积分商城三个主要功能，以及个人主页。\n  本应用正在开发阶段，更多功能敬请期待！");
    }
}