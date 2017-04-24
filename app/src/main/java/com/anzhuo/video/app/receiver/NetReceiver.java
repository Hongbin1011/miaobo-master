package com.anzhuo.video.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.orhanobut.logger.Logger;

/**
 * creat on 2016/6/16 15:50
 * 监听网络状态广播，发送事件，在HomeFragment接受
 */
public class NetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = XUtilNet.isNetConnected();
            Logger.i("网络状态：" + isConnected);
            if (isConnected) {//有网络
                EventUtil.PostListenerNetEvent(true);
            } else {
                EventUtil.PostListenerNetEvent(false);
            }
        }
    }

}
