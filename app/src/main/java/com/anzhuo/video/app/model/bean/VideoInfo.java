package com.anzhuo.video.app.model.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * created on 2016/5/12.
 */
public class VideoInfo extends DataSupport implements Serializable {

    //注意，这里只是数据库映射作用
    private JokeEntity jokeEntity;

    public JokeEntity getJokeEntity() {
        return jokeEntity;
    }

    public void setJokeEntity(JokeEntity jokeEntity) {
        this.jokeEntity = jokeEntity;
    }

    private String jokeVideoID = "";

    @Column(ignore = true)
    private String id = "";

    private String img_url = "";

    private String width = "";

    private String height = "";

    private String video_url = "";

    public String getVId() {
        return getJokeVideoID();
    }

    public void setId(String id) {
        setJokeVideoID(id);
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getJokeVideoID() {
        return jokeVideoID;
    }

    public void setJokeVideoID(String jokeVideoID) {
        this.jokeVideoID = jokeVideoID;
    }

    public String getId() {
        return getJokeVideoID();
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id='" + id + '\'' +
                ", img_url='" + img_url + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", video_url='" + video_url + '\'' +
                '}';
    }
}
