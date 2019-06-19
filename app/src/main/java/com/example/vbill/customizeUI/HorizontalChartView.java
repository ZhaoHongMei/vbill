package com.example.vbill.customizeUI;


import android.content.Context;
import android.graphics.Canvas;
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

public class HorizontalChartView extends View {
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
    private RectF barRect, topRect;

    private Path textPath;

    private int itemNameWidth;

    private int amountTextHeight;

    /**
     * 项目名和条形图之间的距离
     */
    private int betweenMargin;


    public HorizontalChartView(Context context, AttributeSet attrs) {
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

        amountTextPaint = new Paint();
        amountTextPaint.setTextSize(36);
        amountTextPaint.setColor(getResources().getColor(R.color.black));

        barRect = new RectF(0, 0, 0, 0);
        textPath = new Path();

        barWidth = 36;
        barSpace = 40;
        amountTextHeight = 26;
        itemNameWidth = 100;
        betweenMargin = amountTextHeight / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float lineViewWidth = (float) ((this.getWidth() - itemNameWidth) * 0.8);//线的宽度占总宽度的0.8，剩余的部分显示数据
        float amountWidth = lineViewWidth / 5;
        int amountAdd = (int) (maxValue / 5);
        if (isInEditMode()) {
            return;
        }
        for (int i = 0; i < pointList.size(); i++) {
            barRect.left = itemNameWidth;
            barRect.top = barSpace * (i + 1) + barWidth * i;
            barRect.right = (int) ((lineViewWidth-5) * (pointList.get(i).getValue() / maxValue)+5) + itemNameWidth;
            barRect.bottom = barRect.top + barWidth;
            barPaint.setColor(getResources().getColor(R.color.colorPrimary));

            canvas.drawRoundRect(barRect,20,20, barPaint);
            canvas.drawText(pointList.get(i).getValue() + "", barRect.right+20, barRect.bottom - (barWidth - amountTextHeight), textPaint);
            canvas.drawText(pointList.get(i).getName(), itemNameWidth - betweenMargin - textPaint.measureText(pointList.get(i).getName()), barRect.bottom - (barWidth - amountTextHeight), textPaint);
//            canvas.drawText(String.valueOf(amountAdd * (i + 1)), itemNameWidth + amountWidth * (i + 1) - textPaint.measureText(String.valueOf(amountAdd * (i + 1))) / 2, barSpace, textPaint);
        }
//        canvas.drawText(String.valueOf(maxValue), itemNameWidth + lineViewWidth - textPaint.measureText(String.valueOf(maxValue)) / 2, barSpace, textPaint);
//        canvas.drawText("0", itemNameWidth - betweenMargin - textPaint.measureText("0"), barSpace, textPaint);
//        canvas.drawLine(itemNameWidth, 0, itemNameWidth, this.getHeight());
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

