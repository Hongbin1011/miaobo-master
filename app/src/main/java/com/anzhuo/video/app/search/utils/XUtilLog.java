package com.anzhuo.video.app.search.utils;

import android.util.Log;

import java.util.ArrayList;

public class XUtilLog {
    private static final String TAG_I = "log";
    private static final String TAG_E = "log";
    private static boolean isDebug = false;//是否关闭所有打印

    public static ArrayList<String> arrLogs = new ArrayList<String>();

    public static void log_i(String msg) {
        if (isDebug) {
            Log.i(TAG_I, msg);
            addLogs(msg);
        }
    }

    public static void log_i(String tag, String msg) {
        if (isDebug) {
            Log.i(TAG_I + tag, msg);
            addLogs(tag + msg);
        }
    }

    public static void log_e(String msg) {
        if (isDebug) {
            Log.e(TAG_E, msg);
            addLogs(msg);
        }
    }

    public static void log_e(String tag, String msg) {
        if (isDebug) {
            Log.e(TAG_E + tag, msg);
            addLogs(tag + msg);
        }
    }

    static void addLogs(String logValue) {
        if (arrLogs.size() >= 20) {
            int num = arrLogs.size() - 20;
            for (int i = 0; i < num; i++) {
                arrLogs.remove(20);
            }
        }
        arrLogs.add(0, logValue);
    }

    public static void logI(String format, Object... args) {
        String str = String.format(format, args);
        XUtilLog.log_i(str);
    }

    public static void logE(String format, Object... args) {
        String str = String.format(format, args);
        XUtilLog.log_e(str);
    }

}
