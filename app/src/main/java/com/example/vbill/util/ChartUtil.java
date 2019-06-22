package com.example.vbill.util;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbill.R;
import com.example.vbill.bean.ChartVO;
import com.example.vbill.bean.Point;
import com.example.vbill.customizeUI.HorizontalChartView;
import com.example.vbill.home.HomeActivity;
import com.example.vbill.home.details.HomeChartFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChartUtil {
    private static final String TAG = "ChartUtil";
    public static final int DEFAULT_COLOR = Color.parseColor("#DFDFDF");
    public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");
    public static final int COLOR_GREEN = Color.parseColor("#018577");
    public static final int COLOR_GREEN2 = Color.parseColor("#66b29d");
    public static final int COLOR_GREEN3 = Color.parseColor("#81beab");
    public static final int COLOR_GREEN4 = Color.parseColor("#bedfd1");
    public static final int COLOR_GREEN5 = Color.parseColor("#cae1cb");
    public static final int[] COLORS=new int[]{COLOR_GREEN, COLOR_GREEN2, COLOR_GREEN3, COLOR_GREEN4, COLOR_GREEN5};

    public static void generateCharts(
            View view,
            int dateType,
            int dateNumber,
            String customerId,
            String accountType,
            HomeChartFragment homeChartFragment,
            LineChartView lineChart,
            PieChartView pieChart,
            TextView noDataView,
            TextView totalSummaryValueView) {

        Map dateMap = DateUtil.getDateMap();
        String url = Constants.SERVER_PREFIX + "v1/esc/chartItems?customerId=" + customerId + "&number=" + dateNumber + "&accountType=" + accountType + "&dateType=" + dateMap.get(dateType);
        HttpUtil.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                homeChartFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(homeChartFragment.getContext(), "对不起，获取数据失败，请稍后重试。", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: " + responseData);
                ChartVO chartVo = generateResponseToChartVO(responseData);
                homeChartFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        List<Point> linePoints = chartVo.getLinePoints();
                        List<Point> piePoints = chartVo.getPiePoints();
                        List<Point> columnPoints = chartVo.getColumnPoints();
                        String totalAmount = chartVo.getTotalAmount();
                        LinearLayout rankingLayout = view.findViewById(R.id.ranking_layout);
                        if (piePoints != null && piePoints.size() > 0) {
                            lineChart.setVisibility(View.VISIBLE);
                            pieChart.setVisibility(View.VISIBLE);
                            noDataView.setVisibility(View.GONE);
                            generateLineChart(linePoints, lineChart);
                            generatePieChart(piePoints, pieChart);
                            HorizontalChartView columnChart = (HorizontalChartView) view.findViewById(R.id.ranking_list);
//                            List<Point> barList = ChartUtil.generateMockPiePoints();
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, columnPoints.size() * 76 + 10);
                            columnChart.setLayoutParams(params);
                            columnChart.setPointList(columnPoints);
                            rankingLayout.setVisibility(View.VISIBLE);
                            Log.d(TAG, "run: " + piePoints);
                            totalSummaryValueView.setText(totalAmount);
                        } else {
                            lineChart.setVisibility(View.GONE);
                            pieChart.setVisibility(View.GONE);
                            noDataView.setVisibility(View.VISIBLE);
                            totalSummaryValueView.setText("0");
                            rankingLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private static ChartVO generateResponseToChartVO(String response) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        ChartVO chartVO = gson.fromJson(jsonObject.get("data"), ChartVO.class);
        if (chartVO == null) {
            chartVO = new ChartVO();
        }
        return chartVO;
    }

    /**
     * 绘制折线图的方法
     */
    public static void generateLineChart(List<Point> points, LineChartView lineChart) {
        int size = points == null ? 0 : points.size();
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            AxisValue axisValue = new AxisValue(i);
            axisValue.setLabel(points.get(i).getName());
            axisValues.add(axisValue);
        }
        Axis axisx = new Axis();
        Axis axisy = new Axis();
        axisx.setTextColor(Color.BLACK)
                .setName("时间")
                .setValues(axisValues);
        axisy.setTextColor(Color.BLACK)
//                .setName("金额")
                .setHasLines(true)
                .setMaxLabelChars(5);
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(new PointValue(i, points.get(i).getValue()));
        }
        List<Line> lines = new ArrayList<>();       //绘制的第一条线
        Line line = new Line(values)
                .setColor(Color.parseColor("#008577"))
                .setHasLabelsOnlyForSelected(true)
                .setHasPoints(true)
                .setStrokeWidth(2)
                .setShape(ValueShape.CIRCLE)
                .setPointRadius(3);
        lines.add(line);
        LineChartData lineData = new LineChartData();
        lineData.setLines(lines);
        lineData.setAxisYLeft(axisy);
        lineData.setAxisXBottom(axisx);
        lineChart.setLineChartData(lineData);
    }

    /**
     * 绘制饼状图的方法
     */
    public static void generatePieChart(List<Point> points, PieChartView pieChart) {
        int size = points == null ? 0 : points.size();
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < size; i++) {
            SliceValue sliceValue = new SliceValue(points.get(i).getValue(), COLORS[i]);
            sliceValue.setLabel(points.get(i).getName() + ":" + points.get(i).getValue());
            values.add(sliceValue);
        }
        PieChartData pieData = new PieChartData(values);
        pieData.setHasLabels(true);
        pieChart.setPieChartData(pieData);

    }

    /*
     * 绘制柱状图的方法
     * */
    public static void generateColumnChart(List<Point> points, HorizontalChartView columnChart) {
        columnChart.setPointList(points);
    }

    public static List<Point> generateMockLinePoints() {
        List<Point> points = new ArrayList<>();
        Point point1 = new Point("06-10", 100);
        Point point2 = new Point("06-11", 300);
        Point point3 = new Point("06-12", 900);
        Point point4 = new Point("06-13", 500);
        Point point5 = new Point("06-14", 300);
        Point point6 = new Point("06-15", 100);
        Point point7 = new Point("06-16", 400);

        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        points.add(point7);

        return points;
    }

    public static List<Point> generateMockPiePoints() {
        List<Point> points = new ArrayList<>();
        Point point1 = new Point("汽车", 100);
        Point point2 = new Point("书籍", 300);
        Point point3 = new Point("餐饮", 900);
        Point point4 = new Point("衣服", 500);
        Point point5 = new Point("孩子", 300);
        ;

        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);

        return points;
    }

}
