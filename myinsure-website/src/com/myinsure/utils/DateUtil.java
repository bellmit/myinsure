package com.myinsure.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{
    public static String getDateValue(String format)
    {
	return new SimpleDateFormat(format).format(new Date());
    }

    public static String getTodaySec()
    {
	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public static String getFullYear()
    {
    	Calendar instance = Calendar.getInstance();
    	return String.valueOf(instance.get(Calendar.YEAR));
    }
    public static String getMonth()
    {
    	Calendar instance = Calendar.getInstance();
    	return String.valueOf(instance.get(Calendar.MONTH));
    }
    public static String getDay()
    {
    	Calendar instance = Calendar.getInstance();
    	return String.valueOf(instance.get(Calendar.DATE));
    }

    public static String getTodaySec2()
    {
	return new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(new Date());
    }

    public static String getTodaySecNum()
    {
	return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static String getToday()
    {
	return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getTodayStr()
    {
	return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getTodayYMD()
    {
	return new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
    }

    public static String getCurrYearMonth()
    {
	String today = getToday();
	return today.substring(0, today.length() - 3);
    }

    public static String [] getDateByMonth(String month)
    {
	String [] rtn = new String[2];

	rtn[0] = (month + "-01");
	rtn[1] = dateAddMonth(rtn[0], 1);

	return rtn;
    }

    public static String dateAddMonth(String date, int month)
    {
	Calendar calendar = strToCalendar(date);

	calendar.add(2, month);

	return calendarToDate(calendar);
    }

    public static String getAddMonth(String curMonth, int month)
    {
	String preDate = dateAddMonth(curMonth + "-01", month);
	String [] smonth = preDate.split("-");

	return smonth[0] + "-" + smonth[1];
    }

    // utc格式化
    public static String utcDateFormat(String dateStr)
    {

	String datestr = null;
	// String str = "Sun Aug 1 00:00:00 UTC 0800 2010";// 带星期几的UTC日期格式
	SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'UTC+0800' yyyy", Locale.ENGLISH);// CST格式
	Date date = null;
	try
	{
	    date = (Date) df.parse(dateStr);// parse函数进行转换
	    datestr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	} catch (ParseException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return datestr;
    }

    public static Calendar strToCalendar(String strDate)
    {
	Calendar calendar = Calendar.getInstance();

	int h = 0;
	int m = 0;
	int s = 0;

	String [] sdt = strDate.split(" ");
	if (sdt.length > 1)
	{
	    String [] st = sdt[1].split(":");
	    if (st.length > 0)
		h = Integer.parseInt(st[0]);
	    if (st.length > 1)
		m = Integer.parseInt(st[1]);
	    if (st.length > 2)
		s = Integer.parseInt(st[2]);

	}

	int year = 0;
	int month = 0;
	int day = 0;

	String [] sd = sdt[0].split("-");
	if (sd.length > 0)
	    year = Integer.parseInt(sd[0]);
	if (sd.length > 1)
	    month = Integer.parseInt(sd[1]) - 1;
	if (sd.length > 2)
	    day = Integer.parseInt(sd[2]);

	calendar.set(year, month, day, h, m, s);

	return calendar;
    }

    public static String calendarToDate(Calendar calendar)
    {
	return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static String calendarToDateTime(Calendar calendar)
    {
	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    // fmt = "yyyy年MM月dd日"
    public static String dateString(String stringdate, String fmt)
    {
	String now = "";
	try
	{
	    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringdate);
	    now = new SimpleDateFormat(fmt).format(date);

	} catch (ParseException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return now;
    }

    /**
     * 按传输时间返回时间的下一天
     * 
     * @param date
     *            传输时间
     * @param day
     *            天数左右移动，0-返回当前天
     * @return 下一天
     */
    public static String getNextDay(int day)
    {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(new Date());
	calendar.roll(Calendar.DAY_OF_YEAR, day);
	return sdf.format(calendar.getTime());
    }

    /**
     * 按传输时间返回时间的下一月
     * 
     * @param date
     *            传输时间
     * @param month
     *            月数左右移动，0-返回当前月
     * @return 下一月
     */
    public static String getNextMonth(Date date, int month)
    {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.roll(Calendar.MONTH, month);
	return sdf.format(calendar.getTime());
    }

    /**
     * 返回当前月份的第一天
     */
    public static String getfirstday()
    {
	Calendar cale = null;
	cale = Calendar.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String firstday;
	// 获取前月的第一天
	cale = Calendar.getInstance();
	cale.set(Calendar.DAY_OF_MONTH, 1);
	firstday = format.format(cale.getTime());
	return firstday;
    }

}