package com.anzhuo.video.app.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/9/29.
 */

public class ViewUtil {


    /**
     * 创建
     */
    private static View InitCreateView(Context context, View createView, int ResID) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (createView == null) {
            createView = layoutInflater.inflate(ResID, null);
        }
        return createView;
    }

    /**
     * 添加createView
     */
    public static void AddView(Context context, ViewGroup parentView, View createView, int ResID) {
        if (createView != null && createView.getParent() == parentView) {//首先判断createView是否存在，存在就显示
            Logger.i("createView 已经存在并且添加");
            createView.setVisibility(View.VISIBLE);
        } else if (createView != null && createView.getParent() != parentView) {
            Logger.i("createView 已经存");
            parentView.addView(createView);
        } else if (createView == null) {
            parentView.addView(InitCreateView(context, createView, ResID));
            Logger.i("createView 开始创建");
        }
    }

    /**
     * 添加View
     *
     * @param context
     * @param parentView  父控件
     * @param createView  需要添加的控件
     * @param ResID       布局文件
     * @param addPosition 添加的位置，主要是针对线性布局
     */
    public static void AddView(Context context, LinearLayout parentView, View createView, int ResID, int addPosition) {
        if (createView != null && createView.getParent() == parentView) {//首先判断createView是否存在，存在就显示
            Logger.i("createView 已经存在并且添加");
            createView.setVisibility(View.VISIBLE);
        } else if (createView != null && createView.getParent() != parentView) {
            Logger.i("createView 已经存");
            parentView.addView(createView, addPosition);
        } else if (createView == null) {
            parentView.addView(InitCreateView(context, createView, ResID), addPosition);
            Logger.i("createView 开始创建");
        }
    }


    /**
     * 移除
     */
    public static void RemoveView(ViewGroup parentView, View removeView) {
        if (removeView != null && removeView.getParent() == parentView) {
            parentView.removeView(removeView);
        }
        Logger.i("移除 createView");
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


}
