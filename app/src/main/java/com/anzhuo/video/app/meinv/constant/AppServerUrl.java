package com.anzhuo.video.app.meinv.constant;


import com.anzhuo.video.app.meinv.base.BaseAppServerUrl;

/**
 * Created by wbb on 2016/4/27.
 */
public abstract class AppServerUrl extends BaseAppServerUrl {

    public final static String BASEURL = getAppServerUrl();

    /**
     * 统计接口 BASEURL
     **/
    public final static String STATISTICS_BASEURL = getAppStatisticaUrl();

    //获取图片列表
    public static final String ITEM_LISTS = getAppServerUrl() + "Item.Lists";

    //获取图片集 详情列表
    public static final String PIC_LISTS = getAppServerUrl() + "Pic.Lists";

    //获取顶部导航的分类信息
    public static final String ITEM_INDEX = getAppServerUrl() + "Item.Index";

    //意见反馈 接口
    public static final String ADVICE_CREATE = getAppServerUrl() + "Advice.Create";

    //获取顶部导航的分类信息
    public static final String ITEM_SEARCH = getAppServerUrl() + "Item.Search";

    //版本更新 接口
    public static final String VERSION_INDEX = getAppServerUrl() + "Version.Index";

    //图片集 赞 接口
    public static final String PIC_CREATEGOOD = getAppServerUrl() + "Pic.CreateGood";

    //图片收藏和取消 接口
    public static final String PIC_COLLECT = getAppServerUrl() + "Pic.Collect";

    //热门搜索列表 接口
    public static final String ITEM_HOT = getAppServerUrl() + "Item.Hot";

    //历史搜索列表
    public static final String ITEM_HISTORY = getAppServerUrl() + "Item.History";

    // 清除历史记录
    public static final String ITEM_DEHISTORY = getAppServerUrl() + "Item.Dehistory";

    // 推荐
    public static final String ITEM_TUIJIAN = getAppServerUrl() + "Item.Tuijian";

    // 获取分享链接
    public static final String SHARE_INDEX = getAppServerUrl() + "Share.Index";


}
