package com.anzhuo.video.app.manager.fuli2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.dongtu.ui.activity.DongtuMainActivity;
import com.anzhuo.video.app.manager.fuli2.adapter.FoundBaseAdapter;
import com.anzhuo.video.app.manager.fuli2.adapter.FoundItemVerticalAdapter;
import com.anzhuo.video.app.manager.fuli2.base.BaseFragmet;
import com.anzhuo.video.app.meinv.MeinvMainActivity;
import com.anzhuo.video.app.movie.MovieMainActivity;
import com.anzhuo.video.app.search.entity.BaseFoundInfo;
import com.anzhuo.video.app.search.entity.FoundInfo;
import com.anzhuo.video.app.search.ui.fragment.SearchFragment;
import com.anzhuo.video.app.utils.HttpUtils;
import com.anzhuo.video.app.utils.RecycleViewDivider;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.widget.StateView;
import com.anzhuo.video.app.widget.StateViewOnclickCallback;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class FuliFragment extends BaseFragmet implements FoundItemVerticalAdapter.OnFoundItemClickListener, View.OnClickListener {

    //    private TextView mTitleText;
//    private ImageView mImgBack;
    private RecyclerView mBaseListView;
    private FoundBaseAdapter mAdapter;
    private ArrayList<BaseFoundInfo> mDatas = new ArrayList<>();
    private LinearLayout mFound_gen;
    private StateView mStateView;

    public static FuliFragment getInstance() {
        return new FuliFragment();
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.fragment_found;
    }

    @Override
    public void bindView() {
        mBaseListView = $(R.id.found_listview);
        LinearLayout llSearch = $(R.id.ll_search);
        EditText etSearch = $(R.id.search_edit_baidu);
        mFound_gen = $(R.id.found_gen);

//        mTitleText.setText(R.string.main_nav_text_found);
//        mImgBack.setVisibility(View.GONE);

        etSearch.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBaseListView.setLayoutManager(layoutManager);
        mBaseListView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL, 30, getResources().getColor(R.color.download_manage_group_divide_color)));
        mAdapter = new FoundBaseAdapter(getContext(), mDatas, this);
        mBaseListView.setAdapter(mAdapter);

        //加载状态
        mStateView = new StateView(mActivity)
                .setParentConfig(mFound_gen, mBaseListView)
                .setOnclick(stateViewOnclickCallback)
                .showLoading();

        getFoundData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_edit_baidu:
            case R.id.ll_search:
                Bundle bundle = new Bundle();
                bundle.putString(SearchFragment.FROM_PAGE, SearchFragment2.COMMEN_FROM_TAG);
                PageSwitcher.switchToPage(getContext(), FragmentFactory.FRAGMENT_TYPE_SEARCH2, bundle);
                break;
        }
    }

    private void getFoundData() {
        if (XUtilNet.isWifiConnected()) {
            mDatas.clear();
            String URL = AppServerUrl.MOVIE_FOUND_URL;
            HttpUtils.getStringAsync(URL, "", new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
//                showErrorView();
                    mStateView.showNoNet();//加载失败
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        mStateView.showSuccess();
                        JSONObject jsonObject = JSON.parseObject(response);
                        String ret = jsonObject.getString("ret");
                        if (TextUtils.equals(ret, "200")) {

                            String dataJson = jsonObject.getString("data");
                            List<BaseFoundInfo> ds = JSON.parseArray(dataJson, BaseFoundInfo.class);
                            if (ds.size() > 0) {
                                mDatas.addAll(ds);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mStateView.showNoNet();
                            }
                        } else {
                            //                    showErrorView();
                            mStateView.showNoData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mStateView.showNoNet();//加载失败
                    }
                }
            });
        } else {
            mStateView.showNoNet();//加载失败
        }
    }

//    @Override
//    protected void reload() {
//        super.reload();
//        getFoundData();
//    }

    @Override
    public void OnFoundItemClick(FoundInfo info) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("appName", info.getName());
        if (TextUtils.equals(info.getUrl_open_type(), "2")) {
            //打开APP
            if ("8".equals(info.getId())) {
                MobclickAgent.onEvent(mContext, "miaobo_click", map);
                Intent intent = new Intent(mContext, MeinvMainActivity.class);
                startActivity(intent);
            }
            if ("9".equals(info.getId())) {  //9 = 秒播
//                Intent intent = new Intent(getContext(), VideoMainActivity.class);
//                startActivity(intent);
            }
            if ("10".equals(info.getId())) {// 10 = 动啦
                MobclickAgent.onEvent(mContext, "miaobo_click", map);
                Intent intent = new Intent(getContext(), DongtuMainActivity.class);
                startActivity(intent);
            }
            if (TextUtils.equals(info.getId(), "17")) {//影视天堂
                MobclickAgent.onEvent(mContext, "miaobo_click", map);
//                PageSwitcher.switchToPage(getContext(), FragmentFactory.FRAGMENT_TYPE_DISPLAY_MOVIE);
                Intent intent = new Intent(getContext(), MovieMainActivity.class);
                startActivity(intent);
            }

        } else if (TextUtils.equals(info.getUrl_open_type(), "1")) {
            MobclickAgent.onEvent(mContext, "miaobo_click", map);
            //用webview加载
            Bundle bundle = new Bundle();
            bundle.putString("url", info.getAndroid_url());
            PageSwitcher.switchToPage(getContext(), FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, bundle);
        } else if (TextUtils.equals(info.getUrl_open_type(), "3")) {
            MobclickAgent.onEvent(mContext, "miaobo_click", map);
            //游戏
            Bundle bundle = new Bundle();
            bundle.putString("url", info.getAndroid_url());
            PageSwitcher.switchToGamePage(mContext, FragmentFactory.FRAGMENT_TYPE_GAME_DETAIL2, bundle);
        }
    }


    /**
     * 状态View的点击事件
     */
    private StateViewOnclickCallback stateViewOnclickCallback = new StateViewOnclickCallback() {
        @Override
        public void NoNetClick() {
            Logger.i("没有网络点击事件");
            getFoundData();
        }

        @Override
        public void NoDataClick() {
            Logger.i("没有数据点击事件");
            getFoundData();
        }

        @Override
        public void NoLoginClick() {
            Logger.i("没有登录点击事件");
        }

        @Override
        public void CustomClick() {
            Logger.i("自定义点击事件");

        }
    };
}
