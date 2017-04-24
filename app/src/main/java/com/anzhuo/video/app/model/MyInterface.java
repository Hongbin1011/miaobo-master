package com.anzhuo.video.app.model;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface MyInterface {
    //接口返回失败
    void onError(Call call, Exception e);

    //接口返回成功
    void onSucceed(JSONObject obj);

}
