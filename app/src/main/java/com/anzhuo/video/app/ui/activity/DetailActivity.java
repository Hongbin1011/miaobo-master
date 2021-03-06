package com.anzhuo.video.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.NIManage;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.ContentListBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.ui.base.BaseActivity;
import com.anzhuo.video.app.ui.fragment.DetailFragment;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.FrescoUtils.FrescoUtil;
import com.anzhuo.video.app.utils.NoDoubleClickListener;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.orhanobut.logger.Logger;
import com.sdk9500.media.StartSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class DetailActivity extends BaseActivity {
    private ViewPager mViewpager;
    private MyViewPagerAdapter mMyViewPagerAdapter;
    private List<Fragment> mList;
    private String mPosition;//点击的位置
    private List<ContentListBean> mListContent;//首页的数据集合
    private String Category;
    private String mPage;
    private ImageView mImg_back;
    int page = 0;
    private TextView mTv_no_net_tip;
    private boolean mIsSerach;
    private String m9500Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fragment);
        EventBus.getDefault().register(this);
        initView();
        getAdState();//广告

    }


    private void initView() {
        mList = new ArrayList<>();
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mMyViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mList);
        mViewpager.addOnPageChangeListener(new MyOnPageChangeListener());
//        mViewpager.setOffscreenPageLimit(0);
        initData();

        mTv_no_net_tip = (TextView) findViewById(R.id.tv_no_net_tip);//网络提示
        initNetView();//初始化网络提醒框

        mViewpager.setAdapter(mMyViewPagerAdapter);
        mViewpager.setCurrentItem(Integer.parseInt(mPosition));
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mImg_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (!mIsSerach) {
                    sendChangePostition();
                }
                finish();
            }
        });
        //详情title 的根部局 点击隐藏键盘
        RelativeLayout rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_title.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                DisplayUtil.hideKeyboard(DetailActivity.this);
            }
        });
    }

    public static final String intentGetTag = "list";

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            mListContent = (ArrayList<ContentListBean>) bundle.getSerializable(intentGetTag);
            mPosition = bundle.getString("position");
            mPage = bundle.getString("page");
            positionList.add(Integer.valueOf(mPosition));
            Category = bundle.getString("Category");
            page = Integer.parseInt(mPage);
            mIsSerach = bundle.getBoolean("isSerach");//是否为收索
            Logger.i("position===========[传值2222]===========:" + mPage);
        }

        getFragementList();
    }

    /**
     * 初始化网络条
     * 有网隐藏、无网显示
     */
    private void initNetView() {
        if (XUtilNet.isNetConnected()) {
            mTv_no_net_tip.setVisibility(View.GONE);
        } else {
            mTv_no_net_tip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
        mListContent.clear();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 加载详情fragement
     */
    private void getFragementList() {
        int size = mListContent.size();
        for (int i = 0; i < size; i++) {
            ContentListBean bean = mListContent.get(i);
            DetailFragment detailFragment = DetailFragment.newInstance(bean, Category,
                    positionList.size() == 0 ? 0 : positionList.get(positionList.size() - 1), mIsSerach);
            mList.add(detailFragment);
        }
    }

    /**
     * 当滑动到最后一页的时候请求后面的数据
     */
    private void getData() {
        try {
            if (XUtilNet.isNetConnected()) {
                page = page + 1;
                HomeModel.getMainList(Category, mListContent.get(mListContent.size() - 1).getId(), String.valueOf(page), new NewInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Logger.i("=======[请求失败]===========");
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {

                        Logger.i("=======[请求成功 评论列表 住中的]===========" + data);
                        if (state == 200) {
                            if (data.length() > 2) {
                                List<ContentListBean> mCommentBean = JSON.parseArray(data, ContentListBean.class);
                                //不为空
                                if (mCommentBean != null) {
                                    mListContent.addAll(mCommentBean);
                                    mList.clear();
                                    getFragementList();
                                    mMyViewPagerAdapter.notifyDataSetChanged();
                                } else {//为空
                                }
                            } else {
                                //位空
                            }
                        } else {
                        }
                    }
                });
            } else {
                ToastUtil.showCenterToast(R.string.check_net_is_connect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SetTag() {

    }

    /**
     * Fragment比较多的情况，我们需要切换的时候销毁以前的Fragment以释放内存 FragmentStatePagerAdapter
     */
    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> mList;

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

    List<Integer> positionList = new ArrayList<>();

    /**
     * viewpager滑动监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (!mIsSerach) {
                positionList.add(position);
                int currPosition = positionList.get(positionList.size() - 1);
                Logger.i("=======[当前fragment 的position]===========" + currPosition);
                int size = mListContent.size();
                Logger.i("=======[当前fragment 的positionsize]===========" + size);
                if ((currPosition + 1) == size) {//相等 表示已经滑动到了 最后一页了 加载数据
                    Logger.i("=======[当前fragment 的positionsize2]===========" + size);
                    getData();
                }
                //同步外面列表的位置
                sendChangePostition();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //当滑动的时候 不让fresco 加载网络 否则会有小小的卡顿现在
            //当停止的时候 才让其加载 或者 自动滑动
            switch (state) {
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressedSupport();
        if (!mIsSerach) {
            sendChangePostition();
        }
        finish();
    }

    /**
     * 改变位置
     */
    private void sendChangePostition() {
        EventUtil.EnventSendStateChangePosition changePosition = new EventUtil.EnventSendStateChangePosition();
        changePosition.ToPosition = positionList.get(positionList.size() - 1);
        EventBus.getDefault().post(changePosition);
    }

    /**
     * 接受网络状态变化
     *
     * @param ListenerNetEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenerNetEvent(EventUtil.ListenerNetEvent ListenerNetEvent) {
        if (ListenerNetEvent.isHasNet) {
            mTv_no_net_tip.setVisibility(View.GONE);
        } else {
            mTv_no_net_tip.setVisibility(View.VISIBLE);
        }
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
//                        gdtStatus = jsonObject.getString("gdt_status");
//                        bdStatus = jsonObject1.getString("bd_status");
                        m9500Status = jsonObject2.getString("9500_status");
                        XUtilLog.log_i("wbb", "=======[广告状态数据1]=======:" + m9500Status);

//                        Constants.APPID = jsonObject.getString("appid");
//                        Constants.InterteristalPosID = jsonObject.getString("interteristalposID");
                        if (m9500Status.equals("1")) {
                            handler2.sendEmptyMessageDelayed(12, 15000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 12:
                    XUtilLog.log_i("wbb", "弹广告了 ... ... ");
                    StartSDK.showInApp(DetailActivity.this);
                    break;
            }
        }
    };


}
