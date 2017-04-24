package com.anzhuo.video.app.dongtu.utils;

import android.view.View;

import java.util.Calendar;

/**
 * created on 2016/9/20.
 * 防止连续点击
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {

    /**
     * 控制不可连续点击的时间间隔[  修改控制时间间隔  ]
     */
    public static final int MIN_CLICK_DELAY_TIME = 1000;//大时间间隔用于测试
    /**
     * 上一次点击的时间
     */
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View view);
}