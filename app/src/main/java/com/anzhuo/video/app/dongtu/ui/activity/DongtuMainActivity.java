package com.anzhuo.video.app.dongtu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.BuildConfig;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuTitlesBean;
import com.anzhuo.video.app.dongtu.bean.dongtu.TitlesDefaultBean;
import com.anzhuo.video.app.dongtu.model.base.HomeModel;
import com.anzhuo.video.app.dongtu.model.base.NewInterface;
import com.anzhuo.video.app.dongtu.ui.adapter.MainFragmentAdapter;
import com.anzhuo.video.app.dongtu.ui.base.DongtuBaseActivity;
import com.anzhuo.video.app.dongtu.ui.fragment.DongtuHomeFragment;
import com.anzhuo.video.app.dongtu.utils.CustomViewPager;
import com.anzhuo.video.app.dongtu.utils.DatabaseUtils;
import com.anzhuo.video.app.dongtu.utils.EventUtil;
import com.anzhuo.video.app.dongtu.utils.SharedUtil;
import com.badoo.mobile.util.WeakHandler;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * OnFragmentInteractionListener
 */
public class DongtuMainActivity extends DongtuBaseActivity {

    @Override
    public void SetTag() {

    }

    //UI
    private DongtuHomeFragment homeFragment;//首页
    CustomViewPager viewPager;
    //Data
    private MainFragmentAdapter adapter;
    FragmentManager fragmentManager;
    private List<Fragment> listFragment = new ArrayList<>();
    private WeakHandler mHandler;
    private int onBackPressedNum = 0;//用于双击返回键退出程序


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UpdateBuilder.create().check(MainActivity.this);//检查更新
        setContentView(R.layout.dongtuactivity_main);
        SharedUtil.saveOnlyId();//先存入唯一识别号，如果需要就去缓存取
        viewPager = (CustomViewPager) findViewById(R.id.vp_main_content);
//        ButterKnife.bind(this);
        initTitleData();
        if (BuildConfig.LOG_DEBUG) {
            MobclickAgent.setDebugMode(true);//友盟
        } else {
            MobclickAgent.setDebugMode(false);//友盟
        }
        mHandler = new WeakHandler();
        Logger.i("cq=============[viewPager]===========" + viewPager);
        viewPager.setScanScroll(false);
//        rg_main.getBackground().setAlpha(100);//0~255透明度值 ，0为完全透明，255为不透明

        //---------------解决重影------------
        // 这里一定要在save为null时才加载Fragment，Fragment中onCreateView等生命周里加载根子Fragment同理
        // 因为在页面重启时，Fragment会被保存恢复，而此时再加载Fragment会重复加载，导致重叠
        if (savedInstanceState == null) {
            // 这里加载根Fragment
            initFragment();
        } else {
            initFragment();
        }

        StatisticsAppOpenNumber();
//        getPermission();
    }

    /**
     * 初始化数据库
     */
    private void initTitleData() {
        try {
            boolean isFirstOpen = SharedUtil.getBoolean("isFirstDongtu", false);
            if (!isFirstOpen) {
                /*默认 推荐*/
                boolean isTuiJ = DatabaseUtils.selectsData("推荐");
                if (!isTuiJ) {
                    DongtuTitlesBean titlesBean = new DongtuTitlesBean();
//                    titlesBean.setAlias("tuijian");
                    titlesBean.setCategory("推荐");
//                    titlesBean.setModule("0");
                    titlesBean.setSeltor(true);
//                    titlesBean.setId("0");
                    titlesBean.saveThrows();
                }
                HomeModel.getTitleType(new NewInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        List<TitlesDefaultBean> titlesDefaultBeen = JSON.parseArray(data, TitlesDefaultBean.class);
//                        TITLES = getResources().getStringArray(R.array.navigation_bar);
//                        mTITLES_id = getResources().getStringArray(R.array.navigation_bar_id);
//                        mMTITLES_pingy = getResources().getStringArray(R.array.navigation_bar_pingyin);
                        for (int i = 0; i < titlesDefaultBeen.size(); i++) {
                            TitlesDefaultBean titlesDefaultBean = titlesDefaultBeen.get(i);
                            String state1 = titlesDefaultBean.getRecommend();
                            if (state1.equals("1")) {//说明是默认选中的
                                boolean isTitles = DatabaseUtils.selectsData(titlesDefaultBean.getCategory());//如果没有查询到 就说明没有
                                if (!isTitles) {
                                    DongtuTitlesBean titlesBean = new DongtuTitlesBean();
//                                    titlesBean.setAlias(titlesDefaultBean.getAlias());
                                    titlesBean.setCategory(titlesDefaultBean.getCategory());
//                                    titlesBean.setModule(titlesDefaultBean.getModule());
                                    titlesBean.setSeltor(true);
//                                    titlesBean.setId(titlesDefaultBean.getId());
                                    titlesBean.saveThrows();
                                    Logger.i("cq=============[取出的数据 之保存数据]===========" + titlesBean.toString());
                                }
                            }
                        }
                        //发送eventbus 有可能这还没有走完 homefragemtn 就已经创建完成 了
                        EventUtil.EnventSendStateChangeLocaData p = new EventUtil.EnventSendStateChangeLocaData();
                        EventBus.getDefault().post(p);
                        SharedUtil.putBoolean("isFirstDongtu", true);
                    }
                });
            } else {
                Logger.i("cq=============[取出的数据 第二次进来 不保存数据]===========");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("cq=============[保存出错]===========");
        }
    }

    /**
     * 获取权限
     */
//    private void getPermission() {
//        PermissionUtil.getStoragePermission(this, new PermissionCallBack() {
//            @Override
//            public void PermissionSuccess() {
//                //广告sdk 初始化检查权限 外部权限请求成功了 再去初始化 否则将看不到sdk 的授权框
////                StartSDK.initSDK(MainActivity.this);//广告sdk
//            }
//
//            @Override
//            public void PermissionFail() {
//            }
//        });
//    }
    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        homeFragment = new DongtuHomeFragment();
        listFragment.add(homeFragment);
        adapter = new MainFragmentAdapter(fragmentManager, listFragment);
        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
    }

    @Override
    public void onBackPressed() {
        finish();
//        onBackPressedNum++;
//        if (onBackPressedNum >= 2) {
//            moveTaskToBack(false);
//            ActivityManager.getActivityManager().AppExit();
//        } else {
//            DongtuToastUtil.showToast(getResources().getString(R.string.exit, getResources().getString(R.string.app_name)));
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    onBackPressedNum = 0;
//                }
//            }, 1000);
//        }
    }


    /**
     * 统计APP打开次数
     */
    private void StatisticsAppOpenNumber() {
//        final boolean isFirstOpen = SharedUtil.getBoolean("isFirstOpen", true);
//        //统计第一次打开app  每次安装只执行一次
//        if (isFirstOpen) {
//            if (isFirstOpen) {
//                SharedUtil.putBoolean("isFirstOpen", false);
//                StatisticsModel.FirstOpen(new NewInterface() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                    }
//
//                    @Override
//                    public void onSucceed(int state, String data, JSONObject obj) {
//                        if (state == 200 && data.equals("true")) {
////                        SharedUtil.putBoolean("isFirstOpen", false);
//                        }
//                    }
//                });
//            }
//            //统计app打开次数，每次重新打开都执行
//            StatisticsModel.OpenApp(new NewInterface() {
//                @Override
//                public void onError(Call call, Exception e) {
//                }
//
//                @Override
//                public void onSucceed(int state, String data, JSONObject obj) {
//                }
//            });
//        }
    }

}
