package com.emdoor.yispace.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    // 将日期格式化为 "几年几月几日 周几 时区 几点几分几秒" 格式
    public static String formatDateTime(String dateTimeStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = inputFormat.parse(dateTimeStr);

            int year = date.getYear() + 1900;
            int month = date.getMonth() + 1;
            int day = date.getDate();
            String dayOfWeek = getDayOfWeek(date);
            int hours = date.getHours();
            int minutes = date.getMinutes();
            int seconds = date.getSeconds();

            return String.format("%d年%d月%d日 周%s %02d:%02d:%02d", year, month, day, dayOfWeek, hours, minutes, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String kbToMb(long kb) {
        double mb = (double) kb / 1000000;
        return String.format("%.1f", mb);
    }

    public static String getLatitude(double latitude) {
        if (latitude > 0) {
            return String.format("%.2fN", latitude);
        } else {
            return String.format("%.2fS", Math.abs(latitude));
        }
    }

    public static String getLongitude(double longitude) {
        if (longitude > 0) {
            return String.format("%.2fE", longitude);
        } else {
            return String.format("%.2fW", Math.abs(longitude));
        }
    }

    // 获取日期是星期几
    public static String getDayOfWeek(Date date) {
        String[] days = {"日", "一", "二", "三", "四", "五", "六"};
        return days[date.getDay()];
    }

    public static String getAltitude(double altitude) {
        return String.format("%.2fm", altitude);
    }

    // 获取时区
    public static String getTimezone() {
        TimeZone timeZone = TimeZone.getDefault();
        int offset = timeZone.getRawOffset() / (60 * 1000); // in minutes
        int hours = Math.abs(offset) / 60;
        int minutes = Math.abs(offset) % 60;
        String sign = offset < 0 ? "+" : "-";

        return String.format("UTC%s%02d:%02d", sign, hours, minutes);
    }
}
