package com.anzhuo.video.app.model.home;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONObject;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.manager.jm.JmTools;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.base.BaseModel;
import com.anzhuo.video.app.model.bean.AppInfo;
import com.anzhuo.video.app.model.bean.PhoneInfo;
import com.anzhuo.video.app.utils.AppUtils;
import com.anzhuo.video.app.utils.PackageUtil;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * creat on 2016/8/23.
 * QQ-632671653
 */
public class StatisticsModel extends BaseModel {

    /**
     * 统计第一次安装打开
     *
     * @param myInterface
     */
    public static void FirstOpen(NewInterface myInterface) {
        AppInfo appInfo = AppUtils.getAppInfo(VideoApplication.getContext());
        PhoneInfo phoneInfo = AppUtils.getPhoneInfo(VideoApplication.getContext());
        String t = System.currentTimeMillis() + "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_key", Constants.Statistics_appkey);
        jsonObject.put("qid", appInfo.getCHANNEL());//渠道ID
        jsonObject.put("imei", phoneInfo.getIEMI());
        jsonObject.put("phone_name", phoneInfo.getPhoneName());
        jsonObject.put("internet", phoneInfo.getInternetType());
        jsonObject.put("intertype", phoneInfo.getMobileType());
        jsonObject.put("appcount", phoneInfo.getAllAppNum());
        jsonObject.put("applist", PackageUtil.getAllAppInfoNoSystem(getContext()));
        jsonObject.put("app_version", appInfo.getVersionName());
        jsonObject.put("phone_model", phoneInfo.getPhoneModel());
        jsonObject.put("system_version", phoneInfo.getSystemNum());
        jsonObject.put("type", "1");
        jsonObject.put("contype", "1");
        HashMap hashMap = new HashMap();
        hashMap.put("service", "Firstlogin.Newindex");
        hashMap.put("T", t);
        hashMap.put("data", JmTools.encryptionEnhanced(t, jsonObject.toString()));
        //必须放在最后面 ， 改下方法名即可。
        Logger.i("jsonObject" + jsonObject.toString());
        initHttp(AppServerUrl.Firstlogin_Newindex, hashMap, myInterface);
    }

    public static void OpenApp(NewInterface myInterface) {
        AppInfo appInfo = AppUtils.getAppInfo(VideoApplication.getContext());
        PhoneInfo phoneInfo = AppUtils.getPhoneInfo(VideoApplication.getContext());
        String t = System.currentTimeMillis() + "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_key", Constants.Statistics_appkey);
        jsonObject.put("qid", appInfo.getCHANNEL());
        jsonObject.put("imei", phoneInfo.getIEMI());
        jsonObject.put("internet", phoneInfo.getInternetType());
        jsonObject.put("app_version", appInfo.getVersionName());
        jsonObject.put("type", "1");
        HashMap hashMap = new HashMap();
        hashMap.put("service", "Openapp.Newindex");
        hashMap.put("T", t);
        hashMap.put("data", JmTools.encryptionEnhanced(t, jsonObject.toString()));
        //必须放在最后面 ， 改下方法名即可。
        initHttp(AppServerUrl.Openapp_Newindex, hashMap, myInterface);
    }


    /**
     * 初始化程序
     * 获取打包渠道ID
     */

    public static String getqid(Context context) {
        ApplicationInfo info;
        try {
            info = context.getApplicationContext().getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(),
                    PackageManager.GET_META_DATA);
            String msg = info.metaData.getString("UMENG_CHANNEL");
            return msg;
        } catch (Exception e) {
            return null;
        }
    }
}
