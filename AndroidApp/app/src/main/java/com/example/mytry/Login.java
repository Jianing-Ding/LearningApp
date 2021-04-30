package com.example.mytry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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


public class Login extends AppCompatActivity {
    EditText userName;
    EditText password;
    Button button;
    Button register;
    public static final int LOGINSUCCESS=0;
    public static final int LOGINNOTFOUND=1;
    public static final int LOGINEXCEPT=2;

    public String usernameStr;
    public String passwordStr;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case LOGINSUCCESS:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case LOGINNOTFOUND:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case LOGINEXCEPT:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.description);
        password = findViewById(R.id.passward);
        button = findViewById(R.id.login);
        register = findViewById(R.id.register);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取编辑框内的内容
                usernameStr = userName.getText().toString().trim();
                passwordStr = password.getText().toString().trim();

                //判断是否输入为空（在这里就不再进行正则表达式判断了）
                if (usernameStr.equals("") || passwordStr.equals("")) {
                    Toast.makeText(Login.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }//进行登录操作(联网操作要添加权限)
                else {
                    //联网操作要开子线程，在主线程不能更新UI
                    new Thread() {
                        private HttpURLConnection connection;

                        @Override
                        public void run() {

                            try {
                                //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                                // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                                String data2 = "username=" + URLEncoder.encode(usernameStr, "utf-8") + "&password=" + URLEncoder.encode(passwordStr, "utf-8") + "&sign=" + URLEncoder.encode("1", "utf-8");
                                connection = HttpConnectionUtils.getConnection(data2);
                                int code = connection.getResponseCode();
                                if (code == 200) {
                                    InputStream inputStream = connection.getInputStream();
                                    String str = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串
                                    Message message = Message.obtain();//更新UI就要向消息机制发送消息
                                    message.what = LOGINSUCCESS;//用来标志是哪个消息
                                    String[] result =str.split(",");
                                    message.obj = result[0];//消息主体
                                    handler.sendMessage(message);
                                    if(message.obj.equals("登录成功")) {
                                        SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sp.edit();
                                        editor.putString("avatarPath",result[1]);
                                        editor.putString("userName",usernameStr);
                                        editor.putString("password",passwordStr);
                                        editor.putString("credit",result[2]);
                                        editor.putString("status","1");
                                        editor.apply();
                                        finish();
                                    }else{
                                        password.setText("");
                                    }
                                } else {
                                    Message message = Message.obtain();
                                    message.what = LOGINNOTFOUND;
                                    message.obj = "登录异常...请稍后再试";
                                    handler.sendMessage(message);
                                }
                            } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                                e.printStackTrace();
                                Message message = Message.obtain();
                                message.what = LOGINEXCEPT;
                                message.obj = "服务器异常...请稍后再试";
                                handler.sendMessage(message);
                            }
                        }
                    }.start();//不要忘记开线程
//                    Intent index = new Intent(Login.this, MainActivity.class);
//                    startActivity(index);
                }


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, Register.class);
                startActivity(register);
            }
        });
    }

    @Override
    public void onBackPressed(){
        System.exit(0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //重新获取数据的逻辑，此处根据自己的要求回去
        SharedPreferences sp = this.getSharedPreferences("info", Context.MODE_PRIVATE);
        //设置数据
        userName.setText(sp.getString("userName", ""));
        password.setText(sp.getString("password",""));
    }


}

