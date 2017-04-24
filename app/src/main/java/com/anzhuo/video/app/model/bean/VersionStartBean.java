package com.anzhuo.video.app.model.bean;

/**
 * Created by Administrator on 2017/1/20/020.
 */

public class VersionStartBean {

    /**
     * vision : 1.0.0
     * download_url : http://apptupian.2sdb.com/download_app/meinv_wx_110_1_wx_sign.apk
     * ad_img : http://apptupian.2sdb.com/download_app/img/ad1.jpg
     * ad_url : http://d.gzceub.com/
     * ad_title : 秒播APP下载
     * share_url : http://suo.im/3l0sNF
     */

    private String vision="";
    private String download_url;
    private String ad_img;
    private String ad_url;
    private String ad_title;
    private String share_url;
    /**
     * ad_img_width : 750
     * ad_img_height : 250
     * ad_ios_show : 0
     */

    private String ad_img_width;
    private String ad_img_height;
    private String ad_ios_show;

    private String ad_kaipin_show;

    @Override
    public String toString() {
        return "VersionStartBean{" +
                "vision='" + vision + '\'' +
                ", download_url='" + download_url + '\'' +
                ", ad_img='" + ad_img + '\'' +
                ", ad_url='" + ad_url + '\'' +
                ", ad_title='" + ad_title + '\'' +
                ", share_url='" + share_url + '\'' +
                ", ad_img_width='" + ad_img_width + '\'' +
                ", ad_img_height='" + ad_img_height + '\'' +
                ", ad_ios_show='" + ad_ios_show + '\'' +
                ", ad_kaipin_show='" + ad_kaipin_show + '\'' +
                '}';
    }

    public String getAd_kaipin_show() {
        return ad_kaipin_show;
    }

    public void setAd_kaipin_show(String ad_kaipin_show) {
        this.ad_kaipin_show = ad_kaipin_show;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getAd_img() {
        return ad_img;
    }

    public void setAd_img(String ad_img) {
        this.ad_img = ad_img;
    }

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getAd_img_width() {
        return ad_img_width;
    }

    public void setAd_img_width(String ad_img_width) {
        this.ad_img_width = ad_img_width;
    }

    public String getAd_img_height() {
        return ad_img_height;
    }

    public void setAd_img_height(String ad_img_height) {
        this.ad_img_height = ad_img_height;
    }

    public String getAd_ios_show() {
        return ad_ios_show;
    }

    public void setAd_ios_show(String ad_ios_show) {
        this.ad_ios_show = ad_ios_show;
    }

}
