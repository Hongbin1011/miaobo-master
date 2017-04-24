package com.anzhuo.video.app.widget;

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
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.ShareInfo;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.utils.MyTextUtils;
import com.anzhuo.video.app.utils.NoDoubleClickListener;
import com.anzhuo.video.app.utils.SharedUtil;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

import okhttp3.Call;

/**
 * Created by chenqiang on 2016/4/26.
 * 分享的popuwindow
 */
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
                    HomeModel.getZan(pop.mData.getJokeUserID(), Constants.getShare, new NewInterface() {
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
                        ToastUtil.showCenterToast("微信分享成功");
                    } else if (pop.PlateName.equals("WechatMoments")) {
                        ToastUtil.showCenterToast("朋友圈分享成功");
                    } else if (pop.PlateName.equals("SinaWeibo")) {
                        ToastUtil.showCenterToast("新浪分享成功");
                    } else if (pop.PlateName.equals("QZone")) {
                        ToastUtil.showCenterToast("QQ空间分享成功");
                    } else {
                        ToastUtil.showCenterToast(pop.PlateName + "分享成功");
                    }

                    if (pop.popupWindow != null) {
                        pop.popupWindow.dismiss();
                    }
                    break;
                case SHARE_CANCEL:
                    if (pop == null || pop.popupWindow.isShowing() == false) {
                        return;
                    }
                    ToastUtil.showCenterToast("取消分享");
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
                    ToastUtil.showCenterToast("分享失败  " + msg.obj);
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


    /**
     * 显示分享
     */
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
                ToastUtil.showCenterToast("复制成功");
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

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 分享调用
     * 显示
     */
    public void ShowPop() {
        //测试分享功能
        if (!XUtilNet.isNetConnected()) {
            ToastUtil.showCenterToast(activity.getString(R.string.check_net_is_connect));
        } else {
            //设置添加屏幕的背景透明度
            showVideoTypePOP();
        }
    }

    public void showVideoTypePOP() {
        backgroundAlpha(0.5f);
//        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.pop_anim);
//        //开始执行动画
//        mPopView.startAnimation(animation);
        if (!popupWindow.isShowing()) {
            int width = activity.getWindowManager().getDefaultDisplay().getWidth();
            View parent = activity.getWindow().getDecorView();
            popupWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    /**
     * 设置数据
     */
    @Override
    public void onClick(View v) {
    }
}

