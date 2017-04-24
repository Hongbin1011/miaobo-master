package com.anzhuo.video.app.constant;

/**
 * creat on 2016/4/27.
 */
public final class Configs {

    public static void init() {
        switch (EnvType.getEnvType()) {
            case TEST_D_IP://测试
                // AppServer
                BaseAppServerUrl.baseServerUrl = "http://api.mtdz.xiaohua.com/Xiaohua/";
//                BaseAppServerUrl.userServerUrl = "http://api.user.xiaohua.com/";
                BaseAppServerUrl.userServerUrl = "http://api.gzceub.com";
                BaseAppServerUrl.statisticsUrl = "http://api.tongji.com/";

//                BaseAppServerUrl.dongtuUrl = "http://testapi.dongtu.anzhuo.com/";
                BaseAppServerUrl.videoUrl = "http://api.chzred.com/";

                //美女
                BaseAppServerUrl.baseServerMeinv = "http://apptupian.2sdb.com/api/Public/meinv/";//正常接口 测试路径

                break;
            case RELEASE://正式、线上
                // AppServer
                BaseAppServerUrl.baseServerUrl = "http://api.mtdz.xiaohua.com/Xiaohua/";
//                BaseAppServerUrl.userServerUrl = "http://api.user.xiaohua.com/";
                BaseAppServerUrl.userServerUrl = "http://api.gzceub.com";
                BaseAppServerUrl.statisticsUrl = "http://api.tj.anzhuo.com/";

//                BaseAppServerUrl.dongtuUrl = "http://api.dongtu.anzhuo.com/";
                BaseAppServerUrl.videoUrl = "http://api.chzred.com/";

                //美女
                BaseAppServerUrl.baseServerMeinv = "http://apptupian.2sdb.com/api/Public/meinv/";//正常接口 正式路径


                break;
        }
    }

}
