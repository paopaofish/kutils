package com.github.tky.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String DATE_YYYY_MM = "yyyy-MM";
    public static final String DATE_YYYYMM = "yyyyMM";
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_HH_MM_SS = "HH:mm:ss";
    public static final String DATE_HHMMSS = "HHmmss";
    public static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_YYYY_MM_DD_HH_MM_SS_S = "yyyy-MM-dd HH:mm:ss.S";
    
    /**
     * 将一个字符串的日期按照指定的pattern转换成Date对象。
     * 
     * @param dateStr
     *            字符串的日期
     * @param pattern
     * @return Date
     */
    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.equals("")) {
            return null;
        }
        if (pattern == null || pattern.equals("")) {
            pattern = DATE_YYYY_MM_DD;
        }
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取N周前的周一
     * @param week 几周前
     * @return
     */
    public static String firstDayOfWeek(int week){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
        // 取得本周一
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        calendar.set(Calendar.WEEK_OF_YEAR, iWeekNum - week);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sf.format(calendar.getTime());
    }
    
    
    /**
     *  计算时间间隔（返回：*年*个月）
     *  @param start 开始时间
     *  @param end  结束时间
     *  @throws ParseException 
     * 
     */
    public static String timeBetween(Date start, Date end)  {
        long between = (end.getTime() - start.getTime()) / 1000;// 除以1000是为了转换成秒
        if (between < 0) {
            System.out.println("开始时间大于结束时间！");
        }
        // 小于一年
        if (between / (3600 * 24 * 365) <= 0) {
            long a = between / (3600 * 24 * 30);
            return (a + "个月");
        } else {
            long b = between / (3600 * 24 * 365); // 年
            long c = (between - b * 3600 * 24 * 365) / ((3600 * 24 * 30)); // 月
            return (b + "年" + c + "个月");
        }
    }
    
    /**
     * 格式化时间
     * 
     * @param date 要格式化的时间,可以写null,format(null,"")，默认为当前时间。
     * @param formatType  格式化的类型，若为""，则默认为如 "yyyy-MM-dd"
     *   M 年中的月份, w 年中的周数 ,D 年中的周数 ,S 毫秒数
     * @return 格式化后的字符串类型时间
     */
    public static String format(Date date, String formatType) {

        if (date == null) {
            date = new Date();
        }
        if (formatType == null || "".equals(formatType)) {
            formatType = "yyyy-MM-dd";
        }
        SimpleDateFormat myformat = new SimpleDateFormat(formatType);
        String formatDate = myformat.format(date);
        
        return formatDate;
    }
    
    /**
     * 取出某个日期的多少个月之前或之后的日期 例如：addDateTime(new Date(),-2,1)两年前  </br>
     * addDateTime(new Date(),-2,Calendar.MONTH)两月前，addDateTime(new Date(),-2,Calendar.DAY_OF_MONTH) 两天前
     * @param date 要调整的时间
     * @param count 调整的数量
     * @param model 调整的域，Calendar.YEAR :年， Calendar.MONTH :月 ，Calendar.DAY_OF_MONTH:日 
     * @return
     */
    public static Date addDateTime(Date date,int count ,int model){
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (model == Calendar.YEAR) {
            cal.add(Calendar.YEAR, count);
        }
        if (model == Calendar.MONTH) {
            cal.add(Calendar.MONTH, count);
        }
        if (model == Calendar.DAY_OF_MONTH) {
            cal.add(Calendar.DAY_OF_MONTH, count);
        }
        return cal.getTime();
    }
    
    /**
     * 获取下一个月的第一天
     * @param date
     * @return 下一个月的第一天.
     */
    public static Date firstDayOfNextMonth(Date date){
        Date tmp = truncate(date, Calendar.MONTH) ;
        Date resultDate = addMonths(tmp, 1) ;
        return resultDate;
    }
    
}
