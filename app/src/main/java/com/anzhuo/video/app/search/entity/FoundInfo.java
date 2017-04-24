package com.anzhuo.video.app.search.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by husong on 2017/2/20.
 */

public class FoundInfo implements Parcelable {
    private String id;
    private String name;
    private String item_id;
    private String is_android;
    private String is_ios;
    private String is_wap;
    private String is_recommend;
    private String px;
    private String stat;
    private String is_delete;
    private String url_open_type;
    private String logo_url;
    private String android_url;
    private String ios_url;
    private String wap_url;
    private String creater;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getIs_android() {
        return is_android;
    }

    public void setIs_android(String is_android) {
        this.is_android = is_android;
    }

    public String getIs_ios() {
        return is_ios;
    }

    public void setIs_ios(String is_ios) {
        this.is_ios = is_ios;
    }

    public String getIs_wap() {
        return is_wap;
    }

    public void setIs_wap(String is_wap) {
        this.is_wap = is_wap;
    }

    public String getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(String is_recommend) {
        this.is_recommend = is_recommend;
    }

    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getUrl_open_type() {
        return url_open_type;
    }

    public void setUrl_open_type(String url_open_type) {
        this.url_open_type = url_open_type;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }

    public String getIos_url() {
        return ios_url;
    }

    public void setIos_url(String ios_url) {
        this.ios_url = ios_url;
    }

    public String getWap_url() {
        return wap_url;
    }

    public void setWap_url(String wap_url) {
        this.wap_url = wap_url;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.item_id);
        dest.writeString(this.is_android);
        dest.writeString(this.is_ios);
        dest.writeString(this.is_wap);
        dest.writeString(this.is_recommend);
        dest.writeString(this.px);
        dest.writeString(this.stat);
        dest.writeString(this.is_delete);
        dest.writeString(this.url_open_type);
        dest.writeString(this.logo_url);
        dest.writeString(this.android_url);
        dest.writeString(this.ios_url);
        dest.writeString(this.wap_url);
        dest.writeString(this.creater);
        dest.writeString(this.desc);
    }

    public FoundInfo() {
    }

    protected FoundInfo(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.item_id = in.readString();
        this.is_android = in.readString();
        this.is_ios = in.readString();
        this.is_wap = in.readString();
        this.is_recommend = in.readString();
        this.px = in.readString();
        this.stat = in.readString();
        this.is_delete = in.readString();
        this.url_open_type = in.readString();
        this.logo_url = in.readString();
        this.android_url = in.readString();
        this.ios_url = in.readString();
        this.wap_url = in.readString();
        this.creater = in.readString();
        this.desc = in.readString();
    }

    public static final Creator<FoundInfo> CREATOR = new Creator<FoundInfo>() {
        @Override
        public FoundInfo createFromParcel(Parcel source) {
            return new FoundInfo(source);
        }

        @Override
        public FoundInfo[] newArray(int size) {
            return new FoundInfo[size];
        }
    };
}
