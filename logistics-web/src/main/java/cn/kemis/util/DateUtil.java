package cn.kemis.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @author liutiyang
 * @version v1.0
 * @date 2015/3/7.
 */
public class DateUtil {
    public static final String FORMAT_DATE_HOUR       = "yyyy-MM-dd HH";
    public static final String FORMAT_DATE_MINUTE       = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE       = "yyyy-MM-dd";
    public static final String FORMAT_MONTH       = "yyyyMM";
    public static final String FORMAT_DATE_S       = "yyyy年MM月dd日";
    public static final String FORMAT_SHORT_DATE  = "yyyyMMdd";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_TEMPLATE = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_DATE_FILE = "yyyyMMddHHmm";
    public static final String FORMAT_DATE_TIMESTAMP = "yyyyMMddHHmmss";
    /**
     * 给Date增加second秒
     * @param date
     * @param second
     * @return
     */
    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 给Date增加hourz小时
     * @param date
     * @param hour
     * @return
     */
    public static Date addHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * 将UTC时间转成中国本地时间
     * @param date
     * @return
     */
    public static Date UTC2LocalTime(Date date) {
        return addHour(date, 8);
    }

    /**
     * 判断日期是否在有效期内
     * 返回true时已过期，false未过期
     * @param expire
     * @return
     */
    public static boolean isUserSessionExpire(Date expire) {
        boolean isExpire = true;
        Calendar now = Calendar.getInstance();
        Calendar expireCalendar = Calendar.getInstance();
        expireCalendar.setTime(expire);

        isExpire = now.after(expireCalendar);
        return isExpire;
    }

    /**
     * 格式化时间
     * @param date
     * @param formatPattern
     * @return
     */
    public static String date2String (Date date, String formatPattern)
    {
        String dateStr = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 字符串转日期
     * @param date
     * @param formatPattern
     * @return
     */
    public static Date string2Date (String date, String formatPattern)
    {
        Date result = null;
        if(StringUtils.isBlank(date)){
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern);
            result = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 格式化日期
     * yyyy-MM-dd HH:mm:ss
     * @param datetime
     * @return
     */
    public static Date string2Datetime(String datetime) {
        if (StringUtils.isBlank(datetime)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_TIME);

        Date date = null;
        try {
            date = format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 将时间戳转化成String时间
     * @param time
     * @return
     */
    public static String convertDateTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_TIME);
        String outDate = format.format(time);
        return outDate;
    }

    /**
     * 判断当前时间是否在给定时间范围内
     * @param begin
     * @param end
     * @return
     */
    public static boolean betweenDateForCurrent(Date begin, Date end) {
        return betweenDate(new Date(), begin, end);
    }

    /**
     * 判断目标时间是否在给定时间范围内
     * @param target
     * @param begin
     * @param end
     * @return
     */
    public static boolean betweenDate(Date target, Date begin, Date end) {
        boolean isBetween = false;
        if (begin == null || end == null) {
            return isBetween;
        } else {
            Calendar targetCalendar = Calendar.getInstance();
            targetCalendar.setTime(target);

            Calendar beginCalendar = Calendar.getInstance();
            beginCalendar.setTime(begin);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(end);

            if (targetCalendar.after(beginCalendar) && targetCalendar.before(endCalendar)) {
                isBetween = true;
            }
        }
        return isBetween;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date 较小的时间
     * @param targetDate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date date, Date targetDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.parse(sdf.format(date));
        targetDate = sdf.parse(sdf.format(targetDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long time1 = cal.getTimeInMillis();
        cal.setTime(targetDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) throws ParseException {

        //long t = System.currentTimeMillis();
        //String time = convertDateTime(t);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        String time1 = "20150509";
        String time2 = "2015-05-13 19:46:35";

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //System.out.println(date1.getTime());
        //System.out.println(date2.getTime());

        System.out.println(string2Date(time1,DateUtil.FORMAT_SHORT_DATE));
    }


}
