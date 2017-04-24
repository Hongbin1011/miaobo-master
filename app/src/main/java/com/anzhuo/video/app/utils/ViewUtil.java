package com.anzhuo.video.app.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.search.tool.SDKCompat;

/**
 * View处理
 * Created with Android Studio.
 * <p/>
 * Author:xiaxf
 * <p/>
 * Date:2015/7/16.
 */
public class ViewUtil {


    /**
     * 设置View背景
     *
     * @param view
     * @param background
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setViewBackground(View view, Drawable background) {
        if (SDKCompat.hasJellyBean()) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setViewBackground(View view, int drawableId) {
        Drawable drawable = view.getContext().getResources().getDrawable(drawableId);
        setViewBackground(view, drawable);
    }

    /**
     * 判断view是否显示
     *
     * @param view
     * @return
     */
    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置View为Visible
     *
     * @param view
     */
    public static void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 设置View为Gone
     *
     * @param view
     */
    public static void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }

    /**
     * 设置View为Invisible
     *
     * @param view
     */
    public static void setViewInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }


    /**
     * 强制显示软键盘
     */
    public static void openSoftInput(Activity activity, EditText editText,boolean isShow) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
        if (isShow==false){
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * findViewById 省略强转过程
     *
     * @param activity
     * @param resId
     * @return
     */
    public static <V> V findView(Activity activity, @IdRes int resId) {
        //noinspection unchecked
        return (V) activity.findViewById(resId);
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param rootView
     * @param <V>具体的View类型
     * @return
     */
    @IdRes
    public static <V> V findView(View rootView, @IdRes int resId) {
        //noinspection unchecked
        return (V) rootView.findViewById(resId);
    }

    /**
     * findviewById 并添加点击事件
     *
     * @param activity
     * @param resId
     * @param onClickListener
     * @param <V>具体的View类型
     * @return
     */
    public static <V> V findViewAttachOnclick(Activity activity, @IdRes int resId, View.OnClickListener onClickListener) {
        View view = activity.findViewById(resId);
        view.setOnClickListener(onClickListener);
        //noinspection unchecked
        return (V) view;
    }

    /**
     * findviewById 并添加点击事件
     *
     * @param rootView
     * @param resId
     * @param onClickListener
     * @param <V>具体的View类型
     * @return
     */
    @IdRes
    public static <V> V findViewAttachOnclick(View rootView, @IdRes int resId, View.OnClickListener onClickListener) {
        //noinspection unchecked
        View view = rootView.findViewById(resId);
        view.setOnClickListener(onClickListener);
        //noinspection unchecked
        return (V) view;
    }


    /**
     * RecyclerView 是否滚动到顶部
     *
     * @param recyclerView
     * @return
     */
    public static boolean isScrollTop(RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getChildCount() > 0) {
            if (recyclerView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * ListView 是否滚动到顶部
     *
     * @param listView
     * @return
     */
    public static boolean isScrollTop(ListView listView) {
        if (listView != null && listView.getChildCount() > 0) {
            if (listView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * ExpandableListView 是否滚动到顶部
     *
     * @param listView
     * @return
     */
    public static boolean isScrollTop(ExpandableListView listView) {
        if (listView != null && listView.getChildCount() > 0) {
            if (listView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * ScrollView 是否滚动到顶部
     *
     * @param scrollView
     * @return
     */
    public static boolean isScrollTop(ScrollView scrollView) {
        if (scrollView != null) {
            if (scrollView.getScrollY() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 控制view的visible与Gone
     *
     * @param view
     * @param show
     */
    public static void toggleView(View view, boolean show) {
        if (show) {
            setViewVisible(view);
        } else {
            setViewGone(view);
        }
    }


    public static void hiddenInputKeyboard(View view, Context ctx) {
        InputMethodManager imm = (InputMethodManager)
                ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示视图
     */
    public static void visible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏视图
     */
    public static void invisible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 消失视图
     */
    public static void gone(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置点击
     *
     * @param view
     * @param id
     */
    public static void setTextDrawable(Context context,TextView view, int id) {
        // 设置 Drawables
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable,null , null, null);
    }

    /**
     * 设置点击
     *
     * @param view
     * @param id
     */
    public static void setTextDrawableTop(Context context,TextView view, int id) {
        // 设置 Drawables
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 下拉刷新
     */
    public static void initRefresh(final SwipeRefreshLayout refreshList, final RefreshInterface refresh) {
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        refreshList.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (XUtilNet.isNetConnected()) {
                    refresh.getData();
                } else {
                    ToastUtil.showCenterToast(R.string.check_network);
                    refreshList.setRefreshing(false);
                }
            }
        });

    }

    public static Typeface setIconFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
    }
}
