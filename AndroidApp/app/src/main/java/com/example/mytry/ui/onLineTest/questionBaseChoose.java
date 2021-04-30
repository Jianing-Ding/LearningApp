package com.example.mytry.ui.onLineTest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateTimePatternGenerator;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mytry.R;
import com.example.mytry.SelectWindow;

public class questionBaseChoose extends Fragment {

    private onlineTestViewModel onlineTestViewModel;

    CardView questionBase1;
    CardView questionBase2;
    CardView questionBase3;
    CardView questionBase4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        onlineTestViewModel =
                ViewModelProviders.of(this).get(onlineTestViewModel.class);
        View root = inflater.inflate(R.layout.activity_ques_mode_choose, container, false);
        questionBase1 = root.findViewById(R.id.questionbase1);
        questionBase2 = root.findViewById(R.id.questionBase2);
        questionBase3 = root.findViewById(R.id.questionBase3);
        questionBase4 = root.findViewById(R.id.questionBase4);

        questionBase1.setOnClickListener(new clickListener());
        questionBase2.setOnClickListener(new clickListener());
        questionBase3.setOnClickListener(new clickListener());
        questionBase4.setOnClickListener(new clickListener());
        return root;
    }

    private class clickListener implements View.OnClickListener{
        SharedPreferences sp = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.questionbase1:
                    edit.putString("base","1");
                    break;
                case R.id.questionBase2:
                    edit.putString("base","2");
                    break;
                case R.id.questionBase3:
                    edit.putString("base","3");
                    break;
                case R.id.questionBase4:
                    edit.putString("base","4");
                    break;
                default:
                    break;
            }
            edit.apply();
            SelectWindow selectWindow = new SelectWindow();
            selectWindow.SelectPopupWindow(getActivity());
            selectWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
//        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            setBackgroundAlpha(0.3f,getActivity());
            selectWindow.showAtLocation(getView(), Gravity.CENTER_VERTICAL, 0, 0);

        }
    }

    //设置屏幕背景透明效果
    public static void setBackgroundAlpha(float alpha, Activity mActivity) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = alpha;
        mActivity.getWindow().setAttributes(lp);
    }

}