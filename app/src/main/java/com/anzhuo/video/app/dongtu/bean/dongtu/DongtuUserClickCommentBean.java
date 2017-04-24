package com.anzhuo.video.app.dongtu.bean.dongtu;

import org.litepal.crud.DataSupport;

/**
 * 记录用户点击的情况
 */

public class DongtuUserClickCommentBean extends DataSupport {
    private String dongtuId;
    private boolean Zan = false;
    private boolean cai = false;
    private String imei;
    private boolean commentIs = false;//是否为评论的
    private String itemid;

    public String getItemid() {//类型
        return itemid;
    }

    public DongtuUserClickCommentBean setItemid(String itemid) {
        this.itemid = itemid;
        return this;
    }


    public boolean getCommentIs() {
        return commentIs;
    }

    public DongtuUserClickCommentBean setCommentIs(boolean commentIs) {
        this.commentIs = commentIs;
        return this;
    }

    @Override
    public String toString() {
        return "UserClickCommentBean{" +
                "dongtuId='" + dongtuId + '\'' +
                ", Zan=" + Zan +
                ", cai=" + cai +
                ", imei='" + imei + '\'' +
                ", commentIs=" + commentIs +
                ", itemid='" + itemid + '\'' +
                '}';
    }

    public boolean getZan() {
        return Zan;
    }

    public DongtuUserClickCommentBean setZan(boolean zan) {
        Zan = zan;
        return this;
    }

    public boolean getCai() {
        return cai;
    }

    public DongtuUserClickCommentBean setCai(boolean cai) {
        this.cai = cai;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public DongtuUserClickCommentBean setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getDongtuId() {

        return dongtuId;
    }

    public DongtuUserClickCommentBean setDongtuId(String dongtuId) {
        this.dongtuId = dongtuId;
        return this;
    }
}
