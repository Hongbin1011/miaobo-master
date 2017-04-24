package com.anzhuo.video.app.meinv.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.meinv.entity.NavigationInfo;
import com.anzhuo.video.app.meinv.manager.MyInterface;
import com.anzhuo.video.app.meinv.manager.NIManage;
import com.anzhuo.video.app.meinv.tool.GridSpacingItemDecoration;
import com.anzhuo.video.app.meinv.utils.DatabaseUtils;
import com.anzhuo.video.app.meinv.view.ViewTipModule;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.search.utils.ViewUtil;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.anzhuo.video.app.message.Message.Type.UPDATE_LABEL_LIST;


/**
 * 添加 删除title
 */
public class AddTitleActivity extends BaseActivity {

    private RecyclerView mRecy_view;
    private List<NavigationInfo> mList;
    private CommonAdapter<NavigationInfo> mCommonAdapter;
    private ImageView mIco_selecteds;
    private TextView mTview_titles;
    private LinearLayout mAddTitles;
    private RelativeLayout mRl_title;
    private ViewTipModule mTipModule;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_title2meinv;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        initView();
        mTipModule = new ViewTipModule(this, mAddTitles, mRecy_view, null);
        List<NavigationInfo> list = DatabaseUtils.getAll();
        if (list == null || list.size() == 0 || list.isEmpty()) {
            getDta();
        } else {
            mList.clear();
            mList.addAll(list);
            mList.remove(0);
            mCommonAdapter.notifyDataSetChanged();
            mTipModule.showSuccessState();
        }
    }

    private void initView() {
        mList = new ArrayList<>();
        mAddTitles = (LinearLayout) findViewById(R.id.activity_add_titles);
        mRl_title = (RelativeLayout) findViewById(R.id.rl_title);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecy_view = (RecyclerView) findViewById(R.id.recy_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecy_view.setLayoutManager(gridLayoutManager);

        mCommonAdapter = new CommonAdapter<NavigationInfo>(this, R.layout.video_item_add_titles, mList) {
            @Override
            protected void convert(ViewHolder holder, final NavigationInfo titlesBean, final int position) {
                try {
                    mIco_selecteds = holder.getView(R.id.ico_selecteds);//选择状态图标
                    mTview_titles = holder.getView(R.id.tview_titles);//titles
                    mTview_titles.setText(titlesBean.getTags().toString());
                    if (titlesBean.getTags().trim().equals("推荐")) {
                        ViewUtil.gone(mIco_selecteds, mTview_titles);
                    }
                    List<NavigationInfo> titlesData = DatabaseUtils.getTitlesData(titlesBean.getTags().toString());
                    if (titlesData.size() != 0) {//说明数据库中有数据
                        String seltor = titlesData.get(0).getRecom();
                        if ("1".equals(seltor)) {
                            if (!titlesBean.getTags().trim().equals("推荐")) {
                                mIco_selecteds.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mIco_selecteds.setVisibility(View.GONE);
                        }
                    } else {
                        mIco_selecteds.setVisibility(View.GONE);
                    }
                    mTview_titles.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击先去判断数据库中是否存在点击的title 如果是存在的 那说明是已经选中的
                            String type = DatabaseUtils.selectsDataId(titlesBean.getTags().toString());
                            if (type.equals("1")) {//选中的点击 取消
                                DatabaseUtils.updataData(titlesBean.getTags(), "0");
                                notifyItemChanged(position);
                            }
                            if (type.equals("0")) {//没有选中的点击 添加
                                DatabaseUtils.updataData(titlesBean.getTags(), "1");
                                notifyItemChanged(position);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mRecy_view.setAdapter(mCommonAdapter);
        //分割线
        int line2 = DisplayUtil.px2dp(mContext, 44);
        int size = mList.size();
        mRecy_view.addItemDecoration(new GridSpacingItemDecoration(100, line2, true));
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    /**
     * 获取数据
     */
    private void getDta() {
        try {
            NIManage.getTitleType(new MyInterface() {
                @Override
                public void onError(Call call, Exception e) {
                    mTipModule.showFailState(ViewTipModule.EMPTY_DATA_SUGGEST_LOADING_FAILURE);
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                    mTipModule.showSuccessState();
                    if (state == 200) {
                        List<NavigationInfo> titlesBeen = JSON.parseArray(data, NavigationInfo.class);
                        if (titlesBeen == null || titlesBeen.size() == 0) {
                            mTipModule.showNoDataState("暂时没有分类");
                        } else {
                            DataSupport.saveAll(titlesBeen);
                            mList.clear();
                            mList.addAll(titlesBeen);
                            mList.remove(0);
                        }
                        mCommonAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.toastLong(mContext, "服务器异常");
                    }
                }
            });
        } catch (Exception e) {
            mTipModule.showFailState(ViewTipModule.EMPTY_DATA_SUGGEST_LOADING_FAILURE);
            e.printStackTrace();
        }
    }

    @Override
    protected void attachAllMessage() {
        attachMessage(UPDATE_LABEL_LIST);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoApplication.getInstance().getMessagePump().
                broadcastMessage(UPDATE_LABEL_LIST);
        XUtilLog.log_i("wbb", "发送消息了");
    }
}
