package com.anzhuo.video.app.dongtu.utils;

import android.content.ContentValues;

import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuContentListBean;
import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuTitlesBean;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class DatabaseUtils {
    public static boolean selectsData(String name) {
        Logger.i("========[拿到的数据库]=============");
        List<DongtuTitlesBean> titlesBeen = DataSupport.where("category=?", name).find(DongtuTitlesBean.class);
        if (titlesBeen != null) {
            for (int i = 0; i < titlesBeen.size(); i++) {
                DongtuTitlesBean titlesBean = titlesBeen.get(i);
                Logger.i("========[拿到的数据库]=============" + titlesBean.toString());
                String titles = titlesBean.getCategory();
                if (titles.equals(name)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * 根据id 查找
     *
     * @param name
     * @return
     */
    public static boolean selectsDataId(String name) {
        Logger.i("========[拿到的数据库23]=============" + name);
        List<DongtuTitlesBean> titlesBeen = new ArrayList<>();
        titlesBeen.addAll(DataSupport.where("category=?", name).find(DongtuTitlesBean.class));
        if (titlesBeen != null || titlesBeen.size() != 0) {
            //一般情况 根据名字查询到的数据只有一条  不过不排除有两个相同的名字
            if (titlesBeen.size() == 1) {
                Logger.i("========[数据库 wei 1]=============");
                DongtuTitlesBean titlesBean = titlesBeen.get(0);
                String titles = titlesBean.getCategory();
                if (titles.equals(name)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                for (int i = 0; i < titlesBeen.size(); i++) {
                    DongtuTitlesBean titlesBean = titlesBeen.get(i);
                    String titles = titlesBean.getCategory();
                    if (titles.equals(name)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * 根据名字差数据
     *
     * @param name
     * @return
     */
    public static List<DongtuTitlesBean> getTitlesData(String name) {
        Logger.i("========[拿到的数据库]=============");
        //查询不为空 说明存在
        List<DongtuTitlesBean> titlesBeen = DataSupport.where("category=?", name).find(DongtuTitlesBean.class);
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }

    }

    public static List<DongtuTitlesBean> getAll() {
        Logger.i("========[拿到的数据库]=============");
        List<DongtuTitlesBean> titlesBeen = DataSupport.findAll(DongtuTitlesBean.class);
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }
    }

    public static int deleteData(String title) {
        Logger.i("========[要删除的数据 名字]=============" + title);
        int i = DataSupport.deleteAll(DongtuTitlesBean.class, "category=?", title);
        return i;
    }

    public static int deleteDataid(String title) {
        Logger.i("========[要删除的数据 名字]=============" + title);
        int i = DataSupport.deleteAll(DongtuTitlesBean.class, "category=?", title);
        return i;
    }

    public static void updataData(boolean seltor) {
        Logger.i("========[拿到的数据库13]=============");
        List<DongtuTitlesBean> getal2l3 = DatabaseUtils.getAll();
        for (DongtuTitlesBean t1 : getal2l3) {
            Logger.i("========[拿到的数据库13]=============" + t1.toString());
        }
        ContentValues con = new ContentValues();
        con.put("seltor", "false");
        DataSupport.updateAll(DongtuTitlesBean.class, con, "category=?", "推荐");
        List<DongtuTitlesBean> getal2l32 = DatabaseUtils.getAll();
        for (DongtuTitlesBean t12 : getal2l32) {
            Logger.i("========[保存成功 取出来的13]=============" + t12.toString());
        }
    }

    /**
     * 保存数据
     * videoid 有就删除id
     *
     * @param id
     */
    public static void selectDongtuId(String id) {
        DongtuContentListBean first = DataSupport.where("dongtuId=?", id).findFirst(DongtuContentListBean.class);
        if (first != null) {
            Logger.i("=======[查询数据库中数据]===========" + first.toString());
            //说明数据库中存在
            int i = DataSupport.deleteAll(DongtuContentListBean.class, "dongtuId=?", id);
            Logger.i("=======[查询数据库中数据 删除]===========" + i);
        }
    }
}
