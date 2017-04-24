package com.anzhuo.video.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by Administrator on 2016/3/30.
 */
public class AppUtil {
    public static void uninstallApp(Activity context, String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivityForResult(intent, 1000);
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */

    public static boolean checkPackage(Activity context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取imei
     */
//    public static String getImei() {
//        TelephonyManager telephonyMgr = (TelephonyManager) UIUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceId = telephonyMgr.getDeviceId();
//        if (TextUtils.isEmpty(deviceId)) {
//            File f = new File(Constants.FAKEIMEI);
//            if (f.exists()) {
//                BufferedReader br = null;
//                try {
//                    br = new BufferedReader(new FileReader(f));
//                    deviceId = br.readLine();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    IOUtils.close(br);
//                }
//            } else {
//                deviceId = System.currentTimeMillis() + "";
//                FileUtils.writeFile(deviceId, Constants.FAKEIMEI, false);
//            }
//        }
//        return deviceId;
//    }

    /**
     * 获取APP版本
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取APP版本
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取手机版本
     *
     * @return
     */
    public static String getMobileVersion() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机系统
     *
     * @return
     */
    public static String getMobileSystem() {
        return android.os.Build.VERSION.RELEASE;
    }
}
