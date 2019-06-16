package com.example.vbill.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.vbill.bean.DateItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtil {
    private static final String TAG = "DateUtil";

    public static int getCurrentNumber(int dateType) {
        int number = 1;
        switch (dateType) {
            case Constants.DATE_TYPE_WEEK:
                number = getCurrentWeek();
                break;
            case Constants.DATE_TYPE_MONTH:
                number = getCurrentMonth();
                break;
            default:
                break;
        }
        return number;
    }

    public static List<DateItem> getDateItems(int dateType) {
        List<DateItem> dateItems = new ArrayList<DateItem>();
        switch (dateType) {
            case Constants.DATE_TYPE_WEEK:
                dateItems = getWeeks();
                break;
            case Constants.DATE_TYPE_MONTH:
                dateItems = getMonths();
                break;
            case Constants.DATE_TYPE_YEAR:
                dateItems = getYears();
                break;
            default:
                break;
        }
        return dateItems;
    }

    public static List<DateItem> getWeeks() {
        List<DateItem> weeks = new ArrayList<DateItem>();
        int currentWeek = getCurrentWeek();
        if (currentWeek > 2) {
            for (int i = 1; i < currentWeek - 1; i++) {
                weeks.add(new DateItem(i, i + "周"));
            }
            weeks.add(new DateItem(currentWeek - 1, "上周"));
            weeks.add(new DateItem(currentWeek, "本周"));
        } else {
            weeks.add(new DateItem(currentWeek, "本周"));
        }
        return weeks;
    }

    public static List<DateItem> getMonths() {
        List<DateItem> months = new ArrayList<DateItem>();
        int currentMonth = getCurrentMonth();
        if (currentMonth > 2) {
            for (int i = 1; i < currentMonth - 1; i++) {
                months.add(new DateItem(i, i + "月"));
            }
            months.add(new DateItem(currentMonth - 1, "上月"));
            months.add(new DateItem(currentMonth, "本月"));
        } else {
            months.add(new DateItem(currentMonth, "本月"));
        }
        return months;
    }

    public static List<DateItem> getYears() {
        List<DateItem> years = new ArrayList<DateItem>();
        years.add(new DateItem(1, "本年"));
        return years;
    }

    public static int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        cal.setTime(new Date());
        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        Log.d(TAG, "getCurrentWeek: " + currentWeek);
        return currentWeek;
    }

    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int currentMonth = cal.get(Calendar.MONTH)+1;
        Log.d(TAG, "getCurrentMonth: " + currentMonth);
        return currentMonth;
    }

    public static Map<Integer, String> getDateMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(Constants.DATE_TYPE_WEEK, Constants.DATE_TYPE_WEEK_STR);
        map.put(Constants.DATE_TYPE_MONTH, Constants.DATE_TYPE_MONTH_STR);
        map.put(Constants.DATE_TYPE_YEAR, Constants.DATE_TYPE_YEAR_STR);
        return map;
    }

}
