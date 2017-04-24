package com.anzhuo.video.app.movie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.utils.HttpUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by husong on 2017/2/21.
 */

public class RecommendFragment extends BaseFragment implements MovieAdapter.OnMovieItemClickListener2, RecommendAdapter.onMovieBannerClickListener {

    public RecommendFragment() {
    }

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    public static final String GAME_FROM_TAG = "RecommendFragment";
    protected boolean isOpen = false;
    private String URL = "http://dayidong.cn/index.php?m=vod-xwajaxpage-id-1-page-1";
    protected SearchTextView searchTextView;
    protected TextView mTvTitle;
    protected TextView mTvSwitcher;
    protected LinearLayout mLlDotContainer;
    protected TextView mTvPuttoLancher;

    private PullLoadMoreRecyclerView mRecyclerview;
    private List<MovieInfo> mDatas;
    private List<RecommendFilmInfo> mRecommendDatas;

    private RecommendAdapter mRecommendAdapter;


    protected void beforeInit() {
        searchTextView.setFromWhere(GAME_FROM_TAG);
        isOpen = false;
//        getMovieDate();
        getAdInfo();
        getRecommendMovieDate();
    }


    @Override
    protected void attachAllMessage() {
        super.attachAllMessage();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.main_common_page;
    }

    @Override
    public void bindViews() {
        searchTextView = findView(R.id.stSearchTextView);
        searchTextView.mActivity = mActivity;
        mStateView = findView(R.id.stateView);
        mTvTitle = findView(R.id.tvTitle);
        mTvSwitcher = findViewAttachOnclick(R.id.tvSwitcher);
        mTvPuttoLancher = findViewAttachOnclick(R.id.tvput_to_luncher);
//        mLlDotContainer = findView(R.id.llDotContainer);
        mRecyclerview = findView(R.id.movieRecyclerView);
        mRecyclerview.setPullRefreshEnable(false);
        mRecyclerview.setPushRefreshEnable(false);

        mRecyclerview.setLinearLayout();

        mRecommendAdapter = new RecommendAdapter(mContext);
        mRecommendAdapter.setOnMovieItemClickListener(this);
        mRecyclerview.setAdapter(mRecommendAdapter);

//        mRecyclerview.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.white)));

        beforeInit();
    }

    @Override
    public void onReceiveMessage(Message message) {
        switch (message.type) {
//            case LOAD_MOVIE_TYPE:
//                getMovieDate();
//                break;
        }
    }

    private void getRecommendMovieDate(){
        URL = AppServerUrl.MOVIE_RECOMMEND;
        HttpUtils.getStringAsync(URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
                showErrorView();
            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")) {
                    String dataJson = jsonObject.getString("data");
                    mRecommendDatas = JSON.parseArray(dataJson, RecommendFilmInfo.class);
                    showContentView();
                    mRecommendAdapter.setDatas(mRecommendDatas);
                }
            }
        });
    }

    @Override
    protected void reload() {
        getRecommendMovieDate();
    }

    @Override
    public void onMovieItemClick(View v, String id) {
        Intent intent = new Intent();
        intent.setClass(mContext , FilmPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("d_id", id);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    private void getAdInfo(){
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_AD_URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                List<ADVInfo> advInfos = new ArrayList<ADVInfo>();

                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")) {
                    String dataJson = jsonObject.getString("data");
                    advInfos = JSON.parseArray(dataJson, ADVInfo.class);
                    mRecommendAdapter.setBannerData(advInfos);
                    mRecommendAdapter.setOnMovieBannerClickListener(RecommendFragment.this);
                }

            }
        });
    }

    @Override
    public void onBannerClick(ADVInfo advInfo) {
        if(TextUtils.equals(advInfo.getType(), "1")){
            Intent intent = new Intent();
            intent.setClass(mContext , FilmPlayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("d_id", advInfo.getVod_id());
            intent.putExtra("bundle", bundle);
            startActivity(intent);

        }else if(TextUtils.equals(advInfo.getType(), "2")){
            Bundle bundle = new Bundle();
            bundle.putString("url", advInfo.getAd_url());
            PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, bundle);
        }
    }
}
