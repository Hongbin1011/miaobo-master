package com.anzhuo.video.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.ui.base.BaseActivity;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.utils.TbsLog;

import java.net.URL;


/**
 * 咨询详情页
 */
public class ZxDetailActivityX5 extends BaseActivity {

    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private WebViewX5 mWebView;

    private static String mHomeUrl = "";
    private static final String TAG = "SdkDemo";
    private boolean mNeedTestPage = false;

    private ProgressBar mPageLoadingProgressBar = null;
    private ValueCallback<Uri> uploadFile;
    private URL mIntentUrl;
    private LinearLayout mViewParent;
    private String mZxDetailTitles;
    private ImageView mImage_back;
    private TextView mMZxDetitleTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zx_detailpage_layout_x5);
        try {
            Intent intent = getIntent();
            if (intent != null) {
                //连接地址
                mHomeUrl = intent.getStringExtra("url");
                //title
                mZxDetailTitles = intent.getStringExtra("titles");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 0);
    }

    private void init() {
        mImage_back = (ImageView) findViewById(R.id.image_back);//返回
        mImage_back.setOnClickListener(this);
        mViewParent = (LinearLayout) findViewById(R.id.webView1);

        mMZxDetitleTitle = (TextView) findViewById(R.id.zx_detitle_title);
        if (!TextUtils.isEmpty(mZxDetailTitles)) {//设置标题
            mMZxDetitleTitle.setText(mZxDetailTitles);
        }
//        mWebView = new WebView(this);
        mWebView = new WebViewX5(this);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        initProgressBar();
        mWebView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
                return false;
            }
        });

//        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.getX5WebViewExtension().setScrollBarFadingEnabled(false);//去滚动条

        mWebView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title)) {
                    mMZxDetitleTitle.setText(title);
                }
//                mImgExit.setVisibility(view.canGoBack() ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                mPageLoadingProgressBar.setProgress(newProgress);
                if (mPageLoadingProgressBar != null && newProgress != 100) {
                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                } else if (mPageLoadingProgressBar != null) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
                }
            }
        });

        com.tencent.smtt.sdk.WebSettings webSetting = mWebView.getSettings();
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
        webSetting.setMixedContentMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//不设置放不出视频
//        }


        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);

        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
        TbsLog.d("time-cost", "cost time: " + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

        mWebView.setDownloadListener(new com.tencent.smtt.sdk.DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    /**
     * 进度条
     */
    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar2);// new
        mPageLoadingProgressBar.setMax(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        return ret;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
            } else
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onBackPressed() {
//        if (mWebView != null && mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
//            finish();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode + ",resultCode:" + resultCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    break;
                case 1:
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {
//        if (mWebView != null)
//            mWebView.destroy();

        try {
            // bug
            // java.lang.IllegalArgumentException: Receiver not registered: android.widget.ZoomButtonsController$1@19752548
            if (mWebView != null) {
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.setVisibility(View.GONE);
                long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
                mWebView.destroy();
//                    }
//                }, timeout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }
                    String testUrl = "file:///sdcard/outputHtml/html/" + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }
                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 重写webview  实现滑动onScroll监听
     */
    class WebViewX5 extends com.tencent.smtt.sdk.WebView {
        private OnScrollChangedCallback mOnScrollChangedCallback;

        public WebViewX5(final Context context) {
            super(context);
        }

        public WebViewX5(final Context context, final AttributeSet attrs) {
            super(context, attrs);
        }

        public WebViewX5(final Context context, final AttributeSet attrs, final int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);

            if (mOnScrollChangedCallback != null) {
                mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
            }
        }

        public OnScrollChangedCallback getOnScrollChangedCallback() {
            return mOnScrollChangedCallback;
        }

        public void setOnScrollChangedCallback(
                final OnScrollChangedCallback onScrollChangedCallback) {
            mOnScrollChangedCallback = onScrollChangedCallback;
        }
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    interface OnScrollChangedCallback {
        public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
        }
    }

    @Override
    public void SetTag() {

    }
}
