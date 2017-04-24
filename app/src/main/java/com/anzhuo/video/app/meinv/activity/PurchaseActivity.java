package com.anzhuo.video.app.meinv.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.utils.XUtilNet;


public class PurchaseActivity extends BaseActivity {

    //缓存目录
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private ProgressBar progressBar;
    private WebView purchaseWebview;
    private Intent intent;

    @Override
    protected int getLayoutRes() {
        return R.layout.purchase_layout;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
//        setupStatus();
        intent = getIntent();
        String title = intent.getStringExtra("PICTITLE");
        String url = intent.getStringExtra("BUYURL");
        setHeadTitle(title);
        purchaseWebview = findView(R.id.purchase_webview);
        //实例化进度条控件
        progressBar = findView(R.id.progressbar);
        initWebView(purchaseWebview);
        if (XUtilNet.isNetConnected()){
            purchaseWebview.requestFocusFromTouch();
            purchaseWebview.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    //加载进度条的进度
                    progressBar.setProgress(newProgress);
                    //判断当前进度
                    if (newProgress < 100) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
            purchaseWebview.loadUrl(url);
            purchaseWebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                }
            });
            purchaseWebview.setDownloadListener(new MyWebViewDownLoadListener());
        }

    }

    /**
     * 初始化webView
     * @param webView
     */
    private void initWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        webSettings.setDatabasePath(cacheDirPath);
        webSettings.setAppCachePath(cacheDirPath);
        webSettings.setAppCacheEnabled(true);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.main_back://返回
                onBackPressed();
                break;
        }
    }

    /**
     * 两次点击back键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && purchaseWebview.canGoBack()) {
            purchaseWebview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

}
