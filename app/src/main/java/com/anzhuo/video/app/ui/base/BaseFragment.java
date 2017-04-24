package com.anzhuo.video.app.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.message.MessageCallback;
import com.anzhuo.video.app.message.MessageHelper;
import com.anzhuo.video.app.utils.ViewUtil;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * Created by Administrator on 2016/4/12.
 * <p/>
 * 添加解决重影的问题
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,MessageCallback {
    public View mRootView;//Fragment 返回的View对象
    private boolean mHasBindView;//是否绑定View(初始化View)
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    public FragmentActivity mActivity;
    /**
     * 请求网络的tag
     */
    public String TAG = "";


    /**
     * 懒加载过
     */
    private boolean isLazyLoaded;

    private boolean isPrepared;

    /**
     * 解决重影的关键
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Logger.i("BaseFragment  savedInstanceState!=null");
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            Logger.i("isSupportHidden=" + isSupportHidden);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        } else {
            Logger.i("BaseFragment  savedInstanceState  ==null");
        }

    }
    /**
     * 初始化View
     *
     * @param resId
     * @param <V>
     * @return
     */
    protected <V> V findView(@IdRes int resId) {
        //noinspection unchecked
        return ViewUtil.findView(mRootView, resId);
    }
    /**
     * 初始化View并设置点击事件
     *
     * @param resId
     * @param <V>
     * @return
     */
    protected <V> V findViewAttachOnclick(@IdRes int resId) {
        return ViewUtil.findViewAttachOnclick(mRootView, resId, this);
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V findViewIcon(@IdRes int resId) {
        TextView tv = ViewUtil.findViewAttachOnclick(mRootView, resId, this);
        tv.setTypeface(ViewUtil.setIconFont(mActivity));
        return (V) tv;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
      /*  getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (JCVideoPlayer.backPress()) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });*/
    }
    /**
     * 处理返回事件
     *
     * @return
     */
    public boolean goBack() {
        return false;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        int layoutRes = getLayoutRes();
        if (layoutRes == 0) {
            throw new IllegalArgumentException(
                    "getLayoutRes() returned 0, which is not allowed. "
                            + "If you don't want to use getLayoutRes() but implement your own view for this "
                            + "fragment manually, then you have to override onCreateView();");
        } else {
            if (mRootView == null) {
                View contentView = inflater.inflate(layoutRes, container, false);
                mRootView = contentView;
                SetTag();
                bindViews();//初始化View
                mHasBindView = true;//初始化完成
            } else {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                if (parent != null) {
                    parent.removeView(mRootView);
                }

            }
            return mRootView;
        }
    }

    /**
     * 资源Layout
     *
     * @return
     */
    protected abstract
    @LayoutRes
    int getLayoutRes();

    /**
     * 初始化View
     */

    public abstract void bindViews();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//当Activity创建完成后调用的方法
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        //只有Fragment onCreateView好了，
        //另外这里调用一次lazyLoad(）
        lazyLoad();
        if (mHasBindView) {//初始化View完成
            setupViews();//设置view相关属性
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    /**
     * 调用懒加载
     */

    private void lazyLoad() {
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    @UiThread
    public abstract void onLazyLoad();


    /**
     * 设置view相关属性
     */
    protected void setupViews() {

    }


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

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    /**
     * 设置TAG，用于统一取消网络请求
     */
    public void SetTag() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(TAG)) {
            Logger.i("取消网络的Fragment TAG = " + TAG);
            OkHttpUtils.getInstance().cancelTag(TAG);
        } else {
            Logger.i("TAG  Fragment  没有赋值");
        }
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
    /**
     * 监听所有消息
     */
    protected void attachAllMessage() {

    }
    private MessageHelper mMessageHelper;
    public String className="123";
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
    public void onReceiveMessage(Message message) {

    }

}
