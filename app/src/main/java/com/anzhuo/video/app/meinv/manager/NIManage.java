package com.anzhuo.video.app.meinv.manager;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.meinv.constant.AppServerUrl;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.HttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

/**
 * Created by wbb on 2016/5/4.
 */
public class NIManage {

    public static Context getContext() {
        return VideoApplication.getContext();
    }

    /**
     * 公共请求的
     *
     * @param URL         地址
     * @param hashMap
     * @param myInterface
     */
    public static void initHttp(String URL, HashMap<String, String> hashMap, final MyInterface myInterface) {
        HttpUtils.postStringAsync(URL, hashMap, getContext(), new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e,int id) {
                myInterface.onError(call, e);
            }

            @Override
            public void onResponse(String response,int id) {
                XUtilLog.log_i("wbb","======[ww]=======");
                setMyInterface(response, myInterface);
            }
        });
    }

    /**
     * 公共方法
     * 将请求结果放到 MyInterface 中
     *
     * @param response
     * @param myInterface
     */
    public static void setMyInterface(String response, MyInterface myInterface) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            String ret1 = jsonObject.getString("ret");
            String data = jsonObject.getString("data");
            int ret2 = Integer.parseInt(ret1);
            //返回ret状态码、data数据 、和jsonObject 。
            myInterface.onSucceed(ret2, data, jsonObject);
        } catch (Exception e) {
            myInterface.onError(null, e);
            e.printStackTrace();
        }
    }

//   例子 ~~~

    /**
     * 获取列表 接口
     *
     * @param categoryid  导航菜单ID
     * @param nextid      根据此ID获取后面几条信息（不传则为初始页）
     * @param limit       返回条数
     * @param myInterface
     */
    public static void getItemList(String categoryid, String nextid, String limit, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("categoryid", categoryid);
        hashMap.put("nextid", nextid);
        hashMap.put("limit", limit);
        hashMap.put("imei", Constants.IEMI);
        hashMap.put("type", "1");
//        hashMap.put(Constants.URL_SIGNKEY, JmTools.NRJM(hashMap, "MyDownloads.MyDownloads"));
        initHttp(AppServerUrl.ITEM_LISTS, hashMap, myInterface);
    }

    /**
     * 获取列表 接口(收藏)
     *
     * @param nextid      根据此ID获取后面几条信息（不传则为初始页）
     * @param limit       返回条数
     * @param myInterface
     */
    public static void getItemList(String type, String nextid, String limit,
                                   String iscollection, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nextid", nextid);
        hashMap.put("limit", limit);
        hashMap.put("imei", Constants.IEMI);
        hashMap.put("iscollection", iscollection);
        hashMap.put("type", type);
        XUtilLog.log_i("wbb", "========[hashMap]========:" + hashMap);
        initHttp(AppServerUrl.ITEM_LISTS, hashMap, myInterface);
    }

    /**
     * 收藏 和 收藏取消 接口
     *
     * @param imgIds        图片集id
     * @param isCollectiong 1：收藏 2：取消
     * @param myInterface
     */
    public static void PictureCollection(String imgIds, String isCollectiong, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("arrid", imgIds);
        hashMap.put("imei", Constants.IEMI);
        hashMap.put("type", "1");
        hashMap.put("yn", isCollectiong);
        initHttp(AppServerUrl.PIC_COLLECT, hashMap, myInterface);
    }

    /**
     * 意见反馈 接口
     *
     * @param contact     联系方式
     * @param content     类容
     * @param myInterface
     */
    public static void SuggestionFeedback(String contact, String content, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("imei", Constants.IEMI);
        hashMap.put("contact", contact);
        hashMap.put("app_version", Constants.PHONE_MODEL);
        hashMap.put("mobile_type", "2");
        hashMap.put("mobile_os", Constants.PHONE_NAME);
        hashMap.put("content", content);
        initHttp(AppServerUrl.ADVICE_CREATE, hashMap, myInterface);
    }


    /**
     * 版本更新 接口
     *
     * @param myInterface
     */
    public static void VersionUpdate(final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        initHttp(AppServerUrl.VERSION_INDEX, hashMap, myInterface);
    }
    /**
     * 版本更新 备用接口
     *
     * @param myInterface
     */
    public static void VersionUpdate2(final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        initHttp("http://101.201.104.233/meinv_app_bak_api/update.php", hashMap, myInterface);
    }

    /**
     * 图片点赞接口 接口
     *
     * @param id          图片集 id
     * @param myInterface
     */
    public static void PhotosGood(String id, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("imei", Constants.IEMI);
        hashMap.put("type", "1");
        initHttp(AppServerUrl.PIC_CREATEGOOD, hashMap, myInterface);
    }

    /**
     * 图片集 详情接口
     *
     * @param categoryid  导航菜单ID
     * @param itemid      图片列表id
     * @param myInterface
     */
    public static void PhotoCollections(String categoryid, String itemid, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("categoryid", categoryid);
        hashMap.put("itemid", itemid);
        initHttp(AppServerUrl.PIC_LISTS, hashMap, myInterface);
    }

    /**
     * 获取分类导航信息
     *
     * @param myInterface
     */
    public static void GetDirections(final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put(Constants.URL_SIGNKEY, JmTools.NRJM(hashMap, "MyDownloads.MyDownloads"));
        initHttp(AppServerUrl.ITEM_INDEX, hashMap, myInterface);
    }

    /**
     * 搜索列表接口
     *
     * @param keyword     关键字
     * @param page        第几页
     * @param limit       返回条数
     * @param myInterface
     */
    public static void Search(String keyword, String page, String limit, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("keyword", keyword);
        hashMap.put("page", page);
        hashMap.put("limit", limit);
        hashMap.put("imei", Constants.IEMI);
        hashMap.put("type", "1");
        initHttp(AppServerUrl.ITEM_SEARCH, hashMap, myInterface);
    }


    /**
     * 热门搜索列表
     *
     * @param limit       返回条数
     * @param myInterface
     */
    public static void HotSearch(String limit, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("limit", limit);
        initHttp(AppServerUrl.ITEM_HOT, hashMap, myInterface);
    }

    /**
     * 历史搜索列表
     *
     * @param page        第几页
     * @param limit       返回条数
     * @param myInterface
     */
    public static void SearchRecord(String page, String limit, final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page", page);
        hashMap.put("limit", limit);
        hashMap.put("imei", Constants.IEMI);
        initHttp(AppServerUrl.ITEM_HISTORY, hashMap, myInterface);
    }

    /**
     * 清除历史记录
     *
     * @param myInterface
     */
    public static void clearRecord( final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("imei", Constants.IEMI);
        initHttp(AppServerUrl.ITEM_DEHISTORY, hashMap, myInterface);
    }


    /**
     * 获取分类导航信息
     *
     * @param myInterface
     */
    public static void getTitleType( final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("imei", Constants.IMEI);
        initHttp(AppServerUrl.ITEM_INDEX, hashMap, myInterface);
    }

    /**
     * 获取推荐图片列表
     *
     * @param myInterface
     */
    public static void getRecommend( final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("limit", "50");
        initHttp(AppServerUrl.ITEM_TUIJIAN, hashMap, myInterface);
    }

    /**
     * 获取推荐图片列表
     *
     * @param myInterface
     */
    public static void getShareUrl( final MyInterface myInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        initHttp(AppServerUrl.SHARE_INDEX, hashMap, myInterface);
    }


}
