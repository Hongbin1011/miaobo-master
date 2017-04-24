package com.anzhuo.video.app.dongtu.bean.dongtu;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 列表
 */

public class DongtuContentListBean extends DataSupport implements Serializable {

    /**
     * id : 1
     * title : 做人就该像他一样，设定目标永远不放弃
     * shorttitle :
     * alias :
     * aimurl :
     * picture : http://dtap.oss-cn-beijing.aliyuncs.com/gif/fbb4ad70386fc612f55101cd3e2a17a1.gif
     * bigpicture :
     * lastupdate : 0
     * lastcomment : 0
     * digest :
     * titlecolor :
     * commentnum : 0
     * goodnum : 0
     * badnum : 0
     * favnum : 0
     * downnum : 0
     */

    @Column(ignore = true)
    private String id;

    private String title;
    private String shorttitle;
    private String alias;
    private String aimurl;
    private String picture;
    private String bigpicture;
    private String lastupdate;
    private String lastcomment;
    private String digest;
    private String titlecolor;
    private String commentnum;
    private String goodnum;
    private String badnum;
    private String favnum;
    private String downnum;
    private String dongtuId;
    private String titleType;
    private String picwidth = null;
    private String picheight = null;
    private String lit_url = null;

    /**
     * picwidth : 352
     * picheight : 258
     */
    @Override
    public String toString() {
        return "ContentListBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", shorttitle='" + shorttitle + '\'' +
                ", alias='" + alias + '\'' +
                ", aimurl='" + aimurl + '\'' +
                ", picture='" + picture + '\'' +
                ", bigpicture='" + bigpicture + '\'' +
                ", lastupdate='" + lastupdate + '\'' +
                ", lastcomment='" + lastcomment + '\'' +
                ", digest='" + digest + '\'' +
                ", titlecolor='" + titlecolor + '\'' +
                ", commentnum='" + commentnum + '\'' +
                ", goodnum='" + goodnum + '\'' +
                ", badnum='" + badnum + '\'' +
                ", favnum='" + favnum + '\'' +
                ", downnum='" + downnum + '\'' +
                ", dongtuId='" + dongtuId + '\'' +
                ", titleType='" + titleType + '\'' +
                ", picwidth='" + picwidth + '\'' +
                ", picheight='" + picheight + '\'' +
                ", lit_url='" + lit_url + '\'' +
                '}';
    }

    public String getLit_url() {
        return lit_url;
    }

    public void setLit_url(String lit_url) {
        this.lit_url = lit_url;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getDongtuId() {
        return dongtuId;
    }

    public void setDongtuId(String dongtuId) {
        this.dongtuId = dongtuId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setDongtuId(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShorttitle() {
        return shorttitle;
    }

    public void setShorttitle(String shorttitle) {
        this.shorttitle = shorttitle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAimurl() {
        return aimurl;
    }

    public void setAimurl(String aimurl) {
        this.aimurl = aimurl;
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

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getLastcomment() {
        return lastcomment;
    }

    public void setLastcomment(String lastcomment) {
        this.lastcomment = lastcomment;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getTitlecolor() {
        return titlecolor;
    }

    public void setTitlecolor(String titlecolor) {
        this.titlecolor = titlecolor;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
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

    public String getPicwidth() {
        return picwidth;
    }

    public void setPicwidth(String picwidth) {
        this.picwidth = picwidth;
    }

    public String getPicheight() {
        return picheight;
    }

    public void setPicheight(String picheight) {
        this.picheight = picheight;
    }
}
