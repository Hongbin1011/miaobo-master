package com.anzhuo.video.app.search.entity;

import java.io.Serializable;

/**
 * Created by wbb on 2016/8/2.
 */
public class PictureDetailInfo implements Serializable {

    /**
     * id : 28
     * itemid : 6
     * category : 155
     * filename : http://img.3352.com/upload/picture/2016/07-24/47nC5d.jpg
     * smallfile : http://img.3352.com/upload/sitem/picture/2016/07-24/47nC5d.jpg
     * smallheight : 0
     * filesize : 620x457
     * dateline : 1469328777
     * orderby : 1
     * goodnum : 0
     * title: GFM极度诱惑情趣内衣睡裙
     */
    private String id;
    private String itemid;
    private String category;
    private String filename;
    private String smallfile;
    private String smallheight;
    private String filesize;
    private String dateline;
    private String orderby;
    private String goodnum;
    private String title;
    private String fileurl;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSmallfile() {
        return smallfile;
    }

    public void setSmallfile(String smallfile) {
        this.smallfile = smallfile;
    }

    public String getSmallheight() {
        return smallheight;
    }

    public void setSmallheight(String smallheight) {
        this.smallheight = smallheight;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getGoodnum() {
        return goodnum;
    }

    public void setGoodnum(String goodnum) {
        this.goodnum = goodnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PictureDetailInfo{" +
                "id='" + id + '\'' +
                ", itemid='" + itemid + '\'' +
                ", category='" + category + '\'' +
                ", filename='" + filename + '\'' +
                ", smallfile='" + smallfile + '\'' +
                ", smallheight='" + smallheight + '\'' +
                ", filesize='" + filesize + '\'' +
                ", dateline='" + dateline + '\'' +
                ", orderby='" + orderby + '\'' +
                ", goodnum='" + goodnum + '\'' +
                ", title='" + title + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
