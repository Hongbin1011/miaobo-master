package com.anzhuo.video.app.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22/022.
 */

public class FuliBean {
    @Override
    public String toString() {
        return "FuliBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", logo_url='" + logo_url + '\'' +
                ", showtype='" + showtype + '\'' +
                ", list=" + list +
                '}';
    }

    /**
     * id : 2
     * title : 福利中心
     * logo_url : http://img.gzceub.com/image/2017-02-17/58a6bf7aa8074.png
     * showtype : 0
     * list : [{"id":"6","name":"美女直播","item_id":"2","is_android":"1","is_ios":"1","is_wap":"1","is_recommend":"2","px":"0","stat":"2","is_delete":"0","url_open_type":"1","logo_url":"http://img.gzceub.com/image/2017-02-17/58a6d73450f06.png","android_url":"http://m.v.6.cn/live/u1?src=ummeda5298","ios_url":"","wap_url":"","creater":"70","desc":""},{"id":"8","name":"美女图片","item_id":"2","is_android":"1","is_ios":"0","is_wap":"0","is_recommend":"1","px":"0","stat":"2","is_delete":"0","url_open_type":"2","logo_url":"http://img.gzceub.com/image/2017-02-15/58a4028af40ca.png","android_url":"","ios_url":"","wap_url":"","creater":"70","desc":""},{"id":"10","name":"搞笑动图","item_id":"2","is_android":"1","is_ios":"0","is_wap":"0","is_recommend":"1","px":"0","stat":"2","is_delete":"0","url_open_type":"2","logo_url":"http://img.gzceub.com/image/2017-02-17/58a6d7ad5bf9e.png","android_url":"","ios_url":"","wap_url":"","creater":"70","desc":""}]
     */

    private String id;
    private String title;
    private String logo_url;
    private String showtype;
    private List<ListBean> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", item_id='" + item_id + '\'' +
                    ", is_android='" + is_android + '\'' +
                    ", is_ios='" + is_ios + '\'' +
                    ", is_wap='" + is_wap + '\'' +
                    ", is_recommend='" + is_recommend + '\'' +
                    ", px='" + px + '\'' +
                    ", stat='" + stat + '\'' +
                    ", is_delete='" + is_delete + '\'' +
                    ", url_open_type='" + url_open_type + '\'' +
                    ", logo_url='" + logo_url + '\'' +
                    ", android_url='" + android_url + '\'' +
                    ", ios_url='" + ios_url + '\'' +
                    ", wap_url='" + wap_url + '\'' +
                    ", creater='" + creater + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }

        /**
         * id : 6
         * name : 美女直播
         * item_id : 2
         * is_android : 1
         * is_ios : 1
         * is_wap : 1
         * is_recommend : 2
         * px : 0
         * stat : 2
         * is_delete : 0
         * url_open_type : 1
         * logo_url : http://img.gzceub.com/image/2017-02-17/58a6d73450f06.png
         * android_url : http://m.v.6.cn/live/u1?src=ummeda5298
         * ios_url :
         * wap_url :
         * creater : 70
         * desc :
         */

        private String id;
        private String name;
        private String item_id;
        private String is_android;
        private String is_ios;
        private String is_wap;
        private String is_recommend;
        private String px;
        private String stat;
        private String is_delete;
        private String url_open_type;
        private String logo_url;
        private String android_url;
        private String ios_url;
        private String wap_url;
        private String creater;
        private String desc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getIs_android() {
            return is_android;
        }

        public void setIs_android(String is_android) {
            this.is_android = is_android;
        }

        public String getIs_ios() {
            return is_ios;
        }

        public void setIs_ios(String is_ios) {
            this.is_ios = is_ios;
        }

        public String getIs_wap() {
            return is_wap;
        }

        public void setIs_wap(String is_wap) {
            this.is_wap = is_wap;
        }

        public String getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(String is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getPx() {
            return px;
        }

        public void setPx(String px) {
            this.px = px;
        }

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public String getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }

        public String getUrl_open_type() {
            return url_open_type;
        }

        public void setUrl_open_type(String url_open_type) {
            this.url_open_type = url_open_type;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getAndroid_url() {
            return android_url;
        }

        public void setAndroid_url(String android_url) {
            this.android_url = android_url;
        }

        public String getIos_url() {
            return ios_url;
        }

        public void setIos_url(String ios_url) {
            this.ios_url = ios_url;
        }

        public String getWap_url() {
            return wap_url;
        }

        public void setWap_url(String wap_url) {
            this.wap_url = wap_url;
        }

        public String getCreater() {
            return creater;
        }

        public void setCreater(String creater) {
            this.creater = creater;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
