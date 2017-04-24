package com.anzhuo.video.app.search.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by hulei on 2016/8/5.
 */
public class SearchRecordInfo extends DataSupport {
    private String keyword;
    private String color;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "SearchRecordInfo{" +
                "keyword='" + keyword + '\'' +
                '}';
    }
}
