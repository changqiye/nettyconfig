package com.example.demo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具
 *
 * @author lisong
 */
public class DateUtil {

    /**
     * 日期格式
     **/
    public static final String DATE_PATTERN_HH_MM = "HH:mm";
    public static final String DATE_PATTERN_HH_MM_SS = "HH:mm:ss";
    public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS_1 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATE_PATTERN_MM_DD_HH_MM_CN = "MM月dd日 HH:mm";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_CN = "yyyy年MM月dd日 HH:mm";
    public static final String DATE_PATTERN_YYYY_MM_DD_1 = "yyyyMMdd";
    public static final String DATE_PATTERN_YYYY_MM = "yyyyMM";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_1 = "yyyyMMddHHmm";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH = "yyyyMMddHH";
    public static final String DATE_PATTERN_YYYY_MM_DD_CMD = "yyyy年MM月dd日";
    public static final String DATE_PATTERN_YYYY_MM_DD_2 = "yyyy/MM/dd";
    /**
     * 2015-05-20T13:29:35+02:00
     */
    public static final String DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ssX";

    /**
     * 日期格式通用转换
     **/
    private static String[] timeFormatList = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "yyyy年MM月dd日 HH:mm:ss", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm", "yyyy年MM月dd日 HH:mm",
            "yyyyMMddHHmm", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy年MM月dd日", "yyyyMMdd", "yyyy.MM.dd"};

    public static String date2String(Date date) {
        return date2String(date, DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    public static String date2String(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static Date string2Date(String dateString) {
        return string2Date(dateString, DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    public static Date string2Date(String dateString, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        return format.parse(dateString, pos);
    }

    public static Date string2Date(String dateString, String pattern, Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
        ParsePosition pos = new ParsePosition(0);
        return format.parse(dateString, pos);
    }

    public static Date getMonthStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getMonthEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public static Date getDayStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDayEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public static Date addYear(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, amount);
        return cal.getTime();
    }

    public static Date addMonth(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, amount);
        return cal.getTime();
    }

    public static Date addDay(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }

    public static Date addHour(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, amount);
        return cal.getTime();
    }

    public static Date addMinute(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, amount);
        return cal.getTime();
    }

    public static Date addSecond(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, amount);
        return cal.getTime();
    }

    /**
     * 日期比较
     *
     * @param startDate
     * @param compDate
     * @param endDate
     * @return
     */
    public static boolean compareDate(Date startDate, Date compDate, Date endDate) {
        boolean flag = false;
        if (compare_date(compDate, startDate) >= 0 && compare_date(compDate, endDate) <= 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 日期比较
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compare_date(Date date1, Date date2) {
        try {
            if (date1.getTime() > date2.getTime()) {
                return 1;
            } else if (date1.getTime() < date2.getTime()) {
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
     * 获取日期是周几
     *
     * @param date 0：周日，1-6：周一至周六
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取时间的年部分
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取时间的月部分
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取时间的日部分
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取时间的小时部分
     *
     * @param date
     * @return
     */
    public static int getHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取时间的小时部分
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * 获取时间的小时部分
     *
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.SECOND);
    }

    /**
     * utc时间转换（形如Date(456123456142+0800)）
     *
     * @param utcDate
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date convertUtcDate(String utcDate) {
        try {
            if (utcDate.toLowerCase().contains("date")) {
                utcDate = utcDate.substring(utcDate.indexOf("(") + 1, utcDate.lastIndexOf("+"));
            }
            return new Date(Long.valueOf(utcDate));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取两个日期之间的分钟数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getSecondsBetweenTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / 1000;
    }

    /**
     * 转换日期格式(支持N种格式)
     *
     * @return
     */
    public static Date stringToDate(String dateString) {
        SimpleDateFormat format = null;
        for (String str : timeFormatList) {
            format = new SimpleDateFormat(str);
            try {
                Date date = format.parse(dateString);
                return date;
            } catch (ParseException e) {
                continue;
            }
        }
        return null;
    }

    /**
     * 时间字符匹配yyyy-MM-dd
     *
     * @param str
     * @return
     */
    public static Boolean DataPartten(String str) {
        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        boolean dateFlag = m.matches();
        if (!dateFlag) {
            System.out.println("格式错误");
            return false;
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false);
        try {
            Date date = formatter.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> getEverdayBetweenTwoDate(Date before, Date after) {
        List<String> result = new ArrayList<String>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(before);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(after);
        result.add(DateUtil.date2String(before, "yyyy-MM-dd"));
        while (tempStart.before(tempEnd)) {
            result.add(DateUtil.date2String(tempStart.getTime(), "yyyy-MM-dd"));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        if (before.before(after)) {
            result.add(DateUtil.date2String(after, "yyyy-MM-dd"));
        }

        return result;
    }

    /**
     * 获取当天（今日）零点零分零秒
     *
     * @return
     */
    public static Date today() {
        return getDate(new Date());
    }

    /**
     * 获取日期Date  零点零分零秒
     *
     * @param date
     * @return
     */
    public static Date getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date day = calendar.getTime();
        return day;
    }

    /**
     * 获取当月最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        return cal.getTime();
    }

    /**
     * 只比较日期，不管时间
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static int compareDate(Date dt1, Date dt2) {
        return getDate(dt1).compareTo(getDate(dt2));
    }
}
