package com.anzhuo.video.app.movie;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anzhuo.video.app.manager.fuli2.utils.ActivityUtils;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.message.MessageCallback;
import com.anzhuo.video.app.message.MessageHelper;
import com.anzhuo.video.app.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by husong on 2017/2/21.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
private MessageHelper mMessageHelper;
private FragmentFactory mFragmentFactory;

@Override
protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }
        mFragmentFactory = new FragmentFactory();
        ActivityUtils.getScreenManager().pushActivity(this);
        if(getLayoutRes() > 0){
        setContentView(getLayoutRes());
        bindViews();
        }
        attachAllMessage();
        mMessageHelper.registerMessages();
        }



@LayoutRes
protected int getLayoutRes() {
        return 0;
        }


protected  void bindViews(){}

public void setMessageCallback(MessageCallback messageCallback){
        mMessageHelper.setMessageCallback(messageCallback);
        }

protected void attachAllMessage() {
        mMessageHelper = new MessageHelper();

        }

protected void attachMessage(Message.Type type){
        mMessageHelper.attachMessage(type);
        }



@Override
protected void onDestroy() {
        if (mMessageHelper != null) {
        mMessageHelper.unRegisterMessages();
        mMessageHelper.clearMessages();
        }
        mFragmentFactory.clearCache();
        mFragmentFactory = null;
        super.onDestroy();
        ActivityUtils.getScreenManager().popActivity(this);
        }

@Override
public void onClick(final View v) {

        }

public FragmentFactory getFragmentFactory() {
        return mFragmentFactory;
        }

/**
 * findViewById 省略强转过程
 * @param resId
 * @param <V>具体的View类型
 * @return
 */
protected <V> V findView(@IdRes int resId) {
        return ViewUtil.findView(this, resId);
        }

/**
 * findViewById 并添加OnClick事件
 * @param resId
 * @param <V> 具体的View类型
 * @return
 */
protected <V> V findViewAttachOnclick(@IdRes int resId) {
        return ViewUtil.findViewAttachOnclick(this, resId, this);
        }

@Override
protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        }

@Override
protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        }
}
