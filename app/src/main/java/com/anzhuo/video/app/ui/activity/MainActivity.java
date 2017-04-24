package com.anzhuo.video.app.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.BuildConfig;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.constant.LoginConfig;
import com.anzhuo.video.app.manager.fuli2.FuliFragment;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.CheckVipBean;
import com.anzhuo.video.app.model.bean.LoginData;
import com.anzhuo.video.app.model.bean.TitlesBean;
import com.anzhuo.video.app.model.bean.TitlesDefaultBean;
import com.anzhuo.video.app.model.bean.VersionStartBean;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.model.home.StatisticsModel;
import com.anzhuo.video.app.search.ui.fragment.SearchFragment;
import com.anzhuo.video.app.ui.adapter.MainFragmentAdapter;
import com.anzhuo.video.app.ui.base.BaseActivity;
import com.anzhuo.video.app.ui.fragment.HomeFragment;
import com.anzhuo.video.app.ui.fragment.PersonalFragment;
import com.anzhuo.video.app.utils.ActivityManager;
import com.anzhuo.video.app.utils.DataBase.DatabaseUtils;
import com.anzhuo.video.app.utils.DialogUtils;
import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.SharedUtil;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.utils.commentutil.TempRegexUtil;
import com.anzhuo.video.app.utils.commentutil.TempTimeUtil;
import com.anzhuo.video.app.utils.permission.PermissionCallBack;
import com.anzhuo.video.app.utils.permission.PermissionUtil;
import com.anzhuo.video.app.widget.CustomViewPager;
import com.badoo.mobile.util.WeakHandler;
import com.lf.PayAndShare.tempALiPay.PayResult;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;




/**
 * OnFragmentInteractionListener
 */
public class MainActivity extends BaseActivity {

    private PersonalFragment mPresonalFragment;
    private SearchFragment mSearchFragment;
    private FuliFragment mWealFragment;


    @Override
    public void SetTag() {

    }

    //UI
    private HomeFragment homeFragment;//首页
    @BindView(R.id.vp_main_content)
    CustomViewPager viewPager;
    @BindView(R.id.rg_main)
    RadioGroup rg_main;
    //Data
    private MainFragmentAdapter adapter;
    FragmentManager fragmentManager;
    private List<Fragment> listFragment = new ArrayList<>();
    private WeakHandler mHandler;
    private int onBackPressedNum = 0;//用于双击返回键退出程序
    private PopupWindow mLoginPop;
    private TextView mPhone,mCode,mGetCode;
    private TempTimeUtil mTimeUtil;
    private boolean isFromCenter = true;
    private String mGoodsId;//1 包月 2 包年
    private String mPayId;//1 微信 2 支付宝
    private int currentSelectStatus = R.id.rb_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        SharedUtil.saveOnlyId();//先存入唯一识别号，如果需要就去缓存取
//        UpdateBuilder.create().check(MainActivity.this);//检查更新
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTitleData();
        if (BuildConfig.LOG_DEBUG) {
            MobclickAgent.setDebugMode(true);//友盟
        } else {
            MobclickAgent.setDebugMode(false);//友盟
        }
        mHandler = new WeakHandler();
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
        getPermission();
        initStartVieson();
        initRadio();
        initX5Blink();
    }

    private void initX5Blink() {
//        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
////        TbsDownloader.needDownload(getApplicationContext(), false);
//
//        Log.e("apptbs",""+ QbSdk.isTbsCoreInited());
//        if(!QbSdk.isTbsCoreInited()){
//            TbsDownloader.startDownload(this);
//        }
//
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                // TODO Auto-generated method stub
//                Log.e("apptbs", " onViewInitFinished is " + arg0);
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//                // TODO Auto-generated method stub
//
//            }
//        };
//        QbSdk.setTbsListener(new TbsListener() {
//            @Override
//            public void onDownloadFinish(int i) {
//                Log.e("apptbs", "onDownloadFinish");
//            }
//
//            @Override
//            public void onInstallFinish(int i) {
//                Log.e("apptbs", "onInstallFinish");
//            }
//
//            @Override
//            public void onDownloadProgress(int i) {
//                Log.e("apptbs", "onDownloadProgress:" + i);
//            }
//        });
//        QbSdk.initX5Environment(this, cb);
    }

    private void initRadio() {
        rg_main.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("------currentSelectStatus---"+currentSelectStatus);
                if (v.getId()==R.id.rb_home){
                    if (currentSelectStatus==R.id.rb_home){
                        Logger.e("---------发射值--------");
                        EventUtil.EnventRefesh enventRefesh = new EventUtil.EnventRefesh();
                        EventBus.getDefault().post(enventRefesh);
                    }
                    currentSelectStatus = v.getId();
                }
                Logger.e("------currentSelectStatus---"+currentSelectStatus);

            }
        });
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        viewpagerGetItem(0);
                        Logger.e("---------first--------");
//                        currentSelectStatus = checkedId;
                        break;
                    case R.id.rb_collect:
                        isFromCenter = true;
                        if (XUtilNet.isNetConnected() && LoginConfig.sf_getLoginState()){
                            viewpagerGetItem(1);
                            currentSelectStatus = checkedId;
                        }else {
                           showLoginPop();
                            rg_main.check(currentSelectStatus);
                        }
                        break;
                    case R.id.rb_search:
                        viewpagerGetItem(2);
                        currentSelectStatus = checkedId;
                        break;
                    case R.id.rb_weal:
                        viewpagerGetItem(3);
                        currentSelectStatus = checkedId;
                        break;
                }
            }
        });
    }

    //登录对话框
    private void showLoginPop(){
        //设置contentView
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_login_layout, null);
        mLoginPop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mLoginPop.setContentView(contentView);
         mPhone = (TextView) contentView.findViewById(R.id.login_phone);
         mCode = (TextView) contentView.findViewById(R.id.login_code);
         mGetCode = (TextView) contentView.findViewById(R.id.login_code_btn);
        Button mBtnQuit= (Button) contentView.findViewById(R.id.login_btn_quit);
        Button mBtnLogin= (Button) contentView.findViewById(R.id.login_btn_login);
        ImageView mClose= (ImageView) contentView.findViewById(R.id.dialog_close);
        mGetCode.setOnClickListener(mOnClickListener);
        mBtnQuit.setOnClickListener(mOnClickListener);
        mBtnLogin.setOnClickListener(mOnClickListener);
        mClose.setOnClickListener(mOnClickListener);
        //显示PopupWindow
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mLoginPop.showAtLocation(rootview, Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_code_btn://获取验证码

                    String phone = mPhone.getText().toString().trim();
                    if (phone!=null && !TextUtils.isEmpty(phone) && TempRegexUtil.checkMobile(phone)){
                        mTimeUtil = new TempTimeUtil(60000,1000,mGetCode);
                        getMarkMsg("",phone,"10");
                    }else {
                        ToastUtil.showToast("请输入正确的手机号码！");
                    }



                    break;
                case R.id.dialog_close:
                case R.id.login_btn_quit:
                    if (mLoginPop!=null && mLoginPop.isShowing()) {
                        mLoginPop.dismiss();
                        mLoginPop=null ;
                    }

                    break;
                case R.id.login_btn_login://登录接口

                    String _phone = mPhone.getText().toString().trim();
                    String _code = mCode.getText().toString();
                    if (_phone==null || TextUtils.isEmpty(_phone)){
                        ToastUtil.showToast("请输入电话号码");
                        return;
                    }
                    if (_code==null || TextUtils.isEmpty(_code)){
                        ToastUtil.showToast("请输入验证码");
                        return;
                    }
                    Login("",_phone,_code,"10");

                    break;
            }

        }
    };

    /**
     * 登录
     * @param s
     * @param phone
     * @param mark
     * @param reg_site
     */
    private void Login(String s, final String phone, String mark, String reg_site){

        HomeModel.getLogin(s, phone, mark, reg_site, new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                Logger.i("到这里了state"+state);
                String uid = "";
                List<LoginData> datas=JSON.parseArray(data,LoginData.class);
                if (state==200){
                    ToastUtil.showToast("登录成功");
                    LoginConfig.sf_saveLoginState(true);
                    LoginConfig.sf_saveUserPhone(phone);
                    if (datas!=null && !datas.isEmpty()){
                        Logger.e("--------------------------");
                        LoginData loginBean=datas.get(0);
                        uid = loginBean.getUid();
                        LoginConfig.sf_saveUserInfo(loginBean);
                        Logger.e("---------uid-----------------"+loginBean.getUid());
                    }

                    if (mLoginPop!=null && mLoginPop.isShowing()){
                        mLoginPop.dismiss();
                    }
                    if (isFromCenter){
                        EventUtil.EnventLoginInfo enventLoginInfo = new EventUtil.EnventLoginInfo();
                        enventLoginInfo.toLoginInfo = uid;
                        EventBus.getDefault().post(enventLoginInfo);
                        viewpagerGetItem(1);
                    }else {
                        //判断是否为会员 是会员发送消息 直接播放 不是就提示购买会员

                        checkUserVip("",LoginConfig.getUserPhone(),LoginConfig.getUserId(),"10");

                    }

                }else {
                    ToastUtil.showToast("登录失败");
                }


            }
        });
    }

    /**
     * 获取验证码
     * @param s
     * @param phone
     * @param reg_site
     */
    private void getMarkMsg(String s,String phone,String reg_site){

        HomeModel.getMsg(s, phone, reg_site, new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {

                if (state==200){
                    mTimeUtil.start();
                    ToastUtil.showToast("验证码发送成功");
                }else {
                    ToastUtil.showToast("验证码发送失败，请重试");
                }


            }
        });


    }

    @Override
    protected void onPause() {
        if (mTimeUtil != null) {
            mTimeUtil.onFinish();
        }
        super.onPause();
    }

    private void viewpagerGetItem(int pos) {
        viewPager.setCurrentItem(pos, false);
    }

    /**
     * 版本升级
     */
    private void initStartVieson() {

        HomeModel.getStart(new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {
                Logger.i("=======[加载失败]===========" + e.toString());
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                try {
                    Logger.i("=======[加载成功]===========" + data);
                    VersionStartBean versionStartBean = JSON.parseObject(data, VersionStartBean.class);
                    String share_url1 = versionStartBean.getShare_url();
                    Logger.i("=======[加载成功]===========" + share_url1);
                    if (!TextUtils.isEmpty(share_url1)) {
                        SharedUtil.putString("share_url", share_url1);
                    }
                    SharedUtil.putString("versionCode", versionStartBean.getVision());
                    SharedUtil.putString("download_url", versionStartBean.getDownload_url());
                    int versionCode = Constants.VersionCode;
                    if (!TextUtils.isEmpty(versionStartBean.getVision())) {
                        int nowVersionCode = Integer.parseInt(versionStartBean.getVision());
                        Logger.i("=======[nowVersionCode]===========" + nowVersionCode);
                        if (versionCode < nowVersionCode) {//有更新
                            if (XUtilNet.isNetConnected()) {
                                DialogUtils.showVersionUpDialog(MainActivity.this);
                            }
                        } else {
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化数据库
     */
    private void initTitleData() {
        try {
            boolean isFirstOpen = SharedUtil.getBoolean("isFirst", false);
            if (!isFirstOpen) {
                /*默认 推荐*/
                boolean isTuiJ = DatabaseUtils.selectsData("推荐");
                if (!isTuiJ) {
                    TitlesBean titlesBean = new TitlesBean();
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
                                    TitlesBean titlesBean = new TitlesBean();
//                                    titlesBean.setAlias(titlesDefaultBean.getAlias());
                                    titlesBean.setCategory(titlesDefaultBean.getCategory());
//                                    titlesBean.setModule(titlesDefaultBean.getModule());
                                    titlesBean.setSeltor(true);
//                                    titlesBean.setId(titlesDefaultBean.getId());
                                    titlesBean.saveThrows();
                                    Logger.i("=======[取出的数据 之保存数据]===========" + titlesBean.toString());
                                }
                            }
                        }
                        //发送eventbus 有可能这还没有走完 homefragemtn 就已经创建完成 了
                        EventUtil.EnventSendStateChangeLocaData p = new EventUtil.EnventSendStateChangeLocaData();
                        EventBus.getDefault().post(p);
                        SharedUtil.putBoolean("isFirst", true);
                    }
                });
            } else {
                Logger.i("=======[取出的数据 第二次进来 不保存数据]===========");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("=======[保存出错]===========");
        }
    }

    /**
     * 获取权限
     */
    private void getPermission() {
        PermissionUtil.getStoragePermission(this, new PermissionCallBack() {
            @Override
            public void PermissionSuccess() {
                //广告sdk 初始化检查权限 外部权限请求成功了 再去初始化 否则将看不到sdk 的授权框
//                StartSDK.initSDK(MainActivity.this);//广告sdk
            }

            @Override
            public void PermissionFail() {
            }
        });

//        PermissionUtil.getPermission(this, new PermissionCallBack() {
//            @Override
//            public void PermissionSuccess() {
//
//            }
//
//            @Override
//            public void PermissionFail() {
//
//            }
//        }, Manifest.permission.WRITE_SETTINGS);
    }


    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();

        mSearchFragment = SearchFragment.newInstance();//收索
        mWealFragment = FuliFragment.getInstance();//福利中心
        mPresonalFragment = PersonalFragment.newInstance();//个人中心

        listFragment.add(homeFragment);
        listFragment.add(mPresonalFragment);
        listFragment.add(mSearchFragment);
        listFragment.add(mWealFragment);

        adapter = new MainFragmentAdapter(fragmentManager, listFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
//        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
    }


    @Override
    public void onBackPressed() {
//        CrashReport.testJavaCrash();
        if (JCVideoPlayer.backPress()) {
            return;
        }
        onBackPressedNum++;
        if (onBackPressedNum >= 2) {
            moveTaskToBack(false);
            ActivityManager.getActivityManager().AppExit();
        } else {
            ToastUtil.showToast(getResources().getString(R.string.exit, getResources().getString(R.string.app_name)));
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressedNum = 0;
                }
            }, 1000);
        }
    }


    /**
     * 统计APP打开次数
     */
    private void StatisticsAppOpenNumber() {
        final boolean isFirstOpen = SharedUtil.getBoolean("isFirstOpen", true);
        //统计第一次打开app  每次安装只执行一次
        if (isFirstOpen) {
            if (isFirstOpen) {
                SharedUtil.putBoolean("isFirstOpen", false);
                StatisticsModel.FirstOpen(new NewInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        if (state == 200 && data.equals("true")) {
//                        SharedUtil.putBoolean("isFirstOpen", false);
                        }
                    }
                });
            }
            //统计app打开次数，每次重新打开都执行
            StatisticsModel.OpenApp(new NewInterface() {
                @Override
                public void onError(Call call, Exception e) {
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showLogin(EventUtil.EnventLogin event){
        isFromCenter = false;
        Logger.e("------HomeList---获取推送-");
        if (!event.toLogin){
            showLoginPop();
        }else {//登录后验证是否VIP
            checkUserVip("",LoginConfig.getUserPhone(),LoginConfig.getUserId(),"10");

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showJoinVipPop(EventUtil.EnventJoinVip event){

        Logger.e("------EnventJoinVip---获取推送-");
        showBuyVipNoticePop(this,"包月（￥8）","包年（￥50）",R.layout.activity_main,"加入会员","请选择加入的会员类型","取消","确定",1);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showContinueVipPop(EventUtil.EnventContinueVip event){

        Logger.e("------EnventContinueVip---获取推送-");
    }

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
                        showBuyVipNoticePop(getBaseContext(),"包月（￥8）","包年（￥50）",R.layout.activity_main,"加入会员","请选择加入的会员类型","取消","确定",1);

                    }else if (vipBean.getVip_level()==1 || vipBean.getVip_level()==2){
                        EventUtil.EnventToPlay enventToPlay = new EventUtil.EnventToPlay();
                        EventBus.getDefault().post(enventToPlay);
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
    private void showBuyVipNoticePop(Context context,String btn1Str,String btn2Str, int rootlayout, String title, String content, String leftBtn, String rightBtn, final int what){
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
                    showBuyVipNoticePop(getBaseContext(),"微信支付","支付宝支付",R.layout.activity_main,"加入会员","请选择支付方式","取消","确定",2);

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

    //是否为会员弹窗
    private void showVipNoticePop(Context context, int rootlayout, String title, String content, String leftBtn, String rightBtn){
        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_common_notice_layout, null);
        ((TextView) (contentView.findViewById(R.id.dialog_title))).setText(title);
        ((TextView) (contentView.findViewById(R.id.dialog_notice_content))).setText(content);
        ((Button) (contentView.findViewById(R.id.dialog_btn_left))).setText(leftBtn);
        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setText(rightBtn);
        mLoginPop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mLoginPop.setContentView(contentView);

        //显示PopupWindow
        View rootview = LayoutInflater.from(context).inflate(rootlayout, null);
        mLoginPop.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        ((Button) (contentView.findViewById(R.id.dialog_btn_left))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginPop!=null && mLoginPop.isShowing())
                    mLoginPop.dismiss();
            }
        });

        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LoginConfig.sf_getLoginState()){

                    //TODO 判断是否为会员
                }else {
                    showLoginPop();

                }

            }
        });

    }
    private static final int SDK_PAY_FLAG = 1;
    private android.os.Handler mAliPayHandler = new android.os.Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    Logger.e("payResult.toString()"+payResult.toString());

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    Logger.e(resultInfo);
                    String resultStatus = payResult.getResultStatus();

                    Logger.e(resultStatus);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Logger.e("支付成功");
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String date = sDateFormat.format(new java.util.Date());
                        Logger.e("payResult.toString()" + payResult.toString());
                        Logger.e("---------支付时间 date--------" + date);

                        //TODO  在此进行支付回调 接口

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getBaseContext(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(ActPayment.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
            }
            return false;
        }
    });



    //退出
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitLogin(EventUtil.EnventExit event){
        LoginConfig.sf_saveLoginState(false);//
        viewpagerGetItem(0);
        rg_main.check(R.id.rb_home);
    }


}
