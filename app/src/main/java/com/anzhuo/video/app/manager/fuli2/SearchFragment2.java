package com.anzhuo.video.app.manager.fuli2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.manager.fuli.DefaultSharePrefManager;
import com.anzhuo.video.app.manager.fuli2.x5blink.LongPressListenerWrapper;
import com.anzhuo.video.app.manager.fuli2.x5blink.X5WebView;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.ViewUtil;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by husong on 2017/2/21.
 */

public class SearchFragment2 extends BaseFragment implements AdapterView.OnItemClickListener {

    private EditText mEtSearchContent;
    private ImageView mIvDeleteContent;

    private RelativeLayout mRlServerResultContainer;
    private BaseGridView mGridView;

    private RelativeLayout mRlBaiduResultContainer;
    private X5WebView mWebView;

//    private CommonGridViewAdapter mAdapter;
    public static final String FROM_PAGE = "fromPage";
    private String tag;
    private String baiduSearchUrl = "";
    private ProgressBar mPageLoadingProgressBar;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mRefresh;
    private ImageButton mHome;
    private String mHomeUrl = "http://www.1122.com/";
    private TextView mBaiduResultTvTitle;
    private boolean isWebLoading;
    public static final String COMMEN_FROM_TAG = "CommenFragment";
    public static final String GAME_FROM_TAG = "GameFragment";

    public static SearchFragment2 newInstance() {
        return new SearchFragment2();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.search_main_layout2;
    }

    @Override
    public void bindViews() {
        tag = getArguments().getString(FROM_PAGE);

        mStateView = findView(R.id.stateView);
        findViewAttachOnclick(R.id.iv_SearchBack);
        findViewAttachOnclick(R.id.btnSearch);
        mPageLoadingProgressBar = findView(R.id.progressBar_search);
        mIvDeleteContent = findViewAttachOnclick(R.id.iv_empty_search_words);
        mEtSearchContent = findView(R.id.search_edit_baidu);
        mRlServerResultContainer = findView(R.id.serverResultContainer);
        mGridView = findView(R.id.resultGridView);
        mRlBaiduResultContainer = findView(R.id.baiduResultContainer);
//        findViewIcon(R.id.search_img_icon);
        mWebView = findView(R.id.resultWebview);
//        mBack = findViewAttachOnclick(R.id.btnBack);
//        mForward = findViewAttachOnclick(R.id.btnForward);
//        mRefresh = findViewAttachOnclick(R.id.btnRefresh);
        mBaiduResultTvTitle = findView(R.id.baiduResultTvTitle);
//        mHome = findViewAttachOnclick(R.id.btnHome);

        mGridView.setOnItemClickListener(this);
//        mAdapter = new CommonGridViewAdapter(mGridView, mContext, 0, false, false);
//        mGridView.setAdapter(mAdapter);
        configEditText();
        configWebView();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    private void configWebView() {
        baiduSearchUrl = getSearchUrl();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                mPageLoadingProgressBar.setProgress(newProgress);
                if (mPageLoadingProgressBar != null && newProgress != 100) {
                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                } else if (mPageLoadingProgressBar != null) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
                    mWebView.setWebViewClient(client);
                }
            }
        });

        mWebView.setOnLongClickListener(new LongPressListenerWrapper(mWebView, mContext));
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private WebViewClient client = new WebViewClient() {
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            if (Integer.parseInt(Build.VERSION.SDK) >= 16)
                changGoForwardButton(webView);
            isWebLoading = false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if(!isWebLoading) {
//                Bundle bundle = new Bundle();
//                bundle.putString(SearchDetailsFragment.URL_TAG, url);
//                PageSwitcher.switchToPage(mActivity, FragmentFactory.FRAGMENT_TYPE_SEARCH_DETAIL, bundle);
//            }else
                view.loadUrl(url);
            return true;
        }
    };

    private final int disable = 120;
    private final int enable = 255;

    private void changGoForwardButton(WebView view) {
        mBack.setAlpha(view.canGoBack()?enable:disable);
        mForward.setAlpha(view.canGoForward()?enable:disable);
    }


    private void configEditText() {
        mEtSearchContent.setFocusable(true);
        mEtSearchContent.setFocusableInTouchMode(true);
        mEtSearchContent.addTextChangedListener(textWatcher);
        mEtSearchContent.setOnEditorActionListener(new SearchActionListener());
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            showEditTextEmpty(s.length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private class SearchActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (mStateView.getViewState() == MultiStateView.VIEW_STATE_EMPTY) {
                    mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                }
                if (mRlBaiduResultContainer.getVisibility() == View.GONE) {
                    mRlBaiduResultContainer.setVisibility(View.VISIBLE);
                }
                String key = mEtSearchContent.getText().toString().replace(" ", "");
                queryResult(key);
                searchBaidu(key);
                showEditTextEmpty(key.length());
                hideKeyboard(getActivity());
                return true;
            }
            return false;
        }
    }

    public boolean hideKeyboard(Activity ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View curFocus = ctx.getCurrentFocus();
        return curFocus != null && imm.hideSoftInputFromWindow(curFocus.getWindowToken(), 0);
    }

    /**
     * 显示(隐藏)搜索输入框清除按钮
     *
     * @param length
     */
    private void showEditTextEmpty(int length) {
        if (length > 0) {
            ViewUtil.setViewVisible(mIvDeleteContent);
        } else {
            ViewUtil.setViewGone(mIvDeleteContent);
        }
    }


    private void queryResult(String text) {
        switch (tag) {
            case COMMEN_FROM_TAG:
                searchProgram(text);
                break;
            case GAME_FROM_TAG:
                searchGame(text);
                break;
        }
    }

    private void searchGame(String text) {
//        SearchRequest.createGameRequest(text, new OnResponseListener<List<DetailInfo>>() {
//            @Override
//            public void onSuccess(int code, String msg, List<DetailInfo> detailInfos, boolean cache) {
//                if (detailInfos != null && detailInfos.size() > 0) {
//                    mBaiduResultTvTitle.setVisibility(View.VISIBLE);
//                    mRlServerResultContainer.setVisibility(View.VISIBLE);
//                    mAdapter.setData(detailInfos);
//                    mAdapter.notifyDataSetChanged();
//                }else {
//                    mRlServerResultContainer.setVisibility(View.GONE);
//                    mBaiduResultTvTitle.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//
//            }
//        }).sendRequest();
    }

    private void searchProgram(String text) {
//        SearchRequest.createProgramRequest(text, new OnResponseListener<List<DetailInfo>>() {
//            @Override
//            public void onSuccess(int code, String msg, List<DetailInfo> detailInfos, boolean cache) {
//                if (detailInfos != null && detailInfos.size() > 0) {
//                    mBaiduResultTvTitle.setVisibility(View.VISIBLE);
//                    mRlServerResultContainer.setVisibility(View.VISIBLE);
//                    mAdapter.setData(detailInfos);
//                    mAdapter.notifyDataSetChanged();
//                }else{
//                    mRlServerResultContainer.setVisibility(View.GONE);
//                    mBaiduResultTvTitle.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//
//            }
//        }).sendRequest();
    }

    private void searchBaidu(String key) {
        try {
            String encode = URLEncoder.encode(key, "UTF-8");
            String url = baiduSearchUrl + encode;
            isWebLoading = true;
            mWebView.loadUrl(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_SearchBack:
                hideKeyboard(getActivity());
                mActivity.onBackPressed();
                break;
            case R.id.iv_empty_search_words://Empty the search words
                mEtSearchContent.setText("");
                break;
            case R.id.btnSearch:
                String searchKeyword = mEtSearchContent.getText().toString();
                String hintContent = mEtSearchContent.getHint().toString();
                if (!TextUtils.isEmpty(searchKeyword.replace(" ", ""))) {
                    if (mStateView.getViewState() == MultiStateView.VIEW_STATE_EMPTY) {
                        mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    }
                    if (mRlBaiduResultContainer.getVisibility() == View.GONE) {
                        mRlBaiduResultContainer.setVisibility(View.VISIBLE);
                    }
                    searchBaidu(searchKeyword);
                    queryResult(searchKeyword);
                    hideKeyboard(getActivity());
                } else if (!TextUtils.isEmpty(hintContent.replace(" ", ""))) {
//                    queryResult(hintContent);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

//    @Override
//    public boolean goBack() {
//        if (mWebView.canGoBack()) {
//            mWebView.goBack();
//            return true;
//        } else {
//            return super.goBack();
//        }
//    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        DetailInfo info = mAdapter.getItem(position);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", info.getAndroid_url());
//        PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, bundle);
    }

    private String getSearchUrl() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("http://m.baidu.com/s?from=").
                append(AppServerUrl.BAIDUFROM);
        if (!TextUtils.isEmpty(getParameter_ua())) {
            buffer.append("&ua=").append(getParameter_ua());
        }
        if (!TextUtils.isEmpty(getParameter_pu())) {
            buffer.append("&pu=").append(getParameter_pu());
        }
        buffer.append("&word=");
        return buffer.toString();
    }

    private String getParameter_ua() {
        String ua = "";
        String userId = DefaultSharePrefManager.getString("key_user_id", "0");
        if (!String.valueOf(0).equals(userId)) {
            ua = "cuid@" + md5sum(userId).substring(0, 16);
        }
        return ua;
    }

    private String getParameter_pu() {
        StringBuffer buffer = new StringBuffer();
        String pu = buffer.append("1122").append("_").
                append(DisplayUtil.getScreenWidth(mContext)).append("_").
                append(DisplayUtil.getScreenHeight(mContext)).append("_").
                append(Build.MODEL).append("_").
                append(getVersionName(mContext)).toString();
        return pu;
    }

    @Override
    protected void attachAllMessage() {
        super.attachAllMessage();
//        attachMessage(com.movie.beauty.message.Message.Type.CLOSE_SEARCH_FRAGMENT);
    }

    @Override
    public void onReceiveMessage(Message message) {
        super.onReceiveMessage(message);
        switch (message.type){
//            case CLOSE_SEARCH_FRAGMENT:
//                mActivity.finish();
//                break;
        }
    }

    public String md5sum(String str) {
        // MD5算法
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    // 获取版本名称
    public String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
