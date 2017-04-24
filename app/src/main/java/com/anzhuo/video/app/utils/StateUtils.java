package com.anzhuo.video.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

/**
 * 状态改变
 */

public class StateUtils {
    /**
     * 设置点赞踩等的状态
     *
     * @param mContext
     * @param textView1
     * @param DrawableID
     * @param ColorID
     */
    public static void setDrawbleAndText(Context mContext, TextView textView1, int DrawableID, int ColorID) {
        try {
            if (textView1 != null) {
                MyTextUtils.setTextDrawable(mContext, textView1, DrawableID);
                textView1.setTextColor(mContext.getResources().getColor(ColorID));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
