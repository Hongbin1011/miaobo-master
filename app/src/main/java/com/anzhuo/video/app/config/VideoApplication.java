package com.anzhuo.video.app.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.anzhuo.fulishipin.app.BuildConfig;
import com.anzhuo.video.app.constant.Configs;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.message.MessagePump;
import com.lf.PayAndShare.TempShareConfiger;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.sdk9500.media.StartSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.wapu.wadelibrary.WadeCheck;

import org.litepal.LitePalApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * created on 2016/1/19.
 */
public class VideoApplication extends LitePalApplication {
    String[] TITLES;
    private static VideoApplication sInstance;
    private static Context mContext;
    private static SharedPreferences mPreferences;
    private boolean DEBUG = BuildConfig.LOG_DEBUG;
    private String[] mTITLES_id;


    //消息
    public MessagePump getMessagePump() {
        if (mMessagePump == null)
            mMessagePump = new MessagePump();
        return mMessagePump;
    }

    public static Context getContext() {
        return mContext;
    }

    public static synchronized VideoApplication getInstance() {
        return sInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        if (DEBUG){
//        FreelineCore.init(this); //快速启动应用
//        }
        sInstance = this;
        // 上下文
        mContext = getApplicationContext();
        //初始化log
        initLogger();
//        //极光
//        initJPushInterface();
        //开子线程初始化fresco
        com.anzhuo.video.app.utils.FrescoUtils.FrescoUtil.init(getApplicationContext(), 40);
        LitePalApplication.initialize(VideoApplication.getContext());
        //异步初始化第三方包
//        this.startService(new Intent(this, InitIntentService.class));
        //设置EventBus
//        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        //数据库注
        //设置网络。线下和线上
        Configs.init();
        com.anzhuo.video.app.meinv.constant.Configs.init();
        com.anzhuo.video.app.dongtu.config.Configs.init();

        initShareConfig();

        //9500 广告sdk
        StartSDK.initSDK(this);
        //设置web页面title布局的颜色,默认为蓝色
        StartSDK.setWebTitleColor("#d43d3d");

//        ShareSDK.initSDK(this);//初始化sharesdk
        //初始化sharedpref
        mPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
//        addDefaultCache(mPreferences);
        //监听APP是否在前台
        this.registerActivityLifecycleCallbacks(new com.anzhuo.video.app.utils.AppLifecycleHandler());


        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Constants.Pwide = width;
        Constants.Phigh = height;
        QbSdk.initX5Environment(getApplicationContext(), null);


        WadeCheck.init(getContext(), "com.anzhuo.fulishipin.app", "fulimovie");
        initBugly();
    }

    private void initShareConfig(){
        new TempShareConfiger().initConfig();
    }

    /**
     * 初始化logger
     */
    private void initLogger() {
        LogLevel logLevel;
        if (DEBUG) {
            logLevel = LogLevel.FULL;
        } else {
            logLevel = LogLevel.NONE;
        }
        Logger.init("dongla")        // default PRETTYLOGGER or use just init()  设置tag
                .methodCount(1)                 // default 2  几个方法
                .hideThreadInfo()               // default shown  隐藏线程
                .logLevel(logLevel);      //正式版需要改这里
    }

    private MessagePump mMessagePump;


    /**
     * 添加默认缓存（假数据）
     *
     * @param mPreferences
     */

    private void addDefaultCache(SharedPreferences mPreferences) {
        String defaultCache = mPreferences.getString("cache", "-1");
        SharedPreferences.Editor editor = mPreferences.edit();
        if (defaultCache.equals("-1")) {
            editor.putString("cache", "0");
            String str = com.anzhuo.video.app.utils.TimeUtils.getSystemTime("yyyyMMdd");
            editor.putString("date", str);
        }
        editor.commit();
    }
//
//    private void initJPushInterface() {
//        JPushInterface.setDebugMode(DEBUG);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);            // 初始化 JPush
//        JPushInterface.setLatestNotificationNumber(this, 1);//设置保留最近通知条数
//    }

    public static synchronized SharedPreferences getPreferences() {
        return mPreferences;
    }

    private void initBugly() {
        Context context = getApplicationContext();
        String packageName = context.getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(context, Constants.BUGLY_ID, DEBUG, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}