package com.anzhuo.video.app.dongtu.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuContentListBean;
import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuUserClickBean;
import com.anzhuo.video.app.dongtu.bean.dongtu.ShareInfo;
import com.anzhuo.video.app.dongtu.config.DongtuConstants;
import com.anzhuo.video.app.dongtu.manager.ActivityMy;
import com.anzhuo.video.app.dongtu.model.base.HomeModel;
import com.anzhuo.video.app.dongtu.model.base.NewInterface;
import com.anzhuo.video.app.dongtu.ui.base.DongtuBaseFragment;
import com.anzhuo.video.app.dongtu.utils.DatabaseUtils;
import com.anzhuo.video.app.dongtu.utils.DisplayUtil;
import com.anzhuo.video.app.dongtu.utils.DongtuToastUtil;
import com.anzhuo.video.app.dongtu.utils.EventUtil;
import com.anzhuo.video.app.dongtu.utils.FrescoUtils.FrescoUtil;
import com.anzhuo.video.app.dongtu.utils.NoDoubleClickListener;
import com.anzhuo.video.app.dongtu.utils.StateUtils;
import com.anzhuo.video.app.dongtu.utils.ViewUtils;
import com.anzhuo.video.app.dongtu.widget.StateView;
import com.anzhuo.video.app.dongtu.widget.StateViewOnclickCallback;
import com.anzhuo.video.app.utils.ViewUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.util.LuRecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import okhttp3.Call;


public class DongtuHomeListFragment extends DongtuBaseFragment {

    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mCategory;
    private String mCategoryid; //暂时没有用 留着
    private LuRecyclerView mRecy_view;
    private List<DongtuContentListBean> mMList;
    private LuRecyclerViewAdapter myAdapter;
    private CommonAdapter<DongtuContentListBean> mCommonAdapter;
    private PtrClassicFrameLayout mPtrFrameLayout_public;

    private ImageView mFab_refresh;
    int page = 0;
    private RelativeLayout mData_gen;
    private StateView mStateView;

    DongtuContentListBean contentListBeas2;
//    private ShowPopuWindows mPopuWindows;

    int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;

    public DongtuHomeListFragment() {
    }

    public static DongtuHomeListFragment newInstance(String param1) {
        DongtuHomeListFragment fragment = new DongtuHomeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_PARAM1);
            Logger.i("cq=============[数据名字]===========" + mCategory + "===");
        }
    }

    static final int FooterViewNormal = 1;
    static final int FooterViewTheEnd = 2;
    static final int FooterViewLoading = 3;
    static final int FooterViewNetWorkError = 4;

    private void setFooterViewState(int state) {
        switch (state) {
            case FooterViewNormal:
                //不能用上面这个，这个有  recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1)  ，会自动滑到底部去
//                LuRecyclerViewStateUtils.setFooterViewState(mActivity, recyclerView, 0, LoadingFooter.State.Normal, null);//不能用这个
                LuRecyclerViewStateUtils.setFooterViewState(mRecy_view, LoadingFooter.State.Normal);
                break;
            case FooterViewTheEnd:
                LuRecyclerViewStateUtils.setFooterViewState(mActivity, mRecy_view, 0, LoadingFooter.State.TheEnd, null);
                break;
            case FooterViewLoading:
                LuRecyclerViewStateUtils.setFooterViewState(mActivity, mRecy_view, 0, LoadingFooter.State.Loading, null);
                break;
            case FooterViewNetWorkError:
                LuRecyclerViewStateUtils.setFooterViewState(mActivity, mRecy_view, 0, LoadingFooter.State.NetWorkError, this.mFooterClick);
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dongtu_fragment_home_list;
    }

    @Override
    public void bindViews() {
        EventBus.getDefault().register(this);
        initView();
        //加载状态
        mStateView = new StateView(mActivity)
                .setParentConfig(mData_gen, mPtrFrameLayout_public)
                .setOnclick(stateViewOnclickCallback)
                .showLoading();

    }

    private void initView() {
        mMList = new ArrayList<>();
        mFab_refresh = ViewUtil.findView(mRootView, R.id.fab_refresh);
        mFab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        mData_gen = ViewUtil.findView(mRootView, R.id.data_gen);
//        mFab_refresh.setOnClickListener(this);
        mRecy_view = ViewUtil.findView(mRootView, R.id.rv_publics);
        mPtrFrameLayout_public = ViewUtil.findView(mRootView, R.id.ptrFrameLayout_public);

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecy_view.setLayoutManager(mLayoutManager);
//        mRecy_view.setItemAnimator(new DefaultItemAnimator()); //使用默认的动画

        initPtrFrameLayout(); //初始化头部
        mCommonAdapter = new CommonAdapter<DongtuContentListBean>(mActivity, R.layout.dongtu_item_joke_nine_img, mMList) {
            @Override
            protected void convert(ViewHolder holder, DongtuContentListBean contentListBean, int position) {
                try {
                    contentListBeas2 = null;
                    contentListBeas2 = contentListBean;
//                    if (position <= arrayList.size()) {
//                        contentListBean.setPicture(arrayList.get(position));
//                    }
//                    if (position <= arrayList1.size()) {
//                        contentListBean.setLit_url(arrayList1.get(position));
//                    }
                    setData(holder, contentListBean, position); //设置数据
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        /**
         * 点击跳转至详情
         */
        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) mMList);
                bundle.putString("position", String.valueOf(position));
                bundle.putString("Category", mCategory);//栏目名称了
                bundle.putString("page", String.valueOf(page));
                Logger.i("cq=============[baseHttpHashMap ced]===========" + page);
                ActivityMy.startDetailsActivity(mActivity, bundle);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        myAdapter = new LuRecyclerViewAdapter(mCommonAdapter);
        mRecy_view.setAdapter(myAdapter);
        initLoadMore(); //加载更多数据
    }


    /**
     * 设置数据
     *
     * @param holder
     * @param contentListBean
     */
    private void setData(ViewHolder holder, final DongtuContentListBean contentListBean, final int position) {
        Logger.i("cq=============[contentListBean]===========" + contentListBean.toString());
        TextView mTitleContent = holder.getView(R.id.tv_item_joke_content);
        SimpleDraweeView myImageView = holder.getView(R.id.my_image_view);//图片 gif
        //  SimpleDraweeView my_image_view1 = holder.getView(R.id.my_image_view1);// 低图片
        //  my_image_view1.setVisibility(View.VISIBLE);
//        final ImageView imgItemGifTips = holder.getView(R.id.img_item_gif_tips);
        TextView mTv_item_joke_comment = holder.getView(R.id.tv_item_joke_comment);
        TextView mItemZan = holder.getView(R.id.tv_item_joke_zan);
        TextView mItemUnZan = holder.getView(R.id.tv_item_joke_unzan);
        TextView mItemShare = holder.getView(R.id.tv_item_joke_share);
//        imgItemGifTips.setVisibility(View.VISIBLE);
        //设置数据
        setStateNum(contentListBean, position, mTitleContent, myImageView, null,
                mTv_item_joke_comment, mItemZan, mItemUnZan, mItemShare, null);
        /*点赞*/
        UserClickList(mActivity, mItemZan, contentListBean, position, true);
        /*点踩*/
        UserClickList(mActivity, mItemUnZan, contentListBean, position, false);
        /*分享*/
        UserShare(mItemShare, contentListBean);
    }

    /**
     * 加载数据  数量
     *
     * @param contentListBean
     * @param position
     * @param mTitleContent
     * @param myImageView
     * @param imgItemGifTips
     * @param mTv_item_joke_comment
     * @param mItemZan
     * @param mItemUnZan
     * @param mItemShare
     */
    private void setStateNum(final DongtuContentListBean contentListBean, final int position, TextView mTitleContent,
                             final SimpleDraweeView myImageView, final ImageView imgItemGifTips, TextView mTv_item_joke_comment, TextView mItemZan,
                             TextView mItemUnZan, TextView mItemShare, final SimpleDraweeView my_image_view1) {

        Logger.i("cq=============[点击gif]===========" + myImageView.getTag() + "====" + position);
        //计算图片缩放后的宽高
        String picwidth = contentListBean.getPicwidth();
        String picheight = contentListBean.getPicheight();
        int width = 0;
        int height = 0;
        if (!TextUtils.isEmpty(picheight) && !TextUtils.isEmpty(picwidth)) {
            //高宽不为空 类型转换
            width = Integer.parseInt(picwidth);
            height = Integer.parseInt(picheight);
        }
        int pictureWidth = 0;
        int pictureHeight = 0;
        if (width != 0 || height != 0) {
            //计算后的宽高
            pictureWidth = DisplayUtil.CountPictureWidth();//获取屏幕的宽度 - 控件两边的间距
            pictureHeight = (pictureWidth * height);
            pictureHeight = pictureHeight / width;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pictureWidth, pictureHeight);//动态设置宽高
            myImageView.setLayoutParams(layoutParams);
        }

        final BaseControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable final Animatable anim) {
                Logger.i("图片下载完成" + lastVisibleItem);
                //gif 图片下载完成后 自动播放 隐藏gif 图标
            }
        };

        // false 是否显示长图
        myImageView.setHierarchy(FrescoUtil.GetHierarchy(false));  //进度条
        // FrescoUtil.loadGifUrl(contentListBean.getLit_url(), null, my_image_view1, pictureWidth, pictureHeight, true, null);
        FrescoUtil.loadGifUrl(contentListBean.getPicture(), contentListBean.getLit_url(), myImageView, pictureWidth, pictureHeight, true, controllerListener);

        //根据动图id 从数据库中查找 然后设置点赞踩状态
        String dongtuId = contentListBean.getDongtuId();
        List<DongtuUserClickBean> states = new ArrayList<DongtuUserClickBean>();
        states.addAll(DataSupport.where("dongtuId=?", dongtuId).find(DongtuUserClickBean.class));
        Logger.i("cq=============[states]===========" + states.size());
        if (states.size() != 0) {
            DongtuUserClickBean clickBean = states.get(0);
            boolean zan = clickBean.getZan();
            boolean cai = clickBean.getCai();
            if (zan) {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like_selected, R.color.dongla_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            }
            if (cai) {
                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.ico_unlike_selected, R.color.dongla_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.icon_unlike, R.color.text_remind_gray_light);
            }
        } else {
            StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.icon_unlike, R.color.text_remind_gray_light);
        }
        //设置数量
        mItemZan.setText(contentListBean.getGoodnum());
        mItemUnZan.setText(contentListBean.getBadnum());
        mTitleContent.setText(contentListBean.getTitle());
        mItemShare.setText(contentListBean.getFavnum());
        mTv_item_joke_comment.setText(contentListBean.getCommentnum());
    }

    /**
     * 分享
     */
    private void UserShare(final TextView itemShare, final DongtuContentListBean contentListBean) {
        final ShareInfo shareInfo = new ShareInfo();
        shareInfo.setTitle(getContext().getResources().getString(R.string.app_name));
        shareInfo.setJokeUserID(contentListBean.getDongtuId());
        shareInfo.setContext(contentListBean.getTitle() + "");
        shareInfo.setImgUrl(contentListBean.getPicture());//头像
        shareInfo.setUrl(contentListBean.getPicture()); //点击进去的
        itemShare.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                ViewUtils.hideSoftInput(mActivity);
                //分享成功
//                mPopuWindows = new ShowPopuWindows(mActivity, shareInfo, new ShareInterface() {
//                    @Override
//                    public void ShareRes(String ShareRes) {
//                        if (ShareRes.equals(getResources().getString(R.string.share_success))) {//分享成功
//                            Logger.i("cq=============[分享]===========" + getResources().getString(R.string.share_success));
//                            DongtuToastUtil.showCenterToast(getResources().getString(R.string.share_success));
//                            int favnum = Integer.parseInt(contentListBean.getFavnum()) + 1;
//                            contentListBean.setFavnum(String.valueOf(favnum));
//                            itemShare.setText(String.valueOf(favnum));
//                        }
//                    }
//                });
//                mPopuWindows.ShowPop(true);
            }
        });
    }

    /**
     * 点赞踩
     *
     * @param mActivity
     * @param mItem
     * @param contentListBean
     * @param position
     * @param isZan
     */
    private void UserClickList(final Activity mActivity, final TextView mItem, final DongtuContentListBean contentListBean, final int position, final boolean isZan) {
        mItem.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
//                ToastUtil.showCenterToast("踩" + position);
                final String dongtuId = contentListBean.getDongtuId();
                final List<DongtuUserClickBean> zans = new ArrayList<DongtuUserClickBean>();
                zans.addAll(DataSupport.where("dongtuId=?", dongtuId).find(DongtuUserClickBean.class));
                if (zans.size() == 0) {
                    //说明没有点击
                    if (XUtilNet.isNetConnected()) {
                        //上报的类型
                        String upType = "";
                        if (isZan) {
                            upType = DongtuConstants.getZan;
                        } else {
                            upType = DongtuConstants.getCai;
                        }
                        contentListBean.getId();
                        HomeModel.getZan(contentListBean.getDongtuId(), upType, new NewInterface() {
                            @Override
                            public void onError(Call call, Exception e) {
                                Logger.i("cq=============[点赞上报 失败]===========" + e.toString());
                                DongtuToastUtil.showCenterToast(mActivity.getString(R.string.zanorun));
                            }

                            @Override
                            public void onSucceed(int state, String data, JSONObject obj) {
                                try {
                                    Logger.i("cq=============[点赞上报]===========" + data);
                                    Logger.i("cq=============[点踩上报]===========" + obj.toString());
                                    if (state == 200) {
                                        DongtuUserClickBean userClickBean = new DongtuUserClickBean();
                                        if (isZan) {
                                            userClickBean.setCai(false)
                                                    .setZan(true)
                                                    .setImei(DongtuConstants.IEMI)
                                                    .setDongtuId(dongtuId).save();
                                        } else {
                                            userClickBean.setCai(true)
                                                    .setZan(false)
                                                    .setImei(DongtuConstants.IEMI)
                                                    .setDongtuId(dongtuId).save();
                                        }
                                        if (isZan) {
                                            int goodnum = Integer.parseInt(contentListBean.getGoodnum()) + 1;
                                            contentListBean.setGoodnum(String.valueOf(goodnum));
                                            mItem.setText(contentListBean.getGoodnum());
                                            StateUtils.setDrawbleAndText(mActivity, mItem, R.drawable.icon_like_selected, R.color.video_main);
                                        } else {
                                            int badnum = Integer.parseInt(contentListBean.getBadnum()) + 1;
                                            contentListBean.setBadnum(String.valueOf(badnum));
                                            mItem.setText(contentListBean.getBadnum());
                                            StateUtils.setDrawbleAndText(mActivity, mItem, R.drawable.ico_unlike_selected, R.color.video_main);
                                        }
                                    } else {
                                        DongtuToastUtil.showCenterToast(mActivity.getString(R.string.zanorun));
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    DongtuToastUtil.showCenterToast(mActivity.getString(R.string.zanorun));
                                }
                            }
                        });
                    } else {
                        DongtuToastUtil.showCenterToast(R.string.check_net_is_connect);
                    }

                } else {
                    Logger.i("cq=============[点赞已经有了]===========" + zans.get(0).toString());
                    boolean zan = zans.get(0).getZan();
                    boolean cai = zans.get(0).getCai();
                    if (zan) {
                        DongtuToastUtil.showCenterToast("已经赞了");
                    }
                    if (cai) {
                        DongtuToastUtil.showCenterToast("已经踩了");
                    }
                }
            }
        });
    }

    /**
     * 头部动画
     */
    private void initPtrFrameLayout() {
        //加载头部 动画
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setTextColor(Color.BLACK);
        header.setBackgroundColor(getResources().getColor(R.color.black_10));
        header.setPadding(0, DisplayUtil.dp2px(mActivity, 20), 0, DisplayUtil.dp2px(mActivity, 20));
        header.initWithString("Loading");

        //  ptrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout_public.setHeaderView(header);
        mPtrFrameLayout_public.addPtrUIHandler(header);
        mPtrFrameLayout_public.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }


            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {//刷新
                if (mMList.size() == 0) {//说明没有数据
                    page = page + 1;
                    getData(mCategory, mCategoryid, null, String.valueOf(page), true);
                } else {
                    page = page + 1;
                    getData(mCategory, mCategoryid, mMList.get(0).getId(), String.valueOf(page), true);
                }
            }
        });
    }

    /**
     * 加载更多
     */
    private void initLoadMore() {
        mRecy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Logger.i("cq=============[滑动 状态]===========" + newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        FrescoUtil.resume();//恢复网络 加载
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        FrescoUtil.pause(); //暂停网络 加载
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        FrescoUtil.pause();//恢复网络 加载
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        //加载更多
        mRecy_view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadingFooter.State state = LuRecyclerViewStateUtils.getFooterViewState(mRecy_view);//获取当前底部状态
                if (state == LoadingFooter.State.Loading) {
                    Logger.i("正在请求数据，等一等");
                    return;
                }
                int i = page * Integer.valueOf(DongtuConstants.LIMIT);
                if (myAdapter.getItemCount() + 1 < i) {
                    setFooterViewState(FooterViewTheEnd);
                    Logger.i("cq=============[走的这里哦]===========" + lastVisibleItem + 1);
                    Logger.i("cq=============[走的这里哦]===========" + i);
//                    ToastUtil.showCenterToast(R.string.no_more);
                    return;
                } else {
                    setFooterViewState(FooterViewLoading);
                    page = page + 1;
                    //分页的时候 传 最后一条数据id
                    String id = mMList.get(mMList.size() - 1).getId();
                    getData(mCategory, mCategoryid, id, String.valueOf(page), false);
                }
            }
        });
    }

    /**
     * 加载更多的时候点击
     */
    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setFooterViewState(FooterViewLoading);
            page = page + 1;
            getData(mCategory, mCategoryid, null, String.valueOf(page), true);
        }
    };

    public void getData(String category, String categoryid, String id, String pages, final boolean isResh) {
        Logger.i("cq=============[首页请求数据 page]===========" + page);
        if (XUtilNet.isNetConnected()) {
            // 栏目id 第二个参数 分页的时候传
            HomeModel.getMainList(category, id, pages, new NewInterface() {
                @Override
                public void onError(Call call, Exception e) {
                    DongtuToastUtil.showCenterToast("加载失败");
                    loadFail();  // 没有网络 加载失败
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                    try {
                        List<DongtuContentListBean> requestList = new ArrayList<>();
                        mPtrFrameLayout_public.refreshComplete();
                        Logger.i("cq=============[data主页]===========" + data.toString());
                        if (state == 200) {
                            mStateView.showSuccess();
                            // 如果没有数据是 [] 两个字符 大于两个字符 有数据
                            if (data.length() > 2) {
                                requestList.addAll(JSON.parseArray(data, DongtuContentListBean.class));
//                                if (requestList != null) {
                                pageLoca = page;
                                if (isResh) {
                                    mMList.addAll(0, requestList);
                                } else {
                                    mMList.addAll(requestList);
                                }
                                saveData(requestList);//请求数据不为空 保存至数据库中
                                setFooterViewState(FooterViewTheEnd);
//                                } else {
//                                    //还是data 数据空
//                                    if (mMList.size() == 0) {
//                                        mStateView.showNoData();
//                                    }
//                                }
                            } else {
                                if (mMList.size() == 0) {
                                    mStateView.showNoData();
                                }
                                DongtuToastUtil.showCenterToast(R.string.no_more);
                                //说明数据是空的
                                setFooterViewState(FooterViewTheEnd);
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //解析失败
                        DongtuToastUtil.showCenterToast("解析失败");
                        if (mMList.size() == 0) {
                            loadLocaData();//加载本地数据
                        } else {
                            setFooterViewState(FooterViewNetWorkError);//网络错误
                        }
                    }
                }
            });
        } else {//没有网络
            DongtuToastUtil.showCenterToast(R.string.check_net_is_connect);
            loadFail();//没有网络
        }
    }

    int pageLoca = 0;

    /**
     * 没有网络 加载失败
     */
    private void loadFail() {
        page = pageLoca;
        Logger.i("cq=============[pageLoca = page]===========" + pageLoca + "==============" + page);
        Logger.i("cq=============[pageLoca = page]===========" + mMList.size());
        mPtrFrameLayout_public.refreshComplete();
        if (mMList.size() == 0) {
            loadLocaData();//加载本地数据
        } else {
            setFooterViewState(FooterViewNetWorkError);//网络错误
        }
    }

    /**
     * 当没有数据的时候 mMList.size = 0
     * 加载失败 或者没有网络的时候 加载本地数据
     */
    private void loadLocaData() {
//        if (!XUtilNet.isNetConnected()) {//没有网络的时候才去请求数据库
        try {
            List<DongtuContentListBean> contentListBeen = new ArrayList<>();
            contentListBeen.addAll(DataSupport.where("titleType=?", mCategory).order("id desc").find(DongtuContentListBean.class));
            if (contentListBeen.size() != 0) {
                mMList.addAll(contentListBeen);
                mStateView.showSuccess();
                myAdapter.notifyDataSetChanged();
            } else {
                mStateView.showNoData();
            }
        } catch (Exception e) {
            Logger.i("cq=============[pageLoca = page出错了]===========");
            e.printStackTrace();
        }
    }
//    }

    /**
     * 保存首页数据至数据库
     *
     * @param requestList
     */

    private void saveData(final List<DongtuContentListBean> requestList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.i("cq=============[保存数据库size]===========" + requestList.size());
                for (int i = 0; i < requestList.size(); i++) {
                    DongtuContentListBean contentListBean = requestList.get(i);
                    contentListBean.setTitleType(mCategory);
                    String id = contentListBean.getId();//拿到数据的id
                    Logger.i("cq=============[保存数据库中 title]===========" + contentListBean.getTitle());
                    DatabaseUtils.selectDongtuId(id);//查找此数据 如果有就删除了 说明是重数据
                }
                DataSupport.saveAll(requestList);
                Logger.i("cq=============[保存数据]===========" + DataSupport.findAll(DongtuContentListBean.class));
            }
        }).start();
    }

    /**
     * 加载数据
     */
    @Override
    public void onLazyLoad() {
        mPtrFrameLayout_public.postDelayed(new Runnable() {//刚进来的时候是否自动
            @Override
            public void run() {
                if (mMList.size() == 0)
                    mPtrFrameLayout_public.autoRefresh(true);
            }
        }, 0); //自动刷新数据 时间
    }

    /**
     * 执行刷新的动画，滑到顶部，加载动画，实际请求数据是在 onRefreshBegin 中已经写好了
     */
    private void refresh() {
        if (mPtrFrameLayout_public.isRefreshing()) {
            return;
        }
        mPtrFrameLayout_public.autoRefresh(true);
        mRecy_view.scrollToPosition(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.fab_refresh:
////                refresh();
//                break;
        }
    }

    /**
     * 状态View的点击事件
     */
    private StateViewOnclickCallback stateViewOnclickCallback = new StateViewOnclickCallback() {
        @Override
        public void NoNetClick() {
            Logger.i("没有网络点击事件");
            if (mMList.size() == 0) {//说明没有数据
                mStateView.showLoading();
                page = page + 1;
                getData(mCategory, mCategoryid, null, String.valueOf(page), true);
            }
        }

        @Override
        public void NoDataClick() {
            Logger.i("没有数据点击事件");
            if (mMList.size() == 0) {//说明没有数据
                mStateView.showLoading();
                page = page + 1;
                getData(mCategory, mCategoryid, null, String.valueOf(page), true);
            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 详情点赞 踩等后 接收事件 数量+1
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(EventUtil.EnventChangesStateData event) {
        int position = event.position;
        Logger.i("cq=============[接收事件 位置]===========" + position);
        boolean equals = mCategory.equals(event.titleTypes);
        Logger.i("cq=============[接收事件 类型是否相等]===========" + equals);
        if (equals) {
            String isType = event.isType;
            DongtuContentListBean bean;
            Logger.i("cq=============[接收事件 神马类型]===========" + isType);
            if (slidePosition != Integer.MAX_VALUE) {//不等于最大的这个默认值 说明位置改变了
                bean = mMList.get(slidePosition);
            } else {
                bean = mMList.get(position);
            }
            switch (isType) {
                case DongtuConstants.getZan:
                    int goodnums = Integer.parseInt(bean.getGoodnum()) + 1;
                    bean.setGoodnum(String.valueOf(goodnums));
                    break;
                case DongtuConstants.getCai:
                    int badnum = Integer.parseInt(bean.getBadnum()) + 1;
                    bean.setBadnum(String.valueOf(badnum));
                    break;
                case DongtuConstants.getShare:
                    int favnum = Integer.parseInt(bean.getFavnum()) + 1;
                    bean.setFavnum(String.valueOf(favnum));
                    break;
                case DongtuConstants.getComment:
                    int commentnum = Integer.parseInt(bean.getCommentnum()) + 1;
                    bean.setCommentnum(String.valueOf(commentnum));
                    break;
            }
            myAdapter.notifyDataSetChanged();
        }

    }

    int slidePosition = Integer.MAX_VALUE; //当详情的fragment 滑动的时候 位置改变 更新点赞踩评论分享的时候 需要这个位置

    /**
     * 详情页面滑动  外面item 位置改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePosition(EventUtil.EnventSendStateChangePosition event) {
        try {
            int toPosition = event.ToPosition;
            Logger.i("cq=============[返回的时候 详情的位置]===========" + slidePosition);
            slidePosition = toPosition;//滑动的位置
            Logger.i("cq=============[返回的时候 详情的位置]===========" + toPosition);
            mRecy_view.smoothScrollToPosition(toPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
