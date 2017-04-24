package com.anzhuo.video.app.meinv.base;

/**
 * Created by wbb on 2016/3/24.
 */
public class BaseAppServerUrl {

    public static String baseServerUrl = "";

    public static String userServerUrl = "";

    public static String appStatisticalUrl = "";

    //AppServerUrl
    public static String getAppServerUrl() {
        return BaseAppServerUrl.baseServerUrl + "?service=";
    }

    /*用户注册登录*/
    public static String getAppServerUserUrl() {
        return BaseAppServerUrl.userServerUrl + "?service=";
    }

    /*APP统计接口路径*/
    public static String getAppStatisticaUrl() {
        return BaseAppServerUrl.appStatisticalUrl;
    }

}
