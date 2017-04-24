package com.anzhuo.video.app.dongtu.widget;/*
package com.app.girl.beauty.dongtu.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.movie.beauty.R;
import com.movie.beauty.application.VideoApplication;
import com.movie.beauty.bean.dongtu.ShareInfo;
import com.movie.beauty.dongtus.constant.DongtuConstants;
import com.movie.beauty.manager.dongtu.HomeModel;
import com.movie.beauty.manager.dongtu.NewInterface;
import com.movie.beauty.utils.NoDoubleClickListener;
import com.movie.beauty.utils.SharedUtil;
import com.movie.beauty.utils.XUtilNet;
import com.movie.beauty.utils.dongtu.DongtuToastUtil;
import com.movie.beauty.utils.dongtu.MyTextUtils;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;


*/
/**
 * Created by chenqiang on 2016/4/26.
 * 分享的popuwindow
 *//*

public class ShowPopuWindows implements View.OnClickListener {
    private PopupWindow popupWindow;
    private View mPopView;
    private TextView tv_share_weixin;
    private TextView tv_share_weixin_space;
    private TextView tv_share_qq;
    private TextView tv_share_qq_space;
    private TextView tv_share_xinlang;
    private TextView tv_share_report;
    private TextView tv_share_cancel;
    //    private LinearLayout ll_share_2;
    private View line_share_pop;

    private ShareInfo mData;
    private String jokeID = "";
    private boolean hideDown = false;

    public static final int SHARE_SUCCESS = 1;
    public static final int SHARE_CANCEL = 2;
    public static final int SHARE_FAILURE = 3;
    static boolean isFriend = false;//记录是否选中了朋友圈
    private Activity activity;
    private String PlateName;
    private ShareInterface shareInterface;

    public ShowPopuWindows(Activity activity, ShareInfo info, ShareInterface shareInterface) {
        this.mData = info;
        this.activity = activity;
        this.shareInterface = shareInterface;
        this.jokeID = info.getJId();
        showPopTypewindow();
    }

    public ShowPopuWindows(Activity activity, String text) {
        this.activity = activity;
        showPopTypewindow2(text);
    }

    public ShowPopuWindows(Activity activity, ShareInfo info, boolean hideDown, ShareInterface shareInterface) {
        this.mData = info;
        this.activity = activity;
        this.shareInterface = shareInterface;
        this.jokeID = info.getJId();
        this.hideDown = hideDown;
        showPopTypewindow();
    }

    MyInnerHandler handler = new MyInnerHandler(this);

    static class MyInnerHandler extends Handler {
        WeakReference<ShowPopuWindows> mFrag;

        MyInnerHandler(ShowPopuWindows pop) {
            mFrag = new WeakReference<>(pop);
        }

        @Override
        public void handleMessage(Message msg) {
            this.obtainMessage();
            final ShowPopuWindows pop = mFrag.get();
            switch (msg.what) {
                case SHARE_SUCCESS:
                    if (pop == null || pop.popupWindow.isShowing() == false) {
                        return;
                    }
                    pop.PlateName = (String) msg.obj;
                    //一个用户只会分享一次 下次分享不会进入统计接口
//                    InterfaceManagement.ShareInSuccess(pop.activity, pop.PlateName, pop.mData.getJId());
                    HomeModel.getZan(pop.mData.getJokeUserID(), DongtuConstants.getShare, new NewInterface() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onSucceed(int state, String data, JSONObject obj) {
                            Logger.i("=======[分享调用接口]===========" + obj.toString());
                            if (state == 200) {
                                if (pop.shareInterface != null) {
                                    pop.shareInterface.ShareRes(pop.activity.getResources().getString(R.string.share_success));
                                }
                            }
                        }
                    });

                    if (pop.PlateName.equals("Wechat")) {
                        DongtuToastUtil.showCenterToast("微信分享成功");
                    } else if (pop.PlateName.equals("WechatMoments")) {
                        DongtuToastUtil.showCenterToast("朋友圈分享成功");
                    } else if (pop.PlateName.equals("SinaWeibo")) {
                        DongtuToastUtil.showCenterToast("新浪分享成功");
                    } else if (pop.PlateName.equals("QZone")) {
                        DongtuToastUtil.showCenterToast("QQ空间分享成功");
                    } else {
                        DongtuToastUtil.showCenterToast(pop.PlateName + "分享成功");
                    }

                    if (pop.popupWindow != null) {
                        pop.popupWindow.dismiss();
                    }
                    break;
                case SHARE_CANCEL:
                    if (pop == null || pop.popupWindow.isShowing() == false) {
                        return;
                    }
                    DongtuToastUtil.showCenterToast("取消分享");
                    if (pop.shareInterface != null) {
                        pop.shareInterface.ShareRes(pop.activity.getResources().getString(R.string.share_cancel));
                    }
                    if (pop.popupWindow != null) {
                        pop.popupWindow.dismiss();
                    }
                    break;
                case SHARE_FAILURE:
                    if (pop == null || pop.popupWindow.isShowing() == false) {
                        return;
                    }
                    DongtuToastUtil.showCenterToast("分享失败  " + msg.obj);
                    Logger.i("---分享失败-----" + msg.obj);
                    if (pop.shareInterface != null)
                        pop.shareInterface.ShareRes(pop.activity.getResources().getString(R.string.share_fail));
                    if (pop.popupWindow != null) {
                        pop.popupWindow.dismiss();
                    }
                    break;
                default:
                    break;
            }//end switch
        }
    }


//    public ShowPopuWindows(ShareInfo info, ShareInterface shareInterface) {
//        this.activity = activity;
//        this.shareInterface = shareInterface;
//        showPopTypewindow();
//    }


    */
/**
     * 显示分享
     *//*

    public void showPopTypewindow() {
//        JCVideoPlayer.releaseAllVideos();
        LayoutInflater mLayoutInflater = (LayoutInflater) VideoApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopView = mLayoutInflater.inflate(R.layout.share_dialog_view, null);
        popupWindow = new PopupWindow(mPopView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        // 必须设置background才能消失
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.fullscreen_share_bg));
        popupWindow.setBackgroundDrawable(VideoApplication.getInstance().getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        // 使用系统动画
//		popupWindow.setAnimationStyle(R.style.tudou_encrypt_dialog);
        // 自定义动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
//        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);


        tv_share_weixin = (TextView) mPopView.findViewById(R.id.tv_share_weixin);
        tv_share_weixin_space = (TextView) mPopView.findViewById(R.id.tv_share_weixin_space);
        tv_share_qq = (TextView) mPopView.findViewById(R.id.tv_share_qq);
        tv_share_qq_space = (TextView) mPopView.findViewById(R.id.tv_share_qq_space);
        tv_share_xinlang = (TextView) mPopView.findViewById(R.id.tv_share_xinlang);
        tv_share_cancel = (TextView) mPopView.findViewById(R.id.tv_share_cancel);
        line_share_pop = mPopView.findViewById(R.id.line_share_pop);

        tv_share_weixin.setOnClickListener(this);
        tv_share_weixin_space.setOnClickListener(this);
        tv_share_qq.setOnClickListener(this);
        tv_share_qq_space.setOnClickListener(this);
        tv_share_xinlang.setOnClickListener(this);
        tv_share_cancel.setOnClickListener(this);

        popupWindow.update();
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        // popWindow消失监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                //   handler.sendEmptyMessage(2);
            }
        });
    }

    public void showPopTypewindow2(final String text) {
//        JCVideoPlayer.releaseAllVideos();

        LayoutInflater mLayoutInflater = (LayoutInflater) VideoApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopView = mLayoutInflater.inflate(R.layout.share_dialog, null);
        popupWindow = new PopupWindow(mPopView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        // 必须设置background才能消失
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.fullscreen_share_bg));
        popupWindow.setBackgroundDrawable(VideoApplication.getInstance().getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        // 使用系统动画
//		popupWindow.setAnimationStyle(R.style.tudou_encrypt_dialog);
        // 自定义动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);

        EditText share_edit = (EditText) mPopView.findViewById(R.id.share_edit);
        String share_url = SharedUtil.getString("share_url");
        Logger.i("=======[11share_url]===========" + share_url);
        if (!share_url.equals("0")) {
            share_edit.setText(share_url);
        } else {
            share_url = "分享链接失效，请重启APP试试";
            share_edit.setText(share_url);
        }
        final String share_urla = share_url;
        Button btn_copy = (Button) mPopView.findViewById(R.id.share_copy);
        btn_copy.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                MyTextUtils.copy(share_urla, activity);
                DongtuToastUtil.showCenterToast("复制成功");
                popupWindow.dismiss();
            }
        });

        popupWindow.update();
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        // popWindow消失监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                //   handler.sendEmptyMessage(2);
            }
        });
    }

    */
/**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *//*

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    */
/**
     * 分享调用
     * 显示
     *
     * @param isDongtu 是否为动图
     *//*

    public void ShowPop(boolean isDongtu) {
        //测试分享功能
        if (!XUtilNet.isNetConnected()) {
            DongtuToastUtil.showCenterToast(activity.getString(R.string.check_net_is_connect));
        } else {
            //设置添加屏幕的背景透明度
            showVideoTypePOP(isDongtu);
        }
    }

    public void showVideoTypePOP(boolean isDongtu) {
        backgroundAlpha(0.5f);
//        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.pop_anim);
//        //开始执行动画
//        mPopView.startAnimation(animation);
        if (!popupWindow.isShowing()) {
            int width = activity.getWindowManager().getDefaultDisplay().getWidth();
            View parent = activity.getWindow().getDecorView();
            if (isDongtu) {
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, width, 0);
            } else {
                popupWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        }
    }

    */
/**
     * 设置数据
     *//*

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_weixin://微信好友
                //传入数据
                Platform.ShareParams wechatSp = getShareParams(mData, true, false);
                Platform wechat = ShareSDK.getPlatform(activity, Wechat.NAME);
                wechat.setPlatformActionListener(new PlatformActionListenerImpl());
                // 执行分享
                wechat.share(wechatSp);
                break;
            case R.id.tv_share_weixin_space://微信朋友圈
                isFriend = true;
                Platform.ShareParams wechatMomentsSp = getShareParams(mData, true, false);
                Platform wechatMoments = ShareSDK.getPlatform(activity, WechatMoments.NAME);
                wechatMoments.setPlatformActionListener(new PlatformActionListenerImpl());
                // 执行分享
                wechatMoments.share(wechatMomentsSp);
                break;
            case R.id.tv_share_qq://qq 好友
                Logger.i("点击QQ分享");
                Platform.ShareParams qq = getShareParams(mData, false, false);
                Platform qq2 = ShareSDK.getPlatform(activity, QQ.NAME);
                qq2.setPlatformActionListener(new PlatformActionListenerImpl());
                // 执行分享
                qq2.share(qq);
                break;
            case R.id.tv_share_qq_space://qq空间
                Platform.ShareParams qZoneSp = getShareParams(mData, false, false);
                Platform qZone = ShareSDK.getPlatform(activity, QZone.NAME);
                qZone.setPlatformActionListener(new PlatformActionListenerImpl()); // 设置分享事件回调
                // 执行分享
                qZone.share(qZoneSp);
                break;
            case R.id.tv_share_xinlang://新浪微博
                Platform.ShareParams sinaWeiboSp = getShareParams(mData, false, true);
                Platform sinaWeibo = ShareSDK.getPlatform(activity, SinaWeibo.NAME);
                sinaWeibo.setPlatformActionListener(new PlatformActionListenerImpl()); // 设置分享事件回调
                // 执行分享
                sinaWeibo.share(sinaWeiboSp);
                break;
            case R.id.tv_share_cancel:
                popupWindow.dismiss();
                break;
        }
    }


    private static Platform.ShareParams getShareParams(ShareInfo data, boolean isWeChat, boolean isSinna) {
        // 初始化sdk分享资源
        // 初始化要属相的内容
        Platform.ShareParams shareParams = new Platform.ShareParams();
        if (isSinna) {//新浪分享
            shareParams.setText(data.getContext() + data.getUrl());//详情
            shareParams.setImageUrl(data.getImgUrl());
            // shareParams.setImageUrl("http://m.xiaohua.com/");
        } else {
            if (isWeChat) {
//                shareParams.setShareType(Platform.SHARE_TEXT);
//                shareParams.setShareType(Platform.SHARE_IMAGE);
//                shareParams.setShareType(Platform.SHARE_WEBPAGE);
                if (!isFriend) {
                    shareParams.setShareType(Platform.SHARE_EMOJI);
                }
//                shareParams.setUrl(data.getUrl());// 微信点击跳转的url
            } else {
                shareParams.setShareType(Platform.SHARE_EMOJI);
//                shareParams.setTitleUrl(""); //点击详情链接
            }
            Logger.i("cq====分享的内容==" + data.toString());
            if (isFriend) {//是朋友圈? 朋友圈就是把内容设置给标题
                shareParams.setUrl(data.getUrl());// 微信点击跳转的url
                shareParams.setText(data.getContext());//App详情
                shareParams.setTitle(data.getContext());
                shareParams.setShareType(Platform.SHARE_TEXT);
                shareParams.setShareType(Platform.SHARE_IMAGE);
                shareParams.setShareType(Platform.SHARE_WEBPAGE);
                shareParams.setImageUrl(data.getImgUrl());//分享的图标
                isFriend = false;//朋友圈标志位
            } else {
//                shareParams.setText(null);//App详情
//                shareParams.setTitle(null);
//                shareParams.setUrl(data.getUrl());
//                shareParams.setImageUrl("http://p3.pstatp.com/large/13b90002092635a1695d");//分享的图标
                //  shareParams.setImageUrl("http://dtap.oss-cn-beijing.aliyuncs.com/gif/6f12bd4528fba3c740bb1eb13179f530.gif");//分享的图标
//                shareParams.setShareType(Platform.SHARE_IMAGE);
                shareParams.setImageUrl(data.getImgUrl());//分享的图标
                Logger.i("cq=============[没有]===========" + data.getUrl().toString());
            }
        }
        return shareParams;
    }

    class PlatformActionListenerImpl implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {//回调的地方是子线程，进行UI操作要用handle处理
            Logger.i("cq----成-----" + platform.getName() + "=====");
            Message msg = Message.obtain();
            msg.what = SHARE_SUCCESS;
            msg.obj = platform.getName();
            handler.sendMessage(msg);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {//回调的地方是子线程，进行UI操作要用handle处理
            throwable.printStackTrace();
            Message msg = Message.obtain();
            msg.what = SHARE_FAILURE;
            String expName = throwable.getClass().getSimpleName();
            if ("WechatClientNotExistException".equals(expName) ||
                    "WechatTimelineNotSupportedException".equals(expName)
                    || "WechatFavoriteNotSupportedException".equals(expName)) {//判断是不是因为没有安装微信
                String error = "尚未安装微信，请安装后重试";
                msg.obj = error;
            } else {
                Logger.i("cq----失败 错误信息-----====" + throwable.getMessage());
                msg.obj = throwable.getMessage();
            }
            Logger.i("cq----失败-----" + platform.getName() + "====" + msg.obj);
            handler.sendMessage(msg);
        }

        @Override
        public void onCancel(Platform platform, int i) {//回调的地方是子线程，进行UI操作要用handle处理
            Logger.i("cq----取消-----" + platform.getName());
            handler.sendEmptyMessage(SHARE_CANCEL);
        }
    }
}

*/
