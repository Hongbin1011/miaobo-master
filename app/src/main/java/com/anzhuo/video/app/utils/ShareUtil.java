package com.anzhuo.video.app.utils;

import android.app.Activity;

import com.anzhuo.video.app.widget.ShareDialog;

/**
 * 分享的utils
 */
public class ShareUtil {


    private static ShareDialog shareDialog;

    /**
     * 分享调用
     *
     * @param mActivity
     * @param text      链接地址
     */
    public static void getShareDialog(Activity mActivity, String text) {
        shareDialog = null;
        if (shareDialog == null) {
            shareDialog = new ShareDialog(mActivity, text);
        }
        shareDialog.show();
    }

}
