/*
package com.anzhuo.video.app.ui.fragment;


import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.MyInterface;
import com.anzhuo.video.app.manager.NIManage;
import com.anzhuo.video.app.model.bean.SearchRecordInfo;
import com.anzhuo.video.app.ui.base.BaseFragment;
import com.anzhuo.video.app.utils.DatabaseUtils;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilLog;
import com.anzhuo.video.app.widget.MyObserver;
import com.anzhuo.video.app.widget.ViewTipModule;
import com.anzhuo.video.app.widget.divider.HorizontalDividerItemDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


*/
/**
 * 搜索记录界面
 *//*

public class SearchRecordFragment extends BaseFragment implements View.OnClickListener {

    private List<MyObserver> myObservers = new ArrayList<>();

    private RecyclerView mRecyclerView, mTopSearchView;
    private List<SearchRecordInfo> datas = new ArrayList<>();
    private List<SearchRecordInfo> hotDatas = new ArrayList<>();
    private CommonAdapter<SearchRecordInfo> RecordAdapter;
    private CommonAdapter<SearchRecordInfo> HotAdapter;
    private Button mBtnClearRecord;
    private ViewTipModule module;
    private RelativeLayout searchContainer, searchSecondContainer;

    public SearchRecordFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutRes() {
        return
                R.layout.fragment_search_record;
    }

    @Override
    public void bindViews() {
//        super.bindView();
        setClassName(this.getClass().getName());
        mRecyclerView = $(R.id.rv_search_record);
        mTopSearchView = $(R.id.rv_search_hot);
        mBtnClearRecord = $(R.id.btn_clear_record);
        searchContainer = $(R.id.rl_container);
        searchSecondContainer = $(R.id.rl_second_container);
        mBtnClearRecord.setOnClickListener(this);


        //热门搜索
        mTopSearchView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mTopSearchView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.translucent_bg))
                .size(50)
                .build());
        HotAdapter = new CommonAdapter<SearchRecordInfo>(getContext(), R.layout.layout_simple_text, hotDatas) {
            @Override
            protected void convert(ViewHolder holder, SearchRecordInfo searchRecordInfo, int position) {
                TextView keyWord = holder.getView(R.id.tv_text);
                try {
                    keyWord.setText(searchRecordInfo.getKeyword() + "");
                    keyWord.setTextColor(Color.parseColor(searchRecordInfo.getColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        HotAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (MyObserver observer : myObservers) {
                    observer.updata(hotDatas.get(position).getKeyword());
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mTopSearchView.setAdapter(HotAdapter);


        //搜索记录
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.translucent_bg))
                .size(50)
                .build());
        RecordAdapter = new CommonAdapter<SearchRecordInfo>(getContext(), R.layout.layout_simple_text, datas) {
            @Override
            protected void convert(ViewHolder holder, SearchRecordInfo searchRecordInfo, int position) {
                holder.setText(R.id.tv_text, searchRecordInfo.getKeyword());
                XUtilLog.log_i("leibown", "position:" + position);
            }
        };
        RecordAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (MyObserver observer : myObservers) {
                    observer.updata(datas.get(position).getKeyword());
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecyclerView.setAdapter(RecordAdapter);
        module = new ViewTipModule(getContext(), searchContainer, searchSecondContainer, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        //搜索记录
        requestData(null);
        //热门搜索
        getHotSearchData();
    }


    @Override
    public void onLazyLoad() {

    }

    private void getHotSearchData() {
        NIManage.HotSearch("50", new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                module.showNoDataState(getString(R.string.search_none));
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    try {
                        List<SearchRecordInfo> temp = JSON.parseArray(data, SearchRecordInfo.class);
                        if (temp == null || temp.size() == 0) {
                            ToastUtil.showCenterToast( "服务器数据异常，稍后重试");
                        } else {
                            hotDatas.clear();
                            hotDatas.addAll(temp);
                        }
                        HotAdapter.notifyDataSetChanged();
                        module.showSuccessState();
                    } catch (Exception e) {
                        module.showNoDataState(getString(R.string.search_none));
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void requestData(String key) {
        datas.clear();
        if (key != null) {
            List<SearchRecordInfo> list = DatabaseUtils.getSearchKey(key);
            SearchRecordInfo info = new SearchRecordInfo();
            info.setKeyword(key);
            if (list == null || list.size() == 0) {
                info.save();
            }
        }
        if (DatabaseUtils.getSearchAll() != null) {
            datas.addAll(DatabaseUtils.getSearchAll());
        }
        XUtilLog.log_i("leibown", "SearchRecordFragment.data222:" + datas.toString());
        RecordAdapter.notifyDataSetChanged();
        module.showSuccessState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear_record:
                NIManage.clearRecord(new MyInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        module.showNoDataState(getString(R.string.search_none));
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        datas.clear();
                        RecordAdapter.notifyDataSetChanged();
                        module.showNoDataState(getString(R.string.search_none));
                    }
                });
                break;
        }
    }

    public void registObserver(MyObserver observer) {
        myObservers.add(observer);
    }
}
*/
