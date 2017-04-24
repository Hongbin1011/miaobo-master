package com.anzhuo.video.app.dongtu.config;


import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.dongtu.utils.AppUtil;

/**
 * Created by Administrator on 2016/4/1.
 */
public class DongtuConstants {

    public static String VersionName = AppUtil.getVersionName(VideoApplication.getContext());
    public static int VersionCode = AppUtil.getVersionCode(VideoApplication.getContext());

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
//    public static String INTERNET = AppUtils.getPhoneInfo(VideoApplication.getContext()).getInternetType();//当前网络类型
    public static String DJCCURLKEY = "xwCMX2349WEREWisicms";


    public static String DJCCUSERKEY = "cxmxxiaohua";//加入md5验算
    public static String User_secret = "secret";
    public static String User_r5d87 = "672tg"; //笑话的加密 标志
    public static String USER_VERSION = "2.0";//用户的接口的版本号

}
