package com.weiho.scaffold.common.util;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * UtilityClass注解：为该类中的每个方法添加关键字static,添加一个私有构造方法,使该类不能被实例化
 */
@UtilityClass
public class DateUtils extends DateUtil {
    @Getter
    @AllArgsConstructor
    public enum FormatEnum {
        /**
         * 年份 [yyyy] 格式
         */
        YYYY("yyyy"),

        /**
         * 年份-月份 [yyyy-MM] 格式
         */
        YYYY_MM("yyyy-MM"),

        /**
         * 年份-月份-日 [yyyy-MM-dd] 格式
         */
        YYYY_MM_DD("yyyy-MM-dd"),

        /**
         * 年份月份日 [yyyyMMdd] 格式
         */
        YYYYMMDD("yyyyMMdd"),

        /**
         * 年份-月份-日 时:分:秒 [yyyy-MM-dd HH:mm:ss] 格式
         */
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss");

        private final String format;
    }

    /**
     * 获取服务器启动时间
     *
     * @return Date
     */
    public Date getServerStartDate() {
        return new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
    }

    /**
     * 返回当前时间 [Date 类型]
     *
     * @return Date
     */
    public Date getNowDate() {
        return date();
    }

    /**
     * 返回当前时间的 [yyyy-MM-dd HH:mm:ss] 时间格式
     *
     * @param formatEnum 枚举类中指定格式
     * @return String
     */
    public String getNowDateFormat(FormatEnum formatEnum) {
        return parseDateToStr(formatEnum, getNowDate());
    }

    /**
     * 转换指定的时间[Date]到指定的格式
     *
     * @param format 格式
     * @param date   时间
     * @return String
     */
    public String parseDateToStr(FormatEnum format, Date date) {
        return format(date, new SimpleDateFormat(format.getFormat()));
    }

    /**
     * Timestamp转Date
     *
     * @param timestamp 时间的Timestamp类型
     * @return Timestamp
     */
    public Date parseTimestampToDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    /**
     * 日期型字符串转化为日期 格式
     *
     * @param str Object
     * @return Date
     */
    public Date parseStringToDate(String str) {
        return parse(str);
    }

    /**
     * 计算两个时间差
     *
     * @param nowDate    现在时间
     * @param targetDate 目标时间
     * @return String
     */
    public String getDatePoor(Date nowDate, Date targetDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = nowDate.getTime() - targetDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
    }

    /**
     * 获取当前日期是星期几
     *
     * @return String
     */
    public String getWeekDay() {
        return thisDayOfWeekEnum().name();
    }

    /**
     * 获取当前日期是星期几
     * 指定前缀(星期XXX，周XXX)
     *
     * @return String
     */
    public String getWeekDay(String prefix) {
        return thisDayOfWeekEnum().toChinese(prefix);
    }

    /**
     * 获取某个日期几天(前/后)的日期(正数是往后，负数是往前)
     *
     * @param date 指定的日期
     * @param day  n天(前/后)
     * @return 结果日期
     */
    public Date getDateScope(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 获取某个日期几天(前/后)的日期(正数是往后，负数是往前)
     *
     * @param timestamp 指定的日期(时间戳)
     * @param day       n天(前/后)
     * @return 结果日期
     */
    public Date getDateScope(Timestamp timestamp, int day) {
        return getDateScope(parseTimestampToDate(timestamp), day);
    }
}
