package com.anzhuo.video.app.movie;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.movie.x5blink.X5WebView;
import com.anzhuo.video.app.utils.HttpUtils;
import com.anzhuo.video.app.utils.RecycleViewDivider;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by husong on 2017/2/21.
 */

public class FilmPlayActivity extends AppCompatActivity implements MovieAdapter.OnMovieItemClickListener, AdapterView.OnItemClickListener {

    private X5WebView webView;
    private String mDid;
    private Dialog progressDialog;
    private MovieAdapter movieAdapter;
    private RecyclerView mRecyclerview;
    private TextView mTitleText;

    private final int STATUS_EXITIMG_OPEN = 1;
    private final int STATUS_EXITIMG_CLOSE = 2;
    private int status_exitimg = STATUS_EXITIMG_CLOSE;
    private String URL = "http://dayidong.cn/index.php?m=vod-xwajaxpage-id-1-page-1";
    private MovieDeatilInfo mCurrentMovieDeatilInfo;
    private List<MovieInfo> mDatas;
    private com.anzhuo.video.app.manager.fuli2.BaseGridView mGridview;
    private LinearLayout llChose;
    private TextView tvChose;
    private int mCurrentIndex;

    private LinearLayout llRemark;
    private TextView tvRemark;

    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    private myWebChromeClient xwebchromeclient;
    private View adView;
    private int second;
    private boolean isAding = false;
    private boolean isShowAd = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game_detail);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        progressDialog = ProgressDialog.createProgressDialog(this, getResources().getString(R.string.loading), null);
        mDid = bundle.getString("d_id");
        webView = (X5WebView) findViewById(R.id.webview_game);
        mGridview = (com.anzhuo.video.app.manager.fuli2.BaseGridView) findViewById(R.id.chose_gridview);
        mGridview.setNumColumns(5);
        mGridview.setOnItemClickListener(this);
        llChose = (LinearLayout) findViewById(R.id.ll_chose);
        tvChose = (TextView) findViewById(R.id.select_text);
        llRemark = (LinearLayout) findViewById(R.id.ll_remark);
        tvRemark = (TextView) findViewById(R.id.movie_remark);


        mRecyclerview = (RecyclerView) findViewById(R.id.movieRecyclerView);
        mTitleText = (TextView) findViewById(R.id.film_name);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(this);
        movieAdapter.setOnMovieItemClickListener(this);
        mRecyclerview.setAdapter(movieAdapter);
        mRecyclerview.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.white)));
        mRecyclerview.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.white)));
        mRecyclerview.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {

            }
        });

        adView = findViewById(R.id.adview);
        xwebchromeclient = new myWebChromeClient();
//        webView.setWebChromeClient(xwebchromeclient);
        webView.setWebViewClient(new myWebViewClient());
        webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                super.onShowCustomView(view, customViewCallback);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                webView.setVisibility(View.INVISIBLE);
                // 如果一个视图已经存在，那么立刻终止并新建一个
                TbsVideo.openVideo(FilmPlayActivity.this, mCurrentMovieDeatilInfo.getD_playurl().get(mCurrentIndex).getSrc());
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress != 100) {
                    progressDialog.show();
                } else {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if(isAding) {
                        isAding = false;
                        Message m1 = new Message();
                        m1.what = 1;
                        m1.obj = second;
                        mHandler.sendMessageDelayed(m1, 1000);
                    }
                }
            }
        });
//        X5WebViewJSFunction jsFunction = new X5WebViewJSFunction(this, new Handler(), this);
//        webView.addJavascriptInterface(jsFunction, "Android");
        getFilmAD();
        getFilmDetail();
    }

    private void getFilmAD() {
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_VARIABLES, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                try{
                    JSONObject jsonObject = JSON.parseObject(response);
                    String ret = jsonObject.getString("ret");
                    if (TextUtils.equals(ret, "200")) {
                        String dataJson = jsonObject.getString("data");
                        final ADVInfo datas = JSON.parseObject(dataJson, ADVInfo.class);
                        webView.loadUrl(datas.getAd_img());
                        second = Integer.parseInt(datas.getSeconds());
                        isShowAd = datas.getShow() == 1;
                        if(!isShowAd) return;
                        isAding = true;
                        adView.setVisibility(View.VISIBLE);
                        adView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", datas.getAd_url());
                                PageSwitcher.switchToPage(FilmPlayActivity.this, FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, bundle);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFilmDetail() {
        String url = AppServerUrl.MOVIE_DETAIL + "&id=" + mDid;
        HttpUtils.getStringAsync(url, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                try{
                    JSONObject jsonObject = JSON.parseObject(response);
                    String ret = jsonObject.getString("ret");
                    if (TextUtils.equals(ret, "200")) {
                        String dataJson = jsonObject.getString("data");
                        List<MovieDeatilInfo> datas = JSON.parseArray(dataJson, MovieDeatilInfo.class);
                        if (datas != null && datas.size() > 0) {
                            mCurrentMovieDeatilInfo = datas.get(0);
                            mTitleText.setText(mCurrentMovieDeatilInfo.getD_name());
                            getMovieDate(mCurrentMovieDeatilInfo.getD_type());
                            if(!isShowAd) {
                                if(mCurrentMovieDeatilInfo.getD_playurl().size() > mCurrentIndex)
                                    play(mCurrentMovieDeatilInfo.getD_playurl().get(mCurrentIndex).getSrc());
                            }
                            if (mCurrentMovieDeatilInfo.getD_playurl().size() > 1) {
                                GridViewAdapter adpter = new GridViewAdapter(FilmPlayActivity.this, mCurrentMovieDeatilInfo.getD_playurl());
                                adpter.setPlayIndex(mCurrentIndex);
                                mGridview.setAdapter(adpter);
                                mGridview.setVisibility(View.VISIBLE);
                                llChose.setVisibility(View.VISIBLE);
                                if(mCurrentMovieDeatilInfo.getD_playurl().size() > mCurrentIndex)
                                    tvChose.setText(getResources().getString(R.string.str_choose, mCurrentMovieDeatilInfo.getD_playurl().get(mCurrentIndex).getTitle()));
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int ss = (int) msg.obj;
                if (ss == 0) {
//                    getFilmDetail();
                    play(mCurrentMovieDeatilInfo.getD_playurl().get(mCurrentIndex).getSrc());
                    llRemark.setVisibility(View.GONE);
                    adView.setVisibility(View.GONE);
                    return;
                }
                llRemark.setVisibility(View.VISIBLE);
                tvRemark.setText("广告还剩"+ss + "s");
                Message m1 = new Message();
                m1.what = 1;
                m1.obj = ss - 1;
                mHandler.sendMessageDelayed(m1, 1000);
            }
        }
    };

    private void play(String url) {
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Referer", AppServerUrl.MOVIE_REFERER);
        webView.loadUrl(url, extraHeaders);
    }

    private void clearHandler(){
        if(mHandler.hasMessages(1))
            mHandler.removeMessages(1);
    }

    @Override
    public void onMovieItemClick(View v, int position) {
        mGridview.setVisibility(View.GONE);
        llChose.setVisibility(View.GONE);
        clearHandler();
        mCurrentIndex = 0;
        if (mDatas != null && mDatas.size() > 0) {
            MovieInfo mi = mDatas.get(position);
            if (mi != null) {
                mDid = mi.getD_id();
                getFilmAD();
                getFilmDetail();
            }
        }
    }

    private void getMovieDate(String type) {
        URL = AppServerUrl.MOVIE_LIST+"&recommend=1&id="+type;
        HttpUtils.getStringAsync(URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if (TextUtils.equals(ret, "200")) {
                    String dataJson = jsonObject.getString("data");
                    mDatas = JSON.parseArray(dataJson, MovieInfo.class);
                    movieAdapter.setDatas(mDatas);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mCurrentIndex) return;
        clearHandler();
        mCurrentIndex = position;
        getFilmAD();
        getFilmDetail();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    public class myWebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {
        private View xprogressvideo;

        // 播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webView.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            xCustomView = view;
            xCustomViewCallback = callback;
//            TbsVideo.openVideo(FilmPlayActivity.this, mCurrentMovieDeatilInfo.getD_playurl().get(mCurrentIndex).getSrc());
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            xCustomView = null;
            xCustomViewCallback.onCustomViewHidden();
            webView.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {

            return xprogressvideo;
        }
    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        webView.onResume();
        webView.resumeTimers();

        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        video_fullView.removeAllViews();
        if(mHandler.hasMessages(1))
            mHandler.removeMessages(1);
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
    }
}
