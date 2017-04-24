package com.anzhuo.video.app.model.bean;

/**
 * 默认title的bean
 */

public class TitlesDefaultBean{

    /**
     * id : 189
     * category : 影视
     * module : 10
     * alias : yingshi
     * state : 0
     */

    private String id;
    private String category;
    private String module;
    private String alias;
    private String state;
    private String recommend;

    public String getRecommend() {
        return recommend;
    }

    @Override
    public String toString() {
        return "TitlesDefaultBean{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", module='" + module + '\'' +
                ", alias='" + alias + '\'' +
                ", state='" + state + '\'' +
                ", recommend='" + recommend + '\'' +
                '}';
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

