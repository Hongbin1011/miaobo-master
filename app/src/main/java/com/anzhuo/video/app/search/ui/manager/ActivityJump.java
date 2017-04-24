package com.anzhuo.video.app.search.ui.manager;

import android.content.Context;

import com.anzhuo.video.app.model.bean.ItemListInfo;

import java.util.List;

/**
 * Created by wbb on 2016/8/2.
 */
public class ActivityJump {

    private static ActivityJump ourInstance = new ActivityJump();

    public static ActivityJump getInstance() {
        return ourInstance;
    }

    /**
     * 跳转至 图片详情 界面
     *
     * @author wbb
     */
    public void PictureDetails(Context context,String isCollectioin, int poi, List<ItemListInfo> list) {
        /*Intent intnet = new Intent(context, PictureDetailsActivity.class);
        intnet.putExtra("POI", poi);
        intnet.putExtra("ISCOLLECTION", isCollectioin);
        intnet.putExtra("LISTPIC", (Serializable) list);
        intnet.putExtra("LIST_ID",list.get(poi).getId());
        intnet.putExtra("CATEGORY_ID", list.get(poi).getCategory());
        intnet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intnet);*/
    }

    /**
     * 跳转至 设置 界面
     *
     * @author wbb
     */
    public void JumpSetting(Context context) {
      /*  Intent intnet = new Intent(context, AppRelevantActivity.class);
        intnet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intnet);*/
    }

    /**
     * 跳转至 用户反馈界面
     *
     * @author wbb
     */
    public void JumpUserFeedback(Context context) {
       /* Intent intnet = new Intent(context, UserFeedbackActivity.class);
        intnet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intnet);*/
    }


    /**
     * 跳转至 我的收藏界面
     *
     * @author wbb
     */
    public void JumpMyCollection(Context context) {
      /*  Intent intnet = new Intent(context, MyCollectionActivity.class);
        intnet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intnet);*/
    }

    /**
     * 跳转至 购买界面
     *
     * @author wbb
     */
   // public void JumpBuyPurchase(Context context,PictureDetailInfo info) {
      /*  Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra("PICTITLE",info.getTitle());
        intent.putExtra("BUYURL",info.getFilename());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
//    }//
}
