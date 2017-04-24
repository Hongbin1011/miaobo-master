package com.anzhuo.video.app.search.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by husong on 2017/2/20.
 */

public class BaseFoundInfo implements Parcelable {
    private String id;
    private String title;
    private String logo_url;
    private String showtype;
    private List<FoundInfo> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public List<FoundInfo> getList() {
        return list;
    }

    public void setList(List<FoundInfo> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.logo_url);
        dest.writeString(this.showtype);
        dest.writeList(this.list);
    }

    public BaseFoundInfo() {
    }

    protected BaseFoundInfo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.logo_url = in.readString();
        this.showtype = in.readString();
        this.list = new ArrayList<FoundInfo>();
        in.readList(this.list, FoundInfo.class.getClassLoader());
    }

    public static final Creator<BaseFoundInfo> CREATOR = new Creator<BaseFoundInfo>() {
        @Override
        public BaseFoundInfo createFromParcel(Parcel source) {
            return new BaseFoundInfo(source);
        }

        @Override
        public BaseFoundInfo[] newArray(int size) {
            return new BaseFoundInfo[size];
        }
    };
}
