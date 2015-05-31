/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LegacyTimeUtils {

    // General functions

    public static Calendar getNow() {
        Calendar now = Calendar.getInstance();
        now.setLenient(false);
        return now;
    }

    public static int compare(Calendar time1, Calendar time2) {
        return time1.compareTo(time2);
    }

    public static boolean isSame(Calendar time1, Calendar time2) {
        return compare(time1, time2) == 0;
    }

    // Date related functions

    public static Calendar getDate() {
        Calendar date = getNow();
        clearTime(date);
        return date;
    }

    public static void setDate(Calendar date, int month, int day) {
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
    }

    public static void setDate(Calendar date, int year, int month, int day) {
        date.set(Calendar.YEAR, year);
        setDate(date, month, day);
    }

    public static void setDate(Calendar date, Calendar newDate) {
        date.set(Calendar.ERA, newDate.get(Calendar.ERA));
        date.set(Calendar.YEAR, newDate.get(Calendar.YEAR));
        date.set(Calendar.DAY_OF_YEAR, newDate.get(Calendar.DAY_OF_YEAR));
    }

    public static void clearDate(Calendar time) {
        time.clear(Calendar.ERA);
        time.clear(Calendar.YEAR);
        time.clear(Calendar.MONTH);
        time.clear(Calendar.DAY_OF_MONTH);
    }

/*
    public static boolean isSameDate(Calendar date1, Calendar date2) {
        return date1.get(Calendar.ERA) == date2.get(Calendar.ERA) &&
                date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.YEAR);

        date1 = (Calendar) date1.clone();
        date2 = (Calendar) date2.clone();
        clearTime(date1);
        clearTime(date2);

        return date1.compareTo(date2) == 0;
    }
*/

    public static boolean isToday(Calendar date) {
        date = (Calendar) date.clone();
        clearTime(date);
        return isSame(date, getDate());
    }

    // Time related functions

    public static Calendar getTime() {
        Calendar time = getNow();
        clearTime(time);
        return time;
    }

    public static void setTime(Calendar time, int hour, int minute, int second, int millisecond) {
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, minute);
        time.set(Calendar.SECOND, second);
        time.set(Calendar.MILLISECOND, millisecond);
    }

    public static void setTime(Calendar time, int hour, int minute, int second) {
        setTime(time, hour, minute, second, 0);
    }

    public static void setTime(Calendar time, int hour, int minute) {
        setTime(time, hour, minute, 0, 0);
    }

    public static void setTime(Calendar time, Calendar newTime) {
        setTime(time, newTime.get(Calendar.HOUR_OF_DAY),
                newTime.get(Calendar.MINUTE),
                newTime.get(Calendar.SECOND),
                newTime.get(Calendar.MILLISECOND));
    }

    // NOTE: Calendar.clear() on HOUR_OF_DAY, HOUR or AM_PM doesn't reset the hour of day value of
    // this Calendar.
    public static void clearTime(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.clear(Calendar.MINUTE);
        date.clear(Calendar.SECOND);
        date.clear(Calendar.MILLISECOND);
    }

    // String related functions

    @SuppressLint("SimpleDateFormat")
    public static String getString(Calendar time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(time.getTime());
    }

    //private static final String[] WEEKDAY_NAMES =
    //        new String[] {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String getWeekdayString(Calendar date) {
        return getString(date, "EE");
        //return WEEKDAY_NAMES[date.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getHourMinuteString(Calendar time) {
        return getString(time, "HH:mm");
    }

    public static String getNormalString(Calendar time) {
        return getString(time, "yyyy-M-d HH:mm:ss:SSSS");
    }

    /*
    // This function includes the left boundary while excludes the right boundary of the date range.
    public static Boolean inRange(Calendar time, Calendar start, Calendar end) {
        return !time.before(start) && time.before(end);
    }
    */


    private LegacyTimeUtils() {}
}
