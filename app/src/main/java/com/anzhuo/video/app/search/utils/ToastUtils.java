/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anzhuo.video.app.search.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.MyTextUtils;

import java.lang.ref.WeakReference;

// TODO: Auto-generated Javadoc

/**
 * Toast工具
 */

public class ToastUtils {
    private static Toast toast = null;

    /**
     * 上下文.
     */
    private static Context mContext = VideoApplication.getContext();

    /**
     * 显示Toast.
     */
    public static final int SHOW_TOAST = 0;
//    private static Handler baseHandler=new MyInnerHandler(t);

    /**
     * 主要Handler类，在线程中可用 what：0.提示文本信息
     */
    static class MyInnerHandler extends Handler {
        WeakReference<ToastUtils> mFrag;

        MyInnerHandler(ToastUtils aFragment) {
            mFrag = new WeakReference<ToastUtils>(aFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            ToastUtils theFrag = mFrag.get();
            switch (msg.what) {
                case SHOW_TOAST:
                    String text = msg.getData().getString("TEXT");
                    if (toast == null) {
                        if (!MyTextUtils.isEmpty(text)) {
                            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
                        }
                    } else {
                        toast.setText(text);
                    }
                    toast.show();
                    break;
                default:
                    break;
            }//end switch
        }
    }

//    private static Handler baseHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case SHOW_TOAST:
//                    String text = msg.getData().getString("TEXT");
//                    if (toast == null) {
//                        if (!MyTextUtils.isEmpty(text)) {
//                            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
//                        }
//                    } else {
//                        toast.setText(text);
//                    }
//                    toast.show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * 描述：Toast提示文本.
     *
     * @param text 文本
     */
    public static void showToast(String text) {
        if (toast == null) {
            if (!TextUtils.isEmpty(text)) {
                toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            }
        } else {
            if (!TextUtils.isEmpty(text)) {
                toast.setText(text);
            }
        }
        toast.show();
    }

    /**
     * 描述：Toast提示文本.在屏幕的中间
     *
     * @param text 文本
     */
    public static void showCenterToast(String text) {
        if (toast == null) {
            if (!TextUtils.isEmpty(text)) {
                toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            }
        } else {
            if (!TextUtils.isEmpty(text)) {
                toast.setText(text);
            }
        }
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.show();

    }

    /**
     * 描述：Toast提示文本.在屏幕的中间
     *
     * @param text 文本
     * @param time 显示时间长短  Toast.LENGTH_LONG
     */
    public static void showCenterToast(String text, int time) {
        if (toast == null) {
            if (!TextUtils.isEmpty(text)) {
                toast = Toast.makeText(mContext, text, time);
            }
        } else {
            if (!TextUtils.isEmpty(text)) {
                toast.setText(text);
            }
        }
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.show();

    }

    /**
     * 描述：Toast提示文本.在屏幕的中间
     *
     * @param
     */
    public static void showCenterToast(int resId) {
        if (toast == null) {
            if (!TextUtils.isEmpty(mContext.getResources().getText(resId))) {
                toast = Toast.makeText(mContext, "" + mContext.getResources().getText(resId), Toast.LENGTH_SHORT);
            }
        } else {
            if (!TextUtils.isEmpty(mContext.getResources().getText(resId))) {
                toast.setText(mContext.getResources().getText(resId) + "");
            }
        }
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.show();
    }

    /**
     * 描述：Toast提示文本.
     *
     * @param resId 文本的资源ID
     */
    public static void showToast(int resId) {
        String toastString = mContext.getResources().getText(resId).toString();
        if (toast == null) {
            if (!TextUtils.isEmpty(toastString)) {
                toast = Toast.makeText(mContext, toastString, Toast.LENGTH_SHORT);
            }
        } else {
            if (!TextUtils.isEmpty(toastString)) {
                toast.setText(toastString);
            }
        }
        toast.show();
    }

    /**
     * 描述：在线程中提示文本信息.
     *
     * @param resId 要提示的字符串资源ID，消息what值为0,
     */
//    public static void showToastInThread(int resId) {
//        mContext = mContext;
//        Message msg = baseHandler.obtainMessage(SHOW_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString("TEXT", mContext.getResources().getString(resId));
//        msg.setData(bundle);
//        baseHandler.sendMessage(msg);
//    }

    /**
     * 描述：在线程中提示文本信息.
     *
     * @param
     */
//    public static void showToastInThread(String text) {
//        Message msg = baseHandler.obtainMessage(SHOW_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString("TEXT", text);
//        msg.setData(bundle);
//        baseHandler.sendMessage(msg);
//    }

    /**
     * 显示自定义Toast
     *
     * @param
     */

    public static void showCustomToast(String toastString) {
        float viewHeight = mContext.getResources().getDimension(R.dimen.tab_height) + mContext.getResources().getDimension(R.dimen.tab_layout_height);
        int height = (int) viewHeight;
        showCustomToast(toastString, height);

    }

    public static void showCustomToast(String toastString, int height) {
        Toast toast = null;
        if (!TextUtils.isEmpty(toastString)) {
            toast = new Toast(mContext);
        }
        //使用布局加载器，将编写的toast_layout布局加载进来
        View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_custom, null);
        TextView tvToast = (TextView) layout
                .findViewById(R.id.tv_custom_toast);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtil.getScreenWidth(mContext), LinearLayout.LayoutParams.WRAP_CONTENT);
        tvToast.setText(toastString);
        tvToast.setLayoutParams(layoutParams);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, height);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
