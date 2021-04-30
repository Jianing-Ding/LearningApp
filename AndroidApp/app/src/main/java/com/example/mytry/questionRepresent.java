package com.example.mytry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.example.mytry.Utils.HttpConnectionUtils;
import com.example.mytry.Utils.StreamChangeStrUtils;
import com.example.mytry.domain.Question;
import com.example.mytry.ui.shop.ShopFragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class questionRepresent extends AppCompatActivity {

    String startStr;
    String lengthStr;
    String modeStr;
    String baseStr;
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    boolean[][] selectList;
    boolean[][] answers;
    ListView listView;
    TextView title;
    Button button;
    SingleChoice singleChoiceAdapter =null;
    QuestionAdapter multiChoiceAdapter =null;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_online_test);
        context=this;
        //questionBaseChoose.setBackgroundAlpha(1f,this);
        final SharedPreferences sp=this.getSharedPreferences("info",MODE_PRIVATE);
        startStr=sp.getString("start","");
        lengthStr=sp.getString("length","");
        modeStr=sp.getString("mode","");
        baseStr=sp.getString("base","0");
        selectList = new boolean[Integer.parseInt(lengthStr)][6];
        answers=new boolean[Integer.parseInt(lengthStr)][6];
        title=findViewById(R.id.title);
        button=findViewById(R.id.back);
        listView=findViewById(R.id.singleChoiceList);


        listItem=getData();
        if (modeStr.equals("1")) {
            multiChoiceAdapter = new QuestionAdapter(this, listItem, selectList);
            Log.i("info", "onCreate: myAdapter size:" + multiChoiceAdapter.getCount());
            listView.setAdapter(multiChoiceAdapter);
        }else if(modeStr.equals("0")){
            singleChoiceAdapter = new SingleChoice(this, listItem, selectList);
            Log.i("info", "onCreate: myAdapter size:" + singleChoiceAdapter.getCount());
            listView.setAdapter(singleChoiceAdapter);
        }
        if(modeStr.equals("0"))
            title.setText("单选题");
        else
            title.setText("多选题");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_notifications_black_24dp);
                builder.setTitle("提示");
                builder.setMessage("答题正在进行，点击确定交卷");
                // 确定按钮的点击事件

                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<HashMap<String,Object>> wrongList=new ArrayList<>();
                        int count=-1;
                        if (modeStr.equals("0"))
                            selectList= singleChoiceAdapter.getSelectList();
                        if (modeStr.equals("1"))
                            selectList= multiChoiceAdapter.getSelectList();
                        boolean flag=true;
                        boolean[][] rightAnswer=new boolean[answers.length][6];
                        for (int i = 0; i <answers.length ; i++) {
                            for (int j = 0; j <6 ; j++) {
                                if (selectList[i][j]!=answers[i][j]){
                                    if (flag) {
                                        flag=false;
                                        wrongList.add(listItem.get(i));
                                        count++;
                                    }
                                }
                            }
                            if (!flag) {
                                for (int j = 0; j < 6; j++) {
                                    rightAnswer[count][j]=answers[i][j];
                                }
                            }
                            flag=true;
                        }
                        System.out.println("selectList:\n");
                        print(selectList);
                        System.out.println("wrongList answers:\n");
                        print(rightAnswer);
                        Intent intent=new Intent(context,answerRepresent.class);
                        int credit=selectList.length-wrongList.size();
                        Toast.makeText(context,"本次测试获得积分："+credit,Toast.LENGTH_LONG).show();
                        credit=credit+Integer.parseInt(sp.getString("credit",""));
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("credit",""+credit);
                        editor.apply();
                        ShopFragment.updateCredit(credit+"",sp.getString("userName",""));
                        intent.putExtra("wrongList",wrongList);
                        intent.putExtra("choice",rightAnswer);
                        startActivity(intent);
                        finish();
                    }
                }
                );

                // 取消按钮的点击事件
                builder.setNegativeButton("取消",

                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCancelable(true);
                            }

                });

                builder.create().show();
            }
        });
    }

        /*添加一个得到数据的方法，方便使用*/
    private ArrayList<HashMap<String, Object>> getData() {

        /*为动态数组添加数据*/

        listItem.clear();
        Thread thread=new Thread() {

            private HttpURLConnection connection;

            @Override
            public void run() {

                try {
                    Log.i("info", "run: 线程开始"+"base:"+baseStr+"start:"+startStr);
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data2 = "base="+URLEncoder.encode(baseStr,"utf-8")+"&start=" + URLEncoder.encode(startStr, "utf-8") + "&length=" + URLEncoder.encode(lengthStr, "utf-8") + "&mode=" + URLEncoder.encode(modeStr, "utf-8");
                    connection = HttpConnectionUtils.getConnectionQ(data2);
                    int code = connection.getResponseCode();
                    Log.i("info", "run: 获取response code："+code);
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String str = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串
                        Log.i("info",str);
                        JSONArray jsonArray = JSONArray.parseArray(str);
                        List<Question> questions = JSONArray.parseObject(jsonArray.toJSONString(),new TypeReference<ArrayList<Question>>() {});
                        for (int i = 0; i <questions.size() ; i++) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("question", questions.get(i).getQuestion());
                            map.put("radioButton",questions.get(i).getChoiceA());
                            map.put("radioButton2",questions.get(i).getChoiceB());
                            map.put("radioButton3",questions.get(i).getChoiceC());
                            map.put("radioButton4",questions.get(i).getChoiceD());
                            map.put("checkboxE",questions.get(i).getChoiceE());
                            map.put("checkboxF",questions.get(i).getChoiceF());
                            listItem.add(map);
                            String answer=questions.get(i).getAnswer();
                            if (answer.contains(questions.get(i).getChoiceA()))
                                answers[i][0]=true;
                            if (answer.contains(questions.get(i).getChoiceB()))
                                answers[i][1]=true;
                            if (questions.get(i).getChoiceC()!=null&&answer.contains(questions.get(i).getChoiceC()))
                                answers[i][2]=true;
                            if (questions.get(i).getChoiceD()!=null&&answer.contains(questions.get(i).getChoiceD()))
                                answers[i][3]=true;
                            if (questions.get(i).getChoiceE()!=null&&answer.contains(questions.get(i).getChoiceE()))
                                answers[i][4]=true;
                            if (questions.get(i).getChoiceF()!=null&&answer.contains(questions.get(i).getChoiceF()))
                                answers[i][5]=true;
                        }
//                        System.out.println("获取数据所得answers:\n");
//                        print(answers);
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
        return listItem;
    }

    public void print(boolean[][] selectList){
        for (int i = 0; i <selectList.length ; i++) {
            System.out.print(i+1+"");
            for (int j = 0; j <6 ; j++) {
                System.out.print("      "+selectList[i][j]);
            }
            System.out.print("\n");
        }
    }

}