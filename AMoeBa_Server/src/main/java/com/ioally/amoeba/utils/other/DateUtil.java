package com.ioally.amoeba.utils.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    public static final SimpleDateFormat sdf_en = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat sdf_zh = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

    /**
     * 年
     */
    public static final int YEAR = Calendar.YEAR;
    /**
     * 月
     */
    public static final int MONTH = Calendar.MONTH;
    /**
     * 日
     */
    public static final int DAY_OF_YEAR = Calendar.DAY_OF_YEAR;
    /**
     * 时
     */
    public static final int HOUR = Calendar.HOUR;
    /**
     * 分
     */
    public static final int MINUTE = Calendar.MINUTE;
    /**
     * 秒
     */
    public static final int SECOND = Calendar.SECOND;
    /**
     * 毫秒
     */
    public static final int MILLISECOND = Calendar.MILLISECOND;

    public static boolean isNowMonthHoliday = false;


    /**
     * 校验时间是否超出当前时间
     *
     * @param date    待校验时间
     * @param outTime 超时时长，单位/分钟
     * @return true超时
     */
    public static boolean isTimeout(Date date, int outTime) {
        Date nowDate = new Date();
        Date calculateDate = calculateDate(MINUTE, date, outTime);
        return nowDate.compareTo(calculateDate) > 0;
    }

    /**
     * 指定时间进行加法运算
     *
     * @param calculateType 要加的运算单位
     * @param date          指定时间
     * @param outTime       加数
     * @return 计算过后的时间
     */
    public static Date calculateDate(int calculateType, Date date, int outTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calculateType, outTime);
        return calendar.getTime();
    }

    /**
     * 判断日期是否时双休日
     *
     * @param date 待验证的日期
     * @return true-是双休日
     */
    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * 判断指定时间是否是上午
     *
     * @param time 指定时间
     * @return true-是上午
     */
    public static boolean isAM(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String format = simpleDateFormat.format(time);
        return format.compareTo("12:00:00") < 0;
    }

    /**
     * 返回当前时间的字符串
     *
     * @return 当前日期
     */
    public static String getNowDate() {
        return sdf_en.format(Calendar.getInstance().getTime());
    }

}
