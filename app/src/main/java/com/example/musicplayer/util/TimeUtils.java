package com.example.musicplayer.util;


import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : PengLiang
 * Time : 2019/8/26
 * Description : 时间转换工具类，将毫秒的时间转换成指定格式
 */
public class TimeUtils {
    /**
     * The constant DEFAULT_DATE_FORMAT.
     */
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "HH:mm:ss");
    /**
     * The constant DEFAULT_MIN_FORMAT.
     */
    public static final SimpleDateFormat DEFAULT_MIN_FORMAT = new SimpleDateFormat(
            "mm:ss");
    /**
     * The constant DATE_FORMAT_DATE.
     */
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy/MM/dd");
    /**
     * The constant DEL_FORMAT_DATE.
     */
    public static final SimpleDateFormat DEL_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * The constant INT_HOUR_FORMAT.
     */
    public static final SimpleDateFormat INT_HOUR_FORMAT = new SimpleDateFormat(
            "HH");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * Gets current hour in int.
     *
     * @param timeInMillis the time in millis
     * @param dateFormat   the date format
     * @return the current hour in int
     */
    public static int getCurrentHourInInt(long timeInMillis,
                                          SimpleDateFormat dateFormat) {
        String date = dateFormat.format(new Date(timeInMillis));
        int time = 0;
        if (!TextUtils.isEmpty(date)) {
            time = Integer.parseInt(date);
        }
        return time;
    }

    /**
     * Gets time.
     *
     * @param timeInMillis the time in millis
     * @param dateFormat   the date format
     * @return the time
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * Gets current date in long.
     *
     * @param timeInMillis the time in millis
     * @param dateFormat   the date format
     * @return the current date in long
     */
    public static long getCurrentDateInLong(long timeInMillis,
                                            SimpleDateFormat dateFormat) {
        String date = dateFormat.format(new Date(timeInMillis));
        long time = 0;
        if (!TextUtils.isEmpty(date)) {
            time = Long.parseLong(date);
        }
        return time;
    }

    /**
     * Gets time.
     *
     * @param timeInMillis the time in millis
     * @return the time
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * Gets current time in long.
     *
     * @return the current time in long
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * Gets current time in string.
     *
     * @return the current time in string
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * Gets current time in string.
     *
     * @param dateFormat the date format
     * @return the current time in string
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
}