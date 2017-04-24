package com.anzhuo.video.app.model.bean;

/**
 * Created by Administrator on 2017/3/13/013.
 */

public class CheckVipBean {

    /**
     * vip_level : 0
     * vip_expire : 0
     * vip_level_1 : 1
     * vip_level_2 : 10
     */

    private int vip_level;
    private int vip_expire;
    private int vip_level_1;
    private int vip_level_2;

    public int getVip_level() {
        return vip_level;
    }

    public void setVip_level(int vip_level) {
        this.vip_level = vip_level;
    }

    public int getVip_expire() {
        return vip_expire;
    }

    public void setVip_expire(int vip_expire) {
        this.vip_expire = vip_expire;
    }

    public int getVip_level_1() {
        return vip_level_1;
    }

    public void setVip_level_1(int vip_level_1) {
        this.vip_level_1 = vip_level_1;
    }

    public int getVip_level_2() {
        return vip_level_2;
    }

    public void setVip_level_2(int vip_level_2) {
        this.vip_level_2 = vip_level_2;
    }
}
