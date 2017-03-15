package com.cu.yunxindemo.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time * 1000));
    }

    public static String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time * 1000));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time * 1000));
    }

    public static String getMM_DD_HH_mm(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(time * 1000L));
    }

    public static String getTime2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getHourAndMin2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getChatTime(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        LogUtils.d("----------->getChatTime:today" + today + "----otherDay" + otherDay);
        switch (temp) {
            case 0:
                result = getHourAndMin2(timesamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin2(timesamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin2(timesamp);
                break;

            default:
                // result = temp + "天前 ";
                result = getTime2(timesamp);
                break;
        }

        return result;
    }


    public static boolean is5Minute(long timesamp, long totime) {

        SimpleDateFormat msdf = new SimpleDateFormat("mm");//分
        SimpleDateFormat dsdf = new SimpleDateFormat("dd");//日
        SimpleDateFormat hsdf = new SimpleDateFormat("HH");//小时
        Date otherDay = null;

        Date today = new Date(totime);

        if (timesamp == 0) {
            otherDay = new Date(System.currentTimeMillis());
        } else {
            otherDay = new Date(timesamp);
        }


        LogUtils.d("----------->is5Minute:today" + today + "----otherDay" + otherDay);
        int mtemp = Integer.parseInt(msdf.format(today))
                - Integer.parseInt(msdf.format(otherDay));
        int dtemp = Integer.parseInt(dsdf.format(today))
                - Integer.parseInt(dsdf.format(otherDay));
        int htemp = Integer.parseInt(hsdf.format(today))
                - Integer.parseInt(hsdf.format(otherDay));

        LogUtils.d("--------->mtemp:" + mtemp + "----dtemp:" + dtemp + "-----htemp:" + htemp);
        if (dtemp - 1 > 0) {
            return true;
        } else if (htemp - 1 > 0) {
            return true;
        } else if (mtemp > 5) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return "今天 ";//+time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {

            return "昨天 ";//+time.split(" ")[1];
        } else {
            //int index = time.indexOf("-")+1;
            //return time.substring(index, time.length());
            return time;
        }
    }


    public static Date GetDateFromLong(long millis) {
        Date date = new Date(millis);
        return date;
    }



}
