package com.example.vbill.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import com.example.vbill.bean.discovery.recommend.DiscoveryRecommendResponse;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static DiscoveryRecommendResponse handleDiscoveryRecommend(String response){

        try{
            JSONObject jsonObject = new JSONObject(response);
//                JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
//                String weatherContent = jsonArray.getJSONObject(0).toString();
            String discoveryRecommendResponse = jsonObject.get("discoveryRecommendResponse").toString();


            return new Gson().fromJson(discoveryRecommendResponse, DiscoveryRecommendResponse.class);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
