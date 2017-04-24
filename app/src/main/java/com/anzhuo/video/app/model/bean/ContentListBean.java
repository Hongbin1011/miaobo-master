package com.anzhuo.video.app.model.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 列表
 */

public class ContentListBean extends DataSupport implements Serializable {

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
     * v_aimurl : upload/video/T5HFib1nm_s.mp4
     * v_oss_aimurl : http://apptupian.2sdb.com/video/T5HFib1nm_s.mp4
     * v_id : T5HFib1nm_s
     * v_width : 360
     * v_height : 360
     * pic_url : upload/img/T5HFib1nm_s.jpeg
     * pic_oss_url : http://apptupian.2sdb.com/images/T5HFib1nm_s.jpeg
     * pic_width : 196
     * pic_height : 110
     */

    private String v_aimurl;
    private String v_oss_aimurl;
    private String v_id;
    private String v_width;
    private String v_height;
    private String pic_url;
    private String pic_oss_url;
    private String pic_width;
    private String pic_height;

    private String ad_url;
    private String is_ad;
    private String price;

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public String getIs_ad() {
        return is_ad;
    }

    public void setIs_ad(String is_ad) {
        this.is_ad = is_ad;
    }

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
                ", v_aimurl='" + v_aimurl + '\'' +
                ", v_oss_aimurl='" + v_oss_aimurl + '\'' +
                ", v_id='" + v_id + '\'' +
                ", v_width='" + v_width + '\'' +
                ", v_height='" + v_height + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", pic_oss_url='" + pic_oss_url + '\'' +
                ", pic_width='" + pic_width + '\'' +
                ", pic_height='" + pic_height + '\'' +
                ", ad_url='" + ad_url + '\'' +
                ", is_ad='" + is_ad + '\'' +
                ", price='" + price + '\'' +
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getV_aimurl() {
        return v_aimurl;
    }

    public void setV_aimurl(String v_aimurl) {
        this.v_aimurl = v_aimurl;
    }

    public String getV_oss_aimurl() {
        return v_oss_aimurl;
    }

    public void setV_oss_aimurl(String v_oss_aimurl) {
        this.v_oss_aimurl = v_oss_aimurl;
    }

    public String getV_id() {
        return v_id;
    }

    public void setV_id(String v_id) {
        this.v_id = v_id;
    }

    public String getV_width() {
        return v_width;
    }

    public void setV_width(String v_width) {
        this.v_width = v_width;
    }

    public String getV_height() {
        return v_height;
    }

    public void setV_height(String v_height) {
        this.v_height = v_height;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPic_oss_url() {
        return pic_oss_url;
    }

    public void setPic_oss_url(String pic_oss_url) {
        this.pic_oss_url = pic_oss_url;
    }

    public String getPic_width() {
        return pic_width;
    }

    public void setPic_width(String pic_width) {
        this.pic_width = pic_width;
    }

    public String getPic_height() {
        return pic_height;
    }

    public void setPic_height(String pic_height) {
        this.pic_height = pic_height;
    }
}
