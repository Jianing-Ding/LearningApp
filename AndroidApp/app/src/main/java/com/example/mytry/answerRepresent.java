package com.example.mytry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class answerRepresent extends AppCompatActivity {
    ListView listView;
    TextView title;
    Button back;
    ImageView like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_represent);

        title = findViewById(R.id.title);
        listView = findViewById(R.id.singleChoiceList);
        back = findViewById(R.id.back);


        SharedPreferences sp = this.getSharedPreferences("info", MODE_PRIVATE);
        String modeStr = sp.getString("mode", "");
        boolean[][] selectList = (boolean[][]) getIntent().getSerializableExtra("choice");
        ArrayList<HashMap<String, Object>> wrongList = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("wrongList");
        print(selectList);
        if (modeStr.equals("0"))
            title.setText("单选题");
        else
            title.setText("多选题");

        BaseAdapter adapter = new AnswerAdapter(this, wrongList, selectList);
        listView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void print(boolean[][] selectList){
        for (int i = 0; i <selectList.length ; i++) {
            for (int j = 0; j <6 ; j++) {
                System.out.print("      "+selectList[i][j]);
            }
            System.out.print("\n");
        }
    }
}
