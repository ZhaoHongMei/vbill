package com.example.vbill.create;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.vbill.R;

public class CreateActivity extends AppCompatActivity {
    private TextView createHeaderIncome,createHeaderOutcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //获取页面元素
        createHeaderIncome = findViewById(R.id.create_header_income);
        createHeaderOutcome = findViewById(R.id.create_header_outcome);
        //点击收入支出请求数据重新渲染相应的数据
        createHeaderIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        createHeaderOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Intent intent = getIntent();
        String aa = intent.getStringExtra("position");
        Log.d("product",aa);

    }
}
