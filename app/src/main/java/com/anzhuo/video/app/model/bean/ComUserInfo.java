package com.anzhuo.video.app.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * creat on 2016/5/12.
 */
public class ComUserInfo extends DataSupport implements Serializable {
    @Override
    public String toString() {
        return "ComUserInfo{" +
                "user_id='" + user_id + '\'' +
                ", joke_id='" + joke_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_sex='" + user_sex + '\'' +
                ", user_img_url='" + user_img_url + '\'' +
                '}';
    }

    //注意，这里只是数据库映射作用
    private JokeEntity jokeEntity;

    public JokeEntity getJokeEntity() {
        return jokeEntity;
    }

    public void setJokeEntity(JokeEntity jokeEntity) {
        this.jokeEntity = jokeEntity;
    }

    private String user_id = "";

    private String joke_id = "";

    private String user_name = "";

    private String user_sex = "";

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    private String user_img_url = "";

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getJoke_id() {
        return joke_id;
    }

    public void setJoke_id(String joke_id) {
        this.joke_id = joke_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img_url() {
        return user_img_url;
    }

    public void setUser_img_url(String user_img_url) {
        this.user_img_url = user_img_url;
    }

}
