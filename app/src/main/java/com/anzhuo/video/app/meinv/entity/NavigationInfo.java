package com.anzhuo.video.app.meinv.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by wbb on 2016/8/3.
 */
public class NavigationInfo extends DataSupport {

    private String tags;    //类型
    private String recom;   //是否默认推荐

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRecom() {
        return recom;
    }

    public void setRecom(String recom) {
        this.recom = recom;
    }

    @Override
    public String toString() {
        return "NavigationInfo{" +
                "tags='" + tags + '\'' +
                ", recom='" + recom + '\'' +
                '}';
    }
}
