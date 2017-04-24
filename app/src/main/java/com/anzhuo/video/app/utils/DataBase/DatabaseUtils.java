package com.anzhuo.video.app.utils.DataBase;

import android.content.ContentValues;

import com.anzhuo.video.app.model.bean.CollectListBean;
import com.anzhuo.video.app.model.bean.ContentListBean;
import com.anzhuo.video.app.model.bean.TitlesBean;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class DatabaseUtils {
    public static boolean selectsData(String name) {
        Logger.i("========[拿到的数据库]=============");
        List<TitlesBean> titlesBeen = DataSupport.where("category=?", name).find(TitlesBean.class);
        if (titlesBeen != null) {
            for (int i = 0; i < titlesBeen.size(); i++) {
                TitlesBean titlesBean = titlesBeen.get(i);
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
        List<TitlesBean> titlesBeen = new ArrayList<>();
        titlesBeen.addAll(DataSupport.where("category=?", name).find(TitlesBean.class));
        if (titlesBeen != null || titlesBeen.size() != 0) {
            //一般情况 根据名字查询到的数据只有一条  不过不排除有两个相同的名字
            if (titlesBeen.size() == 1) {
                Logger.i("========[数据库 wei 1]=============");
                TitlesBean titlesBean = titlesBeen.get(0);
                String titles = titlesBean.getCategory();
                if (titles.equals(name)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                for (int i = 0; i < titlesBeen.size(); i++) {
                    TitlesBean titlesBean = titlesBeen.get(i);
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
    public static List<TitlesBean> getTitlesData(String name) {
        Logger.i("========[拿到的数据库]=============");
        //查询不为空 说明存在
        List<TitlesBean> titlesBeen = DataSupport.where("category=?", name).find(TitlesBean.class);
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }

    }

    public static List<TitlesBean> getAll() {
        Logger.i("========[拿到的数据库]=============");
        List<TitlesBean> titlesBeen = DataSupport.findAll(TitlesBean.class);
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }
    }

    /**
     * 删除指定的数据
     *
     * @param dongtuId
     * @return
     */
    public static int deleteData(String dongtuId) {
        Logger.i("========[要删除的数据 名字]=============" + dongtuId);
        int i = DataSupport.deleteAll(CollectListBean.class, "dongtuId=?", dongtuId);
        return i;
    }

    public static int deleteDataid(String title) {
        Logger.i("========[要删除的数据 名字]=============" + title);
        int i = DataSupport.deleteAll(TitlesBean.class, "category=?", title);
        return i;
    }

    public static void updataData(boolean seltor) {
        Logger.i("========[拿到的数据库13]=============");
        List<TitlesBean> getal2l3 = DatabaseUtils.getAll();
        for (TitlesBean t1 : getal2l3) {
            Logger.i("========[拿到的数据库13]=============" + t1.toString());
        }
        ContentValues con = new ContentValues();
        con.put("seltor", "false");
        DataSupport.updateAll(TitlesBean.class, con, "category=?", "推荐");
        List<TitlesBean> getal2l32 = DatabaseUtils.getAll();
        for (TitlesBean t12 : getal2l32) {
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
        ContentListBean first = DataSupport.where("dongtuId=?", id).findFirst(ContentListBean.class);
        if (first != null) {
            Logger.i("=======[查询数据库中数据]===========" + first.toString());
            //说明数据库中存在
            int i = DataSupport.deleteAll(ContentListBean.class, "dongtuId=?", id);
            Logger.i("=======[查询数据库中数据 删除]===========" + i);
        }
    }

    /**
     * 查询收藏id
     *
     * @param id
     */
    public static void selectCollectId(String id) {
        CollectListBean first = DataSupport.where("dongtuId=?", id).findFirst(CollectListBean.class);
        if (first != null) {
            Logger.i("=======[查询数据库中数据]===========" + first.toString());
            //说明数据库中存在
            int i = DataSupport.deleteAll(CollectListBean.class, "dongtuId=?", id);
            Logger.i("=======[查询数据库中数据 删除]===========" + i);
        }
    }
}
