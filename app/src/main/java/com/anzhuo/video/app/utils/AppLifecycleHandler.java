package com.anzhuo.video.app.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * created on 2016/10/21 0021.
 * 监听APP是否在前台
 */

public class AppLifecycleHandler implements Application.ActivityLifecycleCallbacks {

    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    public AppLifecycleHandler() {
        resetVariables();
    }

    public void resetVariables() {
        resumed = 0;
        paused = 0;
        started = 0;
        stopped = 0;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        android.util.Log.w("test", "application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        android.util.Log.w("test", "application is visible: " + (started > stopped));
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:


    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

    public static boolean isApplicationInBackground() {
        return started == stopped;
    }

}