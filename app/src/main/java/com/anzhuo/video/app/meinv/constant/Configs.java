package com.anzhuo.video.app.meinv.constant;


import com.anzhuo.video.app.meinv.base.BaseAppServerUrl;

/**
 * Created by wbb on 2016/4/27.
 */
public final class Configs {

    public static void init() {
        switch (EnvType.getEnvType()) {
            case TEST_D_IP:
                // AppServer
                BaseAppServerUrl.baseServerUrl = "http://apptupian.2sdb.com/api/Public/meinv/";//正常接口 测试路径
                break;
            case RELEASE:
                // AppServer
                BaseAppServerUrl.baseServerUrl = "http://apptupian.2sdb.com/api/Public/meinv/";//正常接口 正式路径
                break;
        }
    }

}
