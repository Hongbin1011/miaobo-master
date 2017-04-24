package com.anzhuo.video.app.model.bean;


import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 作者：熊晓清 on 2016/5/4 20:24
 * 笑话列表的实体
 */
public class JokeEntity extends DataSupport implements Serializable {
    //用于区分推荐、视频、图片、段子
    private String typeName = "";

    private String jokeID = "";

    private String is_best = "";//是不是热门
    @Column(ignore = true)
    private String id = "";

    private String creater = "";

    private String biz_no = "";

    private String content = "";

    private String ctime = "";
    private String audit_status = "";//1未审核2审核通过3审核未通过


    private String good_num = "";

    private String low_num = "";

    private String share_num = "";

    private String comment_num = "";

    private String type = "";

    private String source = "";

    private List<VideoInfo> video = new ArrayList<>();

    private ComUserInfo com_user_info = new ComUserInfo();

    private String date = "";

    private List<JokeImgInfo> images = new ArrayList<>();
    private List<CommentInfo> comment = new ArrayList<>();


    public String isGoods = "0";

    public String isLow = "0";

    private int likes = -2;
    //0未关注,1已关注
    private int follow = -1;

    private int keep = -1;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getJokeID() {
        return jokeID;
    }

    public void setJokeID(String jokeID) {
        this.jokeID = jokeID;
    }

    public String getId() {
        return getJokeID();
    }

    public void setId(String id) {
        setJokeID(id);
        this.id = id;
    }

    public String getIs_best() {
        return is_best;
    }

    public void setIs_best(String is_best) {
        this.is_best = is_best;
    }

    public int getKeep() {
        return keep;
    }

    public void setKeep(int keep) {
        this.keep = keep;
    }
    //是否收藏   1 收藏 0 没有收藏


    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public JokeEntity() {
        super();
    }

    public String getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(String isGoods) {
        this.isGoods = isGoods;
    }

    public String getIsLow() {
        return isLow;
    }

    public void setIsLow(String isLow) {
        this.isLow = isLow;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getJId() {
        return getJokeID();
    }


    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getBiz_no() {
        return biz_no;
    }

    public void setBiz_no(String biz_no) {
        this.biz_no = biz_no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getGood_num() {
        return good_num;
    }

    public void setGood_num(String good_num) {
        this.good_num = good_num;
    }

    public String getLow_num() {
        return low_num;
    }

    public void setLow_num(String low_num) {
        this.low_num = low_num;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VideoInfo> getVideo() {
        return video;
//        return DataSupport.where("news_id = ?", String.valueOf(id)).find(Comment.class);
    }


    public void setVideo(List<VideoInfo> video) {
        this.video = video;
    }

    public ComUserInfo getCom_user_info() {
        return com_user_info;
    }

    public void setCom_user_info(ComUserInfo com_user_info) {
        this.com_user_info = com_user_info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<JokeImgInfo> getImages() {
        return images;
    }

    public void setImages(List<JokeImgInfo> images) {
        this.images = images;
    }


    /**
     * 神评论列表
     *
     * @return
     */
    public List<CommentInfo> getComment() {
        return comment;
    }

    public void setComment(List<CommentInfo> comment) {
        this.comment = comment;
    }

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    @Override
    public String toString() {
        return "JokeEntity{" +
                "typeName='" + typeName + '\'' +
                ", jokeID='" + jokeID + '\'' +
                ", is_best='" + is_best + '\'' +
                ", id='" + id + '\'' +
                ", creater='" + creater + '\'' +
                ", biz_no='" + biz_no + '\'' +
                ", content='" + content + '\'' +
                ", ctime='" + ctime + '\'' +
                ", audit_status='" + audit_status + '\'' +
                ", good_num='" + good_num + '\'' +
                ", low_num='" + low_num + '\'' +
                ", share_num='" + share_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", video=" + video +
                ", com_user_info=" + com_user_info +
                ", date='" + date + '\'' +
                ", images=" + images +
                ", comment=" + comment +
                ", isGoods='" + isGoods + '\'' +
                ", isLow='" + isLow + '\'' +
                ", likes=" + likes +
                ", follow=" + follow +
                ", keep=" + keep +
                '}';
    }

}