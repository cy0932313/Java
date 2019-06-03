package com.chris.automated.trading.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:时间处理工具
 * @author: Chris.Y
 * @create: 2019-03-03 20:37
 **/
public class ChrisDateUtils {

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds   精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    public static String timeStamp2Date000(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date   字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    /**
     * 根据日期获得星期
     *
     * @param sDate yyyy-MM-dd
     * @return 当前日期是星期几
     * @author
     */
    public static int getFullDateWeekTime(String pTime) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date tmpDate = null;
        try {
            tmpDate = format.parse(pTime);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();

        int[] weekDays = {7, 1, 2, 3, 4,5, 6};

        try {

            cal.setTime(tmpDate);

        } catch (Exception e) {

            e.printStackTrace();

        }

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。

        if (w < 0)

            w = 0;

        return weekDays[w];
    }

    public static int compare_date(String DATE1, String DATE2,String formate) {
        if(formate == null)
        {
            formate = "yyyy-MM-dd hh:mm";
        }
        DateFormat df = new SimpleDateFormat(formate);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param strDate1
     * @param strDate2
     * @return
     */
    public static int differentDaysByMillisecond(String strDate1, String strDate2, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);

            Date date1 = format.parse(strDate1);
            Date date2 = format.parse(strDate2);

            int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));

            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}