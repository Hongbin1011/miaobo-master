package com.anzhuo.video.app.manager.fuli2;

import android.app.Dialog;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.fuli2.utils.SuspendLayout;
import com.anzhuo.video.app.manager.fuli2.x5blink.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;


/**
 * Created by husong on 2017/2/21.
 */

public class GameDetailFragment2 extends BaseFragment {
    public static GameDetailFragment2 newInstance() {
        return new GameDetailFragment2();
    }

    private X5WebView webView;
    private String mUrl;
    private ImageView ImgFloat;
    private Dialog progressDialog;
    private SuspendLayout mRootLayout;

    private final int STATUS_EXITIMG_OPEN = 1;
    private final int STATUS_EXITIMG_CLOSE = 2;
    private int status_exitimg = STATUS_EXITIMG_CLOSE;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_game_detail2;
    }

    @Override
    public void bindViews() {
        mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = mActivity.getIntent().getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        progressDialog = ProgressDialog.createProgressDialog(mContext, mContext.getResources().getString(R.string.loading), null);
        mUrl = bundle.getString("url");
        String isShowFloat = bundle.getString("isShowFoloat");
        webView = findView(R.id.webview_game);
        ImgFloat = findView(R.id.float_ball);
        ImgFloat.setVisibility(TextUtils.equals(isShowFloat, "false") ? View.GONE : View.VISIBLE);
        mRootLayout = findView(R.id.suspend_root);
        ImgFloat.setOnClickListener(this);

        webView.loadUrl(mUrl);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress != 100 && progressDialog != null) {
                    progressDialog.show();
                } else {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }
            }
        });
//        X5WebViewJSFunction jsFunction=new X5WebViewJSFunction(mContext, new Handler(), mActivity);
//        webView.addJavascriptInterface(jsFunction, "Android");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.float_ball) {
            if (status_exitimg == STATUS_EXITIMG_CLOSE) {
                ImgFloat.setImageResource(R.drawable.tuichu);
                status_exitimg = STATUS_EXITIMG_OPEN;
                ViewTreeObserver vto = ImgFloat.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRootLayout.reDraw();
                    }
                });


            } else if (status_exitimg == STATUS_EXITIMG_OPEN) {
//                ImgFloat.setImageResource(R.drawable.tuichu_before);
                status_exitimg = STATUS_EXITIMG_CLOSE;
                mActivity.finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }
}
