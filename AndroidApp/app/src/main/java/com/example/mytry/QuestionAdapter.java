package com.example.mytry;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private ArrayList<HashMap<String, Object>> data = new ArrayList<>();
    private boolean[][] selectList;

    /*构造函数*/
    public QuestionAdapter(Context context, ArrayList<HashMap<String, Object>> list, boolean[][] select) {
        this.mInflater = LayoutInflater.from(context);
        this.data.addAll(list);
        this.selectList = select;
    }

    @Override
    public int getCount() {
        Log.i("info", "getCount: run/ data size:" + data.size());
        return data.size();
    }

    public void update(ArrayList<HashMap<String, Object>> newList) {
        this.data.clear();
        this.data.addAll(newList);
    }

    public boolean[][] getSelectList() {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.question_item, null);
            holder = new ViewHolder();
            holder.question = convertView.findViewById(R.id.question);
            holder.buttonA = convertView.findViewById(R.id.buttonA);
            holder.buttonB = convertView.findViewById(R.id.buttonB);
            holder.buttonC = convertView.findViewById(R.id.buttonC);
            holder.buttonD = convertView.findViewById(R.id.buttonD);
            holder.buttonE = convertView.findViewById(R.id.buttonE);
            holder.buttonF = convertView.findViewById(R.id.buttonF);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (data.size() != 0) {
            holder.question.setText(position + 1 + ". " + data.get(position).get("question").toString());
            Log.i("info", "choiceA:" + data.get(position).get("radioButton").toString());
            holder.buttonA.setText(data.get(position).get("radioButton").toString());
            Log.i("info", "after :" + holder.buttonA.getText());
            holder.buttonB.setText(data.get(position).get("radioButton2").toString());
            if (data.get(position).get("radioButton3") == null)
                holder.buttonC.setVisibility(View.GONE);
            else
                holder.buttonC.setText(data.get(position).get("radioButton3").toString());
            if (data.get(position).get("radioButton4") == null) {
                holder.buttonD.setVisibility(View.GONE);
            } else
                holder.buttonD.setText(data.get(position).get("radioButton4").toString());
            if (data.get(position).get("checkboxE") == null)
                holder.buttonE.setVisibility(View.GONE);
            else
                holder.buttonE.setText(data.get(position).get("checkboxE").toString());
            if (data.get(position).get("checkboxF") == null)
                holder.buttonF.setVisibility(View.GONE);
            else
                holder.buttonF.setText(data.get(position).get("checkboxF").toString());
        }

        holder.buttonA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][0] = isChecked;
            }
        });
        holder.buttonB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][1] = isChecked;
            }
        });
        holder.buttonC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][2] = isChecked;
            }
        });
        holder.buttonD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][3] = isChecked;
            }
        });
        holder.buttonE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][4] = isChecked;
            }
        });
        holder.buttonF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectList[position][5] = isChecked;
            }
        });
        notifyDataSetChanged();
        holder.buttonA.setChecked(selectList[position][0]);
        holder.buttonB.setChecked(selectList[position][1]);
        holder.buttonC.setChecked(selectList[position][2]);
        holder.buttonD.setChecked(selectList[position][3]);
        holder.buttonD.setChecked(selectList[position][4]);
        holder.buttonF.setChecked(selectList[position][5]);


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


}


