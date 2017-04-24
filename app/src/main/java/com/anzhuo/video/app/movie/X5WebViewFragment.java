package com.anzhuo.video.app.movie;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.movie.x5blink.LongPressListenerWrapper;
import com.anzhuo.video.app.movie.x5blink.X5WebView;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by husong on 2017/2/21.
 */

public class X5WebViewFragment extends BaseFragment {

    public static X5WebViewFragment newInstance() {
        return new X5WebViewFragment();
    }
    X5WebView webView;
    ProgressBar mPageLoadingProgressBar;

    private RelativeLayout rlBestClose;

    public static final int MSG_REQUEST_H5_LOCATION=1;
    public static final int MSG_REQUEST_H5_LED = 2;
    public static final int MSG_REQUEST_H5_LOGIN = 13;//登录成功失败回调
    public static final int MSG_REQUEST_H5_SHARE = 14;//分享成功失败回调
    public static final int MSG_REQUEST_H5_GET_FILE = 6;
    public static final int MSG_REQUEST_H5_DOWNLOAD_FILE = 7;

    public static final int REQUEST_CROP_PICTURE = 99;

//    private X5WebViewJSFunction jsFunction;
    private String mUrl;
    private String mColor;
    private TextView mTitleText;
    private ImageView mImgBack;
    private ImageView mImgExit;
    private RelativeLayout mMainLayout;

    private ValueCallback<Uri[]> mUMA;
    private int MAX_ATTACHMENT_COUNT = 8;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_x5test;
    }

    @Override
    public void bindViews() {
        Bundle bundle = mActivity.getIntent().getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        mUrl = bundle.getString("url");
        mColor = bundle.getString("color");
        String isShowBestClose = bundle.getString("isShowBestClose");

        mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        webView = findView(R.id.forum_context);
        mPageLoadingProgressBar = findView(R.id.progressBar1);
        rlBestClose = findView(R.id.img_center) ;
        mTitleText = findView(R.id.head_title);
        mImgBack = findView(R.id.main_back);
        mImgExit = findView(R.id.main_close);
        mMainLayout = findView(R.id.main_head_layout);

        mImgBack.setOnClickListener(this);
        mImgExit.setOnClickListener(this);
        rlBestClose.setOnClickListener(this);
        if(!TextUtils.isEmpty(mColor)){
            mMainLayout.setBackgroundColor(Color.parseColor(mColor));
        }

        if(TextUtils.equals(isShowBestClose, "true")){
            rlBestClose.setVisibility(View.VISIBLE);
            mMainLayout.setVisibility(View.GONE);
        }

        initProgressBar();
        Log.d("husong", "webview get url ="+mUrl);
        webView.loadUrl(mUrl);
//        jsFunction=new X5WebViewJSFunction(mContext, mHandler, mActivity);
//        webView.addJavascriptInterface(jsFunction, "Android");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mTitleText.setText(title);
                mImgExit.setVisibility(view.canGoBack() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mPageLoadingProgressBar.setProgress(newProgress);
                if (mPageLoadingProgressBar != null && newProgress != 100) {
                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                } else if (mPageLoadingProgressBar != null) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
                super.openFileChooser(valueCallback, s, s1);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                if(mUMA != null){
//                    mUMA.onReceiveValue(null);
//                }
                mUMA = filePathCallback;
                return true;
            }
        });

        webView.setOnLongClickListener(new LongPressListenerWrapper(webView, mContext));
    }

    @Override
    public void onDestroy() {
//        jsFunction.destroy();
        webView.destroy();
        super.onDestroy();
    }

    private void initProgressBar() {
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REQUEST_H5_LED:
                    String request_h5 = msg.obj.toString();
                    webView.loadUrl("javascript:cxmx.requestLight(" + request_h5 + ")");
                    break;

                case MSG_REQUEST_H5_LOCATION:
                    String result = msg.obj.toString();
                    webView.loadUrl("javascript:cxmx.requestLocation(" + result + ")");
                    break;

                case 3://测试
//                    Bitmap bm= (Bitmap) msg.obj;
//                    imgTest.setVisibility(View.VISIBLE);
//                    imgTest.setImageBitmap(bm);
                    break;

                case MSG_REQUEST_H5_GET_FILE:
                    String result2 = msg.obj.toString();
                    webView.loadUrl("javascript:requestGetFile('" + result2 + "')"); //h5暂没提供
                    break;

                case MSG_REQUEST_H5_DOWNLOAD_FILE:
                    String r3 = msg.obj.toString();
                    webView.loadUrl("javascript:cxmx.requestDownload(" + r3 + ")");
                    break;
                case MSG_REQUEST_H5_LOGIN://调起js 登录成功失败回调方法
                    String resultLogin = msg.obj.toString();
                    //arg = 0 = 成功 ； arg = 1 = 失败 ；arg = 2 =取消
                    int argLogin = msg.arg1;
                    webView.loadUrl("javascript:cxmx.requestLocation(" + resultLogin + ")");
                    break;
                case MSG_REQUEST_H5_SHARE://调起js 分享成功失败回调方法
                    String resultShare = msg.obj.toString();
                    //arg 0=微信成功 1=朋友圈成功 2=微博成功 3=QQ成功 4=分享失败 5=取消分享
                    int argShare = msg.arg1;
                    webView.loadUrl("javascript:cxmx.requestLocation(" + resultShare + ")");
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean goBack() {
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
            return super.goBack();
        }
    }

    @Override
    protected void attachAllMessage() {
        super.attachAllMessage();
//        attachMessage(com.movie.beauty.message.Message.Type.REQUEST_H5_SPEECHRECOGNIZER);
//        attachMessage(com.movie.beauty.message.Message.Type.REQUEST_H5_SETWALLPAPER);
//        attachMessage(com.movie.beauty.message.Message.Type.REQUEST_H5_UPLOAD);
//        attachMessage(com.movie.beauty.message.Message.Type.REQUEST_H5_REDUCTIONCONTACT);
//        attachMessage(com.movie.beauty.message.Message.Type.REQUEST_H5_SET_TITLE_COLOR);
    }

    public void doBackDown(){
    }

    public void doVolumeDown(){
    }

    public void doVolumeUp(){
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_back:
                if(webView.canGoBack()){
                    webView.goBack();
                }else {
                    mActivity.finish();
                }
                break;

            case R.id.main_close:
                mActivity.finish();
                break;

            case R.id.img_center:
                mActivity.finish();
                break;
        }
    }
}
