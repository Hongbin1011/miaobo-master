package com.anzhuo.video.app.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
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

public class TVplayFragment extends BaseFragment implements MovieAdapter.OnMovieItemClickListener{

    public TVplayFragment() {
    }

    public static TVplayFragment newInstance() {
        return new TVplayFragment();
    }

    public static final String GAME_FROM_TAG = "FilmFragment";
    private MovieAdapter movieAdapter;
    protected boolean isOpen = false;
    private String URL = "http://dayidong.cn/index.php?m=vod-xwajaxpage-id-1-page-1";
    protected SearchTextView searchTextView;
    protected TextView mTvTitle;
    protected TextView mTvSwitcher;
    protected LinearLayout mLlDotContainer;
    protected TextView mTvPuttoLancher;

    private PullLoadMoreRecyclerView mRecyclerview;
    private List<MovieInfo> mDatas = new ArrayList<MovieInfo>();
    private int mPageNo = 1;
    private final int mPageSize = 15;


    protected void beforeInit() {
        searchTextView.setFromWhere(GAME_FROM_TAG);
        isOpen = false;
        getMovieDate();
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
//            case LOAD_MOVIE_TYPE:
//                getMovieDate();
//                break;
        }
    }

    private String getMovieId(){
//        List<MovieTypeInfo> typeInfos = DataSupport.findAll(MovieTypeInfo.class);
//        for (MovieTypeInfo mti : typeInfos){
//            if(TextUtils.equals(mti.getT_name(), getResources().getString(R.string.main_nav_text_game))){
//                return mti.getT_id();
//            }
//        }
//        return null;
        return "2";
    }


    private void getMovieDate(){
        URL = AppServerUrl.MOVIE_LIST + "&id="+getMovieId()+"&limit="+mPageSize+"&page="+mPageNo;
        Log.d("husong",""+URL);
        HttpUtils.getStringAsync(URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
                showErrorView();
            }

            @Override
            public void onResponse(String response,int ind) {
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
}
