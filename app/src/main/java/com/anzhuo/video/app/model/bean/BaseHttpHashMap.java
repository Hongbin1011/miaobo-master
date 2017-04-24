package com.anzhuo.video.app.model.bean;

import com.anzhuo.video.app.constant.Constants;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/9.
 * 访问网络的基本HashMap
 */

public class BaseHttpHashMap extends HashMap {
    public BaseHttpHashMap() {
        this.put("devices", Constants.DEVICES_TAG);
        this.put("version", Constants.VersionName);
        this.put("internet", Constants.INTERNET);
    }
}
