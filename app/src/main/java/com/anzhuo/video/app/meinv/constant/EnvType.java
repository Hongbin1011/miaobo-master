package com.anzhuo.video.app.meinv.constant;

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
        return RELEASE;
    }

}
