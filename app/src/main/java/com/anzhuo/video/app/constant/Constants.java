package com.anzhuo.video.app.constant;


import android.os.Build;

import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.utils.AppUtil;
import com.anzhuo.video.app.utils.AppUtils;

/**
 * Created by Administrator on 2016/4/1.
 */
public class Constants {

    //    public static String VersionName = AppUtils.getAppInfo(DonglaApplication.getContext()).getVersionName();
//    public static int VersionCode = AppUtils.getAppInfo(DonglaApplication.getContext()).getVersionCode();
    public static String VersionName = AppUtil.getVersionName(VideoApplication.getContext());
    public static int VersionCode = AppUtil.getVersionCode(VideoApplication.getContext());
    //应用 APP_KEY
    public static final String NAME_DEFAULT = "mixiu";
    //手机型号
    public static final String PHONE_MODEL = Build.MODEL;
    //手机名称
    public static final String PHONE_NAME = Build.MANUFACTURER +"——"+ Build.DEVICE;

    //屏幕 宽度
    public static int Pwide = 480;
    //屏幕 高度
    public static int Phigh = 800;
    //包名
    public static String Packages = VideoApplication.getContext().getPackageName();

    public static final String DEVICES_TAG = "1";//设备标识  Android  :1    ios:2

    public static final String LIMIT = "20";//一页获取的数据条数 默认

    //统计appkey
    public static final String Statistics_appkey = "251";


    public static final String Zan = "zan";//点赞
    public static final String uZan = "uzan";//踩
    public static final String ZanId = "zanid";//点赞
    public static final String uZanId = "uzanId";//踩
    //0是赞,1是踩,2是分享
    public static final String getZan = "0";//
    public static final String getCai = "1";//
    public static final String getShare = "2";//
    public static final String getComment = "3";//评论

    /*=====================================*/
    public static String IEMI = android.os.Build.SERIAL;
    //获取到激光推送的Registration ID
//    public static String JpushID = JPushInterface.getRegistrationID(DonlaApplication.getContext());
    public static String INTERNET = AppUtils.getPhoneInfo(VideoApplication.getContext()).getInternetType();//当前网络类型
    public static String DJCCURLKEY = "xwCMX2349WEREWisicms";


    public static String DJCCUSERKEY = "cxmxxiaohua";//加入md5验算
    public static String User_secret = "secret";
    public static String User_r5d87 = "672tg"; //笑话的加密 标志
    public static String USER_VERSION = "2.0";//用户的接口的版本号


    //广点通广告id
    public static String APPID = "1101152570";
    public static String InterteristalPosID = "8575134060152130849";
    //百度广告 重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
    public static final String adPlaceId = "2403633";

    public static final String BUGLY_ID = "4461108a81";

}
