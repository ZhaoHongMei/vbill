package com.example.vbill.customizeUI;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.vbill.R;
import com.example.vbill.bean.Point;

import java.util.List;

/**
 * TODO 因为行高和间距都是固定的，所以当统计条数改变时，请在布局文件中设置好高度
 * 水平进度条
 * Created by cuihuihui on 2017/5/5.
 */

public class ItemColumnView extends View {
    /**
     * 最大值
     */
    private float maxValue;
    /**
     * 统计项目
     */
    private List<Point> pointList;

    /**
     * 线的宽度
     */
    private int lineStrokeWidth;
    /**
     * 统计条宽度
     */
    private int barWidth;

    /**
     * 两条统计图之间空间
     */
    private int barSpace;

    /**
     * 各画笔
     */
    private Paint barPaint, linePaint, textPaint, amountTextPaint;

    /**
     * 矩形区域
     */
    private Rect barRect, topRect;

    private Path textPath;

    private int itemNameWidth;

    private int amountTextHeight;

    /**
     * 项目名和条形图之间的距离
     */
    private int betweenMargin;

    public static final int COLOR_GREEN = Color.parseColor("#018577");
    public static final int COLOR_GREEN2 = Color.parseColor("#66b29d");
    public static final int COLOR_GREEN3 = Color.parseColor("#81beab");
    public static final int COLOR_GREEN4 = Color.parseColor("#bedfd1");
    public static final int COLOR_GREEN5 = Color.parseColor("#cae1cb");
    public static final int[] COLORS = new int[]{COLOR_GREEN, COLOR_GREEN2, COLOR_GREEN3, COLOR_GREEN4, COLOR_GREEN5};
    public ItemColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化设置
     */
    private void init(Context context) {
        barPaint = new Paint();
        barPaint.setColor(getResources().getColor(R.color.colorPrimary));

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.grey));
        textPaint.setTextSize(36);
        textPaint.setAntiAlias(true);

        barRect = new Rect(0, 0, 0, 0);
        textPath = new Path();

        barWidth = 40;
        barSpace = 40;
        amountTextHeight = 26;
        itemNameWidth = 100;
        betweenMargin = amountTextHeight / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        if (pointList == null) {
            return;
        }
        for (int i = 0; i < pointList.size(); i++) {
            barPaint.setColor(COLORS[i]);
            barRect.left = 10;
            barRect.top = barSpace * (i + 1) + barWidth * i;
            barRect.right = 50;
            barRect.bottom = barRect.top + barWidth;

            canvas.drawRect(barRect, barPaint);
            canvas.drawText(pointList.get(i).getName() + "", barRect.right + 20, barRect.bottom - (barWidth - amountTextHeight), textPaint);
        }
    }


    /**
     * 设置统计项目列表
     *
     * @param pointList
     */
    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
        if (pointList == null) {
            throw new RuntimeException("BarChartView.setItems(): the param items cannot be null.");
        }
        if (pointList.size() == 0) {
            return;
        }
        maxValue = pointList.get(0).getValue();
        for (Point point : pointList) {
            if (point.getValue() > maxValue) {
                maxValue = point.getValue();
            }
        }
        invalidate();

    }

    //根据xml的设定获取宽度
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {

        }
        Log.i("这个控件的宽度----------", "specMode=" + specMode + " specSize=" + specSize);

        return specSize;
    }

    //根据xml的设定获取高度
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {

        }
        Log.i("这个控件的高度----------", "specMode:" + specMode + " specSize:" + specSize);

        return specSize;
    }
}

