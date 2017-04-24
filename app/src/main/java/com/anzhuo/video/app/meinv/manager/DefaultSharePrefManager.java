package com.anzhuo.video.app.meinv.manager;


import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.manager.fuli.SharePrefUtil;

/**
 * Created by wbb on 2016/8/24.
 * Author:Eric_chan
 */
public class DefaultSharePrefManager {

    public static void putString(String key, String value) {
        SharePrefUtil.putString(Constants.NAME_DEFAULT, key, value);
    }

    public static void putInt(String key, int value) {
        SharePrefUtil.putInt(Constants.NAME_DEFAULT, key, value);
    }

    public static void putLong(String key, long value) {
        SharePrefUtil.putLong(Constants.NAME_DEFAULT, key, value);
    }

    public static void putFloat(String key, float value) {
        SharePrefUtil.putFloat(Constants.NAME_DEFAULT, key, value);
    }

    public static void putBoolean(String key, boolean value) {
        SharePrefUtil.putBoolean(Constants.NAME_DEFAULT, key, value);
    }

    public static String getString(String key, String defaultValue) {
        return SharePrefUtil.getString(Constants.NAME_DEFAULT, key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return SharePrefUtil.getInt(Constants.NAME_DEFAULT, key, defaultValue);

    }

    public static long getLong(String key, long defaultValue) {
        return SharePrefUtil.getLong(Constants.NAME_DEFAULT, key, defaultValue);

    }

    public static float getFloat(String key, float defaultValue) {
        return SharePrefUtil.getFloat(Constants.NAME_DEFAULT, key, defaultValue);

    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return SharePrefUtil.getBoolean(Constants.NAME_DEFAULT, key, defaultValue);
    }

    public static void remove(String key) {
        SharePrefUtil.remove(Constants.NAME_DEFAULT, key);
    }

}