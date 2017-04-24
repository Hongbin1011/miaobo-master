package com.anzhuo.video.app.model.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/9/9.
 * 将App已经手机相关信息封装
 * 可以用于统计
 */

public class AppInfo {
    private String name;//App名称
    private Drawable icon;//图标
    private String packageName;//包名
    private String packagePath;//包的路径
    private String versionName;//版本名字
    private int versionCode;//版本号
    private boolean isSD;// 是否安装在SD卡
    private boolean isUser;//  是否是用户程序
    private String CHANNEL = "";//渠道

    public AppInfo(String name, Drawable icon, String packageName, String packagePath, String versionName, int versionCode, boolean isSD, boolean isUser, String CHANNEL) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.packagePath = packagePath;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.isSD = isSD;
        this.isUser = isUser;
        this.CHANNEL = CHANNEL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isSD() {
        return isSD;
    }

    public void setSD(boolean SD) {
        isSD = SD;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", packageName='" + packageName + '\'' +
                ", packagePath='" + packagePath + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", isSD=" + isSD +
                ", isUser=" + isUser +
                ", CHANNEL='" + CHANNEL + '\'' +
                '}';
    }
}
