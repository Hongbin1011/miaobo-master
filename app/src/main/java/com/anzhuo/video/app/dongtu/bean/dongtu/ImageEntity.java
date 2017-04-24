package com.anzhuo.video.app.dongtu.bean.dongtu;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class ImageEntity {
    private String bigImageUrl = "";//大图地址
    private String bigWidth = "";
    private String bigHeight = "";
    private String smallImageUrl = "";
    private String smallWidth = "";
    private String smallHeight = "";
    private String imgType="";

    public String getImgType() {
        return imgType;
    }

    public ImageEntity setImgType(String imgType) {
        this.imgType = imgType;
        return this;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public ImageEntity setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
        return this;
    }

    public String getBigWidth() {
        return bigWidth;
    }

    public ImageEntity setBigWidth(String bigWidth) {
        this.bigWidth = bigWidth;
        return this;
    }

    public String getBigHeight() {
        return bigHeight;
    }

    public ImageEntity setBigHeight(String bigHeight) {
        this.bigHeight = bigHeight;
        return this;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public ImageEntity setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
        return this;
    }

    public String getSmallWidth() {
        return smallWidth;
    }

    public ImageEntity setSmallWidth(String smallWidth) {
        this.smallWidth = smallWidth;
        return this;
    }

    public String getSmallHeight() {
        return smallHeight;
    }

    public ImageEntity setSmallHeight(String smallHeight) {
        this.smallHeight = smallHeight;
        return this;
    }
}
