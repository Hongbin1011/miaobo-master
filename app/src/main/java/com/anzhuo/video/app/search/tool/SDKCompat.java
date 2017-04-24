package com.anzhuo.video.app.search.tool;

import android.os.Build;

/**
 * Android SDK版本兼容处理
 * Author:Eric_chan
 */
public class SDKCompat {

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


}