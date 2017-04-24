package com.anzhuo.video.app.model.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * 存放title的bean
 */

public class TitlesBean extends DataSupport {
    /**
     * id : 184
     * category : 热点
     * module : 10
     * alias : redian
     */
    @Column(ignore = true)
    private String id;

//    private String categoryid;//id
    private String category;//类名字
//    private String module;//id
//    private String alias;//拼音
    private boolean seltor;//是否选中
//    private String recommend;


    @Override
    public String toString() {
        return "TitlesBean{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", seltor=" + seltor +
                '}';
    }

  /*  public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }*/

    public boolean isSeltor() {
        return seltor;
    }

    public boolean getSeltor() {
        return seltor;
    }

    public void setSeltor(boolean seltor) {
        this.seltor = seltor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
//        setCategoryid(id);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    public String getModule() {
//        return module;
//    }
//
//    public void setModule(String module) {
//        this.module = module;
//    }
//
//    public String getAlias() {
//        return alias;
//    }
//
//    public void setAlias(String alias) {
//        this.alias = alias;
//    }

}

