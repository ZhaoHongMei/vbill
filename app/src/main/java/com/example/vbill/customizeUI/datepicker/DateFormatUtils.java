package com.example.vbill.customizeUI.datepicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 说明：日期格式化工具
 */
public class DateFormatUtils {

    private static final String DATE_FORMAT_PATTERN_YM = "yyyy-MM";
    private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
    private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";

    /**
     * 时间戳转字符串
     *
     * @param timestamp     时间戳
     * @param isPreciseTime 是否包含时分
     * @return 格式化的日期字符串
     */
    public static String long2Str(long timestamp, boolean isPreciseTime) {
        return long2Str(timestamp, getFormatPattern(isPreciseTime));
    }
    //年月
    public static String long2Str(long timestamp, int showOption) {
        return long2Str(timestamp, getFormatPattern(showOption));
    }

    private static String long2Str(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr       日期字符串
     * @param isPreciseTime 是否包含日时分
     * @return 时间戳
     */
    public static long str2Long(String dateStr,boolean isPreciseTime) {
        return str2Long(dateStr, getFormatPattern(isPreciseTime));
    }
    //年月
    public static long str2Long(String dateStr,int showOption) {
        return str2Long(dateStr, getFormatPattern(showOption));
    }
    private static long str2Long(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static String getFormatPattern(boolean showSpecificTime) {
        if (showSpecificTime) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else {
            return DATE_FORMAT_PATTERN_YMD;
        }
    }
    //只显示年月
    private static String getFormatPattern(int showOption) {
        if (showOption == 3) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else  if(showOption == 2){
            return DATE_FORMAT_PATTERN_YMD;
        }else{
            return DATE_FORMAT_PATTERN_YM;
        }
    }
}