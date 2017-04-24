package com.anzhuo.video.app.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.model.bean.FuliBean;
import com.anzhuo.video.app.ui.adapter.MyFuliAdapter;
import com.anzhuo.video.app.ui.base.BaseFragment;
import com.anzhuo.video.app.utils.HttpUtils;
import com.anzhuo.video.app.utils.ViewUtil;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 福利
 */
public class WealFragment extends BaseFragment {

    private RecyclerView mRview;
    private List<FuliBean> mArrayList;
    private MyFuliAdapter mMyFuliAdapter;

    public WealFragment() {
    }

    public static WealFragment newInstance() {
        WealFragment fragment = new WealFragment();
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_weal;
    }

    @Override
    public void bindViews() {
        try {
            mArrayList = new ArrayList();
            mRview = ViewUtil.findView(mRootView, R.id.rv_main);
            mRview.setLayoutManager(new LinearLayoutManager(mActivity));
            mMyFuliAdapter = new MyFuliAdapter(mActivity, mArrayList);
            mRview.setAdapter(mMyFuliAdapter);
            getFoundData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void onClick(View v) {

    }

    private void getFoundData() {
        String URL = AppServerUrl.MOVIE_FOUND_URL;
        Logger.i("cq=============[发现页面]===========" + URL);
        HttpUtils.getStringAsync(URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.i("cq=============[发现页面 加载失败]===========" + e.toString());

            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if (TextUtils.equals(ret, "200")) {
                    String dataJson = jsonObject.getString("data");
                    List<FuliBean> fu = JSON.parseArray(dataJson, FuliBean.class);
                    Logger.i("cq=============[发现页面 加载成功1]===========" + fu.size());
                    Logger.i("cq=============[发现页面 加载成功]===========" + fu.toString());
                    if (fu != null) {
                        mArrayList.addAll(fu);
                    } else {

                    }
                    mMyFuliAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
