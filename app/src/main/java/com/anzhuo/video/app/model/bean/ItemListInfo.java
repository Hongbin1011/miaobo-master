package com.anzhuo.video.app.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by wbb on 2016/8/4.
 *
 * 首页fragment图片列表模型
 */
public class ItemListInfo extends DataSupport implements Serializable {
    private String id_;//ID
    private String title;//标题
    private String category;//所属导航菜单类别
    private String type;//---
    private String pageview;//---
    private String picture;//图片路径
    private String bigpicture;//大图片路径
    private String goodnum;//点赞数
    private String badnum;//点踩数
    private String favnum;//喜欢数
    private String downnum;//下载数
    private String num;//总条数
    private String isCollection;
    private String isLike;
    private String p_isShould="0";//0=false , 1=true
    private String p_isChecked="0";//0=false , 1=true

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPageview() {
        return pageview;
    }

    public void setPageview(String pageview) {
        this.pageview = pageview;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBigpicture() {
        return bigpicture;
    }

    public void setBigpicture(String bigpicture) {
        this.bigpicture = bigpicture;
    }

    public String getGoodnum() {
        return goodnum;
    }

    public void setGoodnum(String goodnum) {
        this.goodnum = goodnum;
    }

    public String getBadnum() {
        return badnum;
    }

    public void setBadnum(String badnum) {
        this.badnum = badnum;
    }

    public String getFavnum() {
        return favnum;
    }

    public void setFavnum(String favnum) {
        this.favnum = favnum;
    }

    public String getDownnum() {
        return downnum;
    }

    public void setDownnum(String downnum) {
        this.downnum = downnum;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getIsShould() {
        return p_isShould;
    }

    public void setIsShould(String isShould) {
        this.p_isShould = isShould;
    }

    public String getIsChecked() {
        return p_isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.p_isChecked = isChecked;
    }

    public void setAutoChedck() {
        String state = getIsChecked().equals("0")?"1":"0";
        setIsChecked(state);
    }

    @Override
    public String toString() {
        return "ItemListInfo{" +
                "id_='" + id_ + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", pageview='" + pageview + '\'' +
                ", picture='" + picture + '\'' +
                ", bigpicture='" + bigpicture + '\'' +
                ", goodnum='" + goodnum + '\'' +
                ", badnum='" + badnum + '\'' +
                ", favnum='" + favnum + '\'' +
                ", downnum='" + downnum + '\'' +
                ", num='" + num + '\'' +
                ", isCollection='" + isCollection + '\'' +
                ", isLike='" + isLike + '\'' +
                ", p_isShould='" + p_isShould + '\'' +
                ", p_isChecked='" + p_isChecked + '\'' +
                '}';
    }
}
