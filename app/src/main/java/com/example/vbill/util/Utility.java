package com.example.vbill.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.example.vbill.R;

import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
