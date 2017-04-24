package com.anzhuo.video.app.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26 0026.
 * 上传图片异步回调
 */

public interface UpImgCallBack<T> {
    void Success(List<T> list);

    void fail();
}
