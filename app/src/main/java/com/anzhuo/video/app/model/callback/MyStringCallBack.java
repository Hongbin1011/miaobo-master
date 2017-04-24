package com.anzhuo.video.app.model.callback;

import android.content.Context;

import com.anzhuo.video.app.utils.ContextHolder;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * created on 2016/10/12 0012.
 */

public abstract class MyStringCallBack extends StringCallback {
    private ContextHolder contextHolder;
    private Context context;

    public MyStringCallBack(Context context) {
        contextHolder = new ContextHolder(context);
        this.context = context;

    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        String callBackString = response.body().string();
        if (contextHolder.isAlive() == false) {
            Logger.i("MyStringCallBack  id = " + id);
            Logger.i("MyStringCallBack  context = " + context + "  已经不存在");
            callBackString = "";
        }
        return callBackString;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        if (contextHolder.isAlive() == false) {
            Logger.i("MyStringCallBack  context = " + context + "  已经不存在");
        }
        if (call.isCanceled()) {
            Logger.i("MyStringCallBack  call = " + call + "  已经取消");
        }
    }

    @Override
    public void onResponse(String response, int id) {
        if (contextHolder.isAlive() == false) {
            Logger.i("MyStringCallBack  context = " + context + "  已经不存在");
        }


    }
}
