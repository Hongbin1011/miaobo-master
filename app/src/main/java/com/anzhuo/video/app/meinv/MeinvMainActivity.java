package com.anzhuo.video.app.meinv;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.manager.fuli2.utils.ActivityUtils;
import com.anzhuo.video.app.meinv.activity.AddTitleActivity;
import com.anzhuo.video.app.meinv.activity.SearchActivity;
import com.anzhuo.video.app.meinv.adapter.IndicatorFragmentAdapter;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.meinv.entity.NavigationInfo;
import com.anzhuo.video.app.meinv.manager.ActivityJump;
import com.anzhuo.video.app.meinv.manager.DefaultSharePrefManager;
import com.anzhuo.video.app.meinv.manager.MyInterface;
import com.anzhuo.video.app.meinv.manager.NIManage;
import com.anzhuo.video.app.meinv.manager.PermissionManager;
import com.anzhuo.video.app.meinv.utils.DatabaseUtils;
import com.anzhuo.video.app.meinv.utils.FileUtils;
import com.anzhuo.video.app.meinv.view.CollapsibleDialog;
import com.anzhuo.video.app.meinv.view.ShareDialog;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.FileUtil;
import com.anzhuo.video.app.utils.HttpUtils;
import com.anzhuo.video.app.utils.ViewUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.anzhuo.video.app.message.Message.Type.UPDATE_LABEL_LIST;


public class MeinvMainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private IndicatorViewPager indicatorViewPager;
    private List<NavigationInfo> selectedInfos;
    private List<NavigationInfo> navigationInfos;
    private List<NavigationInfo> dbListInInfo;
    private RecyclerView mRVAllType;
    private List<String> datas = new ArrayList<>();
    private CommonAdapter<String> adapter;
    private ScrollIndicatorView scrollIndicatorView;
    //全部类型按钮
    private ImageView refreshImg,collectionImg;
    private ImageView mImageView;
    private RelativeLayout rlBtnContainer;

    private String shareUrl="分享链接失效，请重启APP试试！";
    private CollapsibleDialog dialog = null;
    private int MY_PERSSION_WRITE_EXTERNAL_STORAGE = 1010;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main_m;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
//        setupStatus();
        for (int i = 0; i < 30; i++) {
            datas.add("" + i);
        }
        findViewAttachOnclick(R.id.meinv_back);
        mRVAllType = findView(R.id.rv_all_type);
        refreshImg = findView(R.id.refresh_img);
        collectionImg = findView(R.id.collection_img);
        refreshImg.setOnClickListener(this);
        collectionImg.setOnClickListener(this);

        initRecyclerView();
        selectedInfos = new ArrayList<>();
        navigationInfos = new ArrayList<>();
        mViewPager = findView(R.id.vp_mian);
        findViewAttachOnclick(R.id.rl_search);
        findViewAttachOnclick(R.id.application_relevant);
        mImageView = findViewAttachOnclick(R.id.iv_all_type);
        checkMyPermission();
        //得到版本信息
//        getVersionData();
        //第一次启动app
//        FirstOpenApp();
        //app启动上报
//        startApp();
        //标签列表数据
        initIndicator();
        //获取分享链接
        getShareUrl();
    }

    /**
     * 获取动态权限
     */
    private void checkMyPermission() {
        PermissionManager.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                MY_PERSSION_WRITE_EXTERNAL_STORAGE, new PermissionManager.PermissionInterFace() {
                    @Override
                    public void havePermission() {

                    }
                });
    }

    private void startApp() {
//        Statistics.openAppStatistics(new MyInterface() {
//            @Override
//            public void onError(Call call, Exception e) {
//
//            }
//
//            @Override
//            public void onSucceed(int state, String data, JSONObject obj) {
//                XUtilLog.log_i("wbb", "========app启动上报成功");
//            }
//        });
    }

    private void FirstOpenApp() {
        boolean isFirstLogin = DefaultSharePrefManager.getBoolean("isFirstOpen", true);
//        if (isFirstLogin) {
//            Statistics.FirstOpenApp(new StringCallback() {
//                @Override
//                public void onError(Call call, Exception e) {
//                    XUtilLog.log_i("wbb", "=====[第一次打开app统计失败]=====:");
//                }
//
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = JSON.parseObject(response);
//                        int ret = jsonObject.getInteger("ret");
//                        if (ret == 200) {
//                            /**第一次登录标志位置为false*/
//                            DefaultSharePrefManager.putBoolean("isFirstLogin", false);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
    }

    //获取分享链接
    private void getShareUrl(){
        NIManage.getShareUrl(new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state==200){
                    JSONObject jo = JSON.parseObject(data);
                    shareUrl = jo.getString("share_url");
                }
            }
        });

    }

    private void initRecyclerView() {
        mRVAllType.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new CommonAdapter<String>(this, R.layout.layout_rv_all_type, datas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                RelativeLayout pictureLayout = holder.getView(R.id.rl_all_type_container);
                int height = (DisplayUtil.getScreenWidth(MeinvMainActivity.this) - DisplayUtil.dip2px(MeinvMainActivity.this, 18)) / 3;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                pictureLayout.setLayoutParams(params);
                holder.setText(R.id.tv_type_name, s);
            }
        };
        mRVAllType.setAdapter(adapter);
    }

    private void initIndicator() {
        scrollIndicatorView = (ScrollIndicatorView) findViewById(R.id.moretab_indicator);
        float unSelectSize = 16;
        float selectSize = unSelectSize * 1.1f;
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFFe40280, Color.GRAY).setSize(selectSize, unSelectSize));
        scrollIndicatorView.setScrollBar(new ColorBar(this, 0xFFe40280, 4));
        mViewPager.setOffscreenPageLimit(4);
        dbListInInfo = new ArrayList<>();
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, mViewPager);
        indicatorViewPager.setAdapter(new IndicatorFragmentAdapter(getSupportFragmentManager(), mContext, selectedInfos));
        //数据库的数据
        dbListInInfo.clear();
        selectedInfos.clear();
        dbListInInfo = DatabaseUtils.getAll();
        if (dbListInInfo == null || dbListInInfo.size() == 0 || dbListInInfo.isEmpty()) {
            getLabelList();
        } else {
            for (int i = 0; i < dbListInInfo.size(); i++) {
                if (dbListInInfo.get(i).getRecom().equals("1")) {
                    selectedInfos.add(dbListInInfo.get(i));
                }
            }
        }
        indicatorViewPager.notifyDataSetChanged();
    }

    private void getLabelList() {
        NIManage.GetDirections(new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                XUtilLog.log_i("wbb", "navigationInfos.OnError:" + e.getMessage());
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    navigationInfos = JSON.parseArray(data, NavigationInfo.class);
                    if (navigationInfos == null || navigationInfos.size() == 0) {
                        //mTipModule.showNoDataState("暂时没有分类");
                    } else {
                        DataSupport.saveAll(navigationInfos);
                    }
                } else {
                    ToastUtil.toastLong(mContext, "服务器数据异常");
                }
                for (int i = 0; i < navigationInfos.size(); i++) {
                    if (navigationInfos.get(i).getRecom().equals("1")) {
                        selectedInfos.add(navigationInfos.get(i));
                    }
                }
                indicatorViewPager.notifyDataSetChanged();
            }
        });
    }

    private void getVersionData() {
        NIManage.VersionUpdate(new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                getVersion2();
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    try {
                        JSONObject jo = JSON.parseObject(data);
                        String versioin1 = jo.getString("vision");
                        String appUrl = jo.getString("download_url");
                        String appName = "meinv";
                        String versioin2 = FileUtil.getVersion(mContext);
                        int serviceVersion = Integer.parseInt(versioin1.replace(".", ""));
                        int localVersion = Integer.parseInt(versioin2.replace(".", ""));
                        if (serviceVersion > localVersion) {
                            showDialog(appUrl, appName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getVersion2();
                    }
                } else {
                    getVersion2();
                }
            }
        });
    }

    private void getVersion2() {
        NIManage.VersionUpdate2(new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    try {
                        JSONObject jo = JSON.parseObject(data);
                        String versioin1 = jo.getString("vision");
                        String appUrl = jo.getString("download_url");
                        String appName = "meinv";
                        String versioin2 = FileUtil.getVersion(mContext);
                        int serviceVersion = Integer.parseInt(versioin1.replace(".", ""));
                        int localVersion = Integer.parseInt(versioin2.replace(".", ""));
                        if (serviceVersion > localVersion) {
                            showDialog(appUrl, appName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }

    private void downloadApk(String imgUrl, String name) {
        HttpUtils.downloadNewSelf(imgUrl, new FileCallBack(FileUtils.getDownloadApk(), name + ".apk") {
            @Override
            public void onError(Call call, Exception e,int id) {
                ToastUtil.toastLong(mContext, "app下载失败，请稍后重试！");
            }

            @Override
            public void onResponse(File response,int id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(response),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }
        });
    }

    private void showDialog(final String url, final String name) {
        if (dialog == null) {
            dialog = CollapsibleDialog.createExitPromptDialog(mContext,
                    "确  定", "取  消", "有新版本，确认更新吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //确定按钮
                            ToastUtil.toastLong(mContext,"下载中，请稍后！");
                            downloadApk(url, name);
                        }
                    });
        }
        //取消按钮
        dialog.setNegativeButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getScreenManager().popAllActivity();
                finish();
//                dialog.dismiss();
            }
        });
        //字体颜色
        dialog.setMessageColor(getResources().getColor(R.color.black));
        dialog.show();
    }

    private boolean isAllTypeShow = true;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.meinv_back:
                onBackPressed();
                break;
            case R.id.rl_search://搜索界面
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.application_relevant://应用相关信息/美女天堂改为分享apk
//                ActivityJump.getInstance().JumpSetting(mContext);
                ShareDialog dialog = new ShareDialog(MeinvMainActivity.this,shareUrl);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            case R.id.iv_all_type://标签类型选择界面
                //美女的代码
                startActivity((new Intent(this, AddTitleActivity.class)));
                //咪秀的代码
//                if (isAllTypeShow) {
//                    showAllTypePage();
//                    isAllTypeShow = false;
//                } else {
//                    dismissAllTypePage();
//                    isAllTypeShow = true;
//                }
                break;
            case R.id.refresh_img://刷新
                VideoApplication.getInstance().getMessagePump().
                        broadcastMessage(Message.Type.Refresh_current_page);
                break;
            case R.id.collection_img://收藏
                ActivityJump.getInstance().JumpMyCollection(mContext);
                break;
        }
    }

    /**
     * 让所有类型页面消失
     */
    private void dismissAllTypePage() {
        ViewUtil.visible(mViewPager);

        //ImageView的旋转动画
        PropertyValuesHolder p0 = PropertyValuesHolder.ofFloat("rotation", 360F, 0F);
        oAnimatorImageView = ObjectAnimator.ofPropertyValuesHolder(mImageView, p0);
        oAnimatorImageView.setDuration(1000);
//        oAnimatorIndicator.setInterpolator(new LinearInterpolator());
        oAnimatorImageView.start();

        //导航指示器移动的距离
        if (indicatorScrollWidth == -1)
            indicatorScrollWidth = DisplayUtil.getScreenWidth(this);
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", indicatorScrollWidth, 0);
        oAnimatorIndicator = ObjectAnimator.ofPropertyValuesHolder(scrollIndicatorView, p1);
        oAnimatorIndicator.setDuration(1000);
//        oAnimatorIndicator.setInterpolator(new LinearInterpolator());
        oAnimatorIndicator.start();


        //所有类型页面移动的距离
        if (allTypePageScrollWidth == -1)
            allTypePageScrollWidth = mRVAllType.getHeight();
        XUtilLog.log_i("leibown", "allTypePageScrollWidth:" + allTypePageScrollWidth);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", 0, -allTypePageScrollWidth);
        oAnimatorRecycler = ObjectAnimator.ofPropertyValuesHolder(mRVAllType, p2);
        oAnimatorRecycler.setDuration(1000);
//                oAnimatorRecycler.setInterpolator(new LinearInterpolator());
        oAnimatorRecycler.start();


        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        ObjectAnimator oAnimatorContainer = ObjectAnimator.ofPropertyValuesHolder(rlBtnContainer, pvhX);
        oAnimatorContainer.setDuration(1000);
//                oAnimatorRecycler.setInterpolator(new LinearInterpolator());
        oAnimatorContainer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewUtil.gone(mRVAllType);
                ViewUtil.visible((View) findView(R.id.iv_second_line));
            }
        }, 1000);

    }

    /**
     * 显示所有类型页面
     */
    private ValueAnimator oAnimatorIndicator;
    private ValueAnimator oAnimatorRecycler;
    private ValueAnimator oAnimatorImageView;
    private int indicatorScrollWidth = -1;
    private int allTypePageScrollWidth = -1;

    private void showAllTypePage() {
        ViewUtil.gone((View) findView(R.id.iv_second_line));
        ViewUtil.visible(mRVAllType);

        //ImageView的旋转动画
        PropertyValuesHolder p0 = PropertyValuesHolder.ofFloat("rotation", 0F, 360F);
        oAnimatorImageView = ObjectAnimator.ofPropertyValuesHolder(mImageView, p0);
        oAnimatorImageView.setDuration(1000);
//        oAnimatorImageView.setInterpolator(new LinearInterpolator());
        oAnimatorImageView.start();


        //导航指示器移动的距离
        if (indicatorScrollWidth == -1)
            indicatorScrollWidth = DisplayUtil.getScreenWidth(this);
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", 0F, indicatorScrollWidth);
        oAnimatorIndicator = ObjectAnimator.ofPropertyValuesHolder(scrollIndicatorView, p1);
        oAnimatorIndicator.setDuration(1000);
//        oAnimatorIndicator.setInterpolator(new LinearInterpolator());
        oAnimatorIndicator.start();

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator oAnimatorContainer = ObjectAnimator.ofPropertyValuesHolder(rlBtnContainer, pvhX);
        oAnimatorContainer.setDuration(1000);
//                oAnimatorRecycler.setInterpolator(new LinearInterpolator());
        oAnimatorContainer.start();

        //所有类型页面移动的距离
        mRVAllType.post(new Runnable() {
            @Override
            public void run() {
                if (allTypePageScrollWidth == -1)
                    allTypePageScrollWidth = mRVAllType.getHeight();
                XUtilLog.log_i("leibown", "allTypePageScrollWidth:" + allTypePageScrollWidth);
                PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", -allTypePageScrollWidth, 0);
                oAnimatorRecycler = ObjectAnimator.ofPropertyValuesHolder(mRVAllType, p2);
                oAnimatorRecycler.setDuration(1000);
//                oAnimatorRecycler.setInterpolator(new LinearInterpolator());
                oAnimatorRecycler.start();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewUtil.gone(mViewPager);
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERSSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "获取权限失败，将不能下载图片", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void attachAllMessage() {
        attachMessage(UPDATE_LABEL_LIST);
    }

    @Override
    public void onReceiveMessage(Message message) {
        super.onReceiveMessage(message);
        switch (message.type) {
            case UPDATE_LABEL_LIST: {//更新标签
                XUtilLog.log_i("wbb", "接收到 消息了 .......... ");
                List<NavigationInfo> list = DatabaseUtils.getAll();
                selectedInfos.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getRecom().equals("1")) {
                        selectedInfos.add(list.get(i));
                    }
                }
                indicatorViewPager.notifyDataSetChanged();
                break;
            }
        }
    }
}
