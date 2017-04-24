package com.anzhuo.video.app.model.home;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.base.BaseModel;
import com.anzhuo.video.app.model.bean.BaseHttpHashMap;
import com.anzhuo.video.app.utils.Md5;
import com.anzhuo.video.app.utils.PackageUtil;
import com.anzhuo.video.app.utils.SharedUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 首页相关的网络请求
 */
public class HomeModel extends BaseModel {
/*

    */

    /**
     * @param parentFragmentType pinyin  best精华，new最新
     * @param childFragmentType  -1全部，0推荐，1段子， 2视频，3图片
     *                           //     * @param refreshOrMore      0默认的，1刷新 2加载更多
     *                           //     * @param JokeID             id  笑话ID
     * @param myInterface        limit  一页笑话数量  30
     */
    public static void getHomeJokeList(@NonNull String parentFragmentType, @NonNull String childFragmentType, String mCategory,
                                       NewInterface myInterface) {
        BaseHttpHashMap params = new BaseHttpHashMap();
        params.put("pinyin", parentFragmentType);
        params.put("type", childFragmentType);

        if (!TextUtils.isEmpty(mCategory)) {
            params.put("category", mCategory);
        }
//        params.put("status", refreshOrMore);
//        params.put("id", JokeID);
//        params.put("ctime", ctime);

        params.put("imei", SharedUtil.getOnlyId());
//        params.put("limit", Constants.PAGESIZE + "");  //现在直接由后台控制请求的条数
        //必须放在最后面 ， 改下方法名即可。
//        Logger.i("请求参数=" + params.toString());
//        hashMap.put(Constants.URL_SIGNKEY, JmTools.NRJM(hashMap, "Cd.IndexList"));//之前加密，现在没有加密
        initHttp(AppServerUrl.JOKE_LIST, params, myInterface);
    }


    /**
     * 点赞或者点踩
     *
     * @param IsGood      true :点赞   false：点踩
     * @param jokeID      笑话ID
     * @param myInterface
     */
    public static void goodOrBad(boolean IsGood, String jokeID,
                                 NewInterface myInterface) {
        String goodOrBad;
        if (IsGood == false) {//踩
            goodOrBad = "2";
        } else {
            goodOrBad = "1";
        }

        BaseHttpHashMap hashMap = new BaseHttpHashMap();
        String userID = "0";
        hashMap.put("type", goodOrBad);
        hashMap.put("id", jokeID);
        if (!userID.equals("") && !userID.equals("0")) {//有UID的时候穿UID
            hashMap.put("creater", userID);
        } else {
            hashMap.put("imei", SharedUtil.getOnlyId());
        }
        //必须放在最后面 ， 改下方法名即可。
//        hashMap.put(Constants.URL_SIGNKEY, JmTools.NRJM(hashMap, "Cd.IndexList"));//之前加密，现在没有加密
        initHttp(AppServerUrl.THUMB_UP, hashMap, myInterface);
    }

    /**
     * 获取评论列表
     *
     * @param jokeID
     * @param page
     * @param myInterface
     *//*
    public static void getCommentsData(String jokeID, int page,
                                       NewInterface myInterface) {
        BaseHttpHashMap hashMap = new BaseHttpHashMap();
        String userID = Constants.USER_ID();
        hashMap.put("joke_id", jokeID);
        hashMap.put("page", page + "");
        hashMap.put("limit", Constants.PAGESIZE + "");
        if (!userID.equals("") && !userID.equals("0")) {//有UID的时候穿UID
            hashMap.put("uid", userID);
        } else {
            hashMap.put("imei", SharedUtil.getOnlyId());
        }
        //必须放在最后面 ， 改下方法名即可。
//        hashMap.put(Constants.URL_SIGNKEY, JmTools.NRJM(hashMap, "Cd.IndexList"));//之前加密，现在没有加密
        initHttp(AppServerUrl.COMMENTS_LIST, hashMap, myInterface);
    }

    *//**
     * 评论列表点赞
     *
     * @param cid         当前评论的ID
     * @param myInterface
     *//*
    public static void commentsGoods(String cid, NewInterface myInterface) {
        BaseHttpHashMap hashMap = new BaseHttpHashMap();
        hashMap.put("cid", cid);
        String userID = Constants.USER_ID();
        if (!userID.equals("") && !userID.equals("0")) {//有UID的时候穿UID
            hashMap.put("uid", userID);
        } else {
            hashMap.put("imei", SharedUtil.getOnlyId());
        }
        //必须放在最后面 ， 改下方法名即可。
//        hashMap.put(Constants.URL_SIGNKEY, JmTools.NRJM(hashMap, "Cd.IndexList"));//之前加密，现在没有加密
        initHttp(AppServerUrl.COMMENT_GOOD, hashMap, myInterface);
    }


    *//**
     * 收藏或者取消收藏
     *
     * @param jokeID
     * @param isCollect   true :收藏
     *                    false:取消收藏
     * @param myInterface
     *//*
    public static void CollectOrCancelJoke(String jokeID, boolean isCollect, NewInterface myInterface) {
        String userID = Constants.USER_ID();
        BaseHttpHashMap hashMap = new BaseHttpHashMap();
        if (!userID.equals("") && !userID.equals("0")) {//有UID的时候穿UID
            hashMap.put("uid", userID);
        }
        hashMap.put("joke_id", jokeID);
        String url = "";
        if (isCollect == true) {//收藏的动作
            url = AppServerUrl.COLLECT_OR_CANCEL_1;
        } else {
            url = AppServerUrl.COLLECT_OR_CANCEL_2;
        }
        initHttp(url, hashMap, myInterface);
    }


    *//**
     * 获取消息
     *
     * @param myInterface
     *//*
    public static void GetNewMessage(NewInterface myInterface) {
        //获取手机的imei
        String imei = Constants.IEMI;
        BaseHttpHashMap hashMap = new BaseHttpHashMap();


//            hashMap.put("uid", "2030190");//测试用的uid
//        hashMap.put("uid", Constants.USER_ID());
//    } else
//
//    {
        hashMap.put("imei", imei);
//    }
        Logger.i("请求消息参数 hashMap " + hashMap.toString());

        initHttp(AppServerUrl.MESSAGE_CENTER_LIST, hashMap, myInterface);
    }


    *//**
     * 笑话详情
     *
     * @param myInterface
     *//*
    public static void GetJokeDetail(String jokeID, NewInterface myInterface) {
        //获取手机的imei
        String imei = Constants.IEMI;
        BaseHttpHashMap hashMap = new BaseHttpHashMap();
        hashMap.put("id", jokeID);


//        hashMap.put("uid", Constants.USER_ID());
//    } else{
        hashMap.put("imei", imei);
//        }
        initHttp(AppServerUrl.JOKE_DETAIL, hashMap, myInterface);
    }


    *//**
     * 笑话详情
     *
     * @param myInterface
     *//*
    public static void PublishComment(String jokeID, String comment_id, String content, NewInterface myInterface) {
        //获取手机的imei
        BaseHttpHashMap hashMap = new BaseHttpHashMap();
        hashMap.put("uid", Constants.USER_ID());
        hashMap.put("joke_id", jokeID);
        hashMap.put("comment_id", comment_id);
        hashMap.put("content", content);
        Logger.i("发表评论 comment_id=" + comment_id);
        initHttp(AppServerUrl.ADD_COMMENT, hashMap, myInterface);
    }*/


    /**
     * 获取栏目分类
     *
     * @param myInterface
     */
    public static void getTitleType(NewInterface myInterface) {

        HashMap<String, String> map = new HashMap<>();
        map.put("package", Constants.Packages);
        map.put("version", Constants.VersionName);
        map.put("os", Constants.DEVICES_TAG);

        initHttp(AppServerUrl.DontuTitleType, map, myInterface);
    }

    /**
     * 获取首页的数据内容
     *
     * @param myInterface
     * @param category    栏目的id
     * @param id          分页 最后一条的id + imei
     */
    public static void getMainList(String category, String id, String page, NewInterface myInterface) {

        // if (!TextUtils.isEmpty(category)) { //如果是推荐不传 category字段
        if (category.equals("推荐")) {
            category = null;
        }
        //  }
        //  TextUtils.isEmpty(category)==true?
        //如果是推荐这里就不传 category 分页传最后一条的id + imei
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(category)) { //不为空就是传了的 为空说明是推荐的
            map.put("category", category);
        }
        map.put("page", page);
        map.put("limit", Constants.LIMIT);
        map.put("os", "1");//1位Android
        map.put("version", PackageUtil.getVersionName(VideoApplication.getContext()));
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
            map.put("imei", Constants.IEMI);
        }
        Logger.i("=======[map集合]===========" + map.toString());
        initHttp(AppServerUrl.DontuContent, map, myInterface);
    }

    /**
     * 获取评论列表
     *
     * @param id
     * @param myInterface
     */
    public static void getDetailcomment(String id, String page, NewInterface myInterface) {
        //如果是推荐这里就不传 category 分页传最后一条的id + imei
        Logger.i("=======[page]===========" + page);
        HashMap<String, String> map = new HashMap<>();
        map.put("itemid", id);
        map.put("page", page);
        map.put("limit", Constants.LIMIT);
//        map.put("imei", AppUtil.getImei());
        initHttp(AppServerUrl.DontuComment, map, myInterface);
    }

    /**
     * 点赞
     *
     * @param id          字符串	必须	0		当前videoID
     * @param myInterface
     * @param type        字符串	可选	0		video点赞/踩/分享：0是赞,1是踩,2是分享
     */
    public static void getZan(String id, String type, NewInterface myInterface) {
        //如果是推荐这里就不传 category 分页传最后一条的id + imei
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        initHttp(AppServerUrl.DontuUserClick, map, myInterface);
    }

    /**
     * 点赞 评论
     *
     * @param id          字符串	必须	0		当前videoID
     * @param myInterface
     * @param type        字符串	可选	0		video点赞/踩/分享：0是赞,1是踩,2是分享
     */
    public static void getCommentZan(String id, String type, NewInterface myInterface) {
        //如果是推荐这里就不传 category 分页传最后一条的id + imei

        HashMap<String, String> map = new HashMap<>();
        map.put("itemid", id);
        map.put("type", type);
        initHttp(AppServerUrl.DontuCommentUserClick, map, myInterface);
    }

    /**
     * 举报
     *
     * @param id          字符串	必须	0		当前videoID
     * @param myInterface
     */
    public static void getReport(String id, NewInterface myInterface) {
        //如果是推荐这里就不传 category 分页传最后一条的id + imei
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        initHttp(AppServerUrl.DontuCommentReport, map, myInterface);
    }

    /**
     * 举报
     *
     * @param myInterface
     */
    public static void getSendComment(String itemid, String content, String category, String username, NewInterface myInterface) {
        //如果是推荐这里就不传 category 分页传最后一条的id + imei
        HashMap<String, String> map = new HashMap<>();
        map.put("itemid", itemid);
        map.put("content", content);
        map.put("os", Constants.DEVICES_TAG);
        map.put("version", Constants.VersionName);
        map.put("imei", Constants.IEMI);
        map.put("category", category);
        if (!username.equals("0")) {
            map.put("username", username);
        }
        initHttp(AppServerUrl.DontuSendComment, map, myInterface);
    }

    /**
     * 初始化 数据 短链接
     *
     * @param myInterface
     */
    public static void getStart(NewInterface myInterface) {
        //如果是推荐这里就不传 category 分页传最后一条的id + imei
        HashMap<String, String> map = new HashMap<>();
        initHttp(AppServerUrl.Version_Start, map, myInterface);
    }

    /**
     * 获取验证码
     * @param s
     * @param phone
     * @param reg_site
     * @param myInterface
     */
    public static void getMsg(String s, String phone, String reg_site ,NewInterface myInterface) {

        HashMap<String, String> map = new HashMap<>();
        map.put("s", s);
        map.put("phone", phone);
        map.put("reg_site", reg_site);
        map = sign(map,"User.SendMark");
        initHttp(AppServerUrl.USER_SEND_MSG, map, myInterface);
    }

    /**
     * 用户登录
     * @param s
     * @param phone
     * @param mark
     * @param reg_site
     * @param myInterface
     */
    public static void getLogin(String s, String phone, String mark, String reg_site, NewInterface myInterface) {

        HashMap<String, String> map = new HashMap<>();
        map.put("s", s);
        map.put("phone", phone);
        map.put("os", Constants.DEVICES_TAG);
        map.put("device", Constants.IEMI);
        map.put("mark", mark);
        map.put("reg_site", reg_site);
        map = sign(map,"User.Login");
        initHttp(AppServerUrl.USER_LOGININ, map, myInterface);
    }

    /**
     * 获取用户信息
     * @param s
     * @param phone
     * @param uid
     * @param reg_site
     * @param myInterface
     */
    public static void getUserInfo(String s, String phone,String uid, String reg_site ,NewInterface myInterface) {

        HashMap<String, String> map = new HashMap<>();
        map.put("s", s);
        map.put("phone", phone);
        map.put("uid", uid);
        map.put("reg_site", reg_site);

        map = sign(map,"User.Index");

        initHttp(AppServerUrl.USER_INFO, map, myInterface);
    }

    public static HashMap sign(HashMap<String, String> map,String name){

        map.put("service", name);
        String params_str = "";
        Set<String> keys = map.keySet();
        List<String> keyList = new ArrayList<>();
        keyList.clear();
        for (String key:keys){
            if (!TextUtils.equals(key,"s"))
                keyList.add(key);
        }
        Collections.sort(keyList);
        for (String key : keyList){
            params_str += map.get(key);
        }
        params_str = params_str+"cxmxmiaobosignkey";
        Logger.e("-----------params_str--------"+params_str);
        map.put("s", Md5.MD5(params_str).toLowerCase());
        map.remove("service");

        return map;
    }

    /**
     * 核对用户vip
     * @param s
     * @param phone
     * @param uid
     * @param reg_site
     * @param myInterface
     */
    public static void getUserIsVip(String s, String phone,String uid, String reg_site ,NewInterface myInterface) {

        HashMap<String, String> map = new HashMap<>();
        map.put("s", s);
        map.put("phone", phone);
        map.put("uid", uid);
        map.put("reg_site", reg_site);

        map = sign(map,"User.CheckVip");

        initHttp(AppServerUrl.USER_CHECKVIP, map, myInterface);
    }

    /**
     * 设置用户昵称
     * @param s
     * @param phone
     * @param uid
     * @param username
     * @param myInterface
     */
    public static void setUserName(String s, String phone,String uid, String username ,NewInterface myInterface) {
        HashMap<String, String> map = new HashMap<>();
        map.put("s", s);
        map.put("phone", phone);
        map.put("uid", uid);
        map.put("username", username);
        map = sign(map,"User.SetUserName");
        initHttp(AppServerUrl.USER_SETUSERNAME, map, myInterface);
    }

}
