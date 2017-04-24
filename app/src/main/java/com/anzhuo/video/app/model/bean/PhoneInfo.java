package com.anzhuo.video.app.model.bean;

/**
 * created on 2016/9/9.
 * 手机相关
 */

public class PhoneInfo {
    private String IEMI = "";//唯一识别号
    private String InternetType = "";//当前手机网络类型
    private String postion = "";//当前手机的位置
    private String MobileType = "";//网络运营商（电信 联通 移动 ）
    private String AllAppNum = "";//手机安装的应用总数
    private String PhoneName = "";//手机的名字
    private String phoneModel = "";//手机型号
    private String systemNum = "";//系统版本

    public PhoneInfo(String IEMI, String internetType, String postion,
                     String mobileType, String allAppNum, String phoneName, String phoneModel, String systemNum) {

        this.IEMI = IEMI;
        InternetType = internetType;
        this.postion = postion;
        MobileType = mobileType;
        AllAppNum = allAppNum;
        PhoneName = phoneName;
        this.phoneModel = phoneModel;
        this.systemNum = systemNum;
    }


    public String getIEMI() {
        return IEMI;
    }

    public void setIEMI(String IEMI) {
        this.IEMI = IEMI;
    }

    public String getPostion() {
        return postion;
    }

    public void setPostion(String postion) {
        this.postion = postion;
    }

    public String getInternetType() {
        return InternetType;
    }

    public void setInternetType(String internetType) {
        InternetType = internetType;
    }

    public String getMobileType() {
        return MobileType;
    }

    public void setMobileType(String mobileType) {
        MobileType = mobileType;
    }

    public String getAllAppNum() {
        return AllAppNum;
    }

    public void setAllAppNum(String allAppNum) {
        AllAppNum = allAppNum;
    }

    public String getPhoneName() {
        return PhoneName;
    }

    public void setPhoneName(String phoneName) {
        PhoneName = phoneName;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                ", IEMI='" + IEMI + '\'' +
                ", InternetType='" + InternetType + '\'' +
                ", postion='" + postion + '\'' +
                ", MobileType='" + MobileType + '\'' +
                ", AllAppNum='" + AllAppNum + '\'' +
                ", PhoneName='" + PhoneName + '\'' +
                ", phoneModel='" + phoneModel + '\'' +
                ", systemNum='" + systemNum + '\'' +
                '}';
    }
}
