package com.example.mytry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytry.Utils.HttpConnectionUtils;
import com.example.mytry.Utils.StreamChangeStrUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {
    EditText userName;
    EditText password;
    Button register;
    final int REGISTERSUCCESS=3;
    final int REGISTERNOTFOUND=4;
    final int REGISTEREXCEPT=5;
    public String usernameStr;
    public String passwordStr;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {//具体消息，具体显示
                case REGISTERSUCCESS:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case REGISTERNOTFOUND:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case REGISTEREXCEPT:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register=findViewById(R.id.register);
        userName = findViewById(R.id.description);
        password = findViewById(R.id.passward);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameStr = userName.getText().toString().trim();
                passwordStr = password.getText().toString().trim();
                if (usernameStr.equals("") || passwordStr.equals("")) {
                    Toast.makeText(Register.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread() {

                        HttpURLConnection connection = null;

                        @Override
                        public void run() {
                            try {
                                String data = "username=" + URLEncoder.encode(usernameStr, "utf-8") + "&password=" + URLEncoder.encode(passwordStr, "utf-8") + "&sign=" + URLEncoder.encode("2", "utf-8");
                                connection = HttpConnectionUtils.getConnection(data);
                                int code = connection.getResponseCode();
                                if (code == 200) {
                                    InputStream inputStream = connection.getInputStream();
                                    String str = StreamChangeStrUtils.toChange(inputStream);
                                    Message message = Message.obtain();
                                    message.obj = str;
                                    message.what = REGISTERSUCCESS;
                                    handler.sendMessage(message);
                                    if (message.obj.equals("注册成功")) {
                                        SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                                        //获取sp编辑器
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putString("userName",usernameStr);
                                        edit.putString("password",passwordStr);
                                        edit.apply();
                                        finish();
                                    }else{
                                        userName.setText("");
                                    }
                                } else {
                                    Message message = Message.obtain();
                                    message.what = REGISTERNOTFOUND;
                                    message.obj = "注册异常...请稍后再试";
                                    handler.sendMessage(message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Message message = Message.obtain();
                                message.what = REGISTEREXCEPT;
                                message.obj = "服务器异常...请稍后再试";
                                handler.sendMessage(message);
                            }

                        }
                    }.start();//不要忘记开线程
                }
            }
        });
    }
}
