package com.anzhuo.video.app.model.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * creat on 2016/5/12.
 */
public class JokeImgInfo extends DataSupport implements Serializable {
    @Override
    public String toString() {
        return "JokeImgInfo{" +
                "id='" + id + '\'' +
                ", img_url='" + img_url + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", extension='" + extension + '\'' +
                ", lit_pic='" + lit_pic + '\'' +
                ", lit_width='" + lit_width + '\'' +
                ", lit_height='" + lit_height + '\'' +
                '}';
    }

    private String JokeImgID = "";
    //注意，这里只是数据库映射作用
    private JokeEntity jokeEntity;

    public JokeEntity getJokeEntity() {
        return jokeEntity;
    }

    public void setJokeEntity(JokeEntity jokeEntity) {
        this.jokeEntity = jokeEntity;
    }

    @Column(ignore = true)
    private String id = "";

    private String img_url = "";

    private String width = "";

    private String height = "";

    private String extension = "";

    private String lit_pic = "";

    private String lit_width = "";

    private String lit_height = "";

    public String getLit_pic() {
        return lit_pic;
    }

    public void setLit_pic(String lit_pic) {
        this.lit_pic = lit_pic;
    }

    public String getLit_width() {
        return lit_width;
    }

    public void setLit_width(String lit_width) {
        this.lit_width = lit_width;
    }

    public String getLit_height() {
        return lit_height;
    }

    public void setLit_height(String lit_height) {
        this.lit_height = lit_height;
    }

    public String getImgId() {
        return getJokeImgID();
    }

    public void setId(String id) {
        setJokeImgID(id);
        this.id = id;

    }

    public String getImg_url() {
        return img_url.trim();
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getJokeImgID() {
        return JokeImgID;
    }

    public void setJokeImgID(String jokeImgID) {
        JokeImgID = jokeImgID;
    }
}
