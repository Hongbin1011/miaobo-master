package com.anzhuo.video.app.model.bean;

/**
 * created on 2016/5/5.
 * 需要分享的
 */
public class ShareInfo {

    private String title = "";
    private String context = "";//分享的文字内容
    private String url = "";
    private String imgUrl = "";
    private String Jid = "";//笑话id
    private String isCollect = "";//0  没有收藏  1：已经收藏
    private String jokeUserID = "";//笑话的作者ID


    public ShareInfo() {
    }

    public ShareInfo(String context, String title, String url, String imgUrl, String jid) {
        this.title = title;
        this.context = context;
        this.url = url;
        this.imgUrl = imgUrl;
        Jid = jid;
    }

    public String getJId() {
        return Jid;
    }

    public void setJId(String id) {
        this.Jid = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", Jid='" + Jid + '\'' +
                ", isCollect='" + isCollect + '\'' +
                ", jokeUserID='" + jokeUserID + '\'' +
                '}';
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getJokeUserID() {
        return jokeUserID;
    }

    public void setJokeUserID(String jokeUserID) {
        this.jokeUserID = jokeUserID;
    }
}
