/*
package com.anzhuo.video.app.ui.fragment;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.manager.MyInterface;
import com.anzhuo.video.app.manager.NIManage;
import com.anzhuo.video.app.model.bean.ItemListInfo;
import com.anzhuo.video.app.ui.base.BaseFragment;
import com.anzhuo.video.app.utils.ImageLoaderManager;
import com.anzhuo.video.app.utils.LoadMoreWrapper;
import com.anzhuo.video.app.utils.RefreshInterface;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.ViewUtil;
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
 * 搜索结果界面
 *//*

public class SearchResultFragment extends BaseFragment implements MyObserver {
    private RecyclerView pictureList;
    private CommonAdapter<ItemListInfo> adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ItemListInfo> datas = new ArrayList<>();
    private LoadMoreWrapper mLoadMoreWrapper;
    public String keyword = "";
    private int page = 1;
    public boolean isReload = true;
    private ViewTipModule module;
    private FrameLayout searchContainer;
//    private boolean isNoData = false;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void bindViews() {
        setClassName(this.getClass().getName());
        mSwipeRefreshLayout = $(R.id.srl_child_fragment);
        ViewUtil.initRefresh(mSwipeRefreshLayout, new RefreshInterface() {
            @Override
            public void getData() {
                if (!keyword.equals("")) {
                    isReload = true;
                    notifySearchDataChanged(keyword);
                    XUtilLog.log_i("leibown", "======11111111==========:" + keyword);
                }
            }
        });

        pictureList = $(R.id.picture_list);
        pictureList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pictureList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.translucent_bg))
                .size(4)
                .build());
        MVPictureAdapter();
        searchContainer = $(R.id.fl_container);
        module = new ViewTipModule(getContext(), searchContainer, pictureList, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onLazyLoad() {

    }

    public void notifySearchDataChanged(String keyword) {
        XUtilLog.log_i("leibown", "keyword:" + keyword);
        this.keyword = keyword;
        if (isReload) {
            page = 1;
            datas.clear();
        }
        NIManage.Search(keyword, page + "", "50", new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                XUtilLog.log_i("wbb", "onError:" + e.toString());
                module.showNoDataState(getString(R.string.search_null));
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadMoreWrapper.setLoadMoreViewGone();
                module.showSuccessState();
                if (JSON.parseArray(data, ItemListInfo.class).size() == 0) {
                    if (page == 1)
                        module.showNoDataState(getString(R.string.search_null));
                    else {
                        ToastUtil.showCenterToast("没有更多数据");
                    }
                    return;
                }
                datas.addAll(JSON.parseArray(data, ItemListInfo.class));
                mLoadMoreWrapper.notifyDataSetChanged();
                isReload = false;
                XUtilLog.log_i("leibown111", "datas:" + datas.toString());
            }
        });

    }

    private void MVPictureAdapter() {
        adapter = new CommonAdapter<ItemListInfo>(getContext(), R.layout.picture_item_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, ItemListInfo info, int position) {
                LinearLayout pictureLayout = holder.getView(R.id.picture_item_main_layout);
                ImageView pictureImg = holder.getView(R.id.picture_img);
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (Constants.Phigh / 2) - 110);
                linearParams.setMargins(8, 8, 8, 8);
                pictureLayout.setLayoutParams(linearParams);
                ImageLoaderManager.displayImageByUrl(mContext, pictureImg, info.getPicture());
                holder.setText(R.id.picture_num, info.getNum());
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转到图片详情界面
             //   ActivityJump.getInstance().PictureDetails(getContext(), "0", position, datas);
                XUtilLog.log_i("leibown111", "datas.get(position).getIsLike():" + datas.get(position).getIsLike());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        initLoadMoreData();
    }

    private void initLoadMoreData() {
        mLoadMoreWrapper = new LoadMoreWrapper(adapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.layout_load_more);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                notifySearchDataChanged(keyword);
                XUtilLog.log_i("leibown", "======22222222==========:" + keyword);
            }
        });
        pictureList.setAdapter(mLoadMoreWrapper);
//        rv_music.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void updata(String data) {
        isReload = true;
        notifySearchDataChanged(data);
    }

    @Override
    public void onClick(View v) {

    }
}
*/
