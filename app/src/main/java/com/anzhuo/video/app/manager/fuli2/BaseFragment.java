package com.anzhuo.video.app.manager.fuli2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.fuli2.base.BaseActivity;
import com.anzhuo.video.app.manager.fuli2.manager.ActionBarController;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.message.MessageCallback;
import com.anzhuo.video.app.message.MessageHelper;
import com.anzhuo.video.app.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;


/**
 * Created by husong on 2017/2/21.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener, MessageCallback {
    protected View mRootView;//根布局
    private MessageHelper mMessageHelper;
    protected MultiStateView mStateView;
    private boolean mIsVisibleToUser;
    private ActionBarController mActionBarController;
    private boolean mHasBindView;
    public Context mContext;
    protected BaseActivity mActivity;
    public static boolean isShow = false;//图标是否显示(加号,减号图标)


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageHelper = new MessageHelper();
        mMessageHelper.setMessageCallback(this);
        attachAllMessage();
        mMessageHelper.registerMessages();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = getLayoutRes();
        mContext = getContext();
        if (layoutRes == 0) {
            throw new IllegalArgumentException(
                    "getLayoutRes() returned 0, which is not allowed. "
                            + "If you don't want to use getLayoutRes() but implement your own view for this "
                            + "fragment manually, then you have to override onCreateView();");
        } else {
            if (mRootView == null) {
                if (addActionBar()) {
                    LinearLayout actionbarRootLayout = (LinearLayout) inflater.inflate(R.layout.action_bar_root_page, container, false);
                    View contentView = inflater.inflate(layoutRes, null);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    contentView.setLayoutParams(layoutParams);
                    actionbarRootLayout.addView(contentView);
                    mRootView = actionbarRootLayout;
                    mActionBarController = new ActionBarController(mRootView, this);

                } else if (addOverlayActionBar()) {
                    FrameLayout actionbarRootLayout = (FrameLayout) inflater.inflate(R.layout.action_bar_root_page2, container, false);
                    View contentView = inflater.inflate(layoutRes, null);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    contentView.setLayoutParams(layoutParams);
                    actionbarRootLayout.addView(contentView, 0);
                    mRootView = actionbarRootLayout;
                    mActionBarController = new ActionBarController(mRootView, this);

                } else {
                    mRootView = inflater.inflate(layoutRes, container, false);
                }
                mStateView = findView(R.id.stateView);
                if (mStateView != null) {
                    setViewForState();
                }
                if (isInViewPager()) {
                    if (mStateView != null && mStateView.getView(MultiStateView.VIEW_STATE_LOADING) != null) {
                        mStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    }

                    if (mIsVisibleToUser) {
                        bindViews();
                        mHasBindView = true;
                    }
                } else {
                    bindViews();
                    mHasBindView = true;
                }
            }

            return mRootView;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHasBindView) {
            setupViews();
        }

    }

    public ActionBarController getActionBarController() {
        return mActionBarController;
    }

    /**
     * 添加ActionBar(垂直)
     *
     * @return
     */
    protected boolean addActionBar() {
        return false;
    }

    /**
     * 添加ActionBar (Actionbar覆盖内容区域)
     * 即让ActionBar悬浮在内容上方
     *
     * @return
     */
    protected boolean addOverlayActionBar() {
        return false;
    }


    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && !mHasBindView && mRootView != null) {
            bindViews();
            mHasBindView = true;
            setupViews();
        }
    }


    protected void showLoadingView() {
        mStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    /**
     * 显示错误View并初始化控件
     */
    protected void showErrorView() {
        mStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        View errorView = mStateView.getView(MultiStateView.VIEW_STATE_ERROR);
        if (errorView != null && errorView.findViewById(R.id.tvRetry) != null) {
            errorView.findViewById(R.id.tvRetry).setOnClickListener(this);
        }
    }

    /**
     * 显示空View并初始化控件
     */
    protected void showEmptyView() {
        mStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        View emptyView = mStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        if (emptyView != null && emptyView.findViewById(R.id.tvEmpty) != null) {
            emptyView.findViewById(R.id.tvEmpty).setOnClickListener(this);
        }
    }

    protected void showContentView() {
        mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }


    /**
     * 自定义某个状态的View，MultiStateView中的某种状态View
     * {VIEW_STATE_CONTENT, VIEW_STATE_ERROR, VIEW_STATE_NETWORK_ERROR, VIEW_STATE_EMPTY, VIEW_STATE_LOADING}
     */
    protected void setViewForState() {

    }

    protected boolean isInViewPager() {
        return false;
    }

    protected final void attachMessage(Message.Type type) {
        mMessageHelper.attachMessage(type);
    }

    /**
     * 监听所有消息
     */
    protected void attachAllMessage() {

    }

    protected abstract
    @LayoutRes
    int getLayoutRes();

    /**
     * 设置view相关属性
     */
    protected void setupViews() {

    }

    /**
     * 初始化View
     */
    public abstract void bindViews();


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
        tv.setTypeface(ViewUtil.setIconFont(mContext));
        return (V) tv;
    }

    /**
     * 处理返回事件
     *
     * @return
     */
    public boolean goBack() {
        return false;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.abBack:
            case R.id.abBackLl:
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                break;

            case R.id.tvRetry:
                showLoadingView();
                reload();
                break;
        }
    }

    protected void reload(){

    }

    @Override
    public void onDestroy() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        mMessageHelper.unRegisterMessages();
        mMessageHelper.clearMessages();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    /**
     * @param message
     */
    @Override
    public void onReceiveMessage(Message message) {

    }

    public void onPageSelect() {

    }
}
