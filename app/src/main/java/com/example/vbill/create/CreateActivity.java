package com.example.vbill.create;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbill.R;
import com.example.vbill.adapter.CategoryAdapter;
import com.example.vbill.bean.Category;
import com.example.vbill.bean.ChildBill;
import com.example.vbill.customizeUI.datepicker.CustomDatePicker;
import com.example.vbill.customizeUI.datepicker.DateFormatUtils;
import com.example.vbill.entity.CategorySummaryEntity;
import com.example.vbill.home.HomeActivity;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateActivity extends AppCompatActivity {
    private static final String TAG = "CreateActivity";
    private static View createBodyView;
    private int year;
    private int month;
    private int day;
    public Gson gson;
    private TextView createHeaderIncome, createHeaderOutcome, createTime,preCategoryTexBtn;
    private SharedPreferences sharedPreferences;
    private CategorySummaryEntity categorySummaryEntity;
    private RecyclerView categorySummary;
    private List<Category> incomeCategoryList;
    private List<Category> outcomeCategoryList;

    private Button createCancelBtn, createDoneBtn;
    private EditText createAmount, createComment;
    private CustomDatePicker mDatePicker, mTimerPicker;
    private static String hasCategory;

    private SharedPreferences loginPref;
    private String customerId;
    private ChildBill childBill;


    private static Category selectCategory;

    public static Category getSelectCategory() {
        return selectCategory;
    }

    public static void setSelectCategory(Category selectCategory) {
        CreateActivity.selectCategory = selectCategory;
    }

    public static String getHasCategory() {
        return hasCategory;
    }

    public static void setHasCategory(String hasCategory) {
        CreateActivity.hasCategory = hasCategory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //获取页面元素
        gson = new Gson();
        createHeaderIncome = findViewById(R.id.create_header_income);
        createHeaderOutcome = findViewById(R.id.create_header_outcome);
        categorySummary = findViewById(R.id.category_summary);
        createBodyView = findViewById(R.id.create_body);
        createCancelBtn = findViewById(R.id.create_cancel_btn);
        createDoneBtn = findViewById(R.id.create_done_btn);
        //createComment = findViewById(R.id.create_body_comment);
        //createComment.setLines(1);
        createTime = findViewById(R.id.create_body_time);
        createTime.setInputType(EditorInfo.TYPE_CLASS_DATETIME);
        createAmount = findViewById(R.id.create_body_amount);
        createAmount.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        createAmount.setLines(1);

        //获取intent的参数，判断是更改还是创建
        Intent intent = getIntent();
        childBill = (ChildBill) intent.getSerializableExtra("bill");
        Log.d(TAG, "onCreate: childBill" + childBill);

        initDatePicker();

        loginPref = getSharedPreferences("login", MODE_PRIVATE);
        customerId = String.valueOf(loginPref.getInt("userId", -1));

        createTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDatePickerDialog();
                mDatePicker.show(createTime.getText().toString());
            }
        });


        //一进页面加载判断sharePreference是否有categorysummary，如果没有发送请求得到数据
        sharedPreferences = getSharedPreferences("sharedata", MODE_PRIVATE);
        Log.d(TAG, "onCreate: sharedPreferences" + sharedPreferences.getString("categorysummary", ""));
        String categorysummary = sharedPreferences.getString("categorysummary", "");
//        if("".equals(categorysummary)){
//            getCategorySummary();
//        getCategorySummary();
//        }else{
//            Gson gson=new Gson();
//            JsonObject responseJsonDate = gson.fromJson(categorysummary,JsonObject.class);
//            categorySummaryEntity = gson.fromJson(responseJsonDate,CategorySummaryEntity.class);
//            Log.d(TAG, "onCreate: categorySummaryEntity" + categorySummaryEntity);
//            List<TextView> preTxtBtn = new ArrayList<>();
//            preTxtBtn.add(createHeaderIncome);
//            clickCategoryBtn(outcomeCategoryList,"out",preTxtBtn,createHeaderOutcome);
//        }

        //一进页面默认加载收入的数据
        getCategorySummary();

        //点击收入支出请求数据重新渲染相应的数据
        createHeaderIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preCategoryTexBtn != v){
                    clickCategoryBtn(incomeCategoryList, "in", createHeaderOutcome, (TextView) v,null);
                }
            }
        });
        createHeaderOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preCategoryTexBtn != v){
                    clickCategoryBtn(outcomeCategoryList, "out", createHeaderIncome, (TextView) v,null);}
            }
        });
        //取消和完成的事件
        createCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideCreateBodyView();
                //显示intent，隐式用不了
                Intent homeFragmentIntent = new Intent(CreateActivity.this, HomeActivity.class);
                startActivity(homeFragmentIntent);
            }
        });

        createDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map newChildMap = new HashMap<String,String>();
                Category selectCategoryItem = getSelectCategory();
                String createTimeText = String.valueOf(createTime.getText());
                //String createCommentText = String.valueOf(createComment.getText());
                String createAmountText = String.valueOf(createAmount.getText());
                newChildMap.put("createDay",createTimeText.replace("-","/"));
                newChildMap.put("createTime","12:10:12");
                newChildMap.put("imagePath",selectCategoryItem.getImagePath());
                newChildMap.put("categoryCode",selectCategoryItem.getCode());
                newChildMap.put("categoryDesc",selectCategoryItem.getDescription());
                newChildMap.put("type",selectCategoryItem.getType());
                newChildMap.put("amount",createAmountText);
                if(null != childBill){
                    newChildMap.put("itemId",childBill.getItemId());
                }
                //将requestjson化。
                String newChildMapJson = gson.toJson(newChildMap);

                Log.d(TAG, "onClick: newChildBill" + String.valueOf(newChildMapJson));
                //发送http请求，去添加一笔账单
                String address = Constants.SERVER_PREFIX + "v1/esc/" + customerId + "/account";
//                if (!(createTimeText.equals("")) && !(createCommentText.equals("")) && !(createAmountText.equals(""))) {
                if (!(createTimeText.equals("")) && !(createAmountText.equals(""))) {
                    HttpUtil.sendOkHttpPostRequest(newChildMapJson, address, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateActivity.this, "对不起，处理失败，我们会尽快修复。", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d(TAG, "onResponse: addBill" + responseData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    });
                    //关闭这个activity，返回列表刷新列表。
                    Intent homeFragmentIntent = new Intent(CreateActivity.this, HomeActivity.class);
                    startActivity(homeFragmentIntent);
                } else {
//                    if (createCommentText.equals("")) {
//                        Toast.makeText(CreateActivity.this, "请输入评论", Toast.LENGTH_SHORT).show();
//                    } else
                    if (createAmountText.equals("")) {
                        Toast.makeText(CreateActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                    }else if (createTimeText.equals("")) {
                        Toast.makeText(CreateActivity.this, "请输入时间", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }

    public void getCategorySummary() {

        Log.d(TAG, "getCategorySummary: ");
        String address = Constants.SERVER_PREFIX + "v1/esc/categories";
        HttpUtil.sendOkHttpGetRequest(address, new Callback() {
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
                        if (responseData != null) {
                            Gson gson = new Gson();
                            JsonObject responseJsonDate = gson.fromJson(responseData, JsonObject.class);
                            String statusCode = gson.fromJson(responseJsonDate.get("statusCode"), new TypeToken<String>() {
                            }.getType());
                            if ("200".equals(statusCode)) {
                                Log.d(TAG, "categorysummary: " + responseJsonDate.get("data"));
                                categorySummaryEntity = gson.fromJson(responseJsonDate.get("data"), CategorySummaryEntity.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("categorysummary", String.valueOf(responseJsonDate.get("data")));
                                editor.apply();
                            }
                            clickCategoryBtn(outcomeCategoryList, "out", createHeaderIncome, createHeaderOutcome, null);

                            //获取intent的参数，判断是更改还是创建
                            if(null != childBill){
                                //显示图标

                                //显示页面数据
                                createTime.setText(childBill.getCreateDay().replace("/","-"));
                                if("in".equals(childBill.getType())){
                                    clickCategoryBtn(incomeCategoryList,"in",createHeaderOutcome,createHeaderIncome,null);
                                    createAmount.setText(childBill.getAmount());
                                }else{
                                    clickCategoryBtn(outcomeCategoryList,"out",createHeaderIncome,createHeaderOutcome,null);
                                    createAmount.setText(childBill.getAmount().replace("-",""));
                                }
                                showCreateBodyView();
                            }

                        }

                    }
                });

            }
        });
    }

    public void setCategoryAdapter(List<Category> categoryList, ChildBill childBill) {
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this); //线性
        //瀑布流
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        categorySummary.setLayoutManager(layoutManager);
        categorySummary.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryList, childBill);
        categorySummary.setAdapter(categoryAdapter);
    }

    public List<Category> getInOrOutCategory(CategorySummaryEntity categorySummaryEntity, String type) {
        if ("in".equals(type)) {
            return categorySummaryEntity.getIn();
        } else {
            return categorySummaryEntity.getOut();
        }
    }

    @SuppressLint("ResourceAsColor")
    public void clickCategoryBtn(List<Category> categoryList, String type, TextView preTextBtn, TextView thisTexBtn, ChildBill childBill) {
        Resources resources = this.getResources();
        //恢复其他的背景颜色
        Drawable uncheckedDrawable = resources.getDrawable(R.drawable.income_unchecked_bg);
        preTextBtn.setBackgroundDrawable(uncheckedDrawable);
        preTextBtn.setTextColor(R.color.colorPrimary);
        //改变背景颜色
        Drawable checkedDrawable = resources.getDrawable(R.drawable.income_checked_bg);
        thisTexBtn.setBackgroundDrawable(checkedDrawable);
        thisTexBtn.setTextColor(R.color.colorPrimary);

        //隐藏页脚的view
        hideCreateBodyView();

        //渲染数据
        categoryList = getInOrOutCategory(categorySummaryEntity, type);
        setCategoryAdapter(categoryList, childBill);
        this.preCategoryTexBtn = thisTexBtn;
    }

    //显示隐藏页下脚的createBodyView
    public static void showCreateBodyView() {
        createBodyView.setVisibility(View.VISIBLE);
    }

    public static void hideCreateBodyView() {
        createBodyView.setVisibility(View.GONE);
    }

    public void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        // 获取年月日时分秒的信息
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;        // 注意点，一月是从0开始计算的！！！
        day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                createTime.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
                Toast.makeText(CreateActivity.this, year + "/" + (monthOfYear + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
//                refreshCharts(year, monthOfYear + 1, dayOfMonth);
            }
        }, year, month - 1, day);
        datePickerDialog.show();
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        createTime.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                createTime.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }
}
