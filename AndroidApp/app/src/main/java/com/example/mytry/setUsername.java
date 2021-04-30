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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytry.Utils.HttpConnectionUtils;
import com.example.mytry.Utils.StreamChangeStrUtils;
import com.example.mytry.ui.personal.personalFragment;
import com.example.mytry.ui.onLineTest.questionBaseChoose;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class setUsername extends PopupWindow {

    TextView currentName;
    EditText newName;
    Button confirm;
    Button cancel;
    String oldNameStr;
    private SharedPreferences sp;
    public void setWindow(final Context ctx){
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root=inflater.inflate(R.layout.activity_set_username, null); //可以采用各种样式，自定义view
        setContentView(root);
        sp=getContentView().getContext().getSharedPreferences("info",Context.MODE_PRIVATE);

        currentName=root.findViewById(R.id.currentName);
        newName=root.findViewById(R.id.newName);
        confirm=root.findViewById(R.id.confirm);
        cancel=root.findViewById(R.id.cancel);
        oldNameStr=sp.getString("userName","用户名");
        currentName.setText(oldNameStr);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNameStr=newName.getText().toString();
                if(newNameStr!=null&&!newNameStr.equals("")){
                    String str=userNameChange();
                    Toast.makeText(ctx,str,Toast.LENGTH_SHORT).show();
                    if (str.equals("修改成功")){
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("userName",newNameStr);
                        editor.apply();
                        personalFragment.userName.setText(newNameStr);
                        dismiss();
                    }else if (str.equals("该用户名已存在")){
                        newName.setText("");
                    }else{
                        Toast.makeText(ctx,"服务器连接异常",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ctx,"新用户名不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        setWidth((int)(WindowManager.LayoutParams.WRAP_CONTENT*0.8));
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);   //弹出窗体后可点击

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                questionBaseChoose.setBackgroundAlpha(1f,MainActivity.mainActivity);
            }
        });
    }

    public String userNameChange(){
        final String[] result = {null};
        Thread thread=new Thread() {

            private HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    //Log.i("info", "run: 线程开始"+"base:"+baseStr+"start:"+startStr);
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String newNameStr=sp.getString("userName","");
                    String data2 = "username="+ URLEncoder.encode(newNameStr,"utf-8")+"&oldName="+URLEncoder.encode(oldNameStr,"utf-8")+"&sign="+URLEncoder.encode("3","utf-8");
                    connection = HttpConnectionUtils.getConnection(data2);
                    int code = connection.getResponseCode();
                    Log.i("info", "run: 获取response code："+code);
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