package com.anzhuo.video.app.dongtu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.anzhuo.video.app.config.VideoApplication;

import java.lang.reflect.Field;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/6/23 11:00
 * Des：
 */
public class DisplayUtil {

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static String getDensity(Context context) {
        float d = context.getResources().getDisplayMetrics().density;
        String density = d + "";
        return density;
    }


    /**
     * 显示键盘
     *
     * @param ctx
     */
    public static void showKeyboard(Activity ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    /**
     * 隐藏键盘
     *
     * @param ctx
     * @return
     */
    public static boolean hideKeyboard(Activity ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View curFocus = ctx.getCurrentFocus();
        return curFocus != null && imm.hideSoftInputFromWindow(curFocus.getWindowToken(), 0);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId), Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }


    /**
     * 显示Toast
     *
     * @param context
     * @param msg
     */
    public static void showLongToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    private static Toast mToast = null;

    /**
     * Toast 栈
     *
     * @param context
     * @param text
     * @param duration
     */
    private static void showToast(Context context, String text, int duration) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    /**
     * <p>dp转换为px</p>
     *
     * @param context
     * @param dp
     * @return
     * @author nk
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * <p>px转换为dp</p>
     *
     * @param context
     * @param px
     * @return
     * @author nk
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 设置状态栏和导航栏透明
     */
    public static void setTrans(Activity activity) {
//        activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * @param width        图片原来的宽高
     * @param height
     * @param pictureWidth 图片缩放后的宽度
     * @return 图片缩放后的高度
     */
    public static int CountPictureHeight(int width, int height, int pictureWidth) {
        //然后按照宽高比例计算缩放后图片的高
        return (pictureWidth * height) / width;
    }

    /**
     * 主要是单张图片需要充满横向充满全屏减去间距
     *
     * @return 返回图片缩放的后的宽度
     */
    public static int CountPictureWidth() {
        int marginWidth = DisplayUtil.dp2px(VideoApplication.getContext(), 15);//外间距
        int ScreenWidth = DisplayUtil.getScreenWidth(VideoApplication.getContext());
        //然后按照宽高比例计算缩放后图片的高
        return ScreenWidth - (marginWidth * 2);//屏幕的宽度减去左右两边的间距
    }
    public static int CountPictureWidthno() {
        int marginWidth = DisplayUtil.dp2px(VideoApplication.getContext(), 0);//外间距
        int ScreenWidth = DisplayUtil.getScreenWidth(VideoApplication.getContext());
        //然后按照宽高比例计算缩放后图片的高
        return ScreenWidth - (marginWidth * 2);//屏幕的宽度减去左右两边的间距
    }

}

