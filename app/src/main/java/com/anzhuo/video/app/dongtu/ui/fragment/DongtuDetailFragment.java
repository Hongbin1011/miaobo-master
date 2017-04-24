package com.anzhuo.video.app.dongtu.ui.fragment;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.dongtu.bean.dongtu.CommentBean;
import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuContentListBean;
import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuUserClickBean;
import com.anzhuo.video.app.dongtu.bean.dongtu.ShareInfo;
import com.anzhuo.video.app.dongtu.config.DongtuConstants;
import com.anzhuo.video.app.dongtu.manager.ActivityMy;
import com.anzhuo.video.app.dongtu.model.base.HomeModel;
import com.anzhuo.video.app.dongtu.model.base.NewInterface;
import com.anzhuo.video.app.dongtu.ui.adapter.RefreshAdapter;
import com.anzhuo.video.app.dongtu.ui.base.DongtuBaseFragment;
import com.anzhuo.video.app.dongtu.utils.DisplayUtil;
import com.anzhuo.video.app.dongtu.utils.DongtuToastUtil;
import com.anzhuo.video.app.dongtu.utils.EventUtil;
import com.anzhuo.video.app.dongtu.utils.FrescoUtils.FrescoUtil;
import com.anzhuo.video.app.dongtu.utils.NoDoubleClickListener;
import com.anzhuo.video.app.dongtu.utils.SharedUtil;
import com.anzhuo.video.app.dongtu.utils.StateUtils;
import com.anzhuo.video.app.dongtu.utils.ViewUtils;
import com.anzhuo.video.app.dongtu.widget.MyHeaderAndFooterWrapper;
import com.anzhuo.video.app.dongtu.widget.StateView;
import com.anzhuo.video.app.dongtu.widget.StateViewOnclickCallback;
import com.anzhuo.video.app.utils.ViewUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.orhanobut.logger.Logger;
import com.sdk9500.media.Bean.DataBean;
import com.sdk9500.media.InterFace.IFBannerDataListener;
import com.sdk9500.media.StartSDK;
import com.sdk9500.media.view.InformationBannerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static org.litepal.crud.DataSupport.where;


public class DongtuDetailFragment extends DongtuBaseFragment {
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
//    private ShowPopuWindows mPopuWindows;


    private StateView mStateView;
    private LinearLayout mRv_gen;
    private LinearLayout mRv_gen2;
    private View mHeadView;
    private RefreshAdapter mRefreshComment;
    MyHeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private StateView mStateView2;
    private String mCommentNum;
    private InformationBannerView mInformationBannerView;
    private String mHeight;
    private String mWidth;
    private DongtuContentListBean mMContentBean;
    private TextView mComments_add;
    private EditText mComments_edit;
    private LayoutInflater inflater;
    private TextView mComment_loding;
//    private TextView mTv_no_net_tip;

    public static DongtuDetailFragment newInstance(DongtuContentListBean bean, String Category, int position) {
        DongtuDetailFragment fragment = new DongtuDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, bean.getTitle());
        args.putString(ARG_PARAM2, bean.getPicture());
        args.putString(ARG_PARAM3, bean.getGoodnum());
        args.putString(ARG_PARAM4, bean.getBadnum());
        args.putString(ARG_PARAM5, bean.getFavnum());
        args.putString(ARG_PARAM6, bean.getDongtuId());
        args.putString(ARG_PARAM7, Category);
        args.putString(ARG_PARAM8, String.valueOf(position));
        args.putString(ARG_PARAM9, bean.getCommentnum());
        args.putString(ARG_PARAM10, bean.getPicheight());
        args.putString(ARG_PARAM11, bean.getPicwidth());
        args.putSerializable(ARG_PARAM12, bean);
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
                mMContentBean = (DongtuContentListBean) arguments.getSerializable(ARG_PARAM12);
                Logger.i("cq=============[接收事件 位置 开始]===========" + mPositions);
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
            mHeadView = inflater.inflate(R.layout.dongtu_detail_header, null);//头部的布局
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
                    Logger.i("cq=============[点击了]==========");
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

                Logger.i("cq=============[没有更多的数据了 前]===========" + lastVisibleItem);
                Logger.i("cq=============[没有更多的数据了 前2]===========" + mRefreshComment.getItemCount());
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int i2 = page * 20;
                if (mRefreshComment.getItemCount() + 1 < i2) {
                    Logger.i("cq=============[1 刷新这里的]===========");
                    mRefreshComment.changeMoreStatus(mRefreshComment.NO_LOAD_MORE);
                    return;
                }
//                }
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int i = page * 20;
                    Logger.i("cq=============[没有更多的数据了 中]===========" + (lastVisibleItem + 1));
                    if (lastVisibleItem < i) {
                        Logger.i("cq=============[没有更多的数据了2 刷新这里的]===========");
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
                        Logger.i("cq=============[请求失败]===========");
                        DongtuToastUtil.showCenterToast(R.string.loading_error);
                        mRefreshComment.changeMoreStatus(RefreshAdapter.NO_LOAD_ERRO);
                        mStateView.showSuccess();
//                        if (mComment_loding != null) {
//                            mComment_loding.setVisibility(View.GONE);
//                        }
                        if (listSize.size() == 0) {
                            mComment_loding.setText("加载失败");
                            mComment_loding.setOnClickListener(new NoDoubleClickListener() {
                                @Override
                                public void onNoDoubleClick(View view) {
                                    mComment_loding.setText("正在加载…");
                                    getData();
                                }
                            });
                        }
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        Logger.i("cq=============[请求成功 评论列表 2]===========" + mTitle1 + data);

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
            Logger.i("cq=============[没有网络的时候来这里没有]===========");
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
                    Logger.i("cq=============[添加footview1]===========");
                }

                @Override
                public void NoDataClick() {//没有数据
                    Logger.i("cq=============[添加footview12]===========");
                    page = page + 1;
                    getData();
                    DisplayUtil.hideKeyboard(mActivity);
                }

                @Override
                public void NoLoginClick() {//没有登录
                    Logger.i("cq=============[添加footview13]===========");

                }

                @Override
                public void CustomClick() {//自定义
                    Logger.i("cq=============[添加footview14]===========");

                }
            });
        }
        mStateView2.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                Logger.i("cq=============[添加footview1 点击了4]===========");
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
     * @param header
     */
    private void setHeader(View header) {
        Logger.i("cq=============[点击了21]===========");
        mStateView.showSuccess();
        Logger.i("cq=============[点击了22]===========");
        header.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Logger.i("cq=============[点击了2]===========");
                DisplayUtil.hideKeyboard(mActivity);
                return false;
            }
        });
        header.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                Logger.i("cq=============[点击了]===========");
                DisplayUtil.hideKeyboard(mActivity);
            }
        });
        /**
         Banner广告，在相应Banner嵌入位置调用，必须在初始化之后调用
         */
        mInformationBannerView = (InformationBannerView) header.findViewById(R.id.informationBanner);
        TextView mTitleContent = (TextView) header.findViewById(R.id.tv_item_joke_content);
        final SimpleDraweeView mImageView = (SimpleDraweeView) header.findViewById(R.id.my_image_view);//图片 gif
        //gif标识
//        final ImageView mGifTips = (ImageView) header.findViewById(R.id.img_item_gif_tips);
//        mGifTips.setVisibility(View.VISIBLE);
        TextView mItemZan = (TextView) header.findViewById(R.id.tv_item_joke_zan);//赞
        TextView mItemUnZan = (TextView) header.findViewById(R.id.tv_item_joke_unzan);//踩
        TextView mItemShare = (TextView) header.findViewById(R.id.tv_item_joke_share);//分享
        TextView mTv_item_joke_comment = (TextView) header.findViewById(R.id.tv_item_joke_comment); //评论
        //评论状态
        mComment_loding = (TextView) header.findViewById(R.id.comment_loding);

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

        UserClickList(mActivity, mItemZan, mGoodnum1, 0, true);
        /*点踩*/
        UserClickList(mActivity, mItemUnZan, mBadnum1, 0, false);
        UserShare(mItemShare);//分享
        sendComment(mTv_item_joke_comment, mComments_add, mComments_edit);//发表评论
        String dongtuId = mId;

        //用户点击状态的情况
        List<DongtuUserClickBean> states = new ArrayList<DongtuUserClickBean>();
        states.addAll(where("dongtuId=?", dongtuId).find(DongtuUserClickBean.class));
        Logger.i("cq=============[states]===========" + states.size());
        if (states.size() != 0) {
            DongtuUserClickBean clickBean = states.get(0); // 0 根据dongtuId 查询到的只有一条数据
            boolean zan = clickBean.getZan();
            boolean cai = clickBean.getCai();
            if (zan) {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like_selected, R.color.video_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            }
            if (cai) {
                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.ico_unlike_selected, R.color.video_main);
            } else {
                StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.icon_unlike, R.color.text_remind_gray_light);
            }
        } else {
            StateUtils.setDrawbleAndText(mActivity, mItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.icon_unlike, R.color.text_remind_gray_light);
        }
        //设置数字
        mTitleContent.setText(mTitle1);
        mItemZan.setText(mGoodnum1);
        mItemUnZan.setText(mBadnum1);
        mItemShare.setText(mFavnum1);
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
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pictureWidth, pictureHeight);//动态设置宽高
            int dp_15 = DisplayUtil.dp2px(mActivity, 15);
            layoutParams.setMargins(dp_15, 0, dp_15, 0);
            mImageView.setLayoutParams(layoutParams);
        }
        BaseControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                Logger.i("图片下载完成");
//                mGifTips.setVisibility(View.GONE);
            }
        };
        // false 是否显示长图
        mImageView.setHierarchy(FrescoUtil.GetHierarchy(false));  //进度条
        FrescoUtil.loadGifUrl(mPicture1, mMContentBean.getLit_url(), mImageView, pictureWidth, pictureHeight, true, controllerListener);

        final int finalPictureWidth = pictureWidth;
        final int finalPictureHeight = pictureHeight;
        //点击查看图片详情
        mImageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                ActivityMy.startPhotoActivity(mActivity, mPicture1, mMContentBean.getLit_url(), finalPictureWidth, finalPictureHeight);
            }
        });
    }

    /**
     * 分享
     */
    private void UserShare(final TextView itemShare) {
        final ShareInfo shareInfo = new ShareInfo();
        shareInfo.setTitle(getContext().getResources().getString(R.string.app_name));
        shareInfo.setJokeUserID(mId);
        shareInfo.setContext(mTitle1);
        shareInfo.setImgUrl(mPicture1);//头像
        shareInfo.setUrl(mPicture1); //点击进去的
        itemShare.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (XUtilNet.isNetConnected()) {
                    ViewUtils.hideSoftInput(mActivity);
                    //分享成功
//                    mPopuWindows = new ShowPopuWindows(mActivity, shareInfo, new ShareInterface() {
//                        @Override
//                        public void ShareRes(String ShareRes) {
//                            if (ShareRes.equals(mActivity.getResources().getString(R.string.share_success))) {//分享成功
//                                Logger.i("cq=============[分享]===========" + getResources().getString(R.string.share_success));
//                                Logger.i("cq=============[分享]===========" + mFavnum1);
//                                //ToastUtil.showCenterToast(getResources().getString(R.string.share_success));
//                                Logger.i("cq=============[分享]===========" + mFavnum1);
//                                sendEventBus(DongtuConstants.getShare);
//                                int nFavnum1 = Integer.parseInt(mFavnum1) + 1;
//                                itemShare.setText(String.valueOf(nFavnum1));
//                                Logger.i("cq=============[分享]===========" + nFavnum1);
//                            }
//                        }
//                    });
//                    mPopuWindows.ShowPop(true);
                } else {
                    DongtuToastUtil.showCenterToast(R.string.check_net_is_connect);
                }
            }
        });

    }

    private void UserClickList(final Activity mActivity, final TextView mItem,
                               final String contentListBean, final int position, final boolean isZan) {
        mItem.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                DisplayUtil.hideKeyboard(mActivity);

                final String dongtuId = mId;
                final List<DongtuUserClickBean> zans = new ArrayList<DongtuUserClickBean>();
                zans.addAll(where("dongtuId=?", dongtuId).find(DongtuUserClickBean.class));
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
                        HomeModel.getZan(mId, upType, new NewInterface() {
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
                                            boolean save = userClickBean.setCai(false)
                                                    .setZan(true)
                                                    .setImei(DongtuConstants.IEMI)
                                                    .setDongtuId(dongtuId).save();
                                            if (save) {
                                                sendEventBus(DongtuConstants.getZan); //发送点赞 踩的操作
                                            }
                                        } else {
                                            boolean save = userClickBean.setCai(true)
                                                    .setZan(false)
                                                    .setImei(DongtuConstants.IEMI)
                                                    .setDongtuId(dongtuId).save();
                                            if (save) {
                                                sendEventBus(DongtuConstants.getCai);
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
                    DongtuUserClickBean userClickBean = zans.get(0);
                    boolean zan = userClickBean.getZan();
                    boolean cai = userClickBean.getCai();
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
    /*
     * 发送事件 通知点赞状态更新
     */
    private void sendEventBus(String isZan) {
        EventUtil.EnventChangesStateData state = new EventUtil.EnventChangesStateData();
        EventUtil.EnventChangesStateData.position = Integer.parseInt(mPositions);
        state.titleTypes = mCategory;
        state.isType = isZan;
        EventBus.getDefault().post(state);
    }

    @Override
    public void onLazyLoad() {
        initAd();
    }


    /**
     * 初始化ad
     */
    private void initAd() {
        HomeModel.getStart(new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {
                Logger.i("cq=============[加载失败 ad]===========");
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                try {
                    Logger.i("cq=============[加载成功ad]===========" + data);
                    if (state == 200) {
                        JSONObject jsonObject = JSON.parseObject(data);
                        String sdk = jsonObject.getString("sdk");
                        Logger.i("cq=============[加载成功]===========" + sdk);
                        if (sdk.equals("0")) {//不显示
                            Logger.i("cq=============[加载成功 不显示广告]===========");
                        } else {//显示开屏广告
                            //开屏广告，必须在初始化之后调用
                            loadSdkAd();//加载sdk
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 加载sdk ad
     */
    private void loadSdkAd() {
        Logger.i("cq=============[banner广告232333]===========" + mTitle1);
        Logger.i("cq=============[banner广告232333 成功]===========");
        StartSDK.getInformationBannerData(getActivity(), new IFBannerDataListener() {
            @Override
            public void getDataFinish(final List<DataBean> DataBeanList) {
                Logger.i("cq=============[banner广告]===========" + DataBeanList.toString());
                Logger.i("cq=============[banner广告33]===========" + mInformationBannerView);
                if (mInformationBannerView != null) {
                    mInformationBannerView.setDataForView(DataBeanList, 0);
                }
            }

            @Override
            public void getDataError(String error) {
                Logger.i("cq=============[banner广告]===========" + error.toString());
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
                                    Logger.i("cq=============[失败]===========" + e.toString());
                                    DongtuToastUtil.showCenterToast("评论失败");
                                }

                                @Override
                                public void onSucceed(int state, String data, JSONObject obj) {
                                    if (state == 200) {
                                        if (listSize.size() == 0) {
                                            removeFootView();//说明这是第一条数据
                                        }
                                        DisplayUtil.hideKeyboard(mActivity);
                                        DongtuToastUtil.showCenterToast("评论成功");
                                        mComments_edit.setText("");
                                        Logger.i("cq=============[成功]===========" + obj.toString());
                                        CommentBean commentBean = JSON.parseObject(data, CommentBean.class);
                                        mMList.add(0, commentBean);
                                        mHeaderAndFooterWrapper.notifyDataSetChanged();

                                        String username = commentBean.getUsername();//发表评论后 返回的用户名 保存至本地 下次再发表发送此用户名
                                        SharedUtil.putString("username", username.toString());
                                        int commentNum = Integer.parseInt(mCommentNum) + 1;
                                        mCommentNum = String.valueOf(commentNum);
                                        Logger.i("cq=============[成功3mCommentNum 2]===========" + commentNum);
                                        mTv_item_joke_comment.setText(String.valueOf(commentNum));
                                        sendEventBus(DongtuConstants.getComment);
                                    }
                                }
                            });
                        } else {//为空
                            DongtuToastUtil.showCenterToast(R.string.input_no_empty);
                        }
                    } else {
                        DongtuToastUtil.showCenterToast(R.string.check_net_is_connect);
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
}
