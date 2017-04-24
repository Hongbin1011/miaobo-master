package com.anzhuo.video.app.constant;

/**
 * creat on 2016/3/24.
 */
public class BaseAppServerUrl {

    public static String baseServerUrl = "";

    public static String userServerUrl = "";

    public static String statisticsUrl = "";


    public static String videoUrl = "";

    //美女
    public static String baseServerMeinv = "";

//    public static String videoUrl = "";


    public static String getDongtuServerUrl() {
        return  BaseAppServerUrl.videoUrl + "?service=";
    }
    public static String getBaseServerMeinvUrl() {
        return  BaseAppServerUrl.baseServerMeinv + "?service=";
    }

    //AppServerUrl
    public static String getAppServerUrl() {
        return BaseAppServerUrl.baseServerUrl + "?service=";
    }


    /*用户注册登录*/
    public static String getAppServerUserUrl() {
        return BaseAppServerUrl.userServerUrl + "?service=";
    }

    /*用户注册登录*/
    public static String getStatisticsUrl() {
        return BaseAppServerUrl.statisticsUrl + "?service=";
    }

}
