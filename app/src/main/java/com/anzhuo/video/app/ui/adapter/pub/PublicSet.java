package com.anzhuo.video.app.ui.adapter.pub;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.NotifyPositionInterface;
import com.anzhuo.video.app.model.bean.JokeEntity;
import com.anzhuo.video.app.model.bean.ShareInfo;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.utils.MyTextUtils;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.widget.ShareInterface;
import com.anzhuo.video.app.widget.ShowPopuWindows;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import okhttp3.Call;


/**
 * creat on 2016/8/3 16:09
 * 公共设置(文字类的什么)
 */
public class PublicSet {


    /**
     * 主要是笑话item公共部分的
     *
     * @param mContext
     * @param holder
     * @param bean
     */
    public static void initPublicView(final Context mContext,
                                      final ViewHolder holder, final JokeEntity bean,
                                      final String parentString, final String childString,
                                      final String findUserID, final int position,
                                      final NotifyPositionInterface notifyPositionInterface) {
        final String jokeID = bean.getId();

        //文字内容
        String jokeString = bean.getContent().toString().trim();
        TextView tv_item_joke_content = holder.getView(R.id.tv_item_joke_content);
        if (jokeString.equals("")) {
            tv_item_joke_content.setVisibility(View.GONE);
        } else {
            tv_item_joke_content.setVisibility(View.VISIBLE);
            tv_item_joke_content.setText(jokeString);
        }

        //点赞这行
        TextView tv_item_joke_share = holder.getView(R.id.tv_item_joke_share);//分享
//        tv_item_joke_share.setText(bean.getShare_num());
        setDrawbleAndText(mContext, tv_item_joke_share, R.drawable.icon_share, R.color.text_remind_gray_light);
        TextView tv_item_joke_comment = holder.getView(R.id.tv_item_joke_comment);//评论
        tv_item_joke_comment.setText(bean.getComment_num());
        setDrawbleAndText(mContext, tv_item_joke_comment, R.drawable.icon_comment, R.color.text_remind_gray_light);
        final TextView tv_good = holder.getView(R.id.tv_item_joke_zan);//点赞
        final TextView tv_bad = holder.getView(R.id.tv_item_collect);//点踩
        //点赞数和点踩数
        final int goodNum = Integer.parseInt(bean.getGood_num());
        final int badNum = Integer.parseInt(bean.getLow_num());
//        Logger.i("点赞数量=" + goodNum + "点踩数量=" + badNum);
        tv_good.setText(goodNum + "");
        tv_bad.setText(badNum + "");
        int goodOrBad = bean.getLikes();  //0未点赞，1点赞，2点踩
        if (goodOrBad == 1) {//表示点赞了，就需要将点赞的图片和字体颜色变化了
            setDrawbleAndText(mContext, tv_good, R.drawable.icon_like_selected, R.color.theme_main);
            setDrawbleAndText(mContext, tv_bad, R.drawable.icon_unlike, R.color.text_remind_gray_light);
        } else if (goodOrBad == 2) {//表示踩
            setDrawbleAndText(mContext, tv_good, R.drawable.icon_like, R.color.text_remind_gray_light);
            setDrawbleAndText(mContext, tv_bad, R.drawable.ico_unlike_selected, R.color.theme_main);
        } else {//没有点赞也没有点踩
            setDrawbleAndText(mContext, tv_good, R.drawable.icon_like, R.color.text_remind_gray_light);
            setDrawbleAndText(mContext, tv_bad, R.drawable.icon_unlike, R.color.text_remind_gray_light);
        }

        tv_good.setOnClickListener(new View.OnClickListener() {//点赞  点击事件
            @Override
            public void onClick(View v) {//先判断goodOrBad 的值，如果是
                int goodOrBad = bean.getLikes();
                Logger.i("点赞goodOrBad=" + goodOrBad);
                if (goodOrBad == 1) {//已经点赞
                    Logger.i("已经点赞了");
                } else if (goodOrBad == 2) {//已经点踩
                    ToastUtil.showCenterToast(R.string.bad_had);
                } else {
                    //请求网络
                    if (XUtilNet.isNetConnected()) {
                        Logger.i("jokeID=" + jokeID);
                        HomeModel.goodOrBad(true, jokeID, new NewInterface() {
                            @Override
                            public void onError(Call call, Exception e) {
                                ToastUtil.showCenterToast(R.string.connect_error);
                            }

                            @Override
                            public void onSucceed(int state, String data, JSONObject obj) {
                                if (state == 200) {//点赞成功
                                    Logger.i("点赞已经成功" + "点赞前的数量=" + goodNum);
                                    bean.setLikes(1);//并将data的值改变
                                    bean.setGood_num((goodNum + 1) + "");
//                                    notifyPositionInterface.notify(position);
                                    tv_good.setText((goodNum + 1) + "");
                                    setDrawbleAndText(mContext, tv_good, R.drawable.icon_like_selected, R.color.theme_main);

                                    Logger.i("点赞成功后点赞数量=" + bean.getGood_num());
                                }
                            }
                        });
                    } else {
                        ToastUtil.showToast(R.string.no_net);
                    }
                }
            }
        });
        tv_bad.setOnClickListener(new View.OnClickListener() {//点踩的点击事件
            @Override
            public void onClick(View v) {
                int goodOrBad = bean.getLikes();
                Logger.i("点踩goodOrBad=" + goodOrBad);
                if (goodOrBad == 1) {//已经点赞
                    ToastUtil.showCenterToast(R.string.good_had);
                } else if (goodOrBad == 2) {//已经点踩
                    Logger.i("已经点踩了");
                } else {
                    //请求网络
                    if (XUtilNet.isNetConnected()) {
                        Logger.i("jokeID=" + jokeID);
                        HomeModel.goodOrBad(false, jokeID, new NewInterface() {
                            @Override
                            public void onError(Call call, Exception e) {
                                ToastUtil.showCenterToast(R.string.connect_error);
                            }

                            @Override
                            public void onSucceed(int state, String data, JSONObject obj) {
                                if (state == 200) {//点踩成功
                                    bean.setLikes(2);//并将data的值改变
                                    bean.setLow_num((badNum + 1) + "");
//                                    notifyPositionInterface.notify(position);
                                    Logger.i("点踩后=" + data + "  点踩前的数量=" + badNum);
                                    tv_bad.setText((badNum + 1) + "");
                                    setDrawbleAndText(mContext, tv_bad, R.drawable.ico_unlike_selected, R.color.theme_main);
                                    Logger.i("点踩成功后点踩数量=" + bean.getLow_num());
                                }
                            }
                        });

                    } else {
                        ToastUtil.showToast(R.string.no_net);
                    }
                }

            }
        });


        //分享
        holder.setOnClickListener(R.id.tv_item_joke_share, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareInfo shareInfo = new ShareInfo();
                        //   标题
                        shareInfo.setTitle(VideoApplication.getContext().getResources().getString(R.string.app_name));
                        shareInfo.setJokeUserID("");
                        String type = bean.getType();
                        switch (type) {
                            case "1":
                                //如果是段子就分享图标
                                shareInfo.setImgUrl(bean.getCom_user_info().getUser_img_url());//分享的图标
                                break;
                            case "2":
                                //视频
                                shareInfo.setImgUrl(bean.getVideo().get(0).getImg_url());//分享的图标
                                break;
                            case "3"://图片
                                //如果是段子就分享图标
                                shareInfo.setImgUrl(bean.getImages().get(0).getImg_url());//分享的图标
                                break;
                        }
                        //类型:1段子，2视频，3 图片
                        shareInfo.setContext(bean.getContent() + "");//内容id
                        shareInfo.setJId(bean.getJId());//笑话id
                        shareInfo.setUrl("");//点击的详情页
                        shareInfo.setIsCollect(bean.getKeep() + "");
                        ShowPopuWindows popuWindows = new ShowPopuWindows((Activity) mContext, shareInfo, new ShareInterface() {
                            @Override
                            public void ShareRes(String ShareRes) {//分享

                            }
                        });
                        popuWindows.ShowPop();
                    }
                }

        );
    }

    /**
     * 主要是给tv设置图片和字体颜色
     *
     * @param mContext
     * @param textView1
     * @param DrawableID
     * @param ColorID
     */

    private static void setDrawbleAndText(Context mContext, TextView textView1, int DrawableID, int ColorID) {
        MyTextUtils.setTextDrawable(mContext, textView1, DrawableID);
        textView1.setTextColor(mContext.getResources().getColor(ColorID));
    }
}
