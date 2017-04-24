package com.anzhuo.video.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.utils.NoDoubleClickListener;

import java.io.InputStream;


/**
 * created on 2016/11/2 0002.
 * 状态View
 * 使用方法：
 * 1、 StateView stateView = new StateView(Context).setOnclick(StateViewOnclickCallback);
 * 2、如果需要传入父布局：stateView.setParentConfig( stateViewParent , dataView );
 * 3、stateView.showLoading();
 */

public class StateView extends RelativeLayout {
    private StateViewOnclickCallback stateViewOnclickCallback;
    //
    private Context context;
    //传进来的父布局和需要替换的布局
    private ViewGroup stateViewParent;//StateView的父布局
    private View dataView;//StateView需要替换的View

    //
    private LinearLayout stateContentView;//提示的父布局
    private ImageView imageView;//fresco加载动画有点慢，所以适应帧动画
    private TextView textStateView;//提示的文字
    //Data
    private AnimationDrawable gifAnimation;
    private final int SUCCESS = 0;//加载成功
    private final int LOADING = 1;//加载中
    private final int NODATA = 2;//没有数据
    private final int NONET = 3;//没有网络
    private final int NOLOGIN = 4;//没有登录
    private final int CUSTOM = 5;//自定义

    public StateView(Context context) {
        this(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initStateView();
    }

    /**
     * 初始化状态View
     */
    private void initStateView() {
        LayoutInflater.from(context).inflate(R.layout.view_state, this);
        imageView = (ImageView) findViewById(R.id.img_state);
        textStateView = (TextView) findViewById(R.id.tv_state);
        stateContentView = (LinearLayout) findViewById(R.id.state_content);
        stateContentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setGravity(Gravity.CENTER);
        this.setBackgroundColor(context.getResources().getColor(R.color.white));
    }


    public StateView setParentConfig(ViewGroup stateViewParent, View dataView) {
        this.stateViewParent = stateViewParent;
        this.dataView = dataView;
        //将 stateView添加到stateViewParent中
        FrameLayout.LayoutParams fllp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        fllp.gravity = Gravity.CENTER;
        this.stateViewParent.addView(this, fllp);
        dataView.setVisibility(View.GONE);
        return this;
    }


    public StateView setOnclick(StateViewOnclickCallback onclick) {
        this.stateViewOnclickCallback = onclick;
        return this;
    }

    /**
     * 分情况的点击事件
     *
     * @param isClickable
     * @param state
     */
    private void setImgAndTextOncick(boolean isClickable, final int state) {
        if (!isClickable) {
            imageView.setClickable(false);
            textStateView.setClickable(false);
        } else {
            imageView.setClickable(true);
            textStateView.setClickable(true);
            //可以点击的时候分情况
            final OnClickListener onClickListener = new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (stateViewOnclickCallback != null) {
                        switch (state) {
                            case SUCCESS://加载成功
                                break;
                            case LOADING://加载中
                                break;
                            case NODATA://没有数据
                                stateViewOnclickCallback.NoDataClick();
                                break;
                            case NONET://没有网络
                                stateViewOnclickCallback.NoNetClick();
                                break;
                            case NOLOGIN://没有登录
                                stateViewOnclickCallback.NoLoginClick();
                                break;
                            case CUSTOM://自定义
                                stateViewOnclickCallback.CustomClick();
                                break;
                            default:
                                break;
                        }
                    }
                }
            };
            imageView.setOnClickListener(onClickListener);
            textStateView.setOnClickListener(onClickListener);
        }
    }


    /**
     * 是否显示状态View
     *
     * @param isShow
     */

    private void isShowStateView(boolean isShow) {
        if (isShow) {
            this.setVisibility(View.VISIBLE);
            isShowDataView(false);
        } else {
            this.setVisibility(View.GONE);
            isShowDataView(true);
        }
    }

    private void isShowDataView(boolean isShow) {
        if (dataView != null) {
            if (isShow) {
                dataView.setVisibility(View.VISIBLE);
            } else {
                dataView.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 加载中
     */
    public StateView showLoading() {
        showLoading(true, "加载中…", 0);
        return this;
    }


    public StateView showLoading(boolean isShowGif, String LoadingString, int ResID) {
        isShowStateView(true);
        showState(LoadingString, ResID);
        if (isShowGif) {
            //加载动画
            imageView.setBackgroundResource(R.drawable.refresh_loding);//设置动态
            gifAnimation = (AnimationDrawable) imageView.getBackground();
            gifAnimation.start();
        }
        setImgAndTextOncick(false, LOADING);
        return this;
    }


    /**
     * 加载成功
     */
    public void showSuccess() {
        isShowStateView(false);
        showState("", 0);
        setImgAndTextOncick(false, SUCCESS);
    }

    /**
     * 没有数据
     */
    public StateView showNoData() {
        showNoData(context.getResources().getString(R.string.loading_no_data), R.drawable.weiguanzhu);
        return this;
    }


    public StateView showNoData(String stateString, int ResID) {
        isShowStateView(true);
        showState(stateString, ResID);
        setImgAndTextOncick(true, NODATA);
        return this;
    }


    /**
     * 没有网络
     */
    public StateView showNoNet() {
        showNoNet(context.getResources().getString(R.string.loading_no_net), R.drawable.duanwang);
        return this;
    }


    public StateView showNoNet(String noNetString, int resId) {
        isShowStateView(true);
        showState(noNetString, resId);
        setImgAndTextOncick(true, NONET);
        return this;
    }


    /**
     * 没登录
     */
    public StateView showNoLogin() {
        showNoLogin(context.getResources().getString(R.string.login_no_tips_myfragment), R.drawable.weidenglu);
        return this;
    }

    /**
     * 没登录
     */
    public StateView showNoLogin(String NoLoginString, int resID) {
        isShowStateView(true);
        showState(NoLoginString, resID);
        setImgAndTextOncick(true, NOLOGIN);
        return this;
    }


    /**
     * 设置自定义的状态
     */
    public StateView showCustomState(String stateString, int resId) {
        isShowStateView(true);
        showState(stateString, resId);
        setImgAndTextOncick(true, CUSTOM);
        return this;
    }


    /**
     * 1、先初始化各个控件的状态
     * 2、加载
     *
     * @param stateTextString
     * @param resId
     */
    private void showState(String stateTextString, int resId) {
        initView();
        //加载
        textStateView.setText(stateTextString);
        if (resId != 0) {
            LoadImage(resId);
        }
    }

    /**
     * 初始化各个控件的状态
     */
    private void initView() {
        if (gifAnimation != null) {
            gifAnimation.stop();
            gifAnimation = null;
        }
        imageView.clearAnimation();
        imageView.setBackgroundResource(0);
        imageView.setImageBitmap(null);
    }


    /**
     * 加载本地图片
     *
     * @param resId
     */
    private void LoadImage(int resId) {
        imageView.setImageBitmap(readBitMap(context, resId));
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


    /**
     * 设置StateView 的高度
     *
     * @param height
     */
    public void setStateViewHeight(int height) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.state_content);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        linearLayout.setLayoutParams(layoutParams);
    }

}
