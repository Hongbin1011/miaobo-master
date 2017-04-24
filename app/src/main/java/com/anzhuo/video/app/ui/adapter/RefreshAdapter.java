package com.anzhuo.video.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.CommentBean;
import com.anzhuo.video.app.model.bean.UserClickCommentBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.MyTextUtils;
import com.anzhuo.video.app.utils.NoDoubleClickListener;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.widget.CollapsibleDialog;
import com.anzhuo.video.app.widget.ViewUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static org.litepal.crud.DataSupport.where;


/**
 */
public class RefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mContext;
    LayoutInflater mInflater;
    List<CommentBean> mDatas;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;
    //加载失败
    public static final int NO_LOAD_ERRO = 3;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;

    private String mId;

    public RefreshAdapter(Activity context, List<CommentBean> datas, String mIds) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mId = mIds;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.comment_item, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = mInflater.inflate(R.layout.zx_recycler_load_more_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            try {
                CommentBean commentBean = mDatas.get(position);
                setData(itemViewHolder, commentBean, position);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (holder instanceof FooterViewHolder) {
            Logger.i("=======[没有更多数据了 adapter]===========" + mLoadMoreStatus);

            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
//                case PULLUP_LOAD_MORE:
//                    footerViewHolder.mTvLoadText.setText("上拉加载更多...");
//                    break;
//                case LOADING_MORE:
//                    footerViewHolder.mTvLoadText.setText("正加载更多...");
//                    break;
//                case NO_LOAD_ERRO:
//                    footerViewHolder.progressBar.setVisibility(View.GONE);
//                    footerViewHolder.mTvLoadText.setText("加载失败");
//                    break;
//                case NO_LOAD_MORE:
//                    //隐藏加载更多
//                    Logger.i("=======[没有更多数据了]===========");
//                    footerViewHolder.progressBar.setVisibility(View.GONE);
//                    footerViewHolder.mTvLoadText.setText("加载完成");
//                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        //RecyclerView的count设置为数据总条数+ 1（footerView）

        if (mDatas.size() == 0) {
            return 0;
        } else {
            Logger.i("=======[size大小3]===========" + mDatas.size());
            return mDatas.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            //最后一个item设置为footerView
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_jb;
        //设置数据
        TextView locations;
        TextView commentMains;
        TextView commentMainTimes;
        TextView tvItemZans;
        private final RelativeLayout mRe_main_comment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            locations = ViewUtil.findView(itemView, R.id.location);
            commentMains = ViewUtil.findView(itemView, R.id.comment_main);
            commentMainTimes = ViewUtil.findView(itemView, R.id.comment_main_time);
            tvItemZans = ViewUtil.findView(itemView, R.id.tv_item_joke_zan);
            mTv_jb = ViewUtil.findView(itemView, R.id.tv_jb);
            mRe_main_comment = ViewUtil.findView(itemView, R.id.re_main_comment);
        }


    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView mTvLoadText;
        ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mTvLoadText = (TextView) itemView.findViewById(R.id.foot_view_item_tv);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    /**
     * 添加数据至首部
     *
     * @param items
     */
    public void AddHeaderItem(List<CommentBean> items) {
        mDatas.addAll(0, items);
        Logger.i("=======[当前的状态]===========" + mDatas.toString());
        notifyDataSetChanged();
    }

    /**
     * 添加数据至底部
     *
     * @param items
     */
    public void AddFooterItem(List<CommentBean> items) {
        mDatas.addAll(items);
        Logger.i("=======[当前的状态 的数据 刷新]===========" + mDatas.size());
        notifyDataSetChanged();
    }

    /**
     * 更新加载更多状态
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        Logger.i("=======[当前的状态]===========" + status);
        notifyDataSetChanged();
    }

    private void setData(ItemViewHolder holder, final CommentBean contentListBean, final int positionss) {
        TextView location = holder.locations;
        TextView commentMain = holder.commentMains;
        TextView commentMainTime = holder.commentMainTimes;
        final TextView tvItemZan = holder.tvItemZans;
        TextView mTv_jb = holder.mTv_jb;

//        Logger.i("=======[设置数据 这里 id]===========" + contentListBean.toString());
        List<UserClickCommentBean> states2 = new ArrayList<UserClickCommentBean>();
//        Logger.i("=======[设置数据 这里 id]===========" + contentListBean.getId().toString());
//        states2.clear();
        if (!TextUtils.isEmpty(contentListBean.getId().toString())) {
            states2.add(where("itemid=? and dongtuId = ? ", contentListBean.getItemid(), contentListBean.getId()).findFirst(UserClickCommentBean.class));
            Logger.i("=======[设置数据 qian ]===========" + states2.toString());
            if (states2.get(0) != null) {
                setDrawbleAndText(mContext, tvItemZan, R.drawable.icon_like_selected, R.color.video_main);
            } else {
                setDrawbleAndText(mContext, tvItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
            }
        } else {
            setDrawbleAndText(mContext, tvItemZan, R.drawable.icon_like, R.color.text_remind_gray_light);
        }
        location.setText(contentListBean.getUsername());
        commentMain.setText(contentListBean.getMessage());
        commentMainTime.setText(contentListBean.getDateline());
        if (!TextUtils.isEmpty(contentListBean.getGoodnum())) {
            tvItemZan.setText(contentListBean.getGoodnum());
        } else {
            tvItemZan.setText("0");
        }
        mTv_jb.setOnClickListener(new NoDoubleClickListener() {

            private CollapsibleDialog mDialog;

            @Override
            public void onNoDoubleClick(View view) {
                mDialog = null;
                if (mDialog == null) {
                    //举报请求接口
                    mDialog = CollapsibleDialog.createExitPromptDialog(mContext, "确定", "取消", "确定要举报？", new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View view) {
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                            UpComment(contentListBean);//举报请求接口
                        }
                    });
                }
                if (mDialog != null)
                    mDialog.show();
            }

        });
        //评论点赞
        tvItemZan.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                CommentZan(contentListBean, tvItemZan);//评论点赞
            }
        });

        //点击隐藏键盘
        holder.mRe_main_comment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                DisplayUtil.hideKeyboard(mContext);
            }
        });
    }

    /**
     * 评论点赞
     *
     * @param contentListBean
     * @param tvItemZan
     */
    private void CommentZan(final CommentBean contentListBean, final TextView tvItemZan) {
        DisplayUtil.hideKeyboard(mContext);
        if (XUtilNet.isNetConnected()) {
            List<UserClickCommentBean> itemids = new ArrayList<UserClickCommentBean>();
            Logger.i("=======[data 评论点赞]===========" + itemids.toString());
            itemids.add(where("itemid=? and dongtuId = ? ", mId, contentListBean.getId()).findFirst(UserClickCommentBean.class));
            if (itemids.get(0) != null) {
                ToastUtil.showCenterToast("你已经点赞");
            } else {
                //评论点赞上报
                HomeModel.getCommentZan(contentListBean.getId(), "0", new NewInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showCenterToast("点赞失败");
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        if (state == 200) {
                            Logger.i("=======[data 评论点赞]===========" + obj.toString());
                            boolean save = new UserClickCommentBean()
                                    .setZan(true)
                                    .setCai(false)
                                    .setItemid(contentListBean.getItemid())
                                    .setDongtuId(contentListBean.getId())
                                    .setImei(Constants.IEMI)
                                    .setCommentIs(true)
                                    .save();
                            if (save) {
                                int goodnum = Integer.parseInt(contentListBean.getGoodnum()) + 1;
                                contentListBean.setGoodnum(String.valueOf(goodnum));
                                tvItemZan.setText(String.valueOf(goodnum));
                                setDrawbleAndText(mContext, tvItemZan, R.drawable.icon_like_selected, R.color.video_main);
//                                    myAdapter.notifyItemChanged(positionss);
                                notifyDataSetChanged();
                            }
                        } else {
                            ToastUtil.showCenterToast("点赞失败");
                        }
                    }
                });
            }
        } else {
            ToastUtil.showCenterToast(R.string.check_net_is_connect);
        }
    }

    /**
     * 举报请求
     *
     * @param contentListBean
     */
    private void UpComment(CommentBean contentListBean) {
        if (XUtilNet.isNetConnected()) {
            HomeModel.getReport(contentListBean.getId(), new NewInterface() {
                @Override
                public void onError(Call call, Exception e) {
                    ToastUtil.showCenterToast("举报失败");
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                    if (state == 200) {
                        ToastUtil.showCenterToast("举报成功");
                    }
                }
            });
        } else {
            ToastUtil.showCenterToast(R.string.check_net_is_connect);
        }
    }

    public static void setDrawbleAndText(Context mContext, TextView textView1, int DrawableID, int ColorID) {
        try {
            if (textView1 != null) {
                MyTextUtils.setTextDrawable(mContext, textView1, DrawableID);
                textView1.setTextColor(mContext.getResources().getColor(ColorID));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
