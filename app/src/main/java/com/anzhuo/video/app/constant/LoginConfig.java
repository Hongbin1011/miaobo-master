package com.anzhuo.video.app.constant;

import android.content.Context;
import android.content.SharedPreferences;

import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.model.bean.LoginData;

/**
 * Created by Administrator on 2017/3/9/009.
 */

public class LoginConfig {

    public static String LOGIN_TABLE_NAME = "login";
    public static String LOGIN_REMEMBER_STATE = "isLogin";

    public static String LOGIN_USER_INFO = "userinfo";
    public static String LOGIN_USER_ID = "userid";
    public static String LOGIN_USER_PHONE = "userphone";
    public static String LOGIN_USER_NAME = "username";
    public static String LOGIN_USER_LOGO = "userlogo";
    public static String LOGIN_USER_VIP_LEVEL = "userviplevel";
    public static String LOGIN_USER_VIP_EXPRICE = "uservipexprice";
    public static String LOGIN_USER_ISDELETE = "uservipisdelete";

    public static void sf_saveUserInfo(LoginData info) {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(LOGIN_USER_ID, info.getUid());
        editor.putString(LOGIN_USER_NAME, info.getUsername());
        editor.putString(LOGIN_USER_LOGO, info.getLogo());
        editor.putString(LOGIN_USER_VIP_LEVEL, info.getVip_level());
        editor.putString(LOGIN_USER_VIP_EXPRICE, info.getVip_expire());
        editor.putString(LOGIN_USER_ISDELETE, info.getIs_delete());
        editor.commit();
    }

    public static void sf_saveUserName(String name) {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(LOGIN_USER_NAME, name);
        editor.commit();
    }

    public static String getUserExprice() {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_USER_INFO, Context.MODE_PRIVATE);
        return sf.getString(LOGIN_USER_VIP_EXPRICE,"");
    }

    public static String getUserLogo() {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_USER_INFO, Context.MODE_PRIVATE);
        return sf.getString(LOGIN_USER_LOGO,"");
    }

    public static String getUserName() {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_USER_INFO, Context.MODE_PRIVATE);
        return sf.getString(LOGIN_USER_NAME,"");
    }


    public static String getUserId() {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_USER_INFO, Context.MODE_PRIVATE);
        return sf.getString(LOGIN_USER_ID,"");
    }

    public static void sf_saveUserPhone(String state) {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_TABLE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(LOGIN_USER_PHONE, state);
        editor.commit();
    }

    public static String getUserPhone() {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_TABLE_NAME, Context.MODE_PRIVATE);
       return sf.getString(LOGIN_USER_PHONE,"");
    }


    // 保存用户登录状态
    public static void sf_saveLoginState(boolean state) {
        SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_TABLE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean(LOGIN_REMEMBER_STATE, state);
        editor.commit();
    }


    //    获取用户登录状态
    public static boolean sf_getLoginState() {

        try {
            SharedPreferences sf = VideoApplication.getInstance().getApplicationContext().getSharedPreferences(LOGIN_TABLE_NAME, Context.MODE_PRIVATE);
            return sf.getBoolean(LOGIN_REMEMBER_STATE, false);//改为false
        } catch (Exception e) {

        }
        return false;

    }


}
