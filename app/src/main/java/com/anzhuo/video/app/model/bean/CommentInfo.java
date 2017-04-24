package com.anzhuo.video.app.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * created on 2016/10/10 0010.
 * 列表的神评论
 */
public class CommentInfo extends DataSupport implements Serializable {

    //注意，这里只是数据库映射作用
    private JokeEntity jokeEntity;

    public JokeEntity getJokeEntity() {
        return jokeEntity;
    }

    public void setJokeEntity(JokeEntity jokeEntity) {
        this.jokeEntity = jokeEntity;
    }


    private String cid = "";//评论ID
    private String uid = "";//评论用户ID
    private String goods = "";//点赞数量
    private String content = "";//评论内容
    private String ctime = "";//评论时间
    private String joke_id = "";//笑话ID
    private String name = "";//评论人的名字
    private String sex = "";//评论人的性别
    private String logo = "";//评论人的头像
    private int likes = -2;//我是否点赞

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
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

    public String getJoke_id() {
        return joke_id;
    }

    public void setJoke_id(String joke_id) {
        this.joke_id = joke_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
