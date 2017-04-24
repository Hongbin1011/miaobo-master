package com.anzhuo.video.app.dongtu.model.base;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.utils.HttpUtils;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;


/**
 * creat on 2016/8/5 16:26
 * 基础的网络请求
 */
public class BaseModel {


    /**
     * 得到上下文
     */
    public static Context getContext() {
        return VideoApplication.getContext();
    }

    /**
     * 公共方法
     * 将请求结果放到 MyInterface 中
     *
     * @param response
     * @param myInterface
     */
    public static void setMyInterface(String response, NewInterface myInterface) {
        try {
//            Logger.json(response);
            JSONObject jsonObject = JSON.parseObject(response);
            int ret = jsonObject.getInteger("ret");
            String data = jsonObject.getString("data");
            //返回ret状态码、data数据 、和jsonObject 。
            myInterface.onSucceed(ret, data, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 公共请求的
     *
     * @param URL         地址
     * @param hashMap
     * @param myInterface
     */
    public static void initHttp(String URL, HashMap<String, String> hashMap, final NewInterface myInterface) {
        Logger.i("baseHttpHashMap=" + hashMap.toString());
        Logger.i("=======[baseHttpHashMap url]===========" + URL + hashMap.toString());
      /*  if (XUtilNet.isNetConnected() == false) {//先判断是否有网络
            ToastUtil.showToast(getResource().getString(R.string.check_net_is_connect));
            return;
        }*/
        HttpUtils.postStringAsync(URL, hashMap, getContext(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
                if (!call.isCanceled()) {
                    myInterface.onError(call, e);
                } else {
                    Logger.i("call.isCanceled()");
                }
            }

            @Override
            public void onResponse(String response,int id) {
                if (TextUtils.isEmpty(response)) {
                    Logger.i("");
                } else {
                    setMyInterface(response, myInterface);
                }
            }
        });
//       HttpUtils.postStringAsync(URL, hashMap, getContext(), new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Logger.i("call= " + call + " e=" + e + "  id=" + id);
//                if (!call.isCanceled()) {
//                    myInterface.onError(call, e);
//                } else {
//                    Logger.i("call.isCanceled()");
//                }
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                Logger.i("========[请求数据称]============="+response.toString());
//                if (TextUtils.isEmpty(response)) {
//                    Logger.i("");
//                } else {
//                    setMyInterface(response, myInterface);
//                }
//            }
//        });

    }

    /**
     * 公共请求的
     *
     * @param URL         地址
     * @param hashMap
     * @param myInterface
     */
//    public static void initHttp2(Context context, String URL, HashMap<String, String> hashMap, Object tag, final NewInterface myInterface) {
//        Logger.i("baseHttpHashMap=" + hashMap.toString());
//        if (XUtilNet.isNetConnected() == false) {//先判断是否有网络
//            ToastUtil.showToast(getResource().getString(R.string.check_net_is_connect));
//            return;
//        }
//
//        HttpUtils.postStringAsync(URL, hashMap, tag, new MyStringCallBack(context) {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                if (!call.isCanceled()) {
//                    myInterface.onError(call, e);
//                } else {
//                    Logger.i("call.isCanceled()");
//                }
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                if (TextUtils.isEmpty(response)) {
//                    Logger.i("");
//                } else {
//                    setMyInterface(response, myInterface);
//                }
//            }
//        });
//
//    }

}
