package com.anzhuo.video.app.utils;

import android.content.Context;
import android.content.Intent;

/**
 * creat on 2016/8/17.
 * QQ-632671653
 */
public class UpdateDataUtils {

    /**
     * 更新操作数据，段子数，评论数，粉丝，关注等
     * @param context
     */
    public static void updateData(Context context){
        Intent intent = new Intent("com.joke.mtdz.UPDATEDATA");
        context.sendBroadcast(intent);
    }
}
