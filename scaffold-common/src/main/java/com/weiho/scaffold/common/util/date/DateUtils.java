package com.weiho.scaffold.common.util.date;

import lombok.experimental.UtilityClass;

import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * UtilityClass注解：为该类中的每个方法添加关键字static,添加一个私有构造方法,使该类不能被实例化
 *
 * @author <a href="https://gitee.com/guchengwuyue/yshopmall">参考链接</a>
 */
@UtilityClass
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * 列举可能出现的格式方式
     */
    private static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "yyyy^MM^dd", "yyyy^MM^dd HH:mm:ss", "yyyy^MM^dd HH:mm", "yyyy^MM"
    };

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
        return new Date();
    }

    /**
     * 返回当前时间的 [yyyy-MM-dd HH:mm:ss] 时间格式
     *
     * @return String
     */
    public String getNowDateToStr() {
        return parseDateToStr(FormatEnum.YYYY_MM_DD_HH_MM_SS, new Date());
    }

    /**
     * 返回当前时间的 [yyyy-MM-dd HH:mm:ss] 时间格式
     *
     * @param formatEnum 枚举类中指定格式
     * @return String
     */
    public String getNowDateFormat(FormatEnum formatEnum) {
        return parseDateToStr(formatEnum, new Date());
    }

    /**
     * 返回当前时间 [Timestamp 类型]
     *
     * @return Timestamp
     */
    public Timestamp getNowTimestamp() {
        //获取系统当前时间
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 返回当前时间的 [yyyy-MM-dd HH:mm:ss] 时间格式
     *
     * @return String
     */
    public String getNowTimestampToStr() {
        return parseTimestampToStr(FormatEnum.YYYY_MM_DD_HH_MM_SS, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 返回当前时间的 [yyyy-MM-dd HH:mm:ss] 时间格式
     *
     * @param formatEnum 枚举类中指定格式
     * @return String
     */
    public String getNowTimestampFormat(FormatEnum formatEnum) {
        return parseTimestampToStr(formatEnum, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 转换指定的时间[Date]到指定的格式
     *
     * @param format 格式
     * @param date   时间
     * @return String
     */
    public String parseDateToStr(FormatEnum format, Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format.getFormat()).format(date);
    }

    /**
     * 转换指定的时间[Timestamp]到指定的格式
     *
     * @param format    格式
     * @param timestamp 时间
     * @return String
     */
    public String parseTimestampToStr(FormatEnum format, Timestamp timestamp) {
        //获取系统当前时间
        Timestamp time = new Timestamp(timestamp.getTime());
        SimpleDateFormat df = new SimpleDateFormat(format.getFormat());
        return df.format(time);
    }

    /**
     * Date转Timestamp
     *
     * @param date 时间的Date类型
     * @return Timestamp
     */
    public Timestamp parseDateToTimestamp(Date date) {
        return new Timestamp(date.getTime());
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
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str, PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
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
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
