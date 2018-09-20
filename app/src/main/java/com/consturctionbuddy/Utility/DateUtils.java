package com.consturctionbuddy.Utility;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static final String SIMPLE_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_ZONE_STR = "GMT";

    public static final String DATE_WITHOUT_TIME_FORMAT = "dd-MM-yyyy";
    public static final String DATE_WITH_MONTH_FORMAT = "dd-MMM-yyyy";
    public static final String DATE_WITH_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final String DATE_WITHOUT_TIME_SERVER_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MESSAGE = "MMM-dd HH:mm";
    public static final String DATE_FORMAT_NOTIFICATION = "dd MMM yyyy hh:mm aa";


    public static String getDateStr(Calendar aCalendar, String aConvertFormat) {
        String dateStr = null;
        DateFormat timeFormat = new SimpleDateFormat(aConvertFormat);
        dateStr = timeFormat.format(aCalendar.getTime());
        return dateStr;
    }

    public static String getDateStr(Calendar aCalendar, String aConvertFormat, String aTimeZone) {
        String dateStr = null;
        DateFormat timeFormat = new SimpleDateFormat(aConvertFormat, Locale.ENGLISH);
        timeFormat.setTimeZone(TimeZone.getTimeZone(aTimeZone));
        dateStr = timeFormat.format(aCalendar.getTime());
        return dateStr;
    }


    public static String getDateStrByDate(String aDateStr, String aCurFormatStr, String aConvertFormat) {
        String dateStr = null;
        Date date = null;
        DateFormat format = new SimpleDateFormat(aCurFormatStr, Locale.ENGLISH);
        try {
            date = format.parse(aDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            DateFormat timeFormat = new SimpleDateFormat(aConvertFormat, Locale.ENGLISH);
            dateStr = timeFormat.format(date);
        }
        return dateStr;
    }

    public static Calendar getDate(String aDateStr, String aDateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(aDateFormat, Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(aDateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static Calendar getDate(String aDateStr, String aDateFormat, String aTimeZone) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(aTimeZone));
        SimpleDateFormat sdf = new SimpleDateFormat(aDateFormat, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(aTimeZone));
        try {
            cal.setTime(sdf.parse(aDateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String getFullDateString(String aDateStr, String aDateFormat, String aTimeStr, String aTimeFormat, String aResultDateFormat) {
        String str = "";
        Calendar dateCalendar = getDate(aDateStr, aDateFormat);
        Calendar timeCalendar = getDate(aTimeStr, aTimeFormat);

        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        str = getDateStr(dateCalendar, aResultDateFormat);
        return str;
    }

    public static Calendar getTimeDateWithCurDay(String aTimeStr, String aTimeFormat) {
        Calendar timeCalendar = getDate(aTimeStr, aTimeFormat);
        Calendar gmtCurCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCurCal.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        gmtCurCal.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        gmtCurCal.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        return gmtCurCal;
    }

    public static boolean isTimePassed(String aDateStr) {
        boolean timePassed = false;
        Calendar calendar = DateUtils.getDate(aDateStr, DateUtils.DATE_FORMAT, DateUtils.TIME_ZONE_STR);
        Calendar curCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        if (curCal.getTimeInMillis() > calendar.getTimeInMillis()) {
            timePassed = true;
        }
        return timePassed;
    }

    public static String getGMTDate() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar c = Calendar.getInstance(tz);
        String time = String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) +
                "-" + String.format("%02d", c.get(Calendar.MONTH) + 1) +
                "-" + String.format("%02d", c.get(Calendar.YEAR)) +
                " " + String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) +
                ":" + String.format("%02d", c.get(Calendar.MINUTE)) +
                ":" + String.format("%02d", c.get(Calendar.SECOND));
        return time;
    }

    public static String getCalendarString(Calendar aCal) {
        Calendar c = aCal;
        String time = String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) +
                "-" + String.format("%02d", c.get(Calendar.MONTH) + 1) +
                "-" + String.format("%02d", c.get(Calendar.YEAR)) +
                " " + String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) +
                ":" + String.format("%02d", c.get(Calendar.MINUTE)) +
                ":" + String.format("%02d", c.get(Calendar.SECOND));
        return time;
    }

    public static String getLocalDateSrtFromGMTDateStr(String aDate, String aDateFormat) {
        String timeZone = "GMT";
        Calendar calendar1 = getDate(aDate, aDateFormat, timeZone);
        Calendar localCal = Calendar.getInstance();
        localCal.setTime(calendar1.getTime());
        String timeDate = getDateStr(localCal, "yyyyMMdd.HHmmss");
        return timeDate;
    }


}
