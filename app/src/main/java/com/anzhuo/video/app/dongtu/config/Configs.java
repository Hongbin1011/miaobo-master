package com.anzhuo.video.app.dongtu.config;


/**
 * Created by wbb on 2016/4/27.
 */
public final class Configs {

    public static void init() {
        switch (EnvType.getEnvType()) {
            case TEST_D_IP://测试
                // AppServer
                DongtuAppServerUrl.baseServerUrl = "http://testapi.xiaohua.com/Xiaohua/";
                DongtuAppServerUrl.userServerUrl = "http://api.user.xiaohua.com/";
                DongtuAppServerUrl.statisticsUrl = "http://testapi.tongji.com/";
                //动图
                DongtuAppServerUrl.dongtuUrl = "http://testapi.dongtu.anzhuo.com/";

                break;
            case RELEASE://正式、线上
                // AppServer
                DongtuAppServerUrl.baseServerUrl = "http://api.mtdz.xiaohua.com/Xiaohua/";
                DongtuAppServerUrl.userServerUrl = "http://api.user.xiaohua.com/";
                DongtuAppServerUrl.statisticsUrl = "http://api.tj.anzhuo.com/";

                DongtuAppServerUrl.dongtuUrl = "http://api.dongtu.1122.com/";

                break;
        }
    }

}
