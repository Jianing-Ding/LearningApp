package com.example.mytry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mytry.ui.onLineTest.questionBaseChoose;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SelectWindow extends PopupWindow {
    private static final int cppSingleMax = 43;
    private static final int cppMultiMax = 43;
    private static final int dataSingleMax = 73;
    private static final int dataMultiMax = 16;
    private static final int threadSingleMax = 71;
    private static final int threadMultiMax = 13;
    private static final int operateSingleMax = 46;
    private static final int operateMultiMax = 18;


    Spinner spinner;
    EditText start;
    EditText length;
    Button button;
    Button cancel;

    public void SelectPopupWindow(final Context ctx) {
        List<String> data = new ArrayList<>();
        ArrayAdapter adapter;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.activity_select_window, null); //可以采用各种样式，自定义view
        spinner=root.findViewById(R.id.spinner);
        start=root.findViewById(R.id.start);
        length=root.findViewById(R.id.length);
        button=root.findViewById(R.id.button);
        cancel=root.findViewById(R.id.cancel);
        data.add("单选题");
        data.add("多选题");
//        data.add("单选及多选");
        adapter=new ArrayAdapter<String>(ctx,android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = ctx.getSharedPreferences("info", Context.MODE_PRIVATE);
                String mode=""+spinner.getSelectedItemId();
                String base=sp.getString("base","1");
                String startStr=start.getText().toString();
                String lengthStr=length.getText().toString();
                int startInt=Integer.parseInt(startStr);
                int lengthInt=Integer.parseInt(lengthStr);
                int max=0;
                if (mode.equals("0")){
                    if (base.equals("1"))
                        max=dataSingleMax;
                    if (base.equals("2"))
                        max=cppSingleMax;
                    if (base.equals("3"))
                        max=operateSingleMax;
                    if (base.equals("4"))
                        max=threadSingleMax;
                }else if (mode.equals("1")){
                    if (base.equals("1"))
                        max=dataMultiMax;
                    if (base.equals("2"))
                        max=cppMultiMax;
                    if (base.equals("3"))
                        max=operateMultiMax;
                    if (base.equals("4"))
                        max=threadMultiMax;
                }
                if(mode!=""&&startStr!=""&&lengthStr!="") {
                    if (startInt>=max||startInt+lengthInt>=max) {
                        Toast.makeText(ctx,"开始题目号或长度超出题库范围！\n题目总数："+max,Toast.LENGTH_LONG).show();
                        start.setText("");
                        length.setText("");
                    }else {
                        //获取sp编辑器
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("mode", mode);
                        edit.putString("start", startStr);
                        edit.putString("length", lengthStr);
                        edit.apply();
                        Intent intent = new Intent(ctx, questionRepresent.class);
                        ctx.startActivity(intent);
                        dismiss();
                    }
                }else{
                    Toast.makeText(ctx,"请将选项填写完整",Toast.LENGTH_SHORT).show();
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
        setContentView(root);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);   //弹出窗体后可点击
    }

}