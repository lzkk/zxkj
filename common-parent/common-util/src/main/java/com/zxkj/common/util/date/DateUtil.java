package com.zxkj.common.util.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Date Operation Utils
 * </p>
 *
 * @author IceWee
 * @see DateUtils
 * @see FastDateFormat
 * @see DateFormatUtils
 */
public class DateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	/**
	 * zero - 0
	 */
	public static final int ZERO = 0;

	/**
	 * max hour
	 */
	public static final int MAX_HOUR = 23;

	/**
	 * max minute
	 */
	public static final int MAX_MINUTE = 59;

	/**
	 * max second
	 */
	public static final int MAX_SECOND = 59;

	/**
	 * max millisecond
	 */
	public static final int MAX_MILLISECOND = 999;

	/**
	 * days of one week
	 */
	public static final int DAYS_OF_WEEK = 7;

	/**
	 * default date pattern
	 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public static final String DEFAULT_DATE_PATTERN_YM = "yyyy-MM";

	public static final String PATTERN_CLASSICAL_SIMPLE = "yyyy-MM-dd";

	public static final String PATTERN_CLASSICAL_SIMPLE_HM = "yyyy-MM-dd HH:mm";

	/**
	 * default datetime pattern
	 */
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String DEFAULT_DATETIME_PATTERN_WEBACK = "yyyyMMddHHmmss";

	/**
	 * default date/datetime patterns
	 */
	public static final String[] DEFAULT_PATTERNS = new String[] { "yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy-MM", "yyyy/MM/dd HH:mm:ss.S",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy/MM", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMMdd", "yyyyMM", "MM-dd-yyyy", "MM/dd/yyyy", "dd/MM/yyyy" };

	public static final int DAY_SECONDS = 24 * 60 * 60;
	public static final long DAY_MILLISECONDS = DAY_SECONDS * 1000L;
	
	private DateUtil() {
		super();
	}

	/**
	 * <p>
	 * Parses a object representing a date by trying a variety of different parsers.
	 * </p>
	 * 
	 * <p>
	 * The parse will try each parse pattern in turn. A parse is only deemed successful if it parses the whole of the input string. If no parse
	 * patterns match, a ParseException is thrown.
	 * </p>
	 * The parser will be lenient toward the parsed date.
	 * 
	 * @param obj
	 *            the date to parse, may be null
	 * @param parsePatterns
	 *            the date format patterns to use, see SimpleDateFormat, not null
	 * @return the parsed date or null
	 * @throws ParseException
	 *             if none of the date patterns were suitable (or there were none)
	 */
	public static Date parseDate(final Object obj, final String... parsePatterns) throws ParseException {
		Date date = null;
		if (obj != null) {
			date = DateUtils.parseDate(Objects.toString(obj), parsePatterns);
		}
		return date;
	}

	/**
	 * <p>
	 * Formats a date/calendar/long into a specific pattern.
	 * </p>
	 * 
	 * @param obj
	 *            the {@code Date}/{@code Calendar}/{@code Long} to format, may be null
	 * @param pattern
	 *            the pattern to use to format the date, may be null
	 * @return the formatted date or null
	 */
	public static String format(final Object obj, final String pattern) {
		String str = null;
		if (obj != null) {
			if (obj instanceof Date) {
				str = formatDate((Date) obj, pattern);
			} else if (obj instanceof Calendar) {
				str = formatCalendar((Calendar) obj, pattern);
			} else if (obj instanceof Long) {
				str = formatMillis((Long) obj, pattern);
			}
		}
		return str;
	}

	public static String format(Date date){
		if("".equals(date) || date == null){
			date = new Date();
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	/**
	 * <p>
	 * Formats a date/time/timestamp into a specific pattern.
	 * </p>
	 * 
	 * @param date
	 *            the date to format, may be null
	 * @param pattern
	 *            the pattern to use to format the date, may be null
	 * @return the formatted date or null
	 */
	public static String formatDate(final Date date, final String pattern) {
		String str = null;
		if (date != null && StringUtils.isNotBlank(pattern)) {
			str = DateFormatUtils.format(date, pattern);
		}
		return str;
	}

	/**
	 * <p>
	 * Formats a calendar into a specific pattern.
	 * </p>
	 * 
	 * @param calendar
	 *            the calendar to format, may be null
	 * @param pattern
	 *            the pattern to use to format the calendar, may be null
	 * @return the formatted calendar or null
	 * @see FastDateFormat#format(Calendar)
	 */
	public static String formatCalendar(final Calendar calendar, final String pattern) {
		String str = null;
		if (calendar != null && StringUtils.isNotBlank(pattern)) {
			str = DateFormatUtils.format(calendar, pattern);
		}
		return str;
	}

	/**
	 * <p>
	 * Formats a date/time into a specific pattern.
	 * </p>
	 * 
	 * @param millis
	 *            the date to format expressed in milliseconds
	 * @param pattern
	 *            the pattern to use to format the date, not null
	 * @return the formatted date or null
	 */
	public static String formatMillis(final Long millis, final String pattern) {
		String str = null;
		if (millis != null && StringUtils.isNotBlank(pattern)) {
			str = DateFormatUtils.format(millis, pattern);
		}
		return str;
	}

	/**
	 * <p>
	 * Gets Monday of current week
	 * </p>
	 * 
	 * @return {@code Date} Monday of current week
	 */
	public static Date getMonday() {
		return getWeekday(Calendar.MONDAY);
	}

	/**
	 * <p>
	 * Gets Monday of current week
	 * </p>
	 * 
	 * @param firstDayOfWeek
	 *            Monday or Sunday
	 * @return {@code Date} Monday of current week
	 */
	public static Date getMonday(final int firstDayOfWeek) {
		return getWeekday(Calendar.MONDAY, firstDayOfWeek);
	}

	/**
	 * <p>
	 * Gets Monday of specified date's own week
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} Monday or null
	 */
	public static Date getMonday(final Date date) {
		return getWeekday(Calendar.MONDAY, date);
	}

	/**
	 * <p>
	 * Gets Monday of specified date's own week
	 * </p>
	 * 
	 * @param firstDayOfWeek
	 *            Monday or Sunday
	 * @param date
	 *            may be null
	 * @return {@code Date} Monday or null
	 */
	public static Date getMonday(final int firstDayOfWeek, final Date date) {
		return getWeekday(Calendar.MONDAY, firstDayOfWeek, date);
	}

	/**
	 * <p>
	 * Gets Sunday of current week
	 * </p>
	 * 
	 * @return {@code Date} Sunday of current week
	 */
	public static Date getSunday() {
		return getWeekday(Calendar.SUNDAY);
	}

	/**
	 * <p>
	 * Gets Sunday of current week
	 * </p>
	 * 
	 * @param firstDayOfWeek
	 *            Monday or Sunday
	 * @return {@code Date} Sunday of current week
	 */
	public static Date getSunday(final int firstDayOfWeek) {
		return getWeekday(Calendar.SUNDAY, firstDayOfWeek);
	}

	/**
	 * <p>
	 * Gets Sunday of specified date's own week, first day of week is Monday by default
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} Sunday or null
	 */
	public static Date getSunday(final Date date) {
		return getWeekday(Calendar.SUNDAY, date);
	}

	/**
	 * <p>
	 * Gets Sunday of specified date's own week
	 * </p>
	 * 
	 * @param firstDayOfWeek
	 *            Monday or Sunday
	 * @param date
	 *            may be null
	 * @return {@code Date} Sunday or null
	 */
	public static Date getSunday(final int firstDayOfWeek, final Date date) {
		return getWeekday(Calendar.SUNDAY, firstDayOfWeek, date);
	}

	/**
	 * <p>
	 * Gets weekday of current week
	 * </p>
	 * 
	 * @param dayOfWeek
	 *            Monday to Sunday
	 * @return {@code Date} weekday of current week
	 * @see Calendar#SUNDAY
	 * @see Calendar#MONDAY
	 * @see Calendar#TUESDAY
	 * @see Calendar#WEDNESDAY
	 * @see Calendar#THURSDAY
	 * @see Calendar#FRIDAY
	 * @see Calendar#SATURDAY
	 */
	public static Date getWeekday(final int dayOfWeek) {
		return getWeekday(dayOfWeek, new Date());
	}

	/**
	 * <p>
	 * Gets weekday of current week
	 * </p>
	 *
	 * @param dayOfWeek
	 *            Monday to Sunday
	 * @param firstDayOfWeek
	 *            Monday or Sunday
	 * @return {@code Date} weekday of current week
	 * @see Calendar#SUNDAY
	 * @see Calendar#MONDAY
	 * @see Calendar#TUESDAY
	 * @see Calendar#WEDNESDAY
	 * @see Calendar#THURSDAY
	 * @see Calendar#FRIDAY
	 * @see Calendar#SATURDAY
	 */
	public static Date getWeekday(final int dayOfWeek, final int firstDayOfWeek) {
		return getWeekday(dayOfWeek, firstDayOfWeek, new Date());
	}

	/**
	 * <p>
	 * Gets weekday of specified date's own week, first day of week is Monday by default
	 * </p>
	 *
	 * @param dayOfWeek
	 * @param date
	 *            may be null
	 * @return {@code Date} weekday or null
	 * @see Calendar#SUNDAY
	 * @see Calendar#MONDAY
	 * @see Calendar#TUESDAY
	 * @see Calendar#WEDNESDAY
	 * @see Calendar#THURSDAY
	 * @see Calendar#FRIDAY
	 * @see Calendar#SATURDAY
	 */
	public static Date getWeekday(final int dayOfWeek, final Date date) {
		return getWeekday(dayOfWeek, Calendar.MONDAY, date);
	}

	/**
	 * <p>
	 * Gets weekday of specified date's own week
	 * </p>
	 *
	 * @param dayOfWeek
	 *            Monday to Sunday
	 * @param firstDayOfWeek
	 *            Monday or Sunday
	 * @param date
	 *            may be null
	 * @return {@code Date} weekday or null
	 * @see Calendar#SUNDAY
	 * @see Calendar#MONDAY
	 * @see Calendar#TUESDAY
	 * @see Calendar#WEDNESDAY
	 * @see Calendar#THURSDAY
	 * @see Calendar#FRIDAY
	 * @see Calendar#SATURDAY
	 */
	public static Date getWeekday(final int dayOfWeek, final int firstDayOfWeek, final Date date) {
		Date weekday = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.setFirstDayOfWeek(firstDayOfWeek);
			calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			weekday = calendar.getTime();
		}
		return weekday;
	}

	/**
	 * <p>
	 * Gets first day of current month
	 * </p>
	 * 
	 * @return {@code Date} first day
	 */
	public static Date getFirstDayOfMonth() {
		return getFirstDayOfMonth(new Date());
	}

	/**
	 * <p>
	 * Gets first day of specified date's own month
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} first day or null
	 */
	public static Date getFirstDayOfMonth(final Date date) {
		Date firstDay = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			setBeginTime(calendar);
			firstDay = calendar.getTime();
		}
		return firstDay;
	}

	/**
	 * <p>
	 * Gets last day of current month
	 * </p>
	 * 
	 * @return {@code Date} last day
	 */
	public static Date getLastDayOfMonth() {
		return getLastDayOfMonth(new Date());
	}

	/**
	 * <p>
	 * Gets last day of specified date's own month
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} last day
	 */
	public static Date getLastDayOfMonth(final Date date) {
		Date lastDay = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			setEndTime(calendar);
			lastDay = calendar.getTime();
		}
		return lastDay;
	}

	/**
	 * <p>
	 * Gets first day of current year
	 * </p>
	 * 
	 * @return {@code Date} first day
	 */
	public static Date getFirstDayOfYear() {
		return getFirstDayOfYear(new Date());
	}

	/**
	 * <p>
	 * Gets first day of specified date's own year
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} first day or null
	 */
	public static Date getFirstDayOfYear(final Date date) {
		Date firstDay = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			setBeginTime(calendar);
			firstDay = calendar.getTime();
		}
		return firstDay;
	}

	/**
	 * <p>
	 * Gets last day of current year
	 * </p>
	 * 
	 * @return {@code Date} last day
	 */
	public static Date getLastDayOfYear() {
		return getLastDayOfYear(new Date());
	}

	/**
	 * <p>
	 * Gets last day of specified date's own year
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} last day or null
	 */
	public static Date getLastDayOfYear(final Date date) {
		Date lastDay = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.MONTH, Calendar.DECEMBER);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			setEndTime(calendar);
			lastDay = calendar.getTime();
		}
		return lastDay;
	}

	/**
	 * <p>
	 * Gets first time of {@code date}
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} first time 00:00:00 or null
	 */
	public static Date getBeginTimeOfDate(final Date date) {
		Date beginTime = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			setBeginTime(calendar);
			beginTime = calendar.getTime();
		}
		return beginTime;
	}

	/**
	 * <p>
	 * Gets end time of {@code date}
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @return {@code Date} end time 23:59:59 or null
	 */
	public static Date getEndTimeOfDate(final Date date) {
		Date endTime = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			setEndTime(calendar);
			endTime = calendar.getTime();
		}
		return endTime;
	}

	/**
	 * <p>
	 * Gets {@code days} days before of {@code date}
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @param days
	 * @return {@code Date} some days before or null
	 */
	public static Date beforeDays(final Date date, final int days) {
		Date daysBefore = date;
		if (days > ZERO && date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, -days);
			daysBefore = calendar.getTime();
		}
		return daysBefore;
	}

	/**
	 * <p>
	 * Gets {@code days} days after of {@code date}
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @param days
	 * @return {@code Date} some days after or null
	 */
	public static Date afterDays(final Date date, final int days) {
		Date daysAfter = date;
		if (days > ZERO && date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, days);
			daysAfter = calendar.getTime();
		}
		return daysAfter;
	}

	/**
	 * <p>
	 * Gets {@code weeks} weeks before of {@code date}
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @param weeks
	 * @return {@code Date} some weeks before or null
	 */
	public static Date beforeWeeks(final Date date, final int weeks) {
		return beforeDays(date, weeks * DAYS_OF_WEEK);
	}

	/**
	 * <p>
	 * Gets {@code weeks} weeks after of {@code date}
	 * </p>
	 * 
	 * @param date
	 *            may be null
	 * @param weeks
	 * @return {@code Date} some weeks after or null
	 */
	public static Date afterWeeks(final Date date, final int weeks) {
		return afterDays(date, weeks * DAYS_OF_WEEK);
	}

	/**
	 * <p>
	 * Sets time 00:00:00
	 * </p>
	 * 
	 * @param calendar
	 *            may be null
	 */
	private static void setBeginTime(final Calendar calendar) {
		if (calendar != null) {
			calendar.set(Calendar.HOUR_OF_DAY, ZERO);
			calendar.set(Calendar.MINUTE, ZERO);
			calendar.set(Calendar.SECOND, ZERO);
			calendar.set(Calendar.MILLISECOND, ZERO);
		}
	}

	/**
	 * <p>
	 * Sets time 23:59:59
	 * </p>
	 * 
	 * @param calendar
	 *            may be null
	 */
	private static void setEndTime(final Calendar calendar) {
		if (calendar != null) {
			calendar.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
			calendar.set(Calendar.MINUTE, MAX_MINUTE);
			calendar.set(Calendar.SECOND, MAX_SECOND);
			calendar.set(Calendar.MILLISECOND, MAX_MILLISECOND);
		}
	}
	
	//获得上个月日期
	public static Date getLastDate() {
        return getLastDate(new Date());
    }
	
	public static Date getLastDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

	public static Date addDays(Date date, int days){
		if(null != date){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE,1);
			return c.getTime();
		}
		return null;
	}

	public static Date addDayNum(Date date, int num){
		if(null != date){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE,num);
			return c.getTime();
		}
		return null;
	}

	public static Date addOneDay(Date date){
		return addDays(date, 1);
	}

	/**
	 * 根据指定格式将指定字符串解析成日期
	 *
	 * @param str
	 *            指定日期
	 * @param pattern
	 *            指定格式
	 * @return 返回解析后的日期
	 */
	public static Date parse(String str, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			LOGGER.error("date parse error",e);
		}
		return null;
	}


	/**
	 * 获取指定日期累加指定天数后的时间
	 *
	 * @param date
	 *            指定日期
	 * @param day
	 *            指定天数
	 * @return 返回累加天数后的时间
	 */
	public static Date rollDay(Date date, int day) {
		return rollDate(date, 0, 0, day);
	}


	/**
	 * 获取指定日期累加年月日后的时间
	 *
	 * @param date
	 *            指定日期
	 * @param year
	 *            指定年数
	 * @param month
	 *            指定月数
	 * @param day
	 *            指定天数
	 * @return 返回累加年月日后的时间
	 */
	public static Date rollDate(Date date, int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	public static int getOffsetMinutes(Date date1, Date date2) {
		return getOffsetSeconds(date1, date2) / 60;
	}
	/**
	 * 获取时间date1与date2相差的秒数
	 *
	 * @param date1
	 *            起始时间
	 * @param date2
	 *            结束时间
	 * @return 返回相差的秒数
	 */
	public static int getOffsetSeconds(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / 1000);
	}

	public static String getDiffTime(long begin,long end){
		long diff = end-begin;
		if(diff<1000) return diff+"毫秒";
		long day = diff / (24 * 60 * 60 * 1000);
		long hour = (diff / (60 * 60 * 1000) - day * 24);
		long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
		return String.format("%s%s %s分  %s秒", hour>0?hour:"",hour>0?"小时":"",min,sec);
	}

    /**
     * @author yifan
     * @date 2017/12/7 15:48
     * @Description 当前时间到本周日结束的时间差秒数
     * @params
     * @return
     */
    public static Long getTimeDiffToSundayEnd(){
        Date sunday = DateUtil.getSunday();
        sunday.setHours(0);
        sunday.setMinutes(0);
        sunday.setSeconds(0);
        Calendar sundayEnd = Calendar.getInstance();
        sundayEnd.setTime(sunday);
        sundayEnd.add(Calendar.DAY_OF_WEEK, 1);
        System.out.println(sundayEnd.getTimeInMillis());
        Long timeDiff = sundayEnd.getTimeInMillis() - System.currentTimeMillis();
        return timeDiff / 1000;
    }

    public static Date getDate(Date date,int num){
        if(num==3){
            return date;
        }
        Calendar src = Calendar.getInstance();
        src.setTime(DateUtil.afterDays(date,1));
        int nums = checkDateIsWorkDay(src);
        if(nums==0 || nums==3){
            num++;
        }
        return getDate(DateUtil.afterDays(date,1),num);
    }

    private  static Map<String,String> map = new HashMap<>();

    private static void initHolidayData(){
        map.put("20170930", "1");
        map.put("20171001", "0");
        map.put("20171002", "0");
        map.put("20171003", "0");
        map.put("20171004", "0");
        map.put("20171005", "0");
        map.put("20171006", "0");
        map.put("20171007", "0");
        map.put("20171008", "0");
    }

    // 检查当前日期是 周六日 还是节假日 还是工作日  0 代表工作日  1 代表周六日  2 代表补班  3代表休假
    private static int checkDateIsWorkDay(Calendar src){
        int result = 0;
        if (map.isEmpty()){
            //初始化数据
            initHolidayData();
        }
        //先检查是否是周六周日(有些国家是周五周六)
        if (src.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || src.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            result = 1;
        }
        // 检查 国家节假日
        Set<Map.Entry<String,String>> holiDayMap =  map.entrySet();
        String day = DateUtil.formatCalendar(src,"yyyyMMdd");
        for (Map.Entry<String,String> entry : holiDayMap) {
            if(entry.getKey().equals(day) && entry.getValue().equals("0")){
                result = 2;
            }else if (entry.getKey().equals(day) && entry.getValue().equals("1")){
                result = 3;
            }
        }
        return result;
    }

	/**
	 * @author hjw
	 * @date 2018/5/13 16:35
	 * @Description 计算两个日期相差多少秒
	 * @return
	 */
	public static int getDateDiffer(Date start, Date end) {
		return (int) (end.getTime() - start.getTime()) / 1000;
	}
	
	/**
	 * 
	 * @author mahongliang
	 * @date  2018年6月8日 下午6:09:30
	 * @Description 
	 * @param date
	 * @return
	 */
	public static String getStartDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, -24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date zero = calendar.getTime();
		return DateUtil.formatDate(zero, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	
	public static String getEndDate(Date date) {
		Date zero = getToday(date);
		return DateUtil.formatDate(zero, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public static String getDate(Date date) {
		return DateUtil.formatDate(date, DateUtil.DEFAULT_DATETIME_PATTERN_WEBACK);
	}
	
	
	public static Date getToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date zero = calendar.getTime();
		return zero;
	}
	
    /**
     * 计算两个日期的0点相差天数是否小于等于days
     *
     * @param from 开始日期
     * @param to   结束日期
     * @param days 天数
     * @return
     */
    public static boolean intervalDays(Date from, Date to, int days) {
        if (from == null || to == null) {
            return false;
        }
//        long fromTime = from.getTime();
//        long toTime = to.getTime();
//
//        return Math.abs(fromTime - toTime) < days * DateUtils.MILLIS_PER_DAY;
        return Math.abs(intervalDays(from, to)) < days;
    }

    /**
     * 计算两个时间之间的天数差,按照自然日计算
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 相差的天数
     */
    public static long intervalDays(Date begin, Date end) {
        if (begin == null || end == null)
            return 0;
        begin = DateUtils.truncate(begin, Calendar.DATE);
        end = DateUtils.truncate(end, Calendar.DATE);
        return (end.getTime() - begin.getTime()) / DAY_MILLISECONDS;
    }

    /**
	 * @author 魏冰
	 * @date 2017年1月16日 下午5:33:47
	 * @Description 解析字符串成日期
	 * @param string
	 * @return
	 * @throws ParseException
	 */
    public static Date parseDate(String string) {
		try {
			return DateUtils.parseDate(string, DEFAULT_PATTERNS);
		} catch (ParseException e) {
		}
		return null;
	}


	/**
	* @author hjw
	* @date 2018/2/28 20:03
	* @Description 获取当前时间的周一
	* @return
	*/
	public static String getNowDateWeekMonday(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int d = 0;
		if(cal.get(Calendar.DAY_OF_WEEK)==1){
			d = -6;
		}else{
			d = 2-cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime())+" 00:00:00";
	}

	/**
	 * @author hjw
	 * @date 2018/2/28 20:03
	 * @Description 获取当前时间的周日
	 * @return
	 */
	public static String getNowDateWeekSunday(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int d = 0;
		if(cal.get(Calendar.DAY_OF_WEEK)==1){
			d = -6;
		}else{
			d = 2-cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		cal.add(Calendar.DAY_OF_WEEK, 6);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime())+" 23:59:59";
	}

	/**
	 * @author hjw
	 * @date 2018/2/28 20:14
	 * @Description 获取当天开始时间
	 * @return
	 */
	public static String getNowDateStartString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date)+" 00:00:00";
	}

	/**
	 * @author hjw
	 * @date 2018/2/28 20:14
	 * @Description 获取当天开始时间
	 * @return
	 */
	public static String getDateStartString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	/**
	 * @author baishenghu
	 * @date 2021/01/25 20:14
	 * @Description 获取当天开始时间 去掉时分秒
	 * @return
	 */
	public static Date getDateFormatNoTime(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dt = format.format(date)+" 00:00:00";
		SimpleDateFormat t = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
		try {
			Date s = t.parse(dt);
			return s;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * @author baishenghu
	 * @date 2021/01/25 20:14
	 * @Description 获取当天开始时间 去掉时分秒
	 * @return
	 */
	public static Date getDateFormatNoTime(String date) {
		String dt = date+" 00:00:00";
		SimpleDateFormat t = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
		try {
			Date s = t.parse(dt);
			return s;
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * @author hjw
	 * @date 2018/2/28 20:14
	 * @Description 获取当天结束时间
	 * @return
	 */
	public static String getNowDateEndString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date)+" 23:59:59";
	}

	/**
	 * 将字符串转为yyyy-MM-dd HH:mm:ss格式的Date对象
	 * @param date
	 * @return
	 */
	public static Date parseDatetime(String date){
		SimpleDateFormat t = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
		try {
			Date s = t.parse(date);
			return s;
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDatetime(String date,final String pattern){
		SimpleDateFormat t = new SimpleDateFormat(pattern);
		try {
			Date s = t.parse(date);
			return s;
		} catch (ParseException e) {
			return null;
		}
	}

	//年字符串转date
	public static Date parseDateByYear(String year){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		try {
			Date s = simpleDateFormat.parse(year);
			return s;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 得到yyyy-MM-dd HH:mm:ss格式的日期
	 *
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		DateFormat format = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
		return format.format(date);
	}

    public static void main(String[] args){
    	
//    	System.out.println(getDate(new Date()));
//		BigDecimal num = new BigDecimal(-0.01*0.003);
//		System.out.println(num);
//		System.out.println(num.setScale(2,BigDecimal.ROUND_UP));
//		num = num.setScale(4,BigDecimal.ROUND_UP);
//		System.out.println(num.setScale(3,BigDecimal.ROUND_UP));
//		num = num.setScale(3,BigDecimal.ROUND_UP);
//		System.out.println(num.setScale(2,BigDecimal.ROUND_UP));
//
//
//		BigDecimal d21 = BigDecimal.valueOf(2400.00 ).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d31 = BigDecimal.valueOf(1824.00 ).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d41 = BigDecimal.valueOf(5053.65 ).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d51 = BigDecimal.valueOf(3150.00 ).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d61 = BigDecimal.valueOf(1680.00 ).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d71 = BigDecimal.valueOf(3648.00 ).setScale(2, BigDecimal.ROUND_UP);
//
//		BigDecimal rate = BigDecimal.valueOf(0.02 );
//
//		BigDecimal d2 = d21.multiply(rate).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d3 = d31.multiply(rate).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d4 = d41.multiply(rate).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d5 = d51.multiply(rate).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d6 = d61.multiply(rate).setScale(2, BigDecimal.ROUND_UP);
//		BigDecimal d7 = d71.multiply(rate).setScale(2, BigDecimal.ROUND_UP);
//		System.out.println(d2);
//		System.out.println(d3);
//		System.out.println(d4);
//		System.out.println(d5);
//		System.out.println(d6);
//		System.out.println(d7);
//		BigDecimal d8 = d2.add(d3).add(d4).add(d5).add(d6).add(d7);
//		System.out.println(d8);
//
//
//		BigDecimal d = BigDecimal.valueOf(17755.75 * 0.02).setScale(4, BigDecimal.ROUND_HALF_UP);
//		BigDecimal d1 = BigDecimal.valueOf(17755.75 * 0.02).setScale(4, BigDecimal.ROUND_UP);
//		System.out.println(d);
//		System.out.println(d1);


		BigDecimal d = BigDecimal.valueOf(17755.75 * 0.02).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal d1 = BigDecimal.valueOf(17755.75 * 0.02).setScale(4, BigDecimal.ROUND_UP);
		//System.out.println(d);
		//System.out.println(d1);

        System.out.println(DateUtil.getMonday());
		System.out.println(DateUtil.getBeginTimeOfDate(DateUtil.getMonday()));
    }

	public static boolean sameDate(Date d1, Date d2) {
		if(null == d1 || null == d2) {
			return false;
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		return  cal1.getTime().equals(cal2.getTime());
	}

	/**
	 * 获取当前年
	 * @return
	 */
	public static String getCurrentYear() {
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		return year;
	}

	/**
	 * 获取年月的所有天数列表
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static List<Date> getDatesByYearAndMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		List<Date> list = new ArrayList<Date>();
		do {
			list.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		} while (month - 1 == calendar.get(Calendar.MONTH));
		for (Date date : list) {
			System.out.println(date.toLocaleString());
		}
		return list;
	}

	/**
	 * 获取月的所有天数列表
	 *
	 * @param year
	 * @return
	 */
	public static List<Date> getDatesByYear(int year) {
		Calendar calendar = Calendar.getInstance();
		List<Date> list = new ArrayList<Date>();
		for (int month = 1; month <= 12; month++) {
			calendar.set(year, month - 1, 1);
			do {
				list.add(calendar.getTime());
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			} while (month - 1 == calendar.get(Calendar.MONTH));
		}
		return list;
	}
	public static Date getDay24Time(Date date){
		Calendar endTimeCalendar = Calendar.getInstance();
		endTimeCalendar.setTime(date);
		endTimeCalendar.set(Calendar.HOUR_OF_DAY, 23);
		endTimeCalendar.set(Calendar.MINUTE, 59);
		endTimeCalendar.set(Calendar.SECOND, 59);
		endTimeCalendar.set(Calendar.MILLISECOND, 0);
		Date endTime = endTimeCalendar.getTime();
		return endTime;
	}

	public static Date getDayZeroTime(Date date){
		Calendar startTimeCalendar = Calendar.getInstance();
		startTimeCalendar.setTime(date);
		startTimeCalendar.set(Calendar.HOUR_OF_DAY, 0);
		startTimeCalendar.set(Calendar.MINUTE, 0);
		startTimeCalendar.set(Calendar.SECOND, 0);
		startTimeCalendar.set(Calendar.MILLISECOND, 0);
		Date startTime = startTimeCalendar.getTime();
		return startTime;
	}

	public static String getFirstDayOfMonth(String date){
		String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(date);
		if(mat.matches()){
			throw new RuntimeException(date+"日期格式不正确");
		}
		LocalDate day = LocalDate.parse(date);
		//本月的第一天
		LocalDate firstday = day.with(TemporalAdjusters.firstDayOfMonth());
		return firstday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public static String getLastDayOfMonth(String date){
		String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(date);
		if(!mat.matches()){
			throw new RuntimeException(date+"日期格式不正确");
		}
		LocalDate day = LocalDate.parse(date);
		//本月的最后一天
		LocalDate lastDay = day.with(TemporalAdjusters.lastDayOfMonth());
		return lastDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	/**
	 * 根据指定日期 和 lazyDays， 获得 指定日期推后几天的日期对象
	 *
	 * @param date
	 *            日期对象
	 * @param lazyDays
	 *            倒推的天数(若为负数则代表提前)
	 *
	 * @return 指定日期倒推指定天数后的日期对象
	 *
	 * @throws ParseException
	 */
	public static Date dateAdd(Date date, int lazyDays) {
		Date inputDate = date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear + lazyDays);
		return cal.getTime();
	}




    /**
     * 获得该月最后一天
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int month,String year) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String currentYear = format.format(cal.getTime());
        // 获取某月最大天数
        int lastDay = 0;
        if (!year.equals(currentYear)){
            Date date = DateUtil.parseDateByYear(year);
            cal.setTime(date);
        }
        cal.set(Calendar.MONTH, month - 1);
        //2月的平年瑞年天数
        if(month==2) {
            lastDay = cal.getLeastMaximum(Calendar.DAY_OF_MONTH);
        }else {
            lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime()).replaceAll("-","");
        return lastDayOfMonth;
    }


	public static Map<String,String> getDateLastAndFirst(Date dateDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//获取前一个月第一天
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(dateDay);
		calendar1.set(Calendar.DAY_OF_MONTH,1);
		String firstDay = sdf.format(calendar1.getTime());
		//获取前一个月最后一天
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dateDay);
		//加一个月
		calendar2.add(Calendar.MONTH, 1);
		//设置为该月第一天
		calendar2.set(Calendar.DATE, 1);
		//再减一天即为上个月最后一天
		calendar2.add(Calendar.DATE, -1);
		String lastDay = sdf.format(calendar2.getTime());
		Map<String,String> map =  new HashMap<>();
		map.put("firstDay",firstDay);
		map.put("lastDay",lastDay);
		return map;
	}

    public static String getDayStartStr(String dayStr) {
        if (dayStr != null) {
            dayStr += " 00:00:00";
        }
        return dayStr;
    }

    public static String getDayEndStr(String dayStr) {
        if (dayStr != null) {
            dayStr += " 23:59:59";
        }
        return dayStr;
    }

    public static Date getOffsetBeginTime(long time, int offsetDays) {
        Calendar cc = Calendar.getInstance();
        cc.setTimeInMillis(time);
        cc.add(Calendar.DAY_OF_MONTH, offsetDays);
        cc.set(Calendar.HOUR_OF_DAY, 0);
        cc.set(Calendar.MINUTE, 0);
        cc.set(Calendar.SECOND, 0);
        cc.set(Calendar.MILLISECOND, 0);
        return cc.getTime();
    }

    public static Date getOffsetEndTime(long time, int offsetDays) {
        Calendar cc = Calendar.getInstance();
        cc.setTimeInMillis(time);
        cc.add(Calendar.DAY_OF_MONTH, offsetDays);
        cc.set(Calendar.HOUR_OF_DAY, 23);
        cc.set(Calendar.MINUTE, 59);
        cc.set(Calendar.SECOND, 59);
        cc.set(Calendar.MILLISECOND, 999);
        return cc.getTime();
    }

	public static Date getOffsetTime(long time, int offsetDays) {
		Calendar cc = Calendar.getInstance();
		cc.setTimeInMillis(time);
		cc.add(Calendar.DAY_OF_MONTH, offsetDays);
		return cc.getTime();
	}

	/**
	 * 日期转化为cron表达式
	 *
	 * @param date
	 * @return
	 */
	public static String getCron(Date date) {
		String dateFormat = "ss mm HH dd MM ? yyyy";
		return DateUtil.fmtDateToStr(date, dateFormat);
	}

	/**
	 * cron表达式转为日期
	 *
	 * @param cron
	 * @return
	 */
	public static Date getCronToDate(String cron) {
		String dateFormat = "ss mm HH dd MM ? yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = sdf.parse(cron);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * Description:格式化日期,String字符串转化为Date
	 *
	 * @param date
	 * @param dtFormat 例如:yyyy-MM-dd HH:mm:ss yyyyMMdd
	 * @return
	 */
	public static String fmtDateToStr(Date date, String dtFormat) {
		if (date == null)
			return "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dtFormat);
			return dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
