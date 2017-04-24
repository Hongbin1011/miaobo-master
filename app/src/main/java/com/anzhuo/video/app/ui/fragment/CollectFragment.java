package com.anzhuo.video.app.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.constant.LoginConfig;
import com.anzhuo.video.app.manager.ActivityMy;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.CheckVipBean;
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
import static org.litepal.crud.DataSupport.findAll;


/**
 * 收藏
 */

public class CollectFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mCategory = null;
    private String mCategoryid; //暂时没有用 留着
    private LuRecyclerView mRecy_view;
    private List<CollectListBean> mMList;
    private LuRecyclerViewAdapter myAdapter;
    private CommonAdapter<CollectListBean> mCommonAdapter;
    private PtrClassicFrameLayout mPtrFrameLayout_public;

    private ImageView mFab_refresh;
    int page = 0;
    private RelativeLayout mData_gen;
    private StateView mStateView;

    CollectListBean contentListBeas2;
    private ShowPopuWindows mPopuWindows;

    int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private PopupWindow mLoginPop;
    private String mGoodsId;//1 包月 2 包年
    private String mPayId;//1 微信 2 支付宝
    private JCVideoPlayerStandardFresco mJcVideoPlayerStandard;

    public CollectFragment() {
    }

    public static CollectFragment newInstance() {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        return R.layout.fragment_collect_list;
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
        ImageView mBack = ViewUtil.findView(mRootView, R.id.action_back);
        mPtrFrameLayout_public = ViewUtil.findView(mRootView, R.id.ptrFrameLayout_public);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecy_view.setLayoutManager(mLayoutManager);
//        mRecy_view.setItemAnimator(new DefaultItemAnimator()); //使用默认的动画
        initPtrFrameLayout(); //初始化头部
        mRecy_view.getItemAnimator().setChangeDuration(0);
        RecycleViewVideoUtil.addOnChildAttachStateChangeListener(mRecy_view);

        mCommonAdapter = new CommonAdapter<CollectListBean>(mActivity, R.layout.item_joke_nine_img, mMList) {
            @Override
            protected void convert(ViewHolder holder, CollectListBean contentListBean, int position) {
                try {
                    contentListBeas2 = null;
                    contentListBeas2 = contentListBean;
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
                List<ContentListBean> mContentList = new ArrayList<>();
                Logger.i("=======[baseHttpHashMap mMList]===========" + mMList.size());
                for (int i = 0; i < mMList.size(); i++) {
                    CollectListBean collectListBean = mMList.get(i);
                    ContentListBean contentListBean = CollectSuccess(collectListBean);
                    mContentList.add(contentListBean);
                }

                if (mMList.get(position).getIs_ad().equals("1")) {//跳转网页
                    SkipX5ADetails(mContentList.get(position).getTitle(), mContentList.get(position).getAd_url());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) mContentList);
                    bundle.putString("position", String.valueOf(position));
                    bundle.putString("Category", contentListBeas2.getTitleType());//栏目名称了
                    bundle.putString("page", String.valueOf(page));
                    bundle.putBoolean("isSerach", true); //是否是收藏
                    Logger.i("=======[baseHttpHashMap ced]===========" + page);
//                ContentListBean
                    ActivityMy.startDetailsActivity(getContext(), bundle);
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

    private ContentListBean CollectSuccess(CollectListBean collect) {
        ContentListBean collecBean = new ContentListBean();
        collecBean.setTitleType(collect.getTitleType());
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

        collecBean.setIs_ad(collect.getIs_ad());
        collecBean.setAd_url(collect.getAd_url());
        collecBean.setPrice(collect.getPrice());
        return collecBean;
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param contentListBean
     */
    private void setData(ViewHolder holder, final CollectListBean contentListBean, final int position) {
        Logger.i("=======[contentListBean]===========" + contentListBean.toString());
        TextView mTitleContent = holder.getView(R.id.tv_item_joke_content2);

//        SimpleDraweeView myImageView = holder.getView(R.id.my_image_view);//图片 gif

        JCVideoPlayerStandardFresco jcVideoPlayerStandard = holder.getView(R.id.jc_video);

//        final ImageView imgItemGifTips = holder.getView(R.id.img_item_gif_tips);
        TextView mTv_item_joke_comment = holder.getView(R.id.tv_item_joke_comment);
        TextView mItemZan = holder.getView(R.id.tv_item_joke_zan);
        TextView mItemCollect = holder.getView(R.id.tv_item_collect);//收藏
        TextView mItemShare = holder.getView(R.id.tv_item_joke_share);
        ImageView mItemVip = holder.getView(R.id.item_is_vip);
//        imgItemGifTips.setVisibility(View.VISIBLE);
        //设置数据
        setStateNum(contentListBean, position, mTitleContent, jcVideoPlayerStandard, mTv_item_joke_comment, mItemZan, mItemCollect, mItemShare);
        /*点赞*/
        UserClickList(mActivity, mItemZan, contentListBean, position, true);
        /*点踩*/
        // UserClickList(mActivity, mItemCollect, contentListBean, position, false);
        /*分享*/
        UserShare(mItemShare, contentListBean);
        UserCollect(mItemCollect, contentListBean, mItemCollect);//收藏

        VideoIsVIP(mItemVip,contentListBean);//vip
    }


    /**
     * vip 是否为视频
     * @param
     * @param collectListBean
     */
    private boolean isVipVideo(CollectListBean collectListBean){
        boolean isVipVideo = false;
        String sPrice = collectListBean.getPrice();
        double mPrice=0.0;
        if (sPrice!=null && !TextUtils.isEmpty(sPrice)){
            mPrice = Double.valueOf(sPrice);
            if (mPrice>0){
                isVipVideo = true;
            }
        }
        return isVipVideo;
    }

    /**
     * vip 是否为视频
     * @param imageView
     * @param collectListBean
     */
    private void VideoIsVIP(ImageView imageView,CollectListBean collectListBean){

        boolean isVipV = isVipVideo(collectListBean);
        if (isVipV){
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 收藏
     *
     * @param itemCollect
     * @param collect
     * @param collecTv
     */
    private void UserCollect(final TextView itemCollect, final CollectListBean collect, final TextView collecTv) {
        itemCollect.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                int i = DatabaseUtils.deleteData(collect.getDongtuId());
                if (i != 0) {
                    ToastUtil.showCenterToast("取消收藏成功");
                    mPtrFrameLayout_public.autoRefresh(true);
                    CollectCancelEnvent(collect);//取消收藏发送事件

                    Intent intent = new Intent();
                    intent.setAction("com.android.collectfragmentbro");
                    LocalBroadcastManager instance = LocalBroadcastManager.getInstance(VideoApplication.getContext());
                    instance.sendBroadcast(intent);
                } else {

                }
            }
        });
    }

    /**
     * 取消收藏 发送事件
     */
    private void CollectCancelEnvent(CollectListBean collect) {
        EventUtil.EnventCollectCancel collectCancel = new EventUtil.EnventCollectCancel();
        collectCancel.collectId = collect.getDongtuId();
        EventBus.getDefault().post(collectCancel);
    }


    /**
     * 加载数据  数量
     *
     * @param contentListBean
     * @param position
     * @param mTitleContent
     * @param mTv_item_joke_comment
     * @param mItemZan
     * @param mItemCollect
     * @param mItemShare
     */
    private void setStateNum(final CollectListBean contentListBean, final int position, TextView mTitleContent,
                             final JCVideoPlayerStandardFresco jcVideoPlayerStandard, TextView mTv_item_joke_comment, TextView mItemZan,
                             TextView mItemCollect, TextView mItemShare) {

        Logger.i("=======[点击gif]===========" + jcVideoPlayerStandard.getTag() + "====" + position);
        //计算图片缩放后的宽高
        String picwidth = contentListBean.getPic_width();
        String picheight = contentListBean.getPic_height();

//        int width = 0;
//        int height = 0;
//        if (!TextUtils.isEmpty(picheight) && !TextUtils.isEmpty(picwidth)) {
//            //高宽不为空 类型转换
//            width = Integer.parseInt(picwidth);
//            height = Integer.parseInt(picheight);
//        }
//        int pictureWidth = 0;
//        int pictureHeight = 0;
//        if (width != 0 || height != 0) {
//            //计算后的宽高
//            pictureWidth = DisplayUtil.CountPictureWidth();//获取屏幕的宽度 - 控件两边的间距
//            pictureHeight = (pictureWidth * height);
//            pictureHeight = pictureHeight / width;
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pictureWidth, pictureHeight);//动态设置宽高
//            jcVideoPlayerStandard.setLayoutParams(layoutParams);
//        }

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
                Logger.e("--------我的收藏--isVipVideo(contentListBean----"+isVipVideo(contentListBean)+contentListBean.getPrice());
                jcVideoPlayerStandard.thumbImageView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        mJcVideoPlayerStandard = jcVideoPlayerStandard;
                        RecycleViewVideoUtil.setCurrentItemPosition(position);
                        if (isVipVideo(contentListBean)){
                            checkUserVip("", LoginConfig.getUserPhone(),LoginConfig.getUserId(),"10");
                        }else {
                            startVideo(jcVideoPlayerStandard);
//                            jcVideoPlayerStandard.startVideo();
                        }


                    }
                });
                jcVideoPlayerStandard.startButton.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                       mJcVideoPlayerStandard = jcVideoPlayerStandard;
                        RecycleViewVideoUtil.setCurrentItemPosition(position);
                        if (isVipVideo(contentListBean)){
                            checkUserVip("", LoginConfig.getUserPhone(),LoginConfig.getUserId(),"10");
                        }else {
                            startVideo(jcVideoPlayerStandard);
//                            jcVideoPlayerStandard.startVideo();
                        }
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        //根据videoid 从数据库中查找 然后设置点赞踩状态
        String dongtuId = contentListBean.getDongtuId();
        List<UserClickBean> states = new ArrayList<UserClickBean>();
        states.addAll(DataSupport.where("dongtuId=?", dongtuId).find(UserClickBean.class));
        Logger.i("=======[states]===========" + states.size());
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
            //    StateUtils.setDrawbleAndText(mActivity, mItemUnZan, R.drawable.icon_unlike, R.color.text_remind_gray_light);
        }

        //这是收藏页面所以在这里出现的都是收藏的 默认选中
        StateUtils.setDrawbleAndText(mActivity, mItemCollect, R.drawable.shoucang, R.color.video_main);


        //设置数量
        mItemZan.setText(contentListBean.getGoodnum());
        //   mItemUnZan.setText(contentListBean.getBadnum());
        mTitleContent.setText(contentListBean.getTitle());
//        mItemShare.setText(contentListBean.getFavnum());
        mTv_item_joke_comment.setText(contentListBean.getCommentnum());
    }


    /**
     * 核对vip身份
     * @param s
     * @param phone
     * @param uid
     * @param reg_site
     */
    private void checkUserVip(String s, String phone, String uid, String reg_site){
        HomeModel.getUserIsVip(s, phone, uid, reg_site, new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {

                if (state==200){
                    CheckVipBean vipBean = JSON.parseObject(data,CheckVipBean.class);
                    if (vipBean.getVip_level()==0){
                        showBuyVipNoticePop(getContext(),"包月（￥8）","包年（￥50）",R.layout.activity_main,"加入会员","请选择加入的会员类型","取消","确定",1);

                    }else if (vipBean.getVip_level()==1 || vipBean.getVip_level()==2){//TODO 播放
                        startVideo(mJcVideoPlayerStandard);
                    }
                }
            }
        });
    }

    /**
     *
     * @param context
     * @param rootlayout
     * @param title
     * @param content
     * @param leftBtn
     * @param rightBtn
     * @param what 1 选择会员类型  2 选择支付方式
     */
    private void showBuyVipNoticePop(Context context, String btn1Str, String btn2Str, int rootlayout, String title, String content, String leftBtn, String rightBtn, final int what){
        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_common_notice_rbtn_layout, null);
        ((TextView) (contentView.findViewById(R.id.dialog_title))).setText(title);
        ((TextView) (contentView.findViewById(R.id.dialog_notice_content))).setText(content);
        ((Button) (contentView.findViewById(R.id.dialog_btn_left))).setText(leftBtn);
        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setText(rightBtn);
        (contentView.findViewById(R.id.dialog_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginPop!=null && mLoginPop.isShowing()){
                    mLoginPop.dismiss();
                    mLoginPop=null;

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
                switch (checkedId){
                    case R.id.vip_1:
                        if (what==1){
                            mGoodsId = "1";//1 包月 2 包年
                        }else if (what==2){
                            mPayId = "1";//1 微信 2 支付宝
                        }

                        break;
                    case R.id.vip_2:
                        if (what==1){
                            mGoodsId = "2";//1 包月 2 包年
                        }else if (what==2){
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
                if (mLoginPop!=null && mLoginPop.isShowing()){
                    mLoginPop.dismiss();
                    mLoginPop=null;

                }
            }
        });

        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (what==1){
                    if (mGoodsId==null || TextUtils.isEmpty(mGoodsId)){
                        ToastUtil.showToast("请选择会员类型");
                        return;
                    }
                    if (mLoginPop!=null && mLoginPop.isShowing()){
                        mLoginPop.dismiss();
                        mLoginPop=null;
                    }
                    showBuyVipNoticePop(getContext(),"微信支付","支付宝支付",R.layout.activity_main,"加入会员","请选择支付方式","取消","确定",2);

                }else if (what==2){
                    //TODO 调起支付  支付回调
                    //TODO showVipNoticePop(getBaseContext(),R.layout.activity_main,"加入会员","操作成功，会员期限：1970-01-01","会员中心","完成");

                    if (mPayId==null || TextUtils.isEmpty(mPayId)){
                        ToastUtil.showToast("请选择支付方式！");
                        return;
                    }

                    if (mPayId.equals("1")){//1 微信 2 支付宝
//                        TempWXPayHelper wxPayHelper = new TempWXPayHelper();
//                        wxPayHelper.pay();

                    }else if (mPayId.equals("2")){
//                        TempAliPayHelper aliPayHelper = new TempAliPayHelper(); //mAliPayHandler
//                        aliPayHelper.pay();

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
     * 内容 跳转至广告
     */
    private void SkipX5ADetails(String title, String ad_url) {
        ActivityMy.skipX5Details(mActivity, title, ad_url);
    }

    /**
     * 分享
     */
    private void UserShare(final TextView itemShare, final CollectListBean contentListBean) {
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
    private void UserClickList(final Activity mActivity, final TextView mItem, final CollectListBean contentListBean, final int position, final boolean isZan) {
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
            getData(null, mCategoryid, null, String.valueOf(page), true);
        }
    };

    public void getData(String category, String categoryid, String id, String pages, final boolean isResh) {
        Logger.i("=======[首页请求数据 page]===========" + page);
        if (XUtilNet.isNetConnected()) {
            try {
                List<CollectListBean> contentListBeen = new ArrayList<>();
                List<CollectListBean> all = DataSupport.order("id desc").find(CollectListBean.class);
                contentListBeen.addAll(all);
                Logger.i("=======[首页请求数据 page]===========" + page);
                mStateView.showSuccess();
                mPtrFrameLayout_public.refreshComplete();
                mMList.clear();
                if (contentListBeen.size() != 0) {
                    if (isResh) {
                        mMList.addAll(0, contentListBeen);
                    } else {
                        mMList.addAll(contentListBeen);
                    }
                    setFooterViewState(FooterViewTheEnd);
                } else {
                    Logger.i("=======[首页请求数据 page 收藏界面的]===========" + page);
                    if (mMList.size() == 0) {
                        mStateView.showNoData();
                    }
                    ToastUtil.showCenterToast(R.string.no_more);
                    //说明数据是空的
                    // setFooterViewState(FooterViewTheEnd);
                }
                myAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                if (mMList.size() == 0) {
                    loadLocaData();//加载本地数据
                } else {
                    setFooterViewState(FooterViewNetWorkError);//网络错误
                }
            }
            // 栏目id 第二个参数 分页的时候传
       /*     HomeModel.getMainList(category, id, pages, new NewInterface() {
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
            });*/
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
            List<CollectListBean> contentListBeen = new ArrayList<>();
            List<CollectListBean> all = findAll(CollectListBean.class);
            contentListBeen.addAll(all);
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
        EventBus.getDefault().unregister(this);
        if (mJcVideoPlayerStandard!=null){
            mJcVideoPlayerStandard.release();
            mJcVideoPlayerStandard = null;
        }
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
            CollectListBean bean;
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
     * 收藏成功添加 不管是添加还是删除 这边直接掉刷新就可以了
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void collectAdd(EventUtil.EnventCollectAdd event) {
        //  mPtrFrameLayout_public.autoRefresh(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.i("=======[setUserVisibleHint]===========" + isVisibleToUser);
        if (isVisibleToUser) {
            //当可见的时候 去查询数据库 如果数据变了 才去刷新 否则不刷新
            if (mMList != null) {
                if (mMList.size() != 0) {
                    List<CollectListBean> contentListBeen = new ArrayList<>();
                    List<CollectListBean> all = DataSupport.order("id desc").find(CollectListBean.class);
                    contentListBeen.addAll(all);
                    if (contentListBeen != null) {
                        if (contentListBeen.size() > mMList.size()) {
                            mPtrFrameLayout_public.autoRefresh(true);
                            mRecy_view.scrollToPosition(0);
                        }
                    }
                } else {
                    //开始没有数据 说明是没有加载过
                    mPtrFrameLayout_public.autoRefresh(true);
                    mRecy_view.scrollToPosition(0);
                }
            }
        } else {
            JCVideoPlayer.releaseAllVideos();
        }
    }
}
