package com.anzhuo.video.app.model.bean;

import java.io.Serializable;

/**
 * 作者：熊晓清 on 2016/5/5 11:36
 * 作用：首页界面创建Fragment时候传值
 */
public class FragmentBean implements Serializable {
    public FragmentBean() {

    }

    public FragmentBean(String parentFragmentName, String parentFragmentType, String childFragmentName, String childFragmentType, String FragmentName) {
        this.ParentFragmentName = parentFragmentName;
        this.ParentFragmentType = parentFragmentType;
        this.ChildFragmentName = childFragmentName;
        this.ChildFragmentType = childFragmentType;
        this.FragmentName = FragmentName;
    }

    @Override
    public String toString() {
        return "FragmentBean{" +
                "FragmentName='" + FragmentName + '\'' +
                ", ParentFragmentName='" + ParentFragmentName + '\'' +
                ", ParentFragmentType='" + ParentFragmentType + '\'' +
                ", ChildFragmentName='" + ChildFragmentName + '\'' +
                ", ChildFragmentType='" + ChildFragmentType + '\'' +
                '}';
    }

    public String getFragmentName() {
        return FragmentName;
    }

    public void setFragmentName(String fragmentName) {
        FragmentName = fragmentName;
    }

    private String FragmentName = "";

    //    pinyin  best精华，new最新
    //    -1全部，0推荐，1段子， 2视频，3图片
    //        0默认的，1刷新 2加载更多
    private String ParentFragmentName = "";
    private String ParentFragmentType = "";
    private String ChildFragmentName = "";
    private String ChildFragmentType = "";

    public String getParentFragmentName() {
        return ParentFragmentName;
    }

    public void setParentFragmentName(String parentFragmentName) {
        ParentFragmentName = parentFragmentName;
    }

    public String getParentFragmentType() {
        return ParentFragmentType;
    }

    public void setParentFragmentType(String parentFragmentType) {
        ParentFragmentType = parentFragmentType;
    }

    public String getChildFragmentName() {
        return ChildFragmentName;
    }

    public void setChildFragmentName(String childFragmentName) {
        ChildFragmentName = childFragmentName;
    }

    public String getChildFragmentType() {
        return ChildFragmentType;
    }

    public void setChildFragmentType(String childFragmentType) {
        ChildFragmentType = childFragmentType;
    }
}
