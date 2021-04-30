package com.example.mytry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleChoice extends BaseAdapter {

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private ArrayList<HashMap<String, Object>> data=new ArrayList<>();
    private boolean[][] selectList;

    /*构造函数*/
    public SingleChoice(Context context, ArrayList<HashMap<String,Object>> list, boolean[][] select) {
        this.mInflater = LayoutInflater.from(context);
        this.data.addAll(list);
        this.selectList=select;
    }
    @Override
    public int getCount() {
        Log.i("info", " answerAdapter getCount: run/ data size:"+data.size());
        return data.size();
    }

    public void update(ArrayList<HashMap<String,Object>> newList){
        this.data.clear();
        this.data.addAll(newList);
    }

    public boolean[][] getSelectList(){
        return selectList;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.activity_single_choice,null);
            holder=new ViewHolder();
            holder.question=convertView.findViewById(R.id.question);
            holder.buttonA =convertView.findViewById(R.id.buttonA);
            holder.buttonB =convertView.findViewById(R.id.buttonB);
            holder.buttonC =convertView.findViewById(R.id.buttonC);
            holder.buttonD =convertView.findViewById(R.id.buttonD);
            holder.buttonE=convertView.findViewById(R.id.buttonE);
            holder.buttonF=convertView.findViewById(R.id.buttonF);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }


        if(data.size()!=0) {
            holder.question.setText(position+1+". "+data.get(position).get("question").toString());
            holder.buttonA.setText(data.get(position).get("radioButton").toString());
            holder.buttonB.setText(data.get(position).get("radioButton2").toString());
            if(data.get(position).get("radioButton3")==null)
                holder.buttonC.setVisibility(View.GONE);
            else
                holder.buttonC.setText(data.get(position).get("radioButton3").toString());
            if (data.get(position).get("radioButton4")==null){
                holder.buttonD.setVisibility(View.GONE);
            }else
                holder.buttonD.setText(data.get(position).get("radioButton4").toString());
            if(data.get(position).get("checkboxE")==null)
                holder.buttonE.setVisibility(View.GONE);
            else
                holder.buttonE.setText(data.get(position).get("checkboxE").toString());
            if(data.get(position).get("checkboxF")==null)
                holder.buttonF.setVisibility(View.GONE);
            else
                holder.buttonF.setText(data.get(position).get("checkboxF").toString());
        }



        holder.buttonA.setOnCheckedChangeListener(null);
        holder.buttonB.setOnCheckedChangeListener(null);
        holder.buttonC.setOnCheckedChangeListener(null);
        holder.buttonD.setOnCheckedChangeListener(null);
        holder.buttonE.setOnCheckedChangeListener(null);
        holder.buttonF.setOnCheckedChangeListener(null);

        holder.buttonA.setChecked(selectList[position][0]);
        holder.buttonB.setChecked(selectList[position][1]);
        holder.buttonC.setChecked(selectList[position][2]);
        holder.buttonD.setChecked(selectList[position][3]);
        holder.buttonE.setChecked(selectList[position][4]);
        holder.buttonF.setChecked(selectList[position][5]);
        notifyDataSetChanged();

        holder.buttonA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][0] = true;
                for (int i = 0; i <6 ; i++) {
                    if (i!=0)
                        selectList[position][i]=false;
                }
                notifyDataSetChanged();
            }
        });

        holder.buttonB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][1] = true;
                for (int i = 0; i <6 ; i++) {
                    if (i!=1)
                        selectList[position][i]=false;
                }
                notifyDataSetChanged();
            }
        });
        holder.buttonC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][2] = true;
                for (int i = 0; i <6 ; i++) {
                    if (i!=2)
                        selectList[position][i]=false;
                }
                notifyDataSetChanged();
            }
        });
        holder.buttonD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][3] = true;
                for (int i = 0; i <6 ; i++) {
                    if (i!=3)
                        selectList[position][i]=false;
                }
                notifyDataSetChanged();
            }
        });
        holder.buttonE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][4] = true;
                for (int i = 0; i <6 ; i++) {
                    if (i!=4)
                        selectList[position][i]=false;
                }
                notifyDataSetChanged();
            }
        });
        holder.buttonF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][5] = true;
                for (int i = 0; i <6 ; i++) {
                    if (i != 5)
                        selectList[position][i] = false;
                }
                notifyDataSetChanged();
            }
        });






        return convertView;
    }

    public class ViewHolder {
        TextView question;
        CheckBox buttonA;
        CheckBox buttonB;
        CheckBox buttonC;
        CheckBox buttonD;
        CheckBox buttonE;
        CheckBox buttonF;
    }


//    public class ViewHolder{
//        TextView question;
//        RadioButton buttonA;
//        RadioButton buttonB;
//        RadioButton buttonC;
//        RadioButton buttonD;
//        RadioButton buttonE;
//        RadioButton buttonF;
//        RadioGroup group;
//    }
}