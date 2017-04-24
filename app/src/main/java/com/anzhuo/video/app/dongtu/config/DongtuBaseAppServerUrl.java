package com.anzhuo.video.app.dongtu.config;

/**
 * Created by wbb on 2016/3/24.
 */
public class DongtuBaseAppServerUrl {

    public static String baseServerUrl = "";

    public static String userServerUrl = "";

    public static String statisticsUrl = "";


    public static String dongtuUrl = "";


    public static String getDongtuServerUrl() {
        return  DongtuBaseAppServerUrl.dongtuUrl + "?service=";
    }

    //AppServerUrl
    public static String getAppServerUrl() {
        return DongtuBaseAppServerUrl.baseServerUrl + "?service=";
    }


    /*用户注册登录*/
    public static String getAppServerUserUrl() {
        return DongtuBaseAppServerUrl.userServerUrl + "?service=";
    }

    /*用户注册登录*/
    public static String getStatisticsUrl() {
        return DongtuBaseAppServerUrl.statisticsUrl + "?service=";
    }

}
