package com.anzhuo.video.app.meinv.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.meinv.entity.ItemListInfo;
import com.anzhuo.video.app.meinv.fragment.base.BaseFragmet;
import com.anzhuo.video.app.meinv.load.ImageLoaderManager;
import com.anzhuo.video.app.meinv.load.LoadMoreWrapper;
import com.anzhuo.video.app.meinv.manager.ActivityJump;
import com.anzhuo.video.app.meinv.manager.MyInterface;
import com.anzhuo.video.app.meinv.manager.NIManage;
import com.anzhuo.video.app.meinv.tool.HorizontalDividerItemDecoration;
import com.anzhuo.video.app.meinv.view.ViewTipModule;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.RefreshInterface;
import com.anzhuo.video.app.utils.ViewUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;



/**
 * A simple {@link Fragment} subclass.
 */
public class ChildFragment extends BaseFragmet {

    public static String INTENT_CATEGORY = "INTENT_CATEGORY";
    private RecyclerView pictureList;
    private CommonAdapter<ItemListInfo> adapter;
    private String categoryId;
    private List<ItemListInfo> datas;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isReload = false;
    private String nextId = "";
    private RelativeLayout childContainer;
    private ViewTipModule module;

    public ChildFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.fragment_child;
    }

    @Override
    protected void bindView() {
        super.bindView();
        setClassName(this.getClass().getName());
        categoryId = getArguments().getString(INTENT_CATEGORY);
        datas = new ArrayList<>();

        mSwipeRefreshLayout = $(R.id.srl_child_fragment);
        ViewUtil.initRefresh(mSwipeRefreshLayout, new RefreshInterface() {
            @Override
            public void getData() {
                reload();
            }
        });

        pictureList = $(R.id.picture_list);
        pictureList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pictureList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.translucent_bg))
                .size(4)
                .build());
        MVPictureAdapter();

        childContainer = $(R.id.fl_child);
        module = new ViewTipModule(getContext(), childContainer, pictureList, new ViewTipModule.Callback() {
            @Override
            public void getData() {

            }
        });
        reload();
    }

    private void reload() {
        isReload = true;
        nextId = "";
        requestData();
    }

    @Override
    public void onResume() {
        super.onResume();
//        reload();
        XUtilLog.log_i("leibown", "childFragment");
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
                ImageLoaderManager.displayImageByUrl(VideoApplication.getContext(), pictureImg, info.getPicture());
                holder.setText(R.id.picture_num, info.getNum());
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转到图片详情界面
                ActivityJump.getInstance().PictureDetails(getContext(),"0",position,datas);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        initLoadMoreData();
    }

    /**
     * 加载更多
     */
    private LoadMoreWrapper mLoadMoreWrapper;
    private int page = 1;

    private void initLoadMoreData() {
        mLoadMoreWrapper = new LoadMoreWrapper(adapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.layout_load_more);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
//                if (categoryId.equals("推荐"))
//                    XUtilLog.log_i("leibown", "categoryId_page:" + page);
                if (!isReload) {
                    page++;
                    requestData();
                } else
                    mLoadMoreWrapper.setLoadMoreViewGone();
            }
        });
        pictureList.setAdapter(mLoadMoreWrapper);
//        rv_music.addOnScrollListener(mOnScrollListener);
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        if (isReload) {
            page = 1;
        }
        getItemList();
    }

    /**
     * 得到导航类型列表
     */
    private void getItemList() {
        NIManage.getItemList(categoryId, nextId, "50", new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                if (!isReload) {
//                    ToastUtil.toastShort("没有更多数据");
                } else {
                    ToastUtil.toastShort("加载失败，服务器异常");
                    module.showNoDataState();
                }
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadMoreWrapper.setLoadMoreViewGone();
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    module.showSuccessState();
                    try {
                        if (isReload) {
                            datas.clear();
                            isReload = false;
                            pictureList.smoothScrollToPosition(0);
                        }
                        mLoadMoreWrapper.setLoadMoreViewGone();
                        if (JSON.parseArray(data, ItemListInfo.class).size() == 0) {
                            if (page == 1)
                                module.showNoDataState();
                            return;
                        }
                        datas.addAll(JSON.parseArray(data, ItemListInfo.class));
                        mLoadMoreWrapper.notifyDataSetChanged();
                        nextId = datas.get(datas.size() - 1).getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mSwipeRefreshLayout.setRefreshing(false);
                        ToastUtil.toastShort("推荐数据加载出现错误");
                        module.showNoDataState();
                    }
                } else {
                    ToastUtil.toastShort("推荐数据加载出现错误");
                    module.showNoDataState();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void attachAllMessage() {
        attachMessage(Message.Type.Refresh_current_page);
    }

    @Override
    public void onReceiveMessage(Message message) {
        super.onReceiveMessage(message);
        switch (message.type) {
            case Refresh_current_page: {//更新数据
                mSwipeRefreshLayout.setRefreshing(true);
                XUtilLog.log_i("wbb","点击刷新了  么么哒(づ￣ 3￣)づ");
                reload();
                break;
            }
        }
    }

}
