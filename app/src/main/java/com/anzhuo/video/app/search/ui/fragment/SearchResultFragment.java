package com.anzhuo.video.app.search.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.manager.ActivityMy;
import com.anzhuo.video.app.manager.NIManage;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.CollectListBean;
import com.anzhuo.video.app.model.bean.ContentListBean;
import com.anzhuo.video.app.model.bean.ItemListInfo;
import com.anzhuo.video.app.model.bean.UserClickBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.search.ui.fragment.base.BaseFragmet;
import com.anzhuo.video.app.search.ui.manager.MyInterface;
import com.anzhuo.video.app.search.utils.LoadMoreWrapper;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.search.utils.ToastUtils;
import com.anzhuo.video.app.search.utils.ViewUtil;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.ui.adapter.pub.PublicUtil;
import com.anzhuo.video.app.utils.DataBase.DatabaseUtils;
import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.NoDoubleClickListener;
import com.anzhuo.video.app.utils.RecycleViewVideoUtil;
import com.anzhuo.video.app.utils.RefreshInterface;
import com.anzhuo.video.app.utils.StateUtils;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.widget.JCVideoPlayerStandardFresco;
import com.anzhuo.video.app.widget.MyObserver;
import com.anzhuo.video.app.widget.ShowPopuWindows;
import com.anzhuo.video.app.widget.StateView;
import com.anzhuo.video.app.widget.StateViewOnclickCallback;
import com.anzhuo.video.app.widget.divider.HorizontalDividerItemDecoration;
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

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_AUTO_COMPLETE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_ERROR;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_NORMAL;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PAUSE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.WIFI_TIP_DIALOG_SHOWED;


/**
 * 搜索结果界面
 */
public class SearchResultFragment extends BaseFragmet implements MyObserver {
    private RecyclerView pictureList;
    private CommonAdapter<ContentListBean> adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ContentListBean> datas = new ArrayList<>();
    private LoadMoreWrapper mLoadMoreWrapper;
    public String keyword = "";
    private int page = 1;
    public boolean isReload = true;
    //    private ViewTipModule module;
    private FrameLayout searchContainer;
    private StateView mStateView;
//    private boolean isNoData = false;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.fragment_search_result;
    }


    @Override
    protected void bindView() {
        super.bindView();

        setClassName(this.getClass().getName());
        mSwipeRefreshLayout = $(R.id.srl_child_fragment);
        mSwipeRefreshLayout.setRefreshing(false);
        ViewUtil.initRefresh(mSwipeRefreshLayout, new RefreshInterface() {
            @Override
            public void getData() {
                if (!keyword.equals("")) {
//                    isReload = true;
                    page = page + 1;
                    notifySearchDataChanged(keyword, true);
                    XUtilLog.log_i("leibown", "======11111111==========:" + keyword);
                }
            }
        });
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (XUtilNet.isNetConnected()) {
//                    if (!keyword.equals("")) {
////                    isReload = true;
//                        page = page + 1;
//                        notifySearchDataChanged(keyword, true);
//                        Logger.i("cq=============[刷新监听]===========");
//                    }
//                } else {
//                    ToastUtil.toastShort(R.string.check_network);
//                }
//            }
//
//        });

        pictureList = $(R.id.picture_list);
        pictureList.setLayoutManager(new LinearLayoutManager(getContext()));
        pictureList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.translucent_bg))
                .size(4)
                .build());
        MVPictureAdapter();
        searchContainer = $(R.id.fl_container);
//        module = new ViewTipModule(getContext(), searchContainer, pictureList, null);

        mStateView = new StateView(mActivity)
                .setParentConfig(searchContainer, pictureList)
                .setOnclick(stateViewOnclickCallback)
                .showLoading();

        RecycleViewVideoUtil.addOnChildAttachStateChangeListener(pictureList);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        } else {
            JCVideoPlayer.releaseAllVideos();
        }
    }


    /**
     * 状态View的点击事件
     */
    private StateViewOnclickCallback stateViewOnclickCallback = new StateViewOnclickCallback() {
        @Override
        public void NoNetClick() {
            Logger.i("没有网络点击事件");
            if (datas.size() == 0) {//说明没有数据
                mStateView.showLoading();
                page = 1;
                notifySearchDataChanged(keyword, false);
            }
        }

        @Override
        public void NoDataClick() {
            Logger.i("没有数据点击事件");
            if (datas.size() == 0) {//说明没有数据
                mStateView.showLoading();
                page = 1;
                notifySearchDataChanged(keyword, false);
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

    public void notifySearchDataChanged(String keyword, final boolean isResh) {
        try {
            this.keyword = keyword;
            if (isReload) {
                page = 1;
                datas.clear();
                XUtilLog.log_i("leibown", "keyword:" + keyword);
            }
            NIManage.Search(keyword, page + "", "20", new MyInterface() {
                @Override
                public void onError(Call call, Exception e) {
                    Logger.i("cq=============[错误]===========" + e.getMessage());
//                module.showNoDataState(getString(R.string.search_null));
//                mStateView.showNoData();
                    mStateView.showNoData(getString(R.string.search_null), R.drawable.weiguanzhu);
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                    Logger.i("cq=============[收索的数据]===========" + obj.toString());
                    mSwipeRefreshLayout.setRefreshing(false);
                    mLoadMoreWrapper.setLoadMoreViewGone();
                    mStateView.showSuccess();
                    if (JSON.parseArray(data, ItemListInfo.class).size() == 0) {
                        if (page == 1)
                            mStateView.showNoData(getString(R.string.search_null), R.drawable.weiguanzhu);
                        else {
                            ToastUtil.toastShort("没有更多数据");
                        }
                        return;
                    }
                    if (isResh) {
                        datas.addAll(0, JSON.parseArray(data, ContentListBean.class));
                    } else {
                        datas.addAll(JSON.parseArray(data, ContentListBean.class));
                    }
//                    datas.addAll(JSON.parseArray(data, ContentListBean.class));
                    mLoadMoreWrapper.notifyDataSetChanged();
                    isReload = false;
                    XUtilLog.log_i("leibown111", "datas:" + datas.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MVPictureAdapter() {
        adapter = new CommonAdapter<ContentListBean>(getContext(), R.layout.item_joke_nine_img, datas) {
            @Override
            protected void convert(ViewHolder holder, ContentListBean contentListBean, int position) {
                setData(holder, contentListBean, position); //设置数据
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转到图片详情界面
//                ActivityJump.getInstance().PictureDetails(getContext(), "0", position, datas);
                if (datas.get(position).getIs_ad().equals("1")) {//跳转网页
                    SkipX5ADetails(datas.get(position).getTitle(), datas.get(position).getAd_url());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) datas);
                    bundle.putString("position", String.valueOf(position));
                    bundle.putString("Category", keyword);//栏目名称了 收索的关键字了
                    bundle.putString("page", String.valueOf(page));
                    bundle.putBoolean("isSerach", true); //是否为收索
                    Logger.i("=======[baseHttpHashMap ced]===========" + page);
                    ActivityMy.startDetailsActivity(mActivity, bundle);
                }
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
                if (datas.size() != 0) {
                    page++;
                    notifySearchDataChanged(keyword, false);
                    XUtilLog.log_i("leibown", "======22222222==========:" + keyword);
                }

            }
        });
        pictureList.setAdapter(mLoadMoreWrapper);
//        rv_music.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void updata(String data) {
        isReload = true;
        notifySearchDataChanged(data, false);
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param contentListBean
     */
    private void setData(ViewHolder holder, final ContentListBean contentListBean, final int position) {
        Logger.i("=======[contentListBean]===========" + contentListBean.toString());
        TextView mTitleContent = holder.getView(R.id.tv_item_joke_content2);

//        SimpleDraweeView myImageView = holder.getView(R.id.my_image_view);//图片 gif

        JCVideoPlayerStandardFresco jcVideoPlayerStandard = holder.getView(R.id.jc_video);

//        final ImageView imgItemGifTips = holder.getView(R.id.img_item_gif_tips);
        TextView mTv_item_joke_comment = holder.getView(R.id.tv_item_joke_comment);
        TextView mItemZan = holder.getView(R.id.tv_item_joke_zan);
        TextView mItemCollect = holder.getView(R.id.tv_item_collect);//收藏
        TextView mItemShare = holder.getView(R.id.tv_item_joke_share);
//        imgItemGifTips.setVisibility(View.VISIBLE);
        //设置数据
        setStateNum(contentListBean, position, mTitleContent, jcVideoPlayerStandard, mTv_item_joke_comment, mItemZan, mItemCollect, mItemShare);
        /*点赞*/
        UserClickList(mActivity, mItemZan, contentListBean, position, true);
        /*点踩*/
        //  UserClickList(mActivity, mItemCollect, contentListBean, position, false);
        /*分享*/
        UserShare(mItemShare, contentListBean);
        UserCollect(mItemCollect, contentListBean, mItemCollect);//收藏
    }

    /**
     * 加载数据  数量
     *
     * @param contentListBean
     * @param position
     * @param mTitleContent
     * @param mTv_item_joke_comment
     * @param mItemZan
     * @param mItemUnZan
     * @param mItemShare
     */
    private void setStateNum(final ContentListBean contentListBean, final int position, TextView mTitleContent,
                             final JCVideoPlayerStandardFresco jcVideoPlayerStandard, TextView mTv_item_joke_comment, TextView mItemZan,
                             TextView mItemUnZan, TextView mItemShare) {

        Logger.i("=======[点击gif]===========");
        Logger.i("=======[点击gif2]===========");
        //根据videoid 从数据库中查找 然后设置点赞踩状态
        String dongtuId = contentListBean.getDongtuId();
        List<UserClickBean> states = new ArrayList<UserClickBean>();
        Logger.i("=======[点击gif3]===========");
        states.addAll(DataSupport.where("dongtuId=?", dongtuId).find(UserClickBean.class));
//        Logger.i("=======[有collectListBeen1上 collectListBeen1 ]===========" + mCategory.toString() + "===" + dongtuId);
        CollectListBean collects = DataSupport.where("dongtuId=?", dongtuId).order("id desc").findFirst(CollectListBean.class);
        Logger.i("=======[点击gif4]===========");

        //收藏的状态
        if (collects != null) {
            String collectId = collects.getDongtuId();
            if (collectId.equals(dongtuId)) {
                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.shoucang, R.color.video_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.shoucang_hui, R.color.text_remind_gray_light);
            }
        } else {
            StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.shoucang_hui, R.color.text_remind_gray_light);
        }


        if (states.size() != 0) {
            UserClickBean clickBean = states.get(0);
            boolean zan = clickBean.getZan();
            boolean cai = clickBean.getCai();
            if (zan) {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like_selected, R.color.video_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            }

//            if (cai) {
//                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.ico_unlike_selected, R.color.video_main);
//            } else {
//                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.icon_unlike, R.color.text_remind_gray_light);
//            }
        } else {
            StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            //   StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.icon_unlike, R.color.text_remind_gray_light);
        }
        //设置数量
        mItemZan.setText(contentListBean.getGoodnum());
        //  mItemUnZan.setText(contentListBean.getBadnum());
        mTitleContent.setText(contentListBean.getTitle());
//        mItemShare.setText(contentListBean.getFavnum());
        mTv_item_joke_comment.setText(contentListBean.getCommentnum());

        //计算图片缩放后的宽高
        String picwidth = contentListBean.getPic_width();
        String picheight = contentListBean.getPic_height();
        //视频处理
        try {
            //以下是视频部分
            jcVideoPlayerStandard.setTag(contentListBean.getDongtuId());
            if (!jcVideoPlayerStandard.getTag().equals(contentListBean.getDongtuId())) {
                return;
            }
            PublicUtil.LoadVideos(getContext(), jcVideoPlayerStandard,
                    picwidth, picheight, contentListBean.getV_aimurl(), contentListBean.getPic_url(), false, contentListBean, contentListBean.getTitle());
            String is_ad = contentListBean.getIs_ad();
            if (is_ad.equals("1")) {//说明是要放广告
                //视频处理
                jcVideoPlayerStandard.thumbImageView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        SkipX5ADetails(contentListBean.getTitle(), contentListBean.getAd_url());

                    }
                });
                jcVideoPlayerStandard.startButton.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        SkipX5ADetails(contentListBean.getTitle(), contentListBean.getAd_url());

                    }
                });
            } else {
                //视频处理
                jcVideoPlayerStandard.thumbImageView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        RecycleViewVideoUtil.setCurrentItemPosition(position);
//                        jcVideoPlayerStandard.startVideo();
                        startVideo(jcVideoPlayerStandard);

                    }
                });
                jcVideoPlayerStandard.startButton.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        RecycleViewVideoUtil.setCurrentItemPosition(position);
//                        jcVideoPlayerStandard.startVideo();
                        startVideo(jcVideoPlayerStandard);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 播放视频
     *
     * @param standardFresco
     */
    private void startVideo(JCVideoPlayerStandardFresco standardFresco) {
        {
            String url = standardFresco.url;
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (standardFresco.currentState == CURRENT_STATE_NORMAL || standardFresco.currentState == CURRENT_STATE_ERROR) {
                if (!url.startsWith("file") && !JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    standardFresco.showWifiDialog();
                    return;
                }
                standardFresco.prepareMediaPlayer();
                standardFresco.onEvent(standardFresco.currentState != CURRENT_STATE_ERROR ? JCUserAction.ON_CLICK_START_ICON : JCUserAction.ON_CLICK_START_ERROR);
            } else if (standardFresco.currentState == CURRENT_STATE_PLAYING) {
                standardFresco.onEvent(JCUserAction.ON_CLICK_PAUSE);
                JCMediaManager.instance().mediaPlayer.pause();
                standardFresco.setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
            } else if (standardFresco.currentState == CURRENT_STATE_PAUSE) {
                standardFresco.onEvent(JCUserAction.ON_CLICK_RESUME);
                JCMediaManager.instance().mediaPlayer.start();
                standardFresco.setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
            } else if (standardFresco.currentState == CURRENT_STATE_AUTO_COMPLETE) {
                standardFresco.onEvent(JCUserAction.ON_CLICK_START_AUTO_COMPLETE);
                standardFresco.prepareMediaPlayer();
            }
        }
    }

    /**
     * 内容 跳转至广告
     */
    private void SkipX5ADetails(String title, String ad_url) {
        ActivityMy.skipX5Details(mActivity, title, ad_url);
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
    private void UserClickList(final Activity mActivity, final TextView mItem, final ContentListBean contentListBean, final int position, final boolean isZan) {
        mItem.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
//                ToastUtil.showCenterToast("踩" + position);
                final String dongtuId = contentListBean.getDongtuId();
                final List<UserClickBean> zans = new ArrayList<UserClickBean>();
                zans.addAll(DataSupport.where("dongtuId=?", dongtuId).find(UserClickBean.class));
                if (zans.size() == 0) {
                    //说明没有点击
                    if (XUtilNet.isNetConnected()) {
                        //上报的类型
                        String upType = "";
                        if (isZan) {
                            upType = Constants.getZan;
                        } else {
                            upType = Constants.getCai;
                        }
                        contentListBean.getId();
                        HomeModel.getZan(contentListBean.getDongtuId(), upType, new NewInterface() {
                            @Override
                            public void onError(Call call, Exception e) {
                                Logger.i("=======[点赞上报 失败]===========" + e.toString());
                                ToastUtils.showCenterToast(mActivity.getString(R.string.zanorun));
                            }

                            @Override
                            public void onSucceed(int state, String data, JSONObject obj) {
                                try {
                                    Logger.i("=======[点赞上报]===========" + data);
                                    Logger.i("=======[点踩上报]===========" + obj.toString());
                                    if (state == 200) {
                                        UserClickBean userClickBean = new UserClickBean();
                                        if (isZan) {
                                            userClickBean.setCai(false)
                                                    .setZan(true)
                                                    .setImei(Constants.IEMI)
                                                    .setDongtuId(dongtuId).save();
                                        } else {
                                            userClickBean.setCai(true)
                                                    .setZan(false)
                                                    .setImei(Constants.IEMI)
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
                                        ToastUtils.showCenterToast(mActivity.getString(R.string.zanorun));
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    ToastUtils.showCenterToast(mActivity.getString(R.string.zanorun));
                                }
                            }
                        });
                    } else {
                        ToastUtils.showCenterToast(R.string.check_net_is_connect);
                    }

                } else {
                    Logger.i("=======[点赞已经有了]===========" + zans.get(0).toString());
                    boolean zan = zans.get(0).getZan();
                    boolean cai = zans.get(0).getCai();
                    if (zan) {
                        ToastUtils.showCenterToast("已经赞了");
                    }
                    if (cai) {
                        ToastUtils.showCenterToast("已经踩了");
                    }
                }
            }
        });
    }

    /**
     * 分享
     */
    private void UserShare(final TextView itemShare, final ContentListBean contentListBean) {
        itemShare.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
//                ShareUtil.getShareDialog(mActivity, "我是复制的链接");
                new ShowPopuWindows(mActivity, "分享地址还没有……").ShowPop();
            }
        });

//        final ShareInfo shareInfo = new ShareInfo();
//        shareInfo.setTitle(DonglaApplication.getContext().getResources().getString(R.string.app_name));
//        shareInfo.setJokeUserID(contentListBean.getDongtuId());
//        shareInfo.setContext(contentListBean.getTitle() + "");
//        shareInfo.setImgUrl(contentListBean.getPicture());//头像
//        shareInfo.setUrl(contentListBean.getPicture()); //点击进去的
//
//        itemShare.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View view) {
//                ViewUtils.hideSoftInput(mActivity);
//                //分享成功
//                mPopuWindows = new ShowPopuWindows(mActivity, shareInfo, new ShareInterface() {
//                    @Override
//                    public void ShareRes(String ShareRes) {
//                        if (ShareRes.equals(getResources().getString(R.string.share_success))) {//分享成功
//                            Logger.i("=======[分享]===========" + getResources().getString(R.string.share_success));
//                            ToastUtils.showCenterToast(getResources().getString(R.string.share_success));
//                            int favnum = Integer.parseInt(contentListBean.getFavnum()) + 1;
//                            contentListBean.setFavnum(String.valueOf(favnum));
//                            itemShare.setText(String.valueOf(favnum));
//                        }
//                    }
//                });
//                mPopuWindows.ShowPop();
//
//            }
//        });
    }


    /**
     * 收藏
     *
     * @param itemCollect
     */
    private void UserCollect(final TextView itemCollect, final ContentListBean collect, final TextView collecTv) {
        itemCollect.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                //根据id查询是否已经收藏
                CollectListBean collects = DataSupport.where("dongtuId=?", collect.getDongtuId()).findFirst(CollectListBean.class);
                if (collects != null) {//说明已经收藏了
                    ToastUtils.showCenterToast("已经收藏");
                } else {//没有收藏
                    CollectSuccess(collect, collecTv);//去收藏
                }
            }
        });
    }

    /**
     * 收藏成功
     *
     * @param collect
     * @param collecTv
     */
    private void CollectSuccess(ContentListBean collect, TextView collecTv) {
        CollectListBean collecBean = new CollectListBean();
//        collecBean.setTitleType(mCategory);
        collecBean.setId(collect.getId());
        collecBean.setTitle(collect.getTitle());
        collecBean.setLastupdate(collect.getLastupdate());
        collecBean.setLastcomment(collect.getLastcomment());
        collecBean.setCommentnum(collect.getCommentnum());
        collecBean.setGoodnum(collect.getGoodnum());
        collecBean.setBadnum(collect.getBadnum());
        collecBean.setFavnum(collect.getFavnum());
        collecBean.setDownnum(collect.getDownnum());
        collecBean.setDongtuId(collect.getDongtuId());
        collecBean.setV_aimurl(collect.getV_aimurl());
        collecBean.setV_oss_aimurl(collect.getV_oss_aimurl());
        collecBean.setV_width(collect.getV_width());
        collecBean.setV_height(collect.getV_height());
        collecBean.setPic_url(collect.getPic_url());
        collecBean.setPic_width(collect.getPic_width());
        collecBean.setPic_height(collect.getPic_height());
        collecBean.setAd_url(collect.getAd_url());
        collecBean.setIs_ad(collect.getIs_ad());
        String id = collect.getDongtuId();//拿到数据的id
        Logger.i("=======[保存数据库 中收藏 title]===========" + collect.getTitle());
        DatabaseUtils.selectCollectId(id);//查找此数据 如果有就删除了 说明是重数据
        boolean save = collecBean.save();//如果保存成功就改变状态
        if (save) {
            StateUtils.setDrawbleAndText(mActivity, collecTv, R.drawable.shoucang, R.color.video_main);
        } else {
            StateUtils.setDrawbleAndText(mActivity, collecTv, R.drawable.shoucang_hui, R.color.text_remind_gray_light);
        }
        ToastUtils.showCenterToast("收藏成功");
        AddCollectEvent();//添加 收藏成功实践
    }

    /**
     * 点击收藏 发送事件
     */
    private void AddCollectEvent() {
        EventUtil.EnventCollectAdd collectAdd = new EventUtil.EnventCollectAdd();
        EventBus.getDefault().post(collectAdd);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBus.getDefault().unregister(this);
    }

    /**
     * 收藏成功添加 不管是添加还是删除 这边直接掉刷新就可以了
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void collectAdd(EventUtil.EnventCollectCancel event) {
//        mPtrFrameLayout_public.autoRefresh(true);
        Logger.i("cq=============[是添加还是删除 这边直接掉刷新就可以了]===========");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}
