package com.anzhuo.video.app.utils;

import android.content.Context;

import com.anzhuo.video.app.widget.VersionUpDialog;
import com.anzhuo.video.app.widget.VersionUpDownDialog;

import java.io.File;

/**
 * 分享
 * creat on 2016/6/30.
 * QQ-632671653
 */
public class DialogUtils {

    /**
     * 版本升级弹窗
     *
     * @param context
     * @param
     */
    public static void showVersionUpDialog(Context context) {
        VersionUpDialog versionUpDialog = new VersionUpDialog(context);
        versionUpDialog.show();
    }

    /**
     * 升级下载提示框
     *
     * @param context
     */
    public static void showVersionDownDialog(Context context) {
        VersionUpDownDialog versionUpDownDialog = new VersionUpDownDialog(context);
        versionUpDownDialog.show();
    }

    /**
     * 安装提示框
     *
     * @param context
     */
    public static void showInstallAPKDialog(Context context, File file) {
//        if (PackageUtil.isApkValid(context, file)) {
            PackageUtil.startInstall(context, file);
//        } else {
//            Toast.makeText(context, "apk不合法", Toast.LENGTH_SHORT).show();
//        }
    }






}
