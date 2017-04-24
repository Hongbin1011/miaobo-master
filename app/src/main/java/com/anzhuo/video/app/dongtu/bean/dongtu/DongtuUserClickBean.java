package com.anzhuo.video.app.dongtu.bean.dongtu;

import org.litepal.crud.DataSupport;

/**
 * 记录用户点击的情况
 */

public class DongtuUserClickBean extends DataSupport {
    private String dongtuId;
    private boolean Zan = false;
    private boolean cai = false;
    private String imei;
    private boolean commentIs = false;//是否为评论的

    public boolean getCommentIs() {
        return commentIs;
    }

    public DongtuUserClickBean setCommentIs(boolean commentIs) {
        this.commentIs = commentIs;
        return this;
    }

    @Override
    public String toString() {
        return "UserClickBean{" +
                "dongtuId='" + dongtuId + '\'' +
                ", Zan='" + Zan + '\'' +
                ", cai='" + cai + '\'' +
                ", imei='" + imei + '\'' +
                '}';
    }

    public boolean getZan() {
        return Zan;
    }

    public DongtuUserClickBean setZan(boolean zan) {
        Zan = zan;
        return this;
    }

    public boolean getCai() {
        return cai;
    }

    public DongtuUserClickBean setCai(boolean cai) {
        this.cai = cai;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public DongtuUserClickBean setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getDongtuId() {

        return dongtuId;
    }

    public DongtuUserClickBean setDongtuId(String dongtuId) {
        this.dongtuId = dongtuId;
        return this;
    }
}
