package com.anzhuo.video.app.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.anzhuo.video.app.model.bean.VersionStartBean;
import com.anzhuo.video.app.ui.activity.AddTitleActivity;
import com.anzhuo.video.app.ui.activity.CollectActivity;
import com.anzhuo.video.app.ui.activity.DetailActivity;
import com.anzhuo.video.app.ui.activity.PhotoActivity;
import com.anzhuo.video.app.ui.activity.ZxDetailActivityX5;
import com.orhanobut.logger.Logger;


/**
 * creat on 2016/4/26.
 * 我的相关界面 跳转公共类
 */
public class ActivityMy {


    private static ActivityMy ourInstance = new ActivityMy();

    public static ActivityMy getInstance() {
        return ourInstance;
    }


    /**
     * >>>>>>>>>查看照片
     * <p>
     * created at 2016/5/13 10:01
     */
    public static void startPhotoActivity(Activity activity, String jokeEntity, String Lit_url, int pictureWidth, int pictureHeight) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra("photourl", jokeEntity);
        intent.putExtra("Lit_url", Lit_url);
        intent.putExtra("pictureWidth", String.valueOf(pictureWidth));
        intent.putExtra("pictureHeight", String.valueOf(pictureHeight));
        activity.startActivity(intent);
    }


    /**
     * 跳转到 详情页面
     *
     * @param context
     */
    public static void startDetailsActivity(Context context, Bundle bundle) {
        String parent = bundle.getString("parent");
        Logger.i("position===========[传值2]===========:" + parent);
//        Intent intent = new Intent(context, DetailsActivity2.class);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转至add
     *
     * @param context
     */
    public static void SkipAddTitle(Context context) {
        Intent intent = new Intent(context, AddTitleActivity.class);
        context.startActivity(intent);
    }

    public static void skipX5Detail(Activity mActivity, VersionStartBean versionStartBean) {
        Intent intent = new Intent(mActivity, ZxDetailActivityX5.class);
        intent.putExtra("url", versionStartBean.getAd_url());
        intent.putExtra("titles", versionStartBean.getAd_title());
        mActivity.startActivity(intent);
    }

    public static void skipX5Details(Activity mActivity, String title, String url) {
//        url = "http://m.v.6.cn/live/u1?src=ummeda5298";
        Intent intent = new Intent(mActivity, ZxDetailActivityX5.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("titles", title);
        mActivity.startActivity(intent);
    }

    public static void startCollcetActivity(Context context,Bundle bundle){

        Intent intent = new Intent(context, CollectActivity.class);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        context.startActivity(intent);

    }

}
