package com.anzhuo.video.app.dongtu.config;

import com.anzhuo.fulishipin.app.BuildConfig;
import com.orhanobut.logger.Logger;

public enum EnvType {

    /**
     * 测试环境
     */
    TEST_D_IP,
    /**
     * 现网环境
     */
    RELEASE;

    public static EnvType getEnvType() {
        boolean DEBUG = BuildConfig.LOG_DEBUG;
        Logger.i("DEBUG= " + DEBUG);
        if (DEBUG) //测试环境，测试的是可以更新环境
            return RELEASE;
        else //线上环境  ，不能改变
            return RELEASE;

    }

}
