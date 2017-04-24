package com.anzhuo.video.app.meinv.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.fuli2.utils.ActivityUtils;
import com.anzhuo.video.app.meinv.utils.StatusBarUtil;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.message.MessageCallback;
import com.anzhuo.video.app.message.MessageHelper;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 作者：wbb
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener, MessageCallback {

    //    protected View mRootView;
    public Context mContext;
    public Bundle savedInstanceState;
    private MessageHelper mMessageHelper;

    public ImageView mainBack;
    public TextView headTitle;
    public TextView headRightText;
    public ImageView headRightImg;
    public RelativeLayout mainHeadLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mContext = BaseActivity.this;
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        mMessageHelper = new MessageHelper();
        mMessageHelper.setMessageCallback(this);
        attachAllMessage();
        mMessageHelper.registerMessages();
        this.savedInstanceState = savedInstanceState;
        ActivityUtils.getScreenManager().pushActivity(this);//将activity加入栈中
        int layoutRes = getLayoutRes();
        if (layoutRes > 0) {
//            if (mRootView == null) {
//                mRootView = View.inflate(this, layoutRes, null);
//            }
            setContentView(layoutRes);
            bindViews();
        }
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

    public void setHeadTitle(String title) {
        findHead();
        ViewUtil.visible(headTitle);
        headTitle.setText(title);
    }

    public void setHeadTitle(int StringId) {
        findHead();
        ViewUtil.visible(headTitle);
        headTitle.setText(StringId);
    }

    public void setRightImg(int id) {
        findHead();
        ViewUtil.visible(headRightImg);
        ViewUtil.gone(headRightText);
        headRightImg.setBackgroundResource(id);
    }

    public void setRightText(String name) {
        findHead();
        ViewUtil.visible(headRightText);
        ViewUtil.gone(headRightImg);
        headRightText.setText(name + "");
    }

    private void findHead() {
        mainBack = findView(R.id.main_back);
        mainBack.setOnClickListener(this);
        headTitle = findView(R.id.head_title);
        headRightImg = findView(R.id.head_right_img);
        headRightText = findView(R.id.head_right_text);
        headRightImg.setOnClickListener(this);
        headRightText.setOnClickListener(this);
        mainHeadLayout = findView(R.id.main_head_layout);
    }

    public void setupStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (StatusBarUtil.MIUISetStatusBarLightMode(getWindow(), true)) {
                XUtilLog.log_i("leibown", "miui");//小米
            } else if (StatusBarUtil.FlymeSetStatusBarLightMode(getWindow(), true)) {
                XUtilLog.log_i("leibown", "flyme");//魅族
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                XUtilLog.log_i("leibown", ">6.0");
            } else {
                XUtilLog.log_i("leibown", ">4.0");
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().toString());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 返回Activity的布局文件ID
     *
     * @return
     */
    @LayoutRes
    protected int getLayoutRes() {
        return 0;
    }


    /**
     * 初始化View
     */
    protected void bindViews() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getScreenManager().popActivity(this);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.main_back:
//                onBackPressed();
//                break;
//        }
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V findView(@IdRes int resId) {
        return ViewUtil.findView(this, resId);
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V findView(View rootView, @IdRes int resId) {
        return ViewUtil.findView(rootView, resId);
    }

    /**
     * findViewById 并添加OnClick事件
     *
     * @param resId
     * @param <V>   具体的View类型
     * @return
     */
    protected <V> V findViewAttachOnclick(@IdRes int resId) {
        return ViewUtil.findViewAttachOnclick(this, resId, this);
    }

    protected <V> V findViewAttachOnclick(View rootView, @IdRes int resId) {
        return ViewUtil.findViewAttachOnclick(rootView, resId, this);
    }

    @Override
    public void onReceiveMessage(Message message) {

    }
}
