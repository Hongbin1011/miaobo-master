/*
package com.anzhuo.video.app.utils;

import android.content.ContentValues;

import com.anzhuo.video.app.model.bean.SearchRecordInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class DatabaseUtils {
//    public static boolean selectsData(String name) {
//        XUtilLog.log_i("wbb", "cq==============[拿到的数据库]=============");
//        List<NavigationInfo> titlesBeen = DataSupport.where("tags=?", name).find(NavigationInfo.class);
//        if (titlesBeen != null) {
//            for (int i = 0; i < titlesBeen.size(); i++) {
//                NavigationInfo titlesBean = titlesBeen.get(i);
//                XUtilLog.log_i("wbb", "cq==============[拿到的数据库]=============" + titlesBean.toString());
//                String titles = titlesBean.getTags();
//                if (titles.equals(name)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        } else {
//            return false;
//        }
//        return false;
//    }

    */
/**
     * 根据id 查找
     *
     * @param name
     * @return
     *//*

//    public static String selectsDataId(String name) {
//        XUtilLog.log_i("wbb", "cq==============[拿到的数据库23]=============" + name);
//        List<NavigationInfo> titlesBeen = DataSupport.where("tags=?", name).find(NavigationInfo.class);
//        String type = "0";
//        if (titlesBeen != null) {
//            for (int i = 0; i < titlesBeen.size(); i++) {
//                NavigationInfo titlesBean = titlesBeen.get(i);
//                type = titlesBean.getRecom();
//                return type;
//            }
//        }
//        return type;
//    }

//    */
/**
//     * 根据名字差数据
//     *
//     * @param name
//     * @return
//     *//*

//    public static List<NavigationInfo> getTitlesData(String name) {
//        //查询不为空 说明存在
//        List<NavigationInfo> titlesBeen = new ArrayList<>();
//        titlesBeen.addAll(DataSupport.where("tags=?", name).find(NavigationInfo.class));
//
//        XUtilLog.log_i("wbb", "cq==============[拿到的数据库]=============" + titlesBeen.toString());
//        if (titlesBeen != null) {
//            return titlesBeen;
//        } else {
//            return null;
//        }
//    }

    */
/**
     * 根据搜索key查询数据
     *
     * @param name
     * @return
     *//*

    public static List<SearchRecordInfo> getSearchKey(String name) {
        //查询不为空 说明存在
        List<SearchRecordInfo> titlesBeen = new ArrayList<>();
        titlesBeen.addAll(DataSupport.where("keyword=?", name).find(SearchRecordInfo.class));
        XUtilLog.log_i("wbb", "cq==============[拿到的数据库]=============" + titlesBeen.toString());
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }
    }

   */
/* *//*
*/
/**
     * 根据收藏id查询数据
     * @param cId
     * @return
     *//*
*/
/*

    public static List<ItemListInfo> getCollection(String cId) {
        //查询不为空 说明存在
        List<ItemListInfo> titlesBeen = new ArrayList<>();
        titlesBeen.addAll(DataSupport.where("id_ = ?", cId).find(ItemListInfo.class));
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }
    }*//*

*/
/*

    //收藏
    public static List<ItemListInfo> getCollectionAll() {
        List<ItemListInfo> titlesBeen = DataSupport.findAll(ItemListInfo.class);
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }
    }
*//*



   */
/* //标签
    public static List<NavigationInfo> getAll() {
        XUtilLog.log_i("wbb", "cq==============[拿到的数据库]=============");
        List<NavigationInfo> titlesBeen = DataSupport.findAll(NavigationInfo.class);
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }
    }*//*

    //搜索
    public static List<SearchRecordInfo> getSearchAll() {
        List<SearchRecordInfo> titlesBeen = DataSupport.findAll(SearchRecordInfo.class);
        if (titlesBeen != null) {
            return titlesBeen;
        } else {
            return null;
        }
    }
*/
/*
    public static int deleteCollection(String id) {
        int i = DataSupport.deleteAll(ItemListInfo.class, "id_=?", id);
        return i;
    }*//*


   */
/* public static void updataData(String tags, String recom) {
        ContentValues con = new ContentValues();
        con.put("recom", recom);
        DataSupport.updateAll(NavigationInfo.class, con, "tags=?", tags);
    }*//*


   */
/* public static void SaveJoke(final List<JokeEntity> jokeList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Iterator<JokeEntity> iterator = jokeList.iterator();
                    while (iterator.hasNext()) {
                        JokeEntity jokeEntity = iterator.next();
                        //存视频
                        List<VideoInfo> videoInfo = jokeEntity.getVideo();
                        DataSupport.saveAll(videoInfo);
                        //存用户
                        ComUserInfo comUserInfo = jokeEntity.getCom_user_info();
                        comUserInfo.save();
                        //存图片
                        List<JokeImgInfo> jokeImgInfos = jokeEntity.getImages();
                        DataSupport.saveAll(jokeImgInfos);
                        //存评论
                        List<CommentInfo> comment = jokeEntity.getComment();
                        DataSupport.saveAll(comment);
                    }
//
                    DataSupport.saveAll(jokeList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }*//*


    */
/**
     * 动图id 有就删除id
     *
     * @param id
     *//*

    public static void selectDongtuId(String id) {
//        NavigationInfo first = DataSupport.where("dongtuId=?", id).findFirst(NavigationInfo.class);
//        if (first != null) {
//            XUtilLog.log_i("wbb", "cq=============[查询数据库中数据]===========" + first.toString());
//            //说明数据库中存在
//            int i = DataSupport.deleteAll(NavigationInfo.class, "dongtuId=?", id);
//            XUtilLog.log_i("wbb", "cq=============[查询数据库中数据 删除]===========" + i);
//
//        }
    }
}
*/
