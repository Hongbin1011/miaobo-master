package com.anzhuo.video.app.model.bean;

/**
 * 评论
 */

public class CommentBean {
    @Override
    public String toString() {
        return "CommentBean{" +
                "id='" + id + '\'' +
                ", itemid='" + itemid + '\'' +
                ", category='" + category + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                ", goodnum='" + goodnum + '\'' +
                ", badnum='" + badnum + '\'' +
                '}';
    }

    /**
     * id : 1
     * itemid : 1
     * category : 180
     * uid : 0
     * username : 重庆网友79955
     * title : 0
     * message : 123
     * dateline : 1482487367
     * goodnum : 1
     * badnum : 0
     */

    private String id = "";
    private String itemid;
    private String category;
    private String uid;
    private String username;
    private String title;
    private String message;
    private String dateline;
    private String goodnum = "0";
    private String badnum;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
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
}
