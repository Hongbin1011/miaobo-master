package com.anzhuo.video.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.TitlesBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.ui.base.BaseActivity;
import com.anzhuo.video.app.utils.DataBase.DatabaseUtils;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.widget.StateView;
import com.anzhuo.video.app.widget.StateViewOnclickCallback;
import com.anzhuo.video.app.widget.divider.GridSpacingItemDecoration;
import com.anzhuo.fulishipin.app.R;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;

/**
 * 添加 删除title
 */
public class AddTitleActivity extends BaseActivity {

    private RecyclerView mRecy_view;
    private List<TitlesBean> mList;
    private CommonAdapter<TitlesBean> mCommonAdapter;
    //    private ImageView mIco_selecteds;
//    private TextView mTview_titles;
    private LinearLayout mAddTitles;
    private RelativeLayout mRl_title;
    private StateView mTipModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_title);
        initView();

//        mTipModule = new ViewTipModule(this, mAddTitles, mRecy_view, new ViewTipModule.Callback() {
//            @Override
//            public void getData() {
//                getDta();
//            }
//        });
        mTipModule = new StateView(this)
                .setParentConfig(mAddTitles, mRecy_view)
                .setOnclick(stateViewOnclickCallback)
                .showLoading();
        getDta();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressedSupport();
        finish();
    }

    private void initView() {
        mList = new ArrayList<>();
        mAddTitles = (LinearLayout) findViewById(R.id.activity_add_titles);
        mRl_title = (RelativeLayout) findViewById(R.id.rl_title);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                finish();
            }
        });

        mRecy_view = (RecyclerView) findViewById(R.id.recy_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecy_view.setLayoutManager(gridLayoutManager);

        mCommonAdapter = new CommonAdapter<TitlesBean>(this, R.layout.item_add_titles, mList) {
            @Override
            protected void convert(ViewHolder holder, final TitlesBean titlesBean, final int position) {
                try {
                    Logger.i("=======[全部栏目]===========" + mList.get(position));
                    ImageView mIco_selecteds = holder.getView(R.id.ico_selecteds);//选择状态图标
                    TextView mTview_titles = holder.getView(R.id.tview_titles);//titles
                    mTview_titles.setText(titlesBean.getCategory().toString());
                    List<TitlesBean> titlesData = DatabaseUtils.getTitlesData(titlesBean.getCategory().toString());
                    if (titlesData.size() != 0) {//说明数据库中有数据
                        boolean seltor = titlesData.get(0).getSeltor();
                        if (seltor) {
                            mIco_selecteds.setVisibility(View.VISIBLE);
                        } else {
                            mIco_selecteds.setVisibility(View.GONE);
                        }
                    } else {
                        mIco_selecteds.setVisibility(View.GONE);
                    }

                    mTview_titles.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View view) {
                            //点击先去判断数据库中是否存在点击的title 如果是存在的 那说明是已经选中的
                            if (titlesBean.getCategory().toString().equals("推荐")) {//如果是推荐 不让取消
                                ToastUtil.showCenterToast("默认栏目不能取消");
                            } else {
                                boolean isExist = DatabaseUtils.selectsDataId(titlesBean.getCategory().toString());
                                if (isExist) {//选中的点击 取消
                                    DatabaseUtils.deleteDataid(titlesBean.getCategory());
                                    notifyItemChanged(position);
                                    sendEventChange(position, false);//发送事件
                                } else {//没有选中的点击 添加
                                    TitlesBean titlesBean1 = new TitlesBean();
                                    titlesBean1.setCategory(titlesBean.getCategory());
                                    titlesBean1.setSeltor(true);
                                    titlesBean1.save();
                                    notifyItemChanged(position);
                                    sendEventChange(position, true);//发送事件
                                }
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
        int line2 = DisplayUtil.px2dp(context, 44);
        int size = mList.size();
        Logger.i("=======[aaa]===========" + size);
        mRecy_view.addItemDecoration(new GridSpacingItemDecoration(100, line2, true));
    }


    /**
     * 获取数据
     */

    private void getDta() {
        try {
            if (XUtilNet.isNetConnected()) {
                HomeModel.getTitleType(new NewInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Logger.i("=======[请求栏目数据信息 错误]===========" + e.toString());
                        mTipModule.showNoData("加载失败", R.drawable.weiguanzhu);
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        Logger.i("=======[请求栏目数据信息2342]===========" + data.toString());
                        mTipModule.showSuccess();
                        if (state == 200) {
                            List<TitlesBean> titlesBeen = JSON.parseArray(data, TitlesBean.class);
                            Logger.i("=======[请求栏目数据信息23422342]===========" + titlesBeen.toString());
                            if (titlesBeen.size() == 0 || titlesBeen == null) {
                                mTipModule.showNoData("暂时没有分类", R.drawable.weiguanzhu);
                            } else {
                                mList.clear();
                                mList.addAll(titlesBeen);
                            }
                            mCommonAdapter.notifyDataSetChanged();
                        }
                    }
                });
            } else {//如果没有网络 取数据库中的
                ToastUtil.showCenterToast(R.string.check_net_is_connect);
                mTipModule.showNoNet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 没有网络加载本地数据
     */
//    private void NoNetLoad() {
//        mTipModule.showSuccess();
//        List<TitlesSaveBean> all = new ArrayList<>(); //取出本地的数据
//        all.addAll(DataSupport.findAll(TitlesSaveBean.class));
//        if (all.size() == 0) {//如果为0 说明第一次进来就是没有数据的
//            mTipModule.showNoNet();
//        } else {
//            mList.clear();
//            for (int i = 0; i < all.size(); i++) {
//                TitlesBean titlesBean = new TitlesBean();
//                titlesBean.setCategory(all.get(i).getCategory());
//                titlesBean.setSeltor(all.get(i).getRecommend().equals("1") ? true : false);
//                mList.add(titlesBean);
//            }
//            mCommonAdapter.notifyDataSetChanged();
//        }
//    }

    /**
     * 发送eventbus
     */
    public void sendEventChange(int position, boolean isAdd) {
        final EventUtil.EnventChangesData enventChangesData = new EventUtil.EnventChangesData();
        enventChangesData.positions = position;
        enventChangesData.isAdd = isAdd;
        EventBus.getDefault().post(enventChangesData);
    }

    @Override
    public void SetTag() {

    }

    public abstract class NoDoubleClickListener implements View.OnClickListener {

        /**
         * 控制不可连续点击的时间间隔[  修改控制时间间隔  ]
         */
        public static final int MIN_CLICK_DELAY_TIME = 200;//大时间间隔用于测试
        /**
         * 上一次点击的时间
         */
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }

        public abstract void onNoDoubleClick(View view);
    }

    /**
     * 状态切换点击事件
     */
    private StateViewOnclickCallback stateViewOnclickCallback = new StateViewOnclickCallback() {
        @Override
        public void NoNetClick() {
            Logger.i("没有网络点击事件");
            mList.clear();
            mTipModule.showLoading();
            getDta();
        }

        @Override
        public void NoDataClick() {
            Logger.i("没有数据点击事件");
            mList.clear();
            mTipModule.showLoading();
            getDta();
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
}
