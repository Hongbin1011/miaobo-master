package com.anzhuo.video.app.meinv.view;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;


public class ViewTipModule implements OnClickListener {

    private Context mContext;

    private ViewGroup mMainLayout;

    private View mDataLayot;

    private View mTipLayot;

//    private LoadingView mLoadingView;

    private ImageView tipImage;

//    private GifDrawable gifDrawable;

    private Callback mCallBack;

    private EmptyViewClickCallback mEmptyViewClickCallback;

    //加载视图
    private LinearLayout mLoadingLayout;

    //加载失败
    private LinearLayout mLoadFailLayout;

    //提示文本
    private TextView mTipTextView;

    //通用失败提示(加载失败/网络异常)
    private TextView mGeneralFailurePrompt;

    //检查网络
    private ImageView mCheckNetworkBtn;

    //加载失败（刷新页面）
    private ImageView mLoadFailBtn;

    private boolean mIsDefaultLoading = true;

    private boolean mIsEnableTouchClick = false;

    //空数据提示加载失败
    public final static String EMPTY_DATA_SUGGEST_LOADING_FAILURE = "1001";

    //空数据提示网络异常
    public final static String EMPTY_DATA_PROMPT_NETWORK_ANOMALIES = "1002";

    public ViewTipModule(Context context, ViewGroup mianLayout, View dataLayot) {
        this.mContext = context;
        this.mMainLayout = mianLayout;
        this.mDataLayot = dataLayot;
        init();
    }

    public ViewTipModule(Context context, ViewGroup mianLayout, View dataLayot, Callback callBack) {
        this.mContext = context;
        this.mMainLayout = mianLayout;
        this.mDataLayot = dataLayot;
        this.mCallBack = callBack;
        init();
    }

    public ViewTipModule(Context context, ViewGroup mianLayout, View dataLayot,
                         boolean isDefaultLoading, Callback callBack) {
        this.mContext = context;
        this.mMainLayout = mianLayout;
        this.mDataLayot = dataLayot;
        this.mCallBack = callBack;
        this.mIsDefaultLoading = isDefaultLoading;
        init();
    }

    public ViewTipModule(Context context, ViewGroup mianLayout, View dataLayot,
                         boolean isDefaultLoading, boolean isEnableTouchClick, EmptyViewClickCallback callBack) {
        this.mContext = context;
        this.mMainLayout = mianLayout;
        this.mDataLayot = dataLayot;
        this.mEmptyViewClickCallback = callBack;
        this.mIsDefaultLoading = isDefaultLoading;
        this.mIsEnableTouchClick = isEnableTouchClick;
        init();
    }

    @Deprecated
    public ViewTipModule(Context context, ViewGroup mianLayout, Callback callBack) {
        this(context, mianLayout, null, callBack);
    }

    void init() {
        // 获取提示视图
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mTipLayot = inflater.inflate(R.layout.common_load_page_fail_m, null);
        // 空白视图触摸事件
        LinearLayout mTipLayout = (LinearLayout) mTipLayot.findViewById(R.id.load_page_fail_layout);
        if (mIsEnableTouchClick) {
            mTipLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mEmptyViewClickCallback != null) {
                        mEmptyViewClickCallback.setEmptyViewClick();
                    }
                    return false;
                }
            });
        } else {
            mTipLayout.setOnTouchListener(null);
        }

        mGeneralFailurePrompt = (TextView) mTipLayot.findViewById(R.id.general_failure_prompt);

//        mLoadingView = (LoadingView) mTipLayot.findViewById(R.id.loading_v);
        tipImage = (ImageView) mTipLayot.findViewById(R.id.tip_img);

        mLoadingLayout = (LinearLayout) mTipLayot.findViewById(R.id.loading_layout);
        mLoadFailLayout = (LinearLayout) mTipLayot.findViewById(R.id.load_fail_layout);
        mTipTextView = (TextView) mTipLayot.findViewById(R.id.tip_text);

        mLoadFailBtn = (ImageView) mTipLayot.findViewById(R.id.load_fail_btn);
        mCheckNetworkBtn = (ImageView) mTipLayot.findViewById(R.id.check_network_btn);
        mLoadFailBtn.setOnClickListener(this);
        mCheckNetworkBtn.setOnClickListener(this);

        // 添加视图到主布局文件
        FrameLayout.LayoutParams fllp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        fllp.gravity = Gravity.CENTER;
        this.mMainLayout.addView(mTipLayot, fllp);
        if (mIsDefaultLoading) {
            if (XUtilNet.isNetConnected()) {
                // 默认显示加载状态
                showLodingState();
            } else {
                showFailState(EMPTY_DATA_PROMPT_NETWORK_ANOMALIES);
            }
        }

    }

    /**
     * 数据加载成功视图状态
     */
    public void showLodingState() {
        // 切换到提示状态
        changeToTip();
        //开始显示加载视图
        showLoadingView();
    }

    private void showLoadingView() {
//        mLoadingView.startLoading();
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadFailLayout.setVisibility(View.GONE);
        tipImage.setVisibility(View.VISIBLE);
//        setGifDrawable(R.drawable.loading);
        tipImage.setBackgroundResource(R.drawable.null404);
        mTipTextView.setText("加载中...");
        if (this.mCallBack!=null){
            this.mCallBack.getData();
        }
    }

    public void showLoadingView(String title) {
        // 切换到提示状态
        changeToTip();
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadFailLayout.setVisibility(View.GONE);
        tipImage.setVisibility(View.VISIBLE);
        tipImage.setBackgroundResource(R.drawable.null404);
        mTipTextView.setText(title);
        if (this.mCallBack!=null){
            this.mCallBack.getData();
        }
    }

    /**
     * 数据加载成功视图状态
     */
    public void showSuccessState() {
        // 切换到数据状态
        changeToData();
    }

    /**
     * 数据加载失败视图状态
     */
    public void showFailState(String state) {
        // 切换到提示状态
        changeToTip();
        tipImage.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mLoadFailLayout.setVisibility(View.VISIBLE);
        if (state == null || state.isEmpty()) {
            loadFailed();
        } else {
            if (state.equals(EMPTY_DATA_SUGGEST_LOADING_FAILURE)) {
                //加载失败提示
                loadFailed();
            } else if (state.equals(EMPTY_DATA_PROMPT_NETWORK_ANOMALIES)) {
                //网络异常提示
//                tipImage.setImageResource(R.drawable.ic_tip_image_08);
//                setGifDrawable(R.drawable.network_anomalies);
                mGeneralFailurePrompt.setText("网络异常...");
            } else {
                //加载失败提示
                loadFailed();
            }
        }
    }

    /**
     * 加载失败提示
     */
    private void loadFailed() {
//        setGifDrawable(R.drawable.load_failed);
//        tipImage.setImageResource(R.drawable.ic_tip_image_07);
        mGeneralFailurePrompt.setText("加载失败...");
    }

    /**
     * 暂无数据视图状态
     */
    public void showNoDataState() {
        // 切换到提示状态
        changeToTip();
//        mLoadingView.setVisibility(View.GONE);
        mLoadFailLayout.setVisibility(View.GONE);

        mLoadingLayout.setVisibility(View.VISIBLE);
        tipImage.setVisibility(View.VISIBLE);
        tipImage.setBackgroundResource(R.drawable.null404);

        mTipTextView.setText("空空如也");
    }

//    private void setGifDrawable(int id) {
//        try {
//            gifDrawable = new GifDrawable(mContext.getResources(), id);
//            tipImage.setImageDrawable(gifDrawable);
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 暂无数据视图状态
     *
     * @param text 提示文本
     */
    public void showNoDataState(String text) {
        showNoDataState();
        mTipTextView.setText(text);
    }

    /**
     * 暂无数据视图状态
     */
    public void showNoDataStateWithImgAndText(int drawableRes, String text) {
        // 切换到暂无数据状态
        showNoDataState();
        mTipTextView.setText(text);
        tipImage.setBackgroundResource(drawableRes);
    }

    /**
     * 只显示无数据文本
     */
    public void showNoDataText(String text) {
        // 切换到暂无数据状态
        showNoDataState();
        mTipTextView.setText(text);
        tipImage.setVisibility(View.GONE);
    }

    /**
     * 只默认图片的页面
     */
    public void showDefaultImage() {
        // 切换到暂无数据状态
        showNoDataState();
        mTipTextView.setText("");
        tipImage.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏所有视图
     */
    public void hideAllLayout() {
        // 隐藏数据视图
        if (mDataLayot != null) {
            mDataLayot.setVisibility(View.INVISIBLE);
        }
        // 隐藏提示视图
        mTipLayot.setVisibility(View.GONE);
//        mLoadingView.stopLoading();
    }

    /**
     * 切换到数据状态
     */
    private void changeToData() {
        // 显示数据视图
        if (mDataLayot != null) {
            mDataLayot.setVisibility(View.VISIBLE);
        }
        // 隐藏提示视图
        mTipLayot.setVisibility(View.GONE);
    }

    /**
     * 切换到提示状态
     */
    private void changeToTip() {
        // 隐藏数据视图
        if (mDataLayot != null) {
            mDataLayot.setVisibility(View.GONE);
        }
        // 显示提示视图
        mTipLayot.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //加载失败(刷新页面)
            case R.id.load_fail_btn:
                if (XUtilNet.isNetConnected()) {
                    // 默认显示加载状态
                    showLodingState();
                } else {
                    ToastUtil.toastLong(mContext, "无网络,请打开网络。");
                    showFailState(EMPTY_DATA_PROMPT_NETWORK_ANOMALIES);
                }
//                //开始显示加载视图
//                showLoadingView();
                if (mCallBack != null) {
                    // 加载数据
                    mCallBack.getData();
                }
                break;
            //检查网络
            case R.id.check_network_btn:
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                    mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                } else {
                    mContext.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }
                break;
        }
    }

    public interface Callback {

        public void getData();

    }

    public interface EmptyViewClickCallback {

        public void setEmptyViewClick();

    }

}
