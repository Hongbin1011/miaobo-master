package com.anzhuo.video.app.dongtu.widget;

/**
 * Created by Administrator on 2016/11/10 0010.
 * 状态的点击事件
 */

public interface StateViewOnclickCallback {
    void NoNetClick();//没有网络点击

    void NoDataClick();//没有数据

    void NoLoginClick();//没有登录

    void CustomClick();//自定义点击
}
