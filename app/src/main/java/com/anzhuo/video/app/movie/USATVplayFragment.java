package com.anzhuo.video.app.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.anzhuo.video.app.utils.RecycleViewDivider;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by husong on 2017/2/21.
 */

public class USATVplayFragment extends BaseFragment implements MovieAdapter.OnMovieItemClickListener{

    public static final String GAME_FROM_TAG = "USATVplayFragment";
    private MovieAdapter movieAdapter;
    protected boolean isOpen = false;
    private String URL = "http://dayidong.cn/index.php?m=vod-xwajaxpage-id-19-page-1";
    protected SearchTextView searchTextView;
    protected TextView mTvTitle;
    protected TextView mTvSwitcher;
    protected LinearLayout mLlDotContainer;
    protected TextView mTvPuttoLancher;

    private PullLoadMoreRecyclerView mRecyclerview;
    private List<MovieInfo> mDatas = new ArrayList<MovieInfo>();
    private int mPageNo = 1;
    private final int mPageSize = 15;
    private List<MovieTypeInfo> types = new ArrayList<>();

    public static USATVplayFragment newInstance() {
        return new USATVplayFragment();
    }


    protected void beforeInit() {
        searchTextView.setFromWhere(GAME_FROM_TAG);
        isOpen = false;
//        getMovieType();
//        getShareUrl();
//        getMovieReferer();

        getMovieDate();

    }


    @Override
    protected void attachAllMessage() {
        super.attachAllMessage();
//        attachMessage(Message.Type.GAME_PROGRAM_DATA_LOAD_SUCCESS);
//        attachMessage(Message.Type.ADD_GAME);
//        attachMessage(Message.Type.DELETE_GAME);
//        attachMessage(USER_EXIT_SATEA);
//        attachMessage(Message.Type.USER_LOGIN_END);
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

        mRecyclerview.setGridLayout(3);
        mRecyclerview.setPullRefreshEnable(false);
        mRecyclerview.setPushRefreshEnable(true);
        mRecyclerview.setFooterViewText("正在加载...");

        movieAdapter = new MovieAdapter(mContext);
        movieAdapter.setOnMovieItemClickListener(this);
        mRecyclerview.setAdapter(movieAdapter);
        movieAdapter.setDatas(mDatas);
        mRecyclerview.getRecyclerView().addItemDecoration(new RecycleViewDivider(LinearLayoutManager.HORIZONTAL, DisplayUtil.dip2px(mContext,5f), getResources().getColor(R.color.white)));
        mRecyclerview.getRecyclerView().addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.white)));


        mRecyclerview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                getMovieDate();
            }
        });


        beforeInit();
    }

    @Override
    public void onReceiveMessage(Message message) {
        switch (message.type) {
//            case GAME_PROGRAM_DATA_LOAD_SUCCESS:
//                showContentView();
//                mAdapter.setData(appInfos);
//                addViewPagerDots(mLlDotContainer, mGridViewPager, mGridViewPager.getPageCount());
//                break;
        }
    }

    private String getMovieId(){
//        for (MovieTypeInfo mti : types){
//            if(TextUtils.equals(mti.getT_name(), getResources().getString(R.string.main_nav_text_find))){
//                return mti.getT_id();
//            }
//        }
//        return null;
        return "19";
    }


    private void getMovieDate(){
        URL = AppServerUrl.MOVIE_LIST + "&id="+getMovieId()+"&limit="+mPageSize+"&page="+mPageNo;
        HttpUtils.getStringAsync(URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
                showErrorView();
            }

            @Override
            public void onResponse(String response,int id) {
                mRecyclerview.setPullLoadMoreCompleted();
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")) {
                    String dataJson = jsonObject.getString("data");
                    List<MovieInfo> ds = JSON.parseArray(dataJson, MovieInfo.class);
                    if(ds.size()>0) {
                        mDatas.addAll(ds);
                        showContentView();
                        movieAdapter.notifyDataSetChanged();
                        mPageNo++;
                    }else {
                        mRecyclerview.setPushRefreshEnable(false);
                    }
                }
            }
        });
    }

    @Override
    protected void reload() {
        getMovieDate();
    }

    @Override
    public void onMovieItemClick(View v, int position) {
        if(mDatas!=null && mDatas.size()>0){
            MovieInfo mi = mDatas.get(position);
            if(mi!=null){
                Intent intent = new Intent();
                intent.setClass(mContext , FilmPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("d_id", mi.getD_id());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        }
    }

    private void getMovieType(){
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_TYPE, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
                showErrorView();
            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")){
                    String datajson = jsonObject.getString("data");
                    types = JSON.parseArray(datajson, MovieTypeInfo.class);
                    getMovieDate();
                }
            }
        });
    }

    private void getShareUrl(){
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_SHARE_URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")){
                    String datajson = jsonObject.getString("data");

                    if(!TextUtils.isEmpty(datajson)) {
                        JSONObject jo = JSON.parseObject(datajson);
                        AppServerUrl.SHARE_URL = jo.getString("share_url");
                        AppServerUrl.BAIDUFROM = jo.getString("baidu_from");
                    }
                }
            }
        });
    }

    private void getMovieReferer(){
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_REFERER_URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")){
                    String datajson = jsonObject.getString("data");
                    JSONObject j2 = JSON.parseObject(datajson);
                    AppServerUrl.MOVIE_REFERER = j2.getString("url");
                }
            }
        });
    }
}
