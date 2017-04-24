package com.anzhuo.video.app.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.constant.LoginConfig;
import com.anzhuo.video.app.manager.ActivityMy;
import com.anzhuo.video.app.manager.NIManage;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.CheckVipBean;
import com.anzhuo.video.app.model.bean.CollectListBean;
import com.anzhuo.video.app.model.bean.CommentBean;
import com.anzhuo.video.app.model.bean.ContentListBean;
import com.anzhuo.video.app.model.bean.LoginData;
import com.anzhuo.video.app.model.bean.UserClickBean;
import com.anzhuo.video.app.model.bean.VersionStartBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.ui.adapter.RefreshAdapter;
import com.anzhuo.video.app.ui.adapter.pub.PublicUtil;
import com.anzhuo.video.app.ui.base.BaseFragment;
import com.anzhuo.video.app.utils.DataBase.DatabaseUtils;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.FrescoUtils.FrescoUtil;
import com.anzhuo.video.app.utils.NoDoubleClickListener;
import com.anzhuo.video.app.utils.SharedUtil;
import com.anzhuo.video.app.utils.StateUtils;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.utils.commentutil.TempRegexUtil;
import com.anzhuo.video.app.utils.commentutil.TempTimeUtil;
import com.anzhuo.video.app.widget.JCVideoPlayerStandardFresco;
import com.anzhuo.video.app.widget.MyHeaderAndFooterWrapper;
import com.anzhuo.video.app.widget.ShowPopuWindows;
import com.anzhuo.video.app.widget.StateView;
import com.anzhuo.video.app.widget.StateViewOnclickCallback;
import com.anzhuo.video.app.widget.ViewUtil;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.orhanobut.logger.Logger;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;

import static com.anzhuo.fulishipin.app.R.attr.position;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_AUTO_COMPLETE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_ERROR;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_NORMAL;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PAUSE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.WIFI_TIP_DIALOG_SHOWED;
import static org.litepal.crud.DataSupport.where;


public class DetailFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private static final String ARG_PARAM9 = "param9";
    private static final String ARG_PARAM10 = "param10";
    private static final String ARG_PARAM11 = "param11";
    private static final String ARG_PARAM12 = "param12";
    private static final String ARG_PARAM13 = "param13";//是否为收索
    int page = 0;
    int lastVisibleItem;

    private RecyclerView mRecy_view;
    private List<CommentBean> mMList;

    private String mFavnum1;
    private String mBadnum1;
    private String mGoodnum1;
    private String mPicture1;
    private String mTitle1;

    private String mId;
    private String mCategory;
    private String mPositions;
    private ShowPopuWindows mPopuWindows;


    private StateView mStateView;
    private LinearLayout mRv_gen;
    private LinearLayout mRv_gen2;
    private View mHeadView;
    private RefreshAdapter mRefreshComment;
    MyHeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private StateView mStateView2;
    private String mCommentNum;
    //    private InformationBannerView mInformationBannerView;
    private String mHeight;
    private String mWidth;
    private ContentListBean mMContentBean;
    private TextView mComments_add;
    private EditText mComments_edit;
    private LayoutInflater inflater;
    private TextView mComment_loding;
    private SimpleDraweeView mMImageView;
    private VersionStartBean mVersionStartBean;
    private boolean mIsSearch;
    private PopupWindow mLoginPop;
    private TextView mPhone, mCode, mGetCode;
    private TempTimeUtil mTimeUtil;
    private String mGoodsId;//1 包月 2 包年
    private String mPayId;//1 微信 2 支付宝
    //    private TextView mTv_no_net_tip;

    private TimerTask mAdTask;
    //广告相关
    private com.qq.e.ads.interstitial.InterstitialAD iad;
    private com.baidu.mobads.InterstitialAd interAd;

    private String gdtStatus = "1";
    private String bdStatus = "0";
    private String m9500Status = "0";
    private JCVideoPlayerStandardFresco jcVideoPlayerStandard;

    public static DetailFragment newInstance(ContentListBean bean, String Category, int position, boolean isSearch) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, bean.getTitle());
        args.putString(ARG_PARAM2, bean.getV_aimurl());
        args.putString(ARG_PARAM3, bean.getGoodnum());
        args.putString(ARG_PARAM4, bean.getBadnum());
        args.putString(ARG_PARAM5, bean.getFavnum());
        args.putString(ARG_PARAM6, bean.getDongtuId());
        args.putString(ARG_PARAM7, Category);
        args.putString(ARG_PARAM8, String.valueOf(position));
        args.putString(ARG_PARAM9, bean.getCommentnum());
        args.putString(ARG_PARAM10, bean.getPic_height());
        args.putString(ARG_PARAM11, bean.getPic_width());
        args.putSerializable(ARG_PARAM12, bean);
        args.putBoolean(ARG_PARAM13, isSearch);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_detail_a;
    }


    @Override
    public void bindViews() {
        try {
            EventBus.getDefault().register(this);
            Bundle arguments = getArguments();
            if (arguments != null) {
                mTitle1 = arguments.getString(ARG_PARAM1);
                mPicture1 = arguments.getString(ARG_PARAM2);
                mGoodnum1 = arguments.getString(ARG_PARAM3);
                mBadnum1 = arguments.getString(ARG_PARAM4);
                mFavnum1 = arguments.getString(ARG_PARAM5);
                mId = arguments.getString(ARG_PARAM6);
                mCategory = arguments.getString(ARG_PARAM7);
                mPositions = arguments.getString(ARG_PARAM8);
                mCommentNum = arguments.getString(ARG_PARAM9);
                mHeight = arguments.getString(ARG_PARAM10);
                mWidth = arguments.getString(ARG_PARAM11);
                mIsSearch = arguments.getBoolean(ARG_PARAM13);//是否为收索
                mMContentBean = (ContentListBean) arguments.getSerializable(ARG_PARAM12);
                Logger.i("=======[接收事件 位置 开始]===========" + mPositions);
            }

            inflater = LayoutInflater.from(mActivity);
            mMList = new ArrayList();
            //发表 评论框
            mComments_add = ViewUtil.findView(mRootView, R.id.comments_add);
            mComments_edit = ViewUtil.findView(mRootView, R.id.comments_edit);
//            mTv_no_net_tip = ViewUtil.findView(mRootView, R.id.tv_no_net_tip);//网络提示
//            initNetView();//初始化网络提醒框

            mRecy_view = ViewUtil.findView(mRootView, R.id.rv_recyview);
            mRv_gen = ViewUtil.findView(mRootView, R.id.rv_gen);
            mRv_gen2 = ViewUtil.findView(mRootView, R.id.rv_gen2);
            mStateView = new StateView(mActivity)
                    .setParentConfig(mRv_gen, mRv_gen2)
                    .setOnclick(null)
                    .showLoading();
            mRecy_view.setLayoutManager(new LinearLayoutManager(mActivity));
            //评论的内容
            mRefreshComment = new RefreshAdapter(mActivity, mMList, mId);
            mHeadView = inflater.inflate(R.layout.detail_header, null);//头部的布局
            mHeaderAndFooterWrapper = new MyHeaderAndFooterWrapper(mRefreshComment);
            mHeaderAndFooterWrapper.addHeaderView(mHeadView);
            mRecy_view.setAdapter(mHeaderAndFooterWrapper);
            setHeader(mHeadView);//设置头部的数据
            page = page + 1;
            getData();
            initLoadMoreListener();
            mRv_gen.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    Logger.i("=======[点击了]==========");
                    DisplayUtil.hideKeyboard(mActivity);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化网络条
     * 有网隐藏、无网显示
     */
//    private void initNetView() {
//        if (XUtilNet.isNetConnected()) {
//            mTv_no_net_tip.setVisibility(View.GONE);
//        } else {
//            mTv_no_net_tip.setVisibility(View.VISIBLE);
//        }
//    }

    /**
     * 加载更多
     */
    private void initLoadMoreListener() {
        mRecy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Logger.i("=======[没有更多的数据了 前]===========" + lastVisibleItem);
                Logger.i("=======[没有更多的数据了 前2]===========" + mRefreshComment.getItemCount());
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int i2 = page * 20;
                if (mRefreshComment.getItemCount() + 1 < i2) {
                    Logger.i("=======[1 刷新这里的]===========");
                    mRefreshComment.changeMoreStatus(mRefreshComment.NO_LOAD_MORE);
                    return;
                }
//                }
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int i = page * 20;
                    Logger.i("=======[没有更多的数据了 中]===========" + (lastVisibleItem + 1));
                    if (lastVisibleItem < i) {
                        Logger.i("=======[没有更多的数据了2 刷新这里的]===========");
                        mRefreshComment.changeMoreStatus(mRefreshComment.NO_LOAD_MORE);
                        return;
                    } else {
                        page = page + 1;
                        //设置正在加载更多
                        mRefreshComment.changeMoreStatus(mRefreshComment.LOADING_MORE);
                        getData();
                    }
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

    }

    ArrayList<CommentBean> listSize = new ArrayList<>();

    /**
     * 请求评论数据
     */
    private void getData() {
        try {
            if (XUtilNet.isNetConnected()) {
                HomeModel.getDetailcomment(mId, String.valueOf(page), new NewInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Logger.i("=======[请求失败]===========");
                        mRefreshComment.changeMoreStatus(RefreshAdapter.NO_LOAD_ERRO);
                        mStateView.showSuccess();
                        if (mComment_loding != null) {
                            mComment_loding.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        Logger.i("=======[请求成功 评论列表 2]===========" + mTitle1 + data);
                        if (state == 200) {
                            if (mComment_loding != null) {
                                mComment_loding.setVisibility(View.GONE);
                            }
                            mStateView.showSuccess();
                            if (data.length() > 2) {
                                mRefreshComment.changeMoreStatus(RefreshAdapter.NO_LOAD_MORE);
                                List<CommentBean> mCommentBean = JSON.parseArray(data, CommentBean.class);
                                //不为空
                                if (mCommentBean != null) {
//                                setFooterViewState(FooterViewTheEnd);
//                                mMList.addAll(mCommentBean);
                                    if (page == 1) {
                                        listSize.clear();
                                    }
                                    listSize.addAll(mCommentBean);
                                    //动态设置高度 不让至显示第一页
                                    ViewGroup.LayoutParams layoutParams = mRecy_view.getLayoutParams();
                                    layoutParams.height = 100 * listSize.size();
                                    mRecy_view.setLayoutParams(layoutParams);
                                    mRefreshComment.AddFooterItem(mCommentBean);
                                } else {//为空
//                                mStateView.showSuccess();
                                    NoData();
                                }
                            } else {
                                //位空
//                            mStateView.showSuccess();
                                NoData();//没有数据的时候
                            }
                        } else {
                            mRefreshComment.changeMoreStatus(RefreshAdapter.NO_LOAD_ERRO);
                        }

                    }
                });
            } else {
                if (mStateView != null) {
                    mStateView.showSuccess();
                }
                NoData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mStateView != null) {
                mStateView.showSuccess();
            }
        }
    }

    /**
     * 没有数据 添加footview
     */
    private void NoData() {
        if (mComment_loding != null) {
            mComment_loding.setVisibility(View.GONE);
        }
        if (listSize.size() == 0) {
            Logger.i("=======[没有网络的时候来这里没有]===========");
            setEmptyView();//数据为空的时候
        } else {
            mRefreshComment.changeMoreStatus(RefreshAdapter.NO_LOAD_MORE);
            return;
        }
    }

    /**
     * 添加footview
     */
    private void setEmptyView() {
        if (mStateView2 == null) {
            mStateView2 = new StateView(mActivity).setOnclick(new StateViewOnclickCallback() {
                @Override
                public void NoNetClick() {//没有网络
                    Logger.i("=======[添加footview1]===========");
                }

                @Override
                public void NoDataClick() {//没有数据
                    Logger.i("=======[添加footview12]===========");
                    DisplayUtil.hideKeyboard(mActivity);
                }

                @Override
                public void NoLoginClick() {//没有登录
                    Logger.i("=======[添加footview13]===========");

                }

                @Override
                public void CustomClick() {//自定义
                    Logger.i("=======[添加footview14]===========");

                }
            });
        }
        mStateView2.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                Logger.i("=======[添加footview1 点击了4]===========");
                DisplayUtil.hideKeyboard(mActivity);
            }
        });
        mStateView2.showNoData(getResources().getString(R.string.detail_no_comment), R.drawable.comment_empty);
        mHeaderAndFooterWrapper.addFootView(mStateView2);
    }

    /**
     * 移除footView
     */
    private void removeFootView() {
        mHeaderAndFooterWrapper.removeFooterView();
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 设置头部
     *
     * @param headeView
     */
    private void setHeader(View headeView) {
        mStateView.showSuccess();
        headeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DisplayUtil.hideKeyboard(mActivity);
                return false;
            }
        });
        headeView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                DisplayUtil.hideKeyboard(mActivity);
            }
        });

        ImageView mItemVip = (ImageView) headeView.findViewById(R.id.item_is_vip);

        //图片 gif
        mMImageView = (SimpleDraweeView) headeView.findViewById(R.id.my_image_view);

        TextView mTitleContent = (TextView) headeView.findViewById(R.id.tv_item_joke_content);

        //jc_video
//        final SimpleDraweeView mImageView = (SimpleDraweeView) headeView.findViewById(R.id.my_image_view);//图片 gif
//        final SimpleDraweeView jcVideoPlayerStandard = (SimpleDraweeView) headeView.findViewById(R.id.jc_video);//图片 gif
        jcVideoPlayerStandard = (JCVideoPlayerStandardFresco) headeView.findViewById(R.id.jc_video);

        //gif标识
//        final ImageView mGifTips = (ImageView) headeView.findViewById(R.id.img_item_gif_tips);
//        mGifTips.setVisibility(View.VISIBLE);
        TextView mItemZan = (TextView) headeView.findViewById(R.id.tv_item_joke_zan);//赞
        TextView mItemCollect = (TextView) headeView.findViewById(R.id.tv_item_collect);//收藏
        TextView mItemShare = (TextView) headeView.findViewById(R.id.tv_item_joke_share);//分享
        TextView mTv_item_joke_comment = (TextView) headeView.findViewById(R.id.tv_item_joke_comment); //评论
        //评论状态
        mComment_loding = (TextView) headeView.findViewById(R.id.comment_loding);

        // 点击评论弹出软键盘
        mTv_item_joke_comment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                mComments_edit.setFocusable(true);
                mComments_edit.requestFocus();
                DisplayUtil.showKeyboard(mActivity);
            }
        });



           /*点赞*/
        UserClickList(mActivity, mItemZan, mGoodnum1, position, true);
        /*点踩*/
        // UserClickList(mActivity, mItemCollect, mBadnum1, position, false);
        UserCollect(mItemCollect, mMContentBean, mItemCollect);//收藏
        VideoIsVIP(mItemVip, mMContentBean);//vip
        UserShare(mItemShare);//分享
        sendComment(mTv_item_joke_comment, mComments_add, mComments_edit);//发表评论
        String dongtuId = mId;


        CollectListBean collects = DataSupport.where("dongtuId=?", dongtuId).order("id desc").findFirst(CollectListBean.class);
        Logger.i("=======[点击gif4]===========");

        //收藏的状态
        if (collects != null) {
            String collectId = collects.getDongtuId();
            if (collectId.equals(dongtuId)) {
                StateUtils.setDrawbleAndText(mActivity, mItemCollect, R.drawable.shoucang, R.color.video_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemCollect, R.drawable.shoucang_hui, R.color.text_remind_gray_light);
            }
        } else {
            StateUtils.setDrawbleAndText(mActivity, mItemCollect, R.drawable.shoucang_hui, R.color.text_remind_gray_light);
        }


        //用户点击状态的情况
        List<UserClickBean> states = new ArrayList<UserClickBean>();
        states.addAll(where("dongtuId=?", dongtuId).find(UserClickBean.class));
        Logger.i("=======[states]===========" + states.size());
        if (states.size() != 0) {
            UserClickBean clickBean = states.get(0); // 0 根据dongtuId 查询到的只有一条数据
            boolean zan = clickBean.getZan();
            boolean cai = clickBean.getCai();
            if (zan) {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like_selected, R.color.video_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            }
//            if (cai) {
//                StateUtils.setDrawbleAndText(mActivity, mItemCollect, R.drawable.ico_unlike_selected, R.color.video_main);
//            } else {
//                StateUtils.setDrawbleAndText(mActivity, mItemCollect, R.drawable.icon_unlike, R.color.text_remind_gray_light);
//            }
        } else {
            StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            // StateUtils.setDrawbleAndText(mActivity, mItemCollect, R.drawable.icon_unlike, R.color.text_remind_gray_light);
        }
        //设置数字
        mTitleContent.setText(mTitle1);
        mItemZan.setText(mGoodnum1);
//        mItemCollect.setText(mBadnum1);
//        mItemShare.setText(mFavnum1);
        mTv_item_joke_comment.setText(mCommentNum);

        //计算图片缩放后的宽高
        String picwidth = mWidth;
        String picheight = mHeight;
        int width = 0;
        int height = 0;
        if (!TextUtils.isEmpty(picheight) && !TextUtils.isEmpty(picwidth)) {
            width = Integer.parseInt(picwidth);
            height = Integer.parseInt(picheight);
        }
        int pictureWidth = 0;
        int pictureHeight = 0;
        if (width != 0 || height != 0) {
            //计算后的宽高
            pictureWidth = DisplayUtil.CountPictureWidth(); //获取屏幕的宽度 -控件两边的间距
            pictureHeight = (pictureWidth * height);
            pictureHeight = pictureHeight / width;

//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(pictureWidth, pictureHeight);//动态设置宽高
//            jcVideoPlayerStandard.setLayoutParams(layoutParams);
            Logger.i("=======[没有问题了]===========");
        }

        BaseControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                Logger.i("图片下载完成");
//                mGifTips.setVisibility(View.GONE);
            }
        };


        // false 是否显示长图
//        jcVideoPlayerStandard.setHierarchy(FrescoUtil.GetHierarchy(false));  //进度条
//        FrescoUtil.loadGifUrl(mPicture1, mMContentBean.getLit_url(), mImageView, pictureWidth, pictureHeight, true, controllerListener);


        //视频处理
        try {
            //以下是视频部分
            jcVideoPlayerStandard.setTag(mPicture1);
            if (!jcVideoPlayerStandard.getTag().equals(mPicture1)) {
                return;
            }

            if (mMContentBean.getIs_ad().equals("1")) {
                PublicUtil.LoadVideos(getContext(), jcVideoPlayerStandard, mWidth, mHeight, mPicture1,
                        mMContentBean.getPic_url(), true, mMContentBean, mMContentBean.getTitle());
                jcVideoPlayerStandard.thumbImageView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        Logger.i("cq=============[广告地址这是]===========");
                        SkipX5ADetails(mMContentBean);
                    }
                });
                jcVideoPlayerStandard.startButton.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        Logger.i("cq=============[广告地址这是2]===========");
                        SkipX5ADetails(mMContentBean);
                    }
                });

            } else {
                PublicUtil.LoadVideos(getContext(), jcVideoPlayerStandard, mWidth, mHeight, mPicture1,
                        mMContentBean.getPic_url(), true, mMContentBean, mMContentBean.getTitle());
                jcVideoPlayerStandard.thumbImageView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        if (isVipVideo(mMContentBean)) {

                            if (!LoginConfig.sf_getLoginState()) {
                                showLoginPop();
                            } else {//登录后验证是否VIP
                                checkUserVip("", LoginConfig.getUserPhone(), LoginConfig.getUserId(), "10");
                            }

                        } else {
                           startVideo(jcVideoPlayerStandard);
                        }
                    }
                });
                jcVideoPlayerStandard.startButton.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        if (isVipVideo(mMContentBean)) {

                            if (!LoginConfig.sf_getLoginState()) {
                                showLoginPop();
                            } else {//登录后验证是否VIP
                                checkUserVip("", LoginConfig.getUserPhone(), LoginConfig.getUserId(), "10");
                            }

                        } else {
                            startVideo(jcVideoPlayerStandard);
                        }
                    }
                });


            }

            mTitleContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SkipX5ADetails(mMContentBean);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //登录对话框
    private void showLoginPop() {
        //设置contentView
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_login_layout, null);
        mLoginPop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mLoginPop.setContentView(contentView);
        mPhone = (TextView) contentView.findViewById(R.id.login_phone);
        mCode = (TextView) contentView.findViewById(R.id.login_code);
        mGetCode = (TextView) contentView.findViewById(R.id.login_code_btn);
        Button mBtnQuit = (Button) contentView.findViewById(R.id.login_btn_quit);
        Button mBtnLogin = (Button) contentView.findViewById(R.id.login_btn_login);
        ImageView mClose = (ImageView) contentView.findViewById(R.id.dialog_close);
        mGetCode.setOnClickListener(mOnClickListener);
        mBtnQuit.setOnClickListener(mOnClickListener);
        mBtnLogin.setOnClickListener(mOnClickListener);
        mClose.setOnClickListener(mOnClickListener);
        //显示PopupWindow
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        mLoginPop.showAtLocation(rootview, Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_code_btn://获取验证码

                    String phone = mPhone.getText().toString().trim();
                    if (phone != null && !TextUtils.isEmpty(phone) && TempRegexUtil.checkMobile(phone)) {
                        mTimeUtil = new TempTimeUtil(60000, 1000, mGetCode);
                        getMarkMsg("", phone, "10");
                    } else {
                        ToastUtil.showToast("请输入正确的手机号码！");
                    }


                    break;
                case R.id.dialog_close:
                case R.id.login_btn_quit:
                    if (mLoginPop != null && mLoginPop.isShowing()) {
                        mLoginPop.dismiss();
                        mLoginPop = null;
                    }

                    break;
                case R.id.login_btn_login://登录接口

                    String _phone = mPhone.getText().toString().trim();
                    String _code = mCode.getText().toString();
                    if (_phone == null || TextUtils.isEmpty(_phone)) {
                        ToastUtil.showToast("请输入电话号码");
                        return;
                    }
                    if (_code == null || TextUtils.isEmpty(_code)) {
                        ToastUtil.showToast("请输入验证码");
                        return;
                    }
                    Login("", _phone, _code, "10");

                    break;
            }

        }
    };

    /**
     * 登录
     *
     * @param s
     * @param phone
     * @param mark
     * @param reg_site
     */
    private void Login(String s, final String phone, String mark, String reg_site) {

        HomeModel.getLogin(s, phone, mark, reg_site, new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                Logger.i("到这里了state" + state);
                String uid = "";
                List<LoginData> datas = JSON.parseArray(data, LoginData.class);
                if (state == 200) {
                    ToastUtil.showToast("登录成功");
                    LoginConfig.sf_saveLoginState(true);
                    LoginConfig.sf_saveUserPhone(phone);
                    if (datas != null && !datas.isEmpty()) {
                        Logger.e("--------------------------");
                        LoginData loginBean = datas.get(0);
                        uid = loginBean.getUid();
                        LoginConfig.sf_saveUserInfo(loginBean);
                        Logger.e("---------uid-----------------" + loginBean.getUid());
                    }

                    if (mLoginPop != null && mLoginPop.isShowing()) {
                        mLoginPop.dismiss();
                    }
                    checkUserVip("", LoginConfig.getUserPhone(), LoginConfig.getUserId(), "10");

                } else {
                    ToastUtil.showToast("登录失败");
                }


            }
        });
    }

    /**
     * 核对vip身份
     *
     * @param s
     * @param phone
     * @param uid
     * @param reg_site
     */
    private void checkUserVip(String s, String phone, String uid, String reg_site) {
        HomeModel.getUserIsVip(s, phone, uid, reg_site, new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {

                if (state == 200) {
                    CheckVipBean vipBean = JSON.parseObject(data, CheckVipBean.class);
                    if (vipBean.getVip_level() == 0) {
                        showBuyVipNoticePop(getContext(), "包月（￥8）", "包年（￥50）", R.layout.activity_main, "加入会员", "请选择加入的会员类型", "取消", "确定", 1);

                    } else if (vipBean.getVip_level() == 1 || vipBean.getVip_level() == 2) {//TODO 播放
                        startVideo(jcVideoPlayerStandard);
                    }
                }
            }
        });
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
     * @param context
     * @param rootlayout
     * @param title
     * @param content
     * @param leftBtn
     * @param rightBtn
     * @param what       1 选择会员类型  2 选择支付方式
     */
    private void showBuyVipNoticePop(Context context, String btn1Str, String btn2Str, int rootlayout, String title, String content, String leftBtn, String rightBtn, final int what) {
        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_common_notice_rbtn_layout, null);
        ((TextView) (contentView.findViewById(R.id.dialog_title))).setText(title);
        ((TextView) (contentView.findViewById(R.id.dialog_notice_content))).setText(content);
        ((Button) (contentView.findViewById(R.id.dialog_btn_left))).setText(leftBtn);
        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setText(rightBtn);
        (contentView.findViewById(R.id.dialog_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginPop != null && mLoginPop.isShowing()) {
                    mLoginPop.dismiss();
                    mLoginPop = null;

                }
            }
        });
        RadioGroup mGroup = (RadioGroup) contentView.findViewById(R.id.dialog_rgroup);
        RadioButton mBtn1 = (RadioButton) contentView.findViewById(R.id.vip_1);
        RadioButton mBtn2 = (RadioButton) contentView.findViewById(R.id.vip_2);
        mBtn1.setText(btn1Str);
        mBtn2.setText(btn2Str);
        if (what==1){
            mGoodsId = "";//1 包月 2 包年
        }else if (what==2){
            mPayId = "";//1 微信 2 支付宝
        }
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.vip_1:
                        if (what == 1) {
                            mGoodsId = "1";//1 包月 2 包年
                        } else if (what == 2) {
                            mPayId = "1";//1 微信 2 支付宝
                        }

                        break;
                    case R.id.vip_2:
                        if (what == 1) {
                            mGoodsId = "2";//1 包月 2 包年
                        } else if (what == 2) {
                            mPayId = "2";//1 微信 2 支付宝
                        }
                        break;
                }
            }
        });


        mLoginPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);


        mLoginPop.setContentView(contentView);

        //显示PopupWindow
        View rootview = LayoutInflater.from(context).inflate(rootlayout, null);
        mLoginPop.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        ((Button) (contentView.findViewById(R.id.dialog_btn_left))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginPop != null && mLoginPop.isShowing()) {
                    mLoginPop.dismiss();
                    mLoginPop = null;

                }
            }
        });

        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (what == 1) {
                    if (mGoodsId == null || TextUtils.isEmpty(mGoodsId)) {
                        ToastUtil.showToast("请选择会员类型");
                        return;
                    }
                    if (mLoginPop != null && mLoginPop.isShowing()) {
                        mLoginPop.dismiss();
                        mLoginPop = null;
                    }
                    showBuyVipNoticePop(getContext(), "微信支付", "支付宝支付", R.layout.activity_main, "加入会员", "请选择支付方式", "取消", "确定", 2);

                } else if (what == 2) {
                    //TODO 调起支付  支付回调
                    //TODO showVipNoticePop(getBaseContext(),R.layout.activity_main,"加入会员","操作成功，会员期限：1970-01-01","会员中心","完成");

                    if (mPayId==null || TextUtils.isEmpty(mPayId)){
                        ToastUtil.showToast("请选择支付方式！");
                        return;
                    }

                    if (mPayId.equals("1")) {//1 微信 2 支付宝
//                        TempWXPayHelper wxPayHelper = new TempWXPayHelper();
//                        wxPayHelper.pay();

                    } else if (mPayId.equals("2")) {
//                        TempAliPayHelper aliPayHelper = new TempAliPayHelper(); //mAliPayHandler
//                        aliPayHelper.pay();

                    }

                }

            }
        });

    }


    /**
     * 获取验证码
     *
     * @param s
     * @param phone
     * @param reg_site
     */
    private void getMarkMsg(String s, String phone, String reg_site) {

        HomeModel.getMsg(s, phone, reg_site, new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {

                if (state == 200) {
                    mTimeUtil.start();
                    ToastUtil.showToast("验证码发送成功");
                } else {
                    ToastUtil.showToast("验证码发送失败，请重试");
                }


            }
        });


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
            JCVideoPlayer.releaseAllVideos();//停止播放
        }
    }

    /**
     * 分享
     */
    private void UserShare(final TextView itemShare) {
        itemShare.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                Logger.i("=======[分享的嘛]===========");
//                ShareUtil.getShareDialog(getActivity(),"分享的嘛");
                new ShowPopuWindows(mActivity, "分享地址还没有……").ShowPop();
            }
        });
//        final ShareInfo shareInfo = new ShareInfo();
//        shareInfo.setTitle(DonglaApplication.getContext().getResources().getString(R.string.app_name));
//        shareInfo.setJokeUserID(mId);
//        shareInfo.setContext(mTitle1);
//        shareInfo.setImgUrl(mPicture1);//头像
//        shareInfo.setUrl(mPicture1); //点击进去的
//        itemShare.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View view) {
//                if (XUtilNet.isNetConnected()) {
//                    ViewUtils.hideSoftInput(mActivity);
//                    //分享成功
//                    mPopuWindows = new ShowPopuWindows(mActivity, shareInfo, new ShareInterface() {
//                        @Override
//                        public void ShareRes(String ShareRes) {
//                            if (ShareRes.equals(mActivity.getResources().getString(R.string.share_success))) {//分享成功
//                                Logger.i("=======[分享]===========" + getResources().getString(R.string.share_success));
//                                Logger.i("=======[分享]===========" + mFavnum1);
//                                //ToastUtil.showCenterToast(getResources().getString(R.string.share_success));
//                                Logger.i("=======[分享]===========" + mFavnum1);
//                                sendEventBus(Constants.getShare);
//                                int nFavnum1 = Integer.parseInt(mFavnum1) + 1;
//                                itemShare.setText(String.valueOf(nFavnum1));
//                                Logger.i("=======[分享]===========" + nFavnum1);
//                            }
//                        }
//                    });
//                    mPopuWindows.ShowPop();
//                } else {
//                    ToastUtil.showCenterToast(R.string.check_net_is_connect);
//                }
//            }
//        });

    }

    private void UserClickList(final Activity mActivity, final TextView mItem, final String contentListBean, final int position, final boolean isZan) {
        mItem.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                DisplayUtil.hideKeyboard(mActivity);

                final String dongtuId = mId;
                final List<UserClickBean> zans = new ArrayList<UserClickBean>();
                zans.addAll(where("dongtuId=?", dongtuId).find(UserClickBean.class));
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
                        HomeModel.getZan(mId, upType, new NewInterface() {
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
                                            boolean save = userClickBean.setCai(false)
                                                    .setZan(true)
                                                    .setImei(Constants.IEMI)
                                                    .setDongtuId(dongtuId).save();
                                            if (save) {
                                                sendEventBus(Constants.getZan); //发送点赞 踩的操作
                                            }
                                        } else {
                                            boolean save = userClickBean.setCai(true)
                                                    .setZan(false)
                                                    .setImei(Constants.IEMI)
                                                    .setDongtuId(dongtuId).save();
                                            if (save) {
                                                sendEventBus(Constants.getCai);
                                            }
                                        }
                                        if (isZan) {
                                            int goodnum = Integer.parseInt(mGoodnum1) + 1;
                                            mItem.setText(String.valueOf(goodnum));
                                            StateUtils.setDrawbleAndText(mActivity, mItem, R.drawable.icon_like_selected, R.color.video_main);
                                        } else {
                                            int badnum = Integer.parseInt(mBadnum1) + 1;
                                            mItem.setText(String.valueOf(badnum));
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
                    UserClickBean userClickBean = zans.get(0);
                    boolean zan = userClickBean.getZan();
                    boolean cai = userClickBean.getCai();
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

    /*
     * 发送事件 通知点赞状态更新
     */
    private void sendEventBus(String isZan) {
        EventUtil.EnventChangesStateData state = new EventUtil.EnventChangesStateData();
        state.position = Integer.parseInt(mPositions);
        state.titleTypes = mCategory;
        state.isType = isZan;
        EventBus.getDefault().post(state);
    }

    @Override
    public void onLazyLoad() {
        //mMImageView
        initStartVieson();

        getAdState();
        //广告定时显示
        AdTimerTask();
        //百度广告
        showBaiduAd();
    }

    private void initStartVieson() {
        HomeModel.getStart(new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {
                Logger.i("=======[加载失败]===========" + e.toString());
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                try {
                    if (state == 200) {
                        Logger.i("=======[加载成功]===========" + data);
                        mVersionStartBean = JSON.parseObject(data, VersionStartBean.class);
                        Logger.i("=======[加载成功]===========" + mVersionStartBean.toString());
                        setAdData(mVersionStartBean);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置广告数据
     *
     * @param versionStartBean
     */
    private void setAdData(final VersionStartBean versionStartBean) {
        String picwidth = versionStartBean.getAd_img_width().trim();
        String picheight = versionStartBean.getAd_img_height().trim();
        int width = 0;
        int height = 0;
        if (!TextUtils.isEmpty(picheight) && !TextUtils.isEmpty(picwidth)) {
            width = Integer.parseInt(picwidth);
            height = Integer.parseInt(picheight);
        }
        int pictureWidth = 0;
        int pictureHeight = 0;
        if (width != 0 || height != 0) {
            //计算后的宽高
            pictureWidth = DisplayUtil.CountPictureWidthno(); //获取屏幕的宽度 -控件两边的间距
            pictureHeight = (pictureWidth * height);
            pictureHeight = pictureHeight / width;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pictureWidth, pictureHeight);//动态设置宽高
//            int dp_15 = DisplayUtil.dp2px(mActivity, 0);
//            layoutParams.setMargins(0, 0, 0, 0);
            mMImageView.setLayoutParams(layoutParams);
        }
        String ad_img = versionStartBean.getAd_img();
        if (!TextUtils.isEmpty(ad_img)) {
            Logger.i("=======[图片下载完成地址]===========" + ad_img);
            BaseControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                    Logger.i("图片下载完成");
                }
            };
//            mMImageView.setVisibility(View.VISIBLE);
            mMImageView.setHierarchy(FrescoUtil.GetHierarchy(false));  //进度条
//            versionStartBean.setAd_img("http://img2.imgtn.bdimg.com/it/u=3694962613,2047285895&fm=23&gp=0.jpg");
            FrescoUtil.loadGifUrl(ad_img.trim(), mMImageView, pictureWidth, pictureHeight, true, controllerListener);
        }
        mMImageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                ActivityMy.skipX5Detail(mActivity, versionStartBean);
            }
        });
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedUtil.putString("username", "0");
        EventBus.getDefault().unregister(this);
        if (jcVideoPlayerStandard != null) {
            jcVideoPlayerStandard.release();
            jcVideoPlayerStandard = null;
        }
    }

    /**
     * 发表评论
     *
     * @param
     */
    private void sendComment(final TextView mTv_item_joke_comment, TextView mComments_add, final EditText mComments_edit) {
        try {
            mComments_add.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
//                    ToastUtil.showCenterToast("点击了");
                    if (XUtilNet.isNetConnected()) {
                        String editText = mComments_edit.getText().toString().trim();
                        if (!TextUtils.isEmpty(editText)) {//不为空
                            String username = SharedUtil.getString("username");//如果没有存入 默认是“0”
                            HomeModel.getSendComment(mId, editText, mCategory, username, new NewInterface() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    Logger.i("=======[失败]===========" + e.toString());
                                    ToastUtil.showCenterToast("评论失败");
                                }

                                @Override
                                public void onSucceed(int state, String data, JSONObject obj) {
                                    if (state == 200) {
                                        if (listSize.size() == 0) {
                                            removeFootView();//说明这是第一条数据
                                        }
                                        DisplayUtil.hideKeyboard(mActivity);
                                        ToastUtil.showCenterToast("评论成功");
                                        mComments_edit.setText("");
                                        Logger.i("=======[成功]===========" + obj.toString());
                                        CommentBean commentBean = JSON.parseObject(data, CommentBean.class);
                                        mMList.add(0, commentBean);
                                        mHeaderAndFooterWrapper.notifyDataSetChanged();

                                        String username = commentBean.getUsername();//发表评论后 返回的用户名 保存至本地 下次再发表发送此用户名
                                        SharedUtil.putString("username", username.toString());
                                        int commentNum = Integer.parseInt(mCommentNum) + 1;
                                        mCommentNum = String.valueOf(commentNum);
                                        Logger.i("=======[成功3mCommentNum 2]===========" + commentNum);
                                        mTv_item_joke_comment.setText(String.valueOf(commentNum));
                                        sendEventBus(Constants.getComment);
                                    }
                                }
                            });
                        } else {//为空
                            ToastUtil.showCenterToast(R.string.input_no_empty);
                        }
                    } else {
                        ToastUtil.showCenterToast(R.string.check_net_is_connect);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受网络状态变化
     *
     * @param ListenerNetEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenerNetEvent(EventUtil.ListenerNetEvent ListenerNetEvent) {
        if (ListenerNetEvent.isHasNet) {
            int size = listSize.size();
            if (size == 0) {
                removeFootView();
                if (mComment_loding != null) {
                    mComment_loding.setVisibility(View.VISIBLE);
                }
                getData();
            }
        } else {
        }
    }

    private void AdTimerTask() {
        mAdTask = new TimerTask() {
            public void run() {
                //execute the task
                handler.sendEmptyMessage(11);
            }
        };
        Timer timer = new Timer();
        timer.schedule(mAdTask, 15000);
    }

    //广点通广告
    private InterstitialAD getIAD() {
        if (iad == null) {
            iad = new InterstitialAD(getActivity(), Constants.APPID, Constants.InterteristalPosID);
            XUtilLog.log_i("wbb", "=====APPID=====:" + Constants.APPID);
            XUtilLog.log_i("wbb", "=====InterteristalPosID=====:" + Constants.InterteristalPosID);
        }
        return iad;
    }

    //弹出广点通广告
    private void showGdtAD() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(int arg0) {
                XUtilLog.log_i("wbb", "LoadInterstitialAd Fail:" + arg0);
            }

            @Override
            public void onADReceive() {
                XUtilLog.log_i("wbb", "onADReceive");
                iad.show();
            }
        });
        iad.loadAD();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    XUtilLog.log_i("wbb", "弹广告了 ... ... ");
                    //广点通插屏
                    if (gdtStatus.equals("1")) {
                        showGdtAD();
                    }
                    //百度插屏
                    if (bdStatus.equals("1")) {
                        if (interAd.isAdReady()) {
                            interAd.showAd(getActivity());
                        } else {
                            interAd.loadAd();
                        }
                    }
                    //9500 插屏
//                    if (m9500Status.equals("1")) {
                    //内插屏广告，必须在初始化之后调用
//                        StartSDK.showInApp(getContext());
//                        EventUtil.EnventAd enventAd = new EventUtil.EnventAd();
//                        EventBus.getDefault().post(enventAd);
//                    }
//                    break;
            }
        }
    };


    //百度插屏广告
    private void showBaiduAd() {
        interAd = new InterstitialAd(getContext(), Constants.adPlaceId);
        interAd.setListener(new InterstitialAdListener() {

            @Override
            public void onAdClick(InterstitialAd arg0) {
                XUtilLog.log_i("InterstitialAd", "onAdClick");
            }

            @Override
            public void onAdDismissed() {
                XUtilLog.log_i("InterstitialAd", "onAdDismissed");
                interAd.loadAd();
            }

            @Override
            public void onAdFailed(String arg0) {
                XUtilLog.log_i("InterstitialAd", "onAdFailed");
            }

            @Override
            public void onAdPresent() {
                XUtilLog.log_i("InterstitialAd", "onAdPresent");
            }

            @Override
            public void onAdReady() {
                XUtilLog.log_i("InterstitialAd", "onAdReady");
            }

        });
        interAd.loadAd();
    }

    /**
     * 获取广告开关状态
     */
    private void getAdState() {
        NIManage.getAdState(new com.anzhuo.video.app.search.ui.manager.MyInterface() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    try {
                        JSONObject jsonObject = JSON.parseArray(data).getJSONObject(0);
                        JSONObject jsonObject1 = JSON.parseArray(data).getJSONObject(1);
                        JSONObject jsonObject2 = JSON.parseArray(data).getJSONObject(2);
                        XUtilLog.log_i("wbb", "=======[广告状态数据]=======:" + data);
                        gdtStatus = jsonObject.getString("gdt_status");
                        bdStatus = jsonObject1.getString("bd_status");
                        m9500Status = jsonObject2.getString("9500_status");
                        Constants.APPID = jsonObject.getString("appid");
                        Constants.InterteristalPosID = jsonObject.getString("interteristalposID");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
