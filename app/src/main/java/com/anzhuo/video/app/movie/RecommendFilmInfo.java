package com.anzhuo.video.app.movie;

import java.util.List;

/**
 * Created by husong on 2017/2/21.
 */

public class RecommendFilmInfo {
    private String title;
    private String type;
    private List<MovieInfo> datas;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MovieInfo> getDatas() {
        return datas;
    }

    public void setDatas(List<MovieInfo> datas) {
        this.datas = datas;
    }
}
