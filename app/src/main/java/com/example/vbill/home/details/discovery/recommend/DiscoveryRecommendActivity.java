package com.example.vbill.home.details.discovery.recommend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vbill.R;
import com.example.vbill.bean.discovery.recommend.DiscoveryRecommendResponse;
import com.example.vbill.bean.discovery.recommend.RecommendInfo;
import com.example.vbill.util.Utility;

import java.util.List;

public class DiscoveryRecommendActivity extends AppCompatActivity {

    private SharedPreferences recommendPref;
    private DiscoveryRecommendResponse discoveryRecommendResponse;
    TextView recommendTitleText;
    TextView recommendUpdateDate;
    ImageView recommendContentImage1;
    TextView recommendContent1;
    ImageView recommendContentImage2;
    TextView recommendContent2;
    private String cid;
    private BottomNavigationItemView thumbUpMenuItem;
    private BottomNavigationItemView commendMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_recommend);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        cid = intent.getStringExtra("recommendCId");

        recommendPref = getSharedPreferences("recommend", MODE_PRIVATE);
        String responseText = recommendPref.getString("recommendResponseString", null);
        discoveryRecommendResponse = Utility.handleDiscoveryRecommend(responseText);

        recommendTitleText = (TextView) findViewById(R.id.discovery_recommend_title);
        recommendUpdateDate = (TextView)findViewById(R.id.discovery_recommend_update_date);
        recommendContentImage1 = (ImageView) findViewById(R.id.discovery_recommend_content_img1);
        recommendContent1 = (TextView)findViewById(R.id.discovery_recommend_content1);
        recommendContentImage2 = (ImageView) findViewById(R.id.discovery_recommend_content_img2);
        recommendContent2 = (TextView)findViewById(R.id.discovery_recommend_content2);
        thumbUpMenuItem = (BottomNavigationItemView)findViewById(R.id.recommend_thumbUp);
        commendMenuItem = (BottomNavigationItemView)findViewById(R.id.recommend_comment);

        List<RecommendInfo> recommendInfos = discoveryRecommendResponse.getRecommendInfo();
        for (int i = 0; i < recommendInfos.size(); i++) {
            if(cid.equals(recommendInfos.get(i).getBasic().getCid())){
                recommendTitleText.setText(recommendInfos.get(i).getRecommendContent().getTitle());
                recommendUpdateDate.setText(recommendInfos.get(i).getBasic().getUpdateDate());
                Glide.with(DiscoveryRecommendActivity.this).load(recommendInfos.get(i).getRecommendContent().getImagePath()).into(recommendContentImage1);
                recommendContent1.setText(recommendInfos.get(i).getRecommendContent().getContent());
                Glide.with(DiscoveryRecommendActivity.this).load(recommendInfos.get(i).getRecommendContent().getImagePath()).into(recommendContentImage2);
                recommendContent2.setText(recommendInfos.get(i).getRecommendContent().getContent());
                thumbUpMenuItem.setTitle("赞 " + recommendInfos.get(i).getContentProperty().getThumbUpTimes());
                commendMenuItem.setTitle("评论 " + recommendInfos.get(i).getContentProperty().getCommentTimes());

            }
        }

    }
}
