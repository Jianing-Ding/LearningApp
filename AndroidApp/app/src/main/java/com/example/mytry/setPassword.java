package com.example.mytry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.mytry.Utils.HttpConnectionUtils;
import com.example.mytry.Utils.StreamChangeStrUtils;
import com.example.mytry.ui.onLineTest.questionBaseChoose;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class setPassword extends PopupWindow {
    EditText oldPassword;
    EditText newPassword;
    EditText confirmPassword;
    Button confirm;
    Button cancel;
    private SharedPreferences sp;


    public void setView(final Context ctx) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View root=inflater.inflate(R.layout.activity_set_password, null); //可以采用各种样式，自定义view
        setContentView(root);
        oldPassword=root.findViewById(R.id.oldPassword);
        newPassword=root.findViewById(R.id.newPassword);
        confirmPassword=root.findViewById(R.id.confirmPassword);
        confirm=root.findViewById(R.id.confirm);
        cancel=root.findViewById(R.id.cancel);
        sp=getContentView().getContext().getSharedPreferences("info",Context.MODE_PRIVATE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordStr=sp.getString("password","");
                if (passwordStr.equals(oldPassword.getText().toString())){
                    String newStr=newPassword.getText().toString();
                    String confirmStr=confirmPassword.getText().toString();
                    if (newStr==null||newStr.equals("")) {
                        Toast.makeText(ctx,"新密码不能为空！",Toast.LENGTH_SHORT).show();
                    }else if (newStr != null && newStr.equals(confirmStr)){
                        String str=passwordChange();
                        Toast.makeText(ctx,str,Toast.LENGTH_SHORT).show();
                        if (str.equals("修改成功")){
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putString("password",newStr);
                            editor.apply();
                        }
                        dismiss();
                    }else{
                        Toast.makeText(ctx,"两次密码输入不一致!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ctx,"原密码输入错误",Toast.LENGTH_SHORT).show();
                    oldPassword.setText("");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                questionBaseChoose.setBackgroundAlpha(1f,MainActivity.mainActivity);
            }
        });
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);   //弹出窗体后可点击
    }

    public String passwordChange() {
        final String[] result={null};
        Thread thread = new Thread() {

            private HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    //Log.i("info", "run: 线程开始"+"base:"+baseStr+"start:"+startStr);
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String newPasswordStr = sp.getString("password", "");
                    String userName=sp.getString("userName","");
                    String data2 = "password=" + URLEncoder.encode(newPasswordStr, "utf-8") +"&username="+URLEncoder.encode(userName,"utf-8")+ "&sign=" + URLEncoder.encode("4", "utf-8");
                    connection = HttpConnectionUtils.getConnection(data2);
                    int code = connection.getResponseCode();
                    Log.i("info", "run: 获取response code：" + code);
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        result[0] = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                }
            }
        };//不要忘记开线程
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }
}