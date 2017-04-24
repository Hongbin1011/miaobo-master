package com.anzhuo.video.app.dongtu.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.anzhuo.video.app.config.VideoApplication;
import com.orhanobut.logger.Logger;


/**
 * 名称：保存到 SharedPreferences 的数据.
 *
 * @version v1.0
 */
public class SharedUtil {

    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = VideoApplication.getPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = VideoApplication.getPreferences();
        return sharedPreferences.getInt(key, 0);
    }

    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = VideoApplication.getPreferences();
        Logger.i("cq=============[是否为空 sharedPreferences]===========" + sharedPreferences);
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = VideoApplication.getPreferences();
        return sharedPreferences.getString(key, "0").toString().trim();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = VideoApplication.getPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key,
                                     boolean defValue) {
        SharedPreferences sharedPreferences = VideoApplication.getPreferences();
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void remove(String key) {
        SharedPreferences sharedPreferences = VideoApplication.getPreferences();
        Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
    }


    //-------------在下面就开始保存，减少外面代码---------------

    /**
     * 这个只是作为测试用的
     *
     * @param savaString
     */
    //Vale
    private static String test = "joke";

    /**
     * 存
     *
     * @param savaString
     */
    public static void savaTest(String savaString) {
        putString(test, savaString);
    }

    /**
     * 取
     *
     * @return
     */
    public static String getTest() {
        return getString(test);
    }


    //=========================================================
    //Vale
    private static String test2 = "joke2";

    public static void savaTest2(String savaString) {
        putString(test2, savaString);
    }

    public static String getTest2() {
        return getString(test2);
    }

    //=========================================================
    /**
     * 文字描述什么作用
     */
    private static String test3 = "joke3";

    public static void saveTest3(String savaString) {
        putString(test3, savaString);
    }

    public static String getTest3() {
        return getString(test3);
    }
    //=========================================================
    /**
     * 获取手机唯一识别号
     */
    private static String imei = "imei";

    public static void saveOnlyId() {
        String deviceId = android.os.Build.SERIAL;
        saveIemi(deviceId);
    }

    public static void saveIemi(String savaString) {
        putString(imei, savaString);
    }

    public static String getOnlyId() {
        String onlyId = getString(imei);
        if (onlyId == null || onlyId.equals("")) {
            saveOnlyId();
            onlyId = getString(imei);
        }
        return onlyId;
    }


    //-------------------
    //--------------------------保存用户名和密码--------------
    private static String User_name = "register_Phone";
    private static String password = "Register_Password";


    public static void saveUserName(String UserName) {
        putString(User_name, UserName);
    }

    public static String getUserName() {
        String UserName = getString(User_name);
        return UserName;
    }

    public static void savePswd(String UserName) {
        putString(password, UserName);
    }

    public static String getPswd() {
        String UserName = getString(password);
        return UserName;
    }
    //-------------------------------
}
