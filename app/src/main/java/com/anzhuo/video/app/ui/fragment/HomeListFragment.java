package com.anzhuo.video.app.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.constant.LoginConfig;
import com.anzhuo.video.app.manager.ActivityMy;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.CollectListBean;
import com.anzhuo.video.app.model.bean.ContentListBean;
import com.anzhuo.video.app.model.bean.UserClickBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.ui.adapter.pub.PublicUtil;
import com.anzhuo.video.app.ui.base.BaseFragment;
import com.anzhuo.video.app.utils.DataBase.DatabaseUtils;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.FrescoUtils.FrescoUtil;
import com.anzhuo.video.app.utils.NoDoubleClickListener;
import com.anzhuo.video.app.utils.RecycleViewVideoUtil;
import com.anzhuo.video.app.utils.StateUtils;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.utils.commentutil.TempTimeUtil;
import com.anzhuo.video.app.widget.JCVideoPlayerStandardFresco;
import com.anzhuo.video.app.widget.ShowPopuWindows;
import com.anzhuo.video.app.widget.StateView;
import com.anzhuo.video.app.widget.StateViewOnclickCallback;
import com.anzhuo.video.app.widget.ViewUtil;
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

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import okhttp3.Call;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_AUTO_COMPLETE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_ERROR;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_NORMAL;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PAUSE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.WIFI_TIP_DIALOG_SHOWED;


public class HomeListFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mCategory;
    private String mCategoryid; //暂时没有用 留着
    private LuRecyclerView mRecy_view;
    private List<ContentListBean> mMList;
    private LuRecyclerViewAdapter myAdapter;
    private CommonAdapter<ContentListBean> mCommonAdapter;
    private PtrClassicFrameLayout mPtrFrameLayout_public;

    private ImageView mFab_refresh;
    int page = 0;
    private RelativeLayout mData_gen;
    private StateView mStateView;

    ContentListBean contentListBeas2;
    private ShowPopuWindows mPopuWindows;

    int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private List<CollectListBean> mCollectList;
    private PopupWindow mVipNoticePop;
    private TextView mPhone, mCode, mGetCode;
    private TempTimeUtil mTimeUtil;
    private JCVideoPlayerStandardFresco mJcVideoPlayerStandard;
    private Animation operatingAnim;
//    public   int firstItemPosition = 0;
//    public  int lastItemPosition = 0;
//    public  int currentItemPosition = 0;
//    public  LinearLayoutManager linearManager;

    public HomeListFragment() {
    }

    public static HomeListFragment newInstance(String param1) {
        HomeListFragment fragment = new HomeListFragment();
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
            Logger.i("=======[数据名字]===========" + mCategory + "===");
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
        return R.layout.fragment_home_list;
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
        mData_gen = ViewUtil.findView(mRootView, R.id.data_gen);
        mFab_refresh.setOnClickListener(this);
        mRecy_view = ViewUtil.findView(mRootView, R.id.rv_publics);
        mPtrFrameLayout_public = ViewUtil.findView(mRootView, R.id.ptrFrameLayout_public);

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecy_view.setLayoutManager(mLayoutManager);
//        mRecy_view.setItemAnimator(new DefaultItemAnimator()); //使用默认的动画
        initPtrFrameLayout(); //初始化头部
        mRecy_view.getItemAnimator().setChangeDuration(0);
        RecycleViewVideoUtil.addOnChildAttachStateChangeListener(mRecy_view);
//        mRecy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (linearManager==null){
//                    return;
//                }
//                //查找最后一个可见的item的position
//                int lastItemPositioned = linearManager.findLastVisibleItemPosition();
//                //查找第一个可见的item的position
//                int firstItemPositioned =linearManager.findFirstVisibleItemPosition();
////                Logger.e("---------firstItemPosition--------     "+firstItemPosition);
////                Logger.e("---------lastItemPosition--------     "+lastItemPosition);
////                Logger.e("---------firstItemPositioned--------     "+firstItemPositioned);
////                Logger.e("---------lastItemPositioned--------     "+lastItemPositioned);
////                Logger.e("---------currentItemPosition--------     "+currentItemPosition);
//                if (dy>0){
//                    if (firstItemPositioned>firstItemPosition&&currentItemPosition<firstItemPositioned){
//                        try {
//                            if (JCVideoPlayerManager.getCurrentJcvd() != null) {
//                                JCVideoPlayer videoPlayer = JCVideoPlayerManager.getCurrentJcvd();
//                                if ((videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING ||
//                                        videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING)) {
//                                    JCVideoPlayer.releaseAllVideos();
//                                    Logger.e("------dy>0---停止播放--------     ");
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        firstItemPosition = firstItemPositioned;
//                    }else {
//                        firstItemPosition = firstItemPositioned;
//                    }
//
//                }else {
//                    if (lastItemPositioned<lastItemPosition&&currentItemPosition>lastItemPositioned){
//                        try {
//                            if (JCVideoPlayerManager.getCurrentJcvd() != null) {
//                                JCVideoPlayer videoPlayer = JCVideoPlayerManager.getCurrentJcvd();
//                                if ((videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING ||
//                                        videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING)) {
//                                    JCVideoPlayer.releaseAllVideos();
//                                    Logger.e("-------dy<0--停止播放--------    ");
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        lastItemPosition = lastItemPositioned;
//                    }else {
//                        lastItemPosition = lastItemPositioned;
//                    }
//
//
//                }
//
//
//            }
//        });


        mCommonAdapter = new CommonAdapter<ContentListBean>(mActivity, R.layout.item_joke_nine_img, mMList) {
            @Override
            protected void convert(ViewHolder holder, ContentListBean contentListBean, int position) {
                try {
                    contentListBeas2 = null;
                    contentListBeas2 = contentListBean;
                    //  contentListBean = mMList.get(holder.getAdapterPosition());
//                    if (position == 0) {
//                        contentListBean.setIs_ad("2");
//                    } else if (position == 1) {
//                        contentListBean.setIs_ad("1");
//                        contentListBean.setAd_url("http://www.baidu.com/");
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
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {

                Logger.e("------position-------"+position);

                if (mMList.get(position).getIs_ad().equals("1")) {//跳转网页
                    SkipX5ADetails(mMList.get(position));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) mMList);
                    bundle.putString("position", String.valueOf(position));
                    bundle.putString("Category", mCategory);//栏目名称了
                    bundle.putString("page", String.valueOf(page));
                    bundle.putBoolean("isSerach", false); //是否为收索
                    Logger.i("=======[baseHttpHashMap ced]===========" + page);
                    ActivityMy.startDetailsActivity(mActivity, bundle);
                }
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
    private void setData(ViewHolder holder, ContentListBean contentListBean, final int position) {
        Logger.i("=======[contentListBean]===========" + contentListBean.toString());
        TextView mTitleContent = holder.getView(R.id.tv_item_joke_content2);
        JCVideoPlayerStandardFresco jcVideoPlayerStandard = holder.getView(R.id.jc_video);
        TextView mTv_item_joke_comment = holder.getView(R.id.tv_item_joke_comment);
        TextView mItemZan = holder.getView(R.id.tv_item_joke_zan);
        TextView mItemCollect = holder.getView(R.id.tv_item_collect);//收藏
        TextView mItemShare = holder.getView(R.id.tv_item_joke_share);
        ImageView mItemVip = holder.getView(R.id.item_is_vip);
        RelativeLayout mVLayout = holder.getView(R.id.jc_video_layout);

        //设置数据
        setStateNum(contentListBean, position, mTitleContent, jcVideoPlayerStandard, mTv_item_joke_comment, mItemZan, mItemCollect, mItemShare);
        /*点赞*/
        UserClickList(mActivity, mItemZan, contentListBean, position, true);
        /*点踩*/
        //  UserClickList(mActivity, mItemCollect, contentListBean, position, false);
        /*分享*/
        UserShare(mItemShare, contentListBean);
        UserCollect(mItemCollect, contentListBean, mItemCollect);//收藏
        VideoIsVIP(mItemVip, contentListBean);//vip
    }

    /**
     * vip 是否为视频
     *
     * @param
     * @param collectListBean
     */
    private boolean isVipVideo(ContentListBean collectListBean) {
        boolean isVipVideo = false;
        String sPrice = collectListBean.getPrice();
        double mPrice = 0.0;
        if (sPrice != null && !TextUtils.isEmpty(sPrice)) {
            mPrice = Double.valueOf(sPrice);
            if (mPrice > 0) {
                isVipVideo = true;
            }
        }
        return isVipVideo;
    }

    /**
     * vip 是否为视频
     *
     * @param imageView
     * @param collectListBean
     */
    private void VideoIsVIP(ImageView imageView, ContentListBean collectListBean) {

        boolean isVipV = isVipVideo(collectListBean);
        if (isVipV) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
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
                    ToastUtil.showCenterToast("已经收藏");
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
        collecBean.setTitleType(mCategory);
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
        collecBean.setPrice(collect.getPrice());
        String id = collect.getDongtuId();//拿到数据的id
        Logger.i("=======[保存数据库 中收藏 title]===========" + collect.getTitle());
        DatabaseUtils.selectCollectId(id);//查找此数据 如果有就删除了 说明是重数据
        boolean save = collecBean.save();//如果保存成功就改变状态
        if (save) {
            StateUtils.setDrawbleAndText(mActivity, collecTv, R.drawable.shoucang, R.color.video_main);
        } else {
            StateUtils.setDrawbleAndText(mActivity, collecTv, R.drawable.shoucang_hui, R.color.text_remind_gray_light);
        }
        ToastUtil.showCenterToast("收藏成功");

//        AddCollectEvent();//添加 收藏成功实践
    }

    /**
     * 点击收藏 发送事件
     */
    private void AddCollectEvent() {
        EventUtil.EnventCollectAdd collectAdd = new EventUtil.EnventCollectAdd();
        EventBus.getDefault().post(collectAdd);
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
        //根据videoid 从数据库中查找 然后设置点赞踩状态
        String dongtuId = contentListBean.getDongtuId();
        List<UserClickBean> states = new ArrayList<UserClickBean>();
        states.addAll(DataSupport.where("dongtuId=?", dongtuId).find(UserClickBean.class));

        mCollectList = new ArrayList<>();
        List<CollectListBean> all = DataSupport.order("id desc").find(CollectListBean.class);
        mCollectList.addAll(all);

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
        //以下是视频部分.
        Logger.i("tagID= " + contentListBean.getDongtuId());
        jcVideoPlayerStandard.setTag(contentListBean.getDongtuId());
        if (!jcVideoPlayerStandard.getTag().equals(contentListBean.getDongtuId())) {
            return;
        }
        String IsAd = contentListBean.getIs_ad().trim();
        Logger.i("IsAd= " + IsAd + "  position= " + position + "  标题= " + contentListBean.getTitle() + " 是否VIP price= " + contentListBean.getPrice());
        PublicUtil.LoadVideos(getContext(), jcVideoPlayerStandard,
                picwidth, picheight, contentListBean.getV_aimurl(), contentListBean.getPic_url(), false,
                contentListBean, contentListBean.getTitle());
        if (IsAd.equals("1")) {//说明是要放广告
            //视频处理
            jcVideoPlayerStandard.thumbImageView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    SkipX5ADetails(contentListBean);
                }
            });
            jcVideoPlayerStandard.startButton.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    SkipX5ADetails(contentListBean);
                }
            });
        } else {
            //视频处理
            jcVideoPlayerStandard.thumbImageView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
//                    currentItemPosition = position;
                    RecycleViewVideoUtil.setCurrentItemPosition(position);
                    if (isVipVideo(contentListBean)) {
                        mJcVideoPlayerStandard = jcVideoPlayerStandard;
                        PostLoginIsFromCenter(LoginConfig.sf_getLoginState());
//                        showVipNoticePop(getActivity(),getLayoutRes(),"提示","本视频仅供会员观看","取消","登录");
                    } else {
                        Logger.i("视频当前状态 = " + jcVideoPlayerStandard.currentState);
                        startVideo(jcVideoPlayerStandard);
                    }


                }
            });
            jcVideoPlayerStandard.startButton.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
//                    jcVideoPlayerStandard.startVideo();
//                    currentItemPosition = position;
                    RecycleViewVideoUtil.setCurrentItemPosition(position);
                    if (isVipVideo(contentListBean)) {
                        mJcVideoPlayerStandard = jcVideoPlayerStandard;
                        PostLoginIsFromCenter(LoginConfig.sf_getLoginState());
//                        showVipNoticePop(getActivity(),getLayoutRes(),"提示","本视频仅供会员观看","取消","登录");
                    } else {
                        Logger.i("视频当前状态 = " + jcVideoPlayerStandard.currentState);
                        startVideo(jcVideoPlayerStandard);
                    }

                }
            });
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
                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
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
     * 首页确认vip 身份后 这里播放视频
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getVideoPlay(EventUtil.EnventToPlay event){
        Logger.e("------vip会员播放视频---获取推送-");
        if (mJcVideoPlayerStandard!=null)
        startVideo(mJcVideoPlayerStandard);


    }

    private void PostLoginIsFromCenter(boolean b) {
        EventUtil.EnventLogin enventLogin = new EventUtil.EnventLogin();
        enventLogin.toLogin = b;
        Logger.e("------HomeList---推送-");
        EventBus.getDefault().post(enventLogin);
    }


    /**
     * 内容 跳转至广告
     *
     * @param contentListBean
     */
    private void SkipX5ADetails(ContentListBean contentListBean) {
        ActivityMy.skipX5Details(mActivity, contentListBean.getTitle(), contentListBean.getAd_url());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.i("=======[setUserVisibleHint]===========" + isVisibleToUser);
        if (isVisibleToUser) {
        } else {
            JCVideoPlayer.releaseAllVideos();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            List<CollectListBean> contentListBeen = new ArrayList<>();
            List<CollectListBean> all = DataSupport.order("id desc").find(CollectListBean.class);
            contentListBeen.addAll(all);
            if (contentListBeen != null && mCollectList != null) {
                if (contentListBeen.size() > mCollectList.size()) {
                    myAdapter.notifyDataSetChanged();
                    Logger.i("=======[setUserVisibleHint 刷新]===========");
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
//                            ToastUtil.showCenterToast(getResources().getString(R.string.share_success));
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
                                ToastUtil.showCenterToast(mActivity.getString(R.string.zanorun));
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
                                        ToastUtil.showCenterToast(mActivity.getString(R.string.zanorun));
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    ToastUtil.showCenterToast(mActivity.getString(R.string.zanorun));
                                }
                            }
                        });
                    } else {
                        ToastUtil.showCenterToast(R.string.check_net_is_connect);
                    }

                } else {
                    Logger.i("=======[点赞已经有了]===========" + zans.get(0).toString());
                    boolean zan = zans.get(0).getZan();
                    boolean cai = zans.get(0).getCai();
                    if (zan) {
                        ToastUtil.showCenterToast("已经赞了");
                    }
                    if (cai) {
                        ToastUtil.showCenterToast("已经踩了");
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
        //刷新
        mPtrFrameLayout_public.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }


            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {//刷新
                BeginAnimation(mFab_refresh);
                JCVideoPlayer.releaseAllVideos();
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

    int positions = 0;

    /**
     * 加载更多
     */
    private void initLoadMore() {
        mRecy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Logger.i("=======[滑动 状态]===========" + newState);
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
                LinearLayoutManager layoutManagers = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManagers.findLastVisibleItemPosition();
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
                int i = page * Integer.valueOf(Constants.LIMIT);
                if (myAdapter.getItemCount() + 1 < i) {
                    setFooterViewState(FooterViewTheEnd);
                    Logger.i("=======[走的这里哦]===========" + lastVisibleItem + 1);
                    Logger.i("=======[走的这里哦]===========" + i);
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
        Logger.i("=======[首页请求数据 page]===========" + page);
        if (XUtilNet.isNetConnected()) {
            // 栏目id 第二个参数 分页的时候传
            HomeModel.getMainList(category, id, pages, new NewInterface() {
                @Override
                public void onError(Call call, Exception e) {
                    ToastUtil.showCenterToast("加载失败");
                    loadFail();  // 没有网络 加载失败
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                    try {
                        List<ContentListBean> requestList = new ArrayList<>();
                        mPtrFrameLayout_public.refreshComplete();
                        Logger.i("=======[data主页]===========" + data.toString());
                        if (state == 200) {

                            mStateView.showSuccess();
                            // 如果没有数据是 [] 两个字符 大于两个字符 有数据
                            if (data.length() > 2) {
                                requestList.addAll(JSON.parseArray(data, ContentListBean.class));
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
                                ToastUtil.showCenterToast(R.string.no_more);
                                //说明数据是空的
                                setFooterViewState(FooterViewTheEnd);
                            }
                            myAdapter.notifyDataSetChanged();
                            endAnimation(mFab_refresh);
                            //--------------------------------
//                            currentItemPosition = 0;
//                            linearManager = (LinearLayoutManager) mRecy_view.getLayoutManager();
//                            if (linearManager!=null){
//                                //查找最后一个可见的item的position
//                                lastItemPosition = linearManager.findLastVisibleItemPosition();
//                                //查找第一个可见的item的position
//                                firstItemPosition =linearManager.findFirstVisibleItemPosition();
//                            }

                            //--------------------------------
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //解析失败
                        ToastUtil.showCenterToast("解析失败");
                        if (mMList.size() == 0) {
                            loadLocaData();//加载本地数据
                        } else {
                            setFooterViewState(FooterViewNetWorkError);//网络错误
                        }
                    }
                }
            });
        } else {//没有网络
            ToastUtil.showCenterToast(R.string.check_net_is_connect);
            loadFail();//没有网络
        }
    }

    int pageLoca = 0;

    /**
     * 没有网络 加载失败
     */
    private void loadFail() {
        page = pageLoca;
        Logger.i("=======[pageLoca = page]===========" + pageLoca + "==============" + page);
        Logger.i("=======[pageLoca = page]===========" + mMList.size());
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
            List<ContentListBean> contentListBeen = new ArrayList<>();
            contentListBeen.addAll(DataSupport.where("titleType=?", mCategory).order("id desc").find(ContentListBean.class));
            if (contentListBeen.size() != 0) {
                mMList.addAll(contentListBeen);
                mStateView.showSuccess();
                myAdapter.notifyDataSetChanged();
            } else {
                mStateView.showNoNet();
            }
        } catch (Exception e) {
            Logger.i("=======[pageLoca = page出错了]===========");
            e.printStackTrace();
        }
    }
//    }

    /**
     * 保存首页数据至数据库
     *
     * @param requestList
     */

    private void saveData(final List<ContentListBean> requestList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.i("=======[保存数据库size]===========" + requestList.size());
                for (int i = 0; i < requestList.size(); i++) {
                    ContentListBean contentListBean = requestList.get(i);
                    contentListBean.setTitleType(mCategory);
                    String id = contentListBean.getId();//拿到数据的id
                    Logger.i("=======[保存数据库中 title]===========" + contentListBean.getTitle());
                    DatabaseUtils.selectDongtuId(id);//查找此数据 如果有就删除了 说明是重数据
                }
                DataSupport.saveAll(requestList);
                Logger.i("=======[保存数据]===========" + DataSupport.findAll(ContentListBean.class));
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
//        BeginAnimation(mFab_refresh);
        if (mPtrFrameLayout_public.isRefreshing()) {
            return;
        }
        mPtrFrameLayout_public.autoRefresh(true);
        mRecy_view.scrollToPosition(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_refresh:
                refresh();
                break;
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
        if (mJcVideoPlayerStandard!=null){
            mJcVideoPlayerStandard.release();
            mJcVideoPlayerStandard=null;
        }
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
        Logger.i("=======[接收事件 位置]===========" + position);
        boolean equals = mCategory.equals(event.titleTypes);
        Logger.i("=======[接收事件 类型是否相等]===========" + equals);
        if (equals) {
            String isType = event.isType;
            ContentListBean bean;
            Logger.i("=======[接收事件 神马类型]===========" + isType);
            if (slidePosition != Integer.MAX_VALUE) {//不等于最大的这个默认值 说明位置改变了
                bean = mMList.get(slidePosition);
            } else {
                bean = mMList.get(position);
            }
            switch (isType) {
                case Constants.getZan:
                    int goodnums = Integer.parseInt(bean.getGoodnum()) + 1;
                    bean.setGoodnum(String.valueOf(goodnums));
                    break;
                case Constants.getCai:
                    int badnum = Integer.parseInt(bean.getBadnum()) + 1;
                    bean.setBadnum(String.valueOf(badnum));
                    break;
                case Constants.getShare:
                    int favnum = Integer.parseInt(bean.getFavnum()) + 1;
                    bean.setFavnum(String.valueOf(favnum));
                    break;
                case Constants.getComment:
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
            Logger.i("=======[返回的时候 详情的位置]===========" + slidePosition);
            slidePosition = toPosition;//滑动的位置
            Logger.i("=======[返回的时候 详情的位置]===========" + toPosition);
            mRecy_view.smoothScrollToPosition(toPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收藏取消
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void collectCancel(EventUtil.EnventCollectCancel event) {
        Logger.i("=======[返回的时候 详情的位置 数据改变了没有]===========");
//        myAdapter.notifyDataSetChanged();mCommonAdapter
        mCommonAdapter.notifyDataSetChanged();
    }

    /**
     *
     *如果本身在首页界面  再次点击首页时刷新
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tabRefesh(EventUtil.EnventRefesh event) {
        Logger.e("---------接收发射值--------");
        refresh();
    }

    /**
     * 刷新圈圈旋转动画 begin
     * @param view
     */
    private void BeginAnimation(ImageView view){
        if (operatingAnim==null){
            operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
            LinearInterpolator lin = new LinearInterpolator();
//            AccelerateInterpolator acc = new AccelerateInterpolator(0.5f);
            operatingAnim.setInterpolator(lin);
        }
        if (view!=null)
        view.startAnimation(operatingAnim);


    }
    /**
     * 刷新圈圈旋转动画 end
     * @param view
     */
    private void endAnimation(View view){
        if (operatingAnim!=null && view!=null){
            view.clearAnimation();
        }

    }

}
