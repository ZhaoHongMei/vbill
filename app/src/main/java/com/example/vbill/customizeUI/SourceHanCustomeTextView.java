package com.example.vbill.customizeUI;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class SourceHanCustomeTextView extends AppCompatTextView {

    public SourceHanCustomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写设置字体方法
    @Override
    public void setTypeface(Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "font/sourcehansanscnmedium.otf");
        super.setTypeface(tf);
    }
}