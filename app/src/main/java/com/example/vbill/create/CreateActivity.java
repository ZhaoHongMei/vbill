package com.example.vbill.create;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.adapter.CategoryAdapter;
import com.example.vbill.bean.Category;
import com.example.vbill.entity.CategorySummaryEntity;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateActivity extends AppCompatActivity {
    private static final String TAG = "CreateActivity";
    private TextView createHeaderIncome,createHeaderOutcome;
    private SharedPreferences sharedPreferences;
    CategorySummaryEntity categorySummaryEntity;
    private RecyclerView categorySummary;
    private List<Category> incomeCategoryList;
    private List<Category> outcomeCategoryList;
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
        categorySummary = findViewById(R.id.category_summary);

        //一进页面加载判断sharePreference是否有categorysummary，如果没有发送请求得到数据
        sharedPreferences = getSharedPreferences("sharedata",MODE_PRIVATE);
        Log.d(TAG, "onCreate: sharedPreferences" + sharedPreferences.getString("categorysummary",""));
        String categorysummary = sharedPreferences.getString("categorysummary","");
        if("".equals(categorysummary)){
            getCategorySummary();
        }else{
            Gson gson=new Gson();
            JsonObject responseJsonDate = gson.fromJson(categorysummary,JsonObject.class);
            categorySummaryEntity = gson.fromJson(responseJsonDate,CategorySummaryEntity.class);
            Log.d(TAG, "onCreate: categorySummaryEntity" + categorySummaryEntity);
        }
        //一进页面默认加载收入的数据
        List<TextView> preTxtBtn = new ArrayList<>();
        preTxtBtn.add(createHeaderIncome);
        clickCategoryBtn(outcomeCategoryList,"out",preTxtBtn,createHeaderOutcome);

        //点击收入支出请求数据重新渲染相应的数据
        createHeaderIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TextView> preTxtBtn = new ArrayList<>();
                preTxtBtn.add(createHeaderOutcome);
                clickCategoryBtn(incomeCategoryList,"in",preTxtBtn,createHeaderIncome);
            }
        });
        createHeaderOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TextView> preTxtBtn = new ArrayList<>();
                preTxtBtn.add(createHeaderIncome);
                clickCategoryBtn(outcomeCategoryList,"out",preTxtBtn,createHeaderOutcome);
            }
        });

    }

    public void getCategorySummary(){
        Log.d(TAG, "getCategorySummary: ");
        String address= Constants.SERVER_PREFIX + "v1/esc/categories";
        HttpUtil.sendOkHttpGetRequest(address,new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData: " + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseData!=null){
                            Gson gson=new Gson();
                            JsonObject responseJsonDate = gson.fromJson(responseData,JsonObject.class);
                            String statusCode =gson.fromJson(responseJsonDate.get("statusCode"), new TypeToken<String>() {
                            }.getType());
                            if("200".equals(statusCode)){
                                Log.d(TAG, "categorysummary: " + responseJsonDate.get("data"));
                                categorySummaryEntity = gson.fromJson(responseJsonDate.get("data"),CategorySummaryEntity.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("categorysummary", String.valueOf(responseJsonDate.get("data")));
                                editor.apply();
                            }

                        }

                    }
                });

            }
        });
    }

    public void setCategoryAdapter(List<Category> categoryList){
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this); //线性
        //瀑布流
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        categorySummary.setLayoutManager(layoutManager);
        categorySummary.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,categoryList);
        categorySummary.setAdapter(categoryAdapter);
    }

    public List<Category> getInOrOutCategory(CategorySummaryEntity categorySummaryEntity,String type){
        if("in".equals(type)){
            List<Category> tempList =categorySummaryEntity.getIn();
            return tempList;
        }else {
            List<Category> tempList =categorySummaryEntity.getOut();
            return tempList;
        }
    }
    @SuppressLint("ResourceAsColor")
    public void clickCategoryBtn(List<Category>categoryList, String type, List<TextView> preTextBtnList,TextView thisTexBtn){
        Resources resources = this.getResources();
        //恢复其他的背景颜色
        Drawable uncheckedDrawable = resources.getDrawable(R.drawable.income_unchecked_bg);
        for(int i =0;i<preTextBtnList.size();i++){
            preTextBtnList.get(i).setBackgroundDrawable(uncheckedDrawable);
            preTextBtnList.get(i).setTextColor(R.color.colorPrimary);
        }
        //改变背景颜色
        Drawable checkedDrawable = resources.getDrawable(R.drawable.income_checked_bg);
        thisTexBtn.setBackgroundDrawable(checkedDrawable);
        thisTexBtn.setTextColor(R.color.colorPrimary);

        //渲染数据
        categoryList = getInOrOutCategory(categorySummaryEntity,type);
        setCategoryAdapter(categoryList);
    }
}
