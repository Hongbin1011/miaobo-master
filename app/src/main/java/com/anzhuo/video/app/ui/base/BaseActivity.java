package com.anzhuo.video.app.ui.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.utils.ActivityManager;
import com.anzhuo.video.app.utils.ViewUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * Created by Xiaojin on 2016/1/19.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public ViewGroup mContent;
//    public Toolbar toolbar;

    public Context context;
    public String TAG = "";

    ProgressBar loading_comments;
    LinearLayout ll_title_joke;
    public ImageView img_back;
    public ImageView img_right;
    TextView tv_title;
    ImageView img_center;
    public TextView tv_right;
    View title_line;
    RelativeLayout rl_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTag();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        context = BaseActivity.this;
        mContent = new FrameLayout(this);
        ActivityManager.getActivityManager().addActivity(this);
        super.setContentView(mContent);

//        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    }

//    @Override
//    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void setContentView(int layoutResID) {
        this.setContentView(getLayoutInflater().inflate(layoutResID, mContent, false));
    }

    /**
     * 设置Activity显示的布局
     *
     * @param view
     */
    @Override
    public void setContentView(View view) {
        this.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContent.addView(view, params);
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        ll_title_joke = (LinearLayout) view.findViewById(R.id.ll_title_joke);
        img_center = (ImageView) view.findViewById(R.id.img_theme);
        rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
        img_right = (ImageView) view.findViewById(R.id.img_right);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_right = (TextView) view.findViewById(R.id.tv_right);

        title_line = view.findViewById(R.id.title_line);
        if (img_back != null) {
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityManager.getActivityManager().finishActivity();
                    finish();
                }
            });
        }
//        if (toolbar != null)
//            setToolbar(toolbar);


    }

    /**
     * 设置头部
     *
     * @param toolbar
     */
    private void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * 设置头部是否显示返回键（根据Avtivity是否需要决定）
     */
    public void setToolBarNavigation() {
//        toolbar.setVisibility(View.GONE);
//        if (toolbar != null) {
//            toolbar.setNavigationIcon(R.mipmap.icon_launcher);
//        }
    }

    /**
     * 设置进度条是否显示
     *
     * @param isShow
     */
    public void setProgressShow(boolean isShow) {
        if (isShow) {
            ViewUtils.visible(loading_comments);
        } else {
            ViewUtils.gone(loading_comments);
        }
    }

    /**
     * 添加返回关闭当前视图监听
     */
    public void navigationBack() {
//       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    public void setTitle(String title) {
        if (null != tv_title) {
            tv_title.setText(title);
        }
    }

    public void setTitle(int titleRes) {
        if (null != tv_title) {
            tv_title.setText(titleRes);
        }
    }

    public void setTitleTextColor(int color) {
        if (null != tv_title) {
            tv_title.setTextColor(color);
        }
    }

    public void setImg_themeVisible(int visibleOrNot) {
        if (null != img_center) {
            img_center.setVisibility(visibleOrNot);
        }
    }


    /**
     * 有的title不需要这条线
     *
     * @作者 熊晓清
     * created at 2016/4/26 16:15
     */
    public void setTitleLineVisible(int visibleOrNot) {
        if (null != title_line) {
            title_line.setVisibility(visibleOrNot);
        }
    }

    public void setTvRight(String title) {
        if (null != tv_title) {
            tv_right.setText(title);
        }
    }

    public void setTvRight(int titleRes) {
        if (null != tv_title) {
            tv_right.setText(titleRes);
        }
    }

    /**
     * 修改返回键的图片
     *
     * @作者 熊晓清
     * created at 2016/4/26 16:01
     */
    public void setIMGImageResource(int Resource) {
        if (null != img_back) {
            img_back.setImageResource(Resource);
        }

    }

    /**
     * 修改右边图片
     *
     * @作者 熊晓清
     * created at 2016/4/26 16:01
     */
    public void setRightImageResource(int Resource) {
        if (null != img_right) {
            img_right.setImageResource(Resource);
        }

    }

    /**
     * 副标题的颜色有时候会变化
     *
     * @param txtColor
     */
    public void setTvRightTextColor(int txtColor) {
        tv_right.setTextColor(txtColor);

    }

    /**
     * 设置标题栏的颜色
     *
     * @param
     */
    public void setTitleRelativeLayoutColor(int color) {
        rl_title.setBackgroundColor(getResources().getColor(color));
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.null_layout);
//        JCVideoPlayer.releaseAllVideos();
        ActivityManager.getActivityManager().finishActivity(this);
        if (!TextUtils.isEmpty(TAG)) {
            Logger.i("取消网络的Activity TAG = " + TAG);
            OkHttpUtils.getInstance().cancelTag(TAG);
        } else {
            Logger.i("TAG  Activity 没有赋值");
        }
    }

    @Override
    public void onClick(View v) {

    }
    /**
     * 横竖屏监听
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 设置TAG，用于统一取消网络请求
     */
    public abstract void SetTag();

    /**
     * 字体
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


}
