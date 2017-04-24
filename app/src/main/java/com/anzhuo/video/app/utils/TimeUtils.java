package com.anzhuo.video.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hulei on 2016/4/27.
 */
public class TimeUtils {
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        boolean hasHour = true;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        sb.append("00:00:00");
        if (day > 0) {
            sb.append(day + "MyPhotoView");
        }
        if (hour > 0) {
            sb.replace(0, 2, hour + "");
        } else {
            sb.replace(0, 3, "");
            hasHour = false;
        }
        if (minute > 0) {
            if (hasHour) {
                if (minute >= 10)
                    sb.replace(3, 5, minute + "");
                else
                    sb.replace(3, 5, "0" + minute + "");
            } else {
                if (minute >= 10)
                    sb.replace(0, 2, minute + "");
                else
                    sb.replace(0, 2, "0" + minute + "");
            }

        }
        if (second > 0) {
            if (hasHour) {
                if (second < 10) {
                    sb.replace(6, sb.length(), "0" + second + "");
                } else {
                    sb.replace(6, sb.length(), second + "");
                }
            } else {
                if (second < 10) {
                    sb.replace(3, sb.length(), "0" + second + "");
                } else {
                    sb.replace(3, sb.length(), second + "");
                }
            }
        }


        return sb.toString();
    }
    public  static String getSystemTime(String timeFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
