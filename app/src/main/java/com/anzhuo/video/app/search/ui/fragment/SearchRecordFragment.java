package com.anzhuo.video.app.search.ui.fragment;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.search.entity.SearchRecordInfo;
import com.anzhuo.video.app.search.ui.fragment.base.BaseFragmet;
import com.anzhuo.video.app.search.utils.DatabaseUtils;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.widget.MyObserver;
import com.anzhuo.video.app.widget.StateView;
import com.anzhuo.video.app.widget.StateViewOnclickCallback;
import com.anzhuo.video.app.widget.divider.HorizontalDividerItemDecoration;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * 搜索记录界面
 */
public class SearchRecordFragment extends BaseFragmet implements View.OnClickListener {

    private List<MyObserver> myObservers = new ArrayList<>();

    //    private RecyclerView mRecyclerView, mTopSearchView;
    private RecyclerView mRecyclerView;
    private List<SearchRecordInfo> datas = new ArrayList<>();
    private List<SearchRecordInfo> hotDatas = new ArrayList<>();
    private CommonAdapter<SearchRecordInfo> RecordAdapter;
    //    private CommonAdapter<SearchRecordInfo> HotAdapter;
    private Button mBtnClearRecord;
    //    private ViewTipModule module;
    private RelativeLayout searchContainer, searchSecondContainer;
    private StateView mStateView;

    public SearchRecordFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.fragment_search_record;
    }

    @Override
    protected void bindView() {
        super.bindView();
        setClassName(this.getClass().getName());
        mRecyclerView = $(R.id.rv_search_record);
//        mTopSearchView = $(R.id.rv_search_hot);
        mBtnClearRecord = $(R.id.btn_clear_record);
        searchContainer = $(R.id.rl_container);
        searchSecondContainer = $(R.id.rl_second_container);
        mBtnClearRecord.setOnClickListener(this);


        //热门搜索
//        mTopSearchView.setLayoutManager(new GridLayoutManager(getContext(), 5));
//        mTopSearchView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
//                .color(getResources().getColor(R.color.translucent_bg))
//                .size(50)
//                .build());
//        HotAdapter = new CommonAdapter<SearchRecordInfo>(getContext(), R.layout.layout_simple_text, hotDatas) {
//            @Override
//            protected void convert(ViewHolder holder, SearchRecordInfo searchRecordInfo, int position) {
//                TextView keyWord = holder.getView(R.id.tv_text);
//                try {
//                    keyWord.setText(searchRecordInfo.getKeyword() + "");
//                    keyWord.setTextColor(Color.parseColor(searchRecordInfo.getColor()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        HotAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                for (MyObserver observer : myObservers) {
//                    observer.updata(hotDatas.get(position).getKeyword());//点击热门 请求数据
//                }
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
//        mTopSearchView.setAdapter(HotAdapter);

        //搜索记录
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        //绘制分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(mActivity.getResources().getColor(R.color.translucent_bg))
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
                    observer.updata(datas.get(position).getKeyword());//
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecyclerView.setAdapter(RecordAdapter);

//        module = new ViewTipModule(getContext(), searchContainer, searchSecondContainer, null);
        //加载状态
        mStateView = new StateView(mActivity)
                .setParentConfig(searchContainer, searchSecondContainer)
                .setOnclick(stateViewOnclickCallback)
                .showLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //搜索记录
            requestData(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //热门搜索
//        getHotSearchData();
    }

   /* private void getHotSearchData() {
        NIManage.HotSearch("50", new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                module.showNoDataState(getString(R.string.search_none));
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                Logger.i("cq=============[服务器数据异常]===========" + obj.toString());
                if (state == 200) {
                    try {
                        List<SearchRecordInfo> temp = JSON.parseArray(data, SearchRecordInfo.class);
                        if (temp == null || temp.size() == 0) {
                            ToastUtil.toastLong(getContext(), "服务器数据异常，稍后重试。");
                        } else {
                            hotDatas.clear();
                            hotDatas.addAll(temp);
                        }
//                        HotAdapter.notifyDataSetChanged();
                        module.showSuccessState();
                    } catch (Exception e) {
                        module.showNoDataState(getString(R.string.search_none));
                        e.printStackTrace();
                    }
                }
            }
        });
    }*/

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
//        XUtilLog.log_i("leibown", "SearchRecordFragment.data222:" + datas.toString());

        //说明没有数据
        if (datas.size() == 0) {
            mStateView.showNoData(getString(R.string.search_key), R.drawable.weiguanzhu);
        } else {
            mStateView.showSuccess();
            RecordAdapter.notifyDataSetChanged();
        }
//        module.showSuccessState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear_record:
                int i = DataSupport.deleteAll(SearchRecordInfo.class);
                if (i > 0) {
                    RecordAdapter.notifyDataSetChanged();
//                    module.showNoDataState(getString(R.string.search_none));
                    mStateView.showNoData(getString(R.string.search_key), R.drawable.weiguanzhu);
                }

//                NIManage.clearRecord(new MyInterface() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        module.showNoDataState(getString(R.string.search_none));
//                    }
//
//                    @Override
//                    public void onSucceed(int state, String data, JSONObject obj) {
//                        datas.clear();
//                        RecordAdapter.notifyDataSetChanged();
//                        module.showNoDataState(getString(R.string.search_none));
//                    }
//                });

        }
    }

    public void registObserver(MyObserver observer) {
        myObservers.add(observer);
    }

    /**
     * 状态View的点击事件
     */
    private StateViewOnclickCallback stateViewOnclickCallback = new StateViewOnclickCallback() {
        @Override
        public void NoNetClick() {
            Logger.i("没有网络点击事件");
//            if (mMList.size() == 0) {//说明没有数据
//                mStateView.showLoading();
//                page = page + 1;
//                getData(mCategory, mCategoryid, null, String.valueOf(page), true);
//            }
        }

        @Override
        public void NoDataClick() {
            Logger.i("没有数据点击事件");
//            if (mMList.size() == 0) {//说明没有数据
//                mStateView.showLoading();
//                page = page + 1;
//                getData(mCategory, mCategoryid, null, String.valueOf(page), true);
//            }
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
