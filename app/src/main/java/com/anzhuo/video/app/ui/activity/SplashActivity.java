package com.anzhuo.video.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.video.app.manager.fuli.NetUtil;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.VersionStartBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.badoo.mobile.util.WeakHandler;
import com.orhanobut.logger.Logger;
import com.sdk9500.media.InterFace.SplashListener;
import com.sdk9500.media.StartSDK;

import okhttp3.Call;


/**
 * 启动页
 */
public class SplashActivity extends Activity {

    private WeakHandler WeakHandler; // We still need at least one hard reference to WeakHandler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (NetUtil.isNetworkEnable(this)) {
            HomeModel.getStart(new NewInterface() {
                @Override
                public void onError(Call call, Exception e) {
                    startActivitys();
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                    try {
                        Logger.i("=======[加载成功]===========" + data);
                        VersionStartBean versionStartBean = JSON.parseObject(data, VersionStartBean.class);
                        String ad_kaipin_show = versionStartBean.getAd_kaipin_show();
                        Logger.i("=======[加载成功 广告开关]===========" + ad_kaipin_show);
                        if (ad_kaipin_show.equals("1")) {//开
                            /**
                             开屏广告，必须在初始化之后调用
                             */
                            StartSDK.ShowAplash(SplashActivity.this, new SplashListener() {
                                @Override
                                public void Finish() {
                                    //开屏广告关闭的回调
                                    //检查token时效性
                                    startActivitys();
                                }
                            });
                        } else {//关
                            startActivitys();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        startActivitys();
                    }
                }
            });
        } else {
            Logger.i("=======[加载成功 广告开关 没有网络]===========");
            startActivitys();
        }

//        WeakHandler = new WeakHandler();
//        WeakHandler.postDelayed(mRunnable, 1500);

    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            startActivitys();
        }
    };

    private void startActivitys() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
