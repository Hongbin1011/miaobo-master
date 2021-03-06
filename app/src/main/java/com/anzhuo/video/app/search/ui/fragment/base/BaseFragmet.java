package com.anzhuo.video.app.search.ui.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.message.MessageCallback;
import com.anzhuo.video.app.message.MessageHelper;
import com.anzhuo.video.app.search.utils.ViewUtil;
import com.anzhuo.video.app.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by Administrator on 2016/7/22.
 */
public class BaseFragmet extends Fragment implements MessageCallback {


    private int layoutRes;
    private View mRootView;
    protected int getlayoutRes(){
        return 0;
    }
    private MessageHelper mMessageHelper;
    public String className="123";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMessageHelper = new MessageHelper();
        mMessageHelper.setMessageCallback(this);
        attachAllMessage();
        mMessageHelper.registerMessages();
        layoutRes = getlayoutRes();
        LinearLayout actionbarRootLayout = (LinearLayout)
                View.inflate(getContext(), R.layout.base_layout_m, null);
        View contentView = View.inflate(getContext(), layoutRes, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        actionbarRootLayout.addView(contentView, lp);
        mRootView = actionbarRootLayout;
        bindView();
        return actionbarRootLayout;
    }

    protected void bindView(){

    }
    public FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.mActivity = (BaseActivity) activity;
        } else {
            throw new RuntimeException(activity.toString() + "must extends SupportActivity!");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }
    /**
     * 监听所有消息
     */
    protected void attachAllMessage() {

    }
    /**
     * 注册消息
     */
    protected final void attachMessage(Message.Type type) {
        mMessageHelper.attachMessage(type);
    }

    public void setClassName(String className){
        this.className = className;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(className);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(className);
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V $(@IdRes int resId) {
        return ViewUtil.findView(mRootView, resId);
    }

    @Override
    public void onReceiveMessage(Message message) {

    }
}
